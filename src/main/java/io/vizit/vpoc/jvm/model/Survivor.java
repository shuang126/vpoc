package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.vizit.vpoc.jvm.model.JvmConfig.MaxTenuringThreshold;

@Getter
@Setter
@Component
@Scope("prototype")
public class Survivor {
    protected int capacity = JvmConfig.getSurvivorSize();
    private AtomicInteger allocatedPointer = new AtomicInteger(0);
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    List<ObjectBO> liveObjects = new ArrayList<>();
    private final GcSupervisor gcSupervisor;
    private SpaceEnum name;
    private final Old old;

    public Survivor(GcSupervisor gcSupervisor, Old old) {
        this.gcSupervisor = gcSupervisor;
        this.old = old;
    }

    public synchronized ObjectBO copy(SpaceEnum from, ObjectBO objectBO) {
        ObjectBO copy = (ObjectBO) objectBO.clone();
        if (copy.getAge() >= MaxTenuringThreshold) {
            old.promotion(from, copy);
        } else {
            copy.setAddress(allocatedPointer.get());
            copy.increaseAge();
            allocatedObjects.add(copy);
            allocatedPointer.addAndGet(copy.getSize());
            gcSupervisor.copy(new Copy(from, this.getName(), copy));
        }
        return copy;
    }

    public synchronized boolean available(int size) {
        return allocatedPointer.get() + size < capacity;
    }

    public synchronized void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        gcSupervisor.sweep(new Sweep(this.name));
    }

    public synchronized void mark() {
        if (allocatedObjects.size() == 0) {
            return;
        }
        Collections.sort(allocatedObjects, Comparator.comparing(ObjectBO::getAge).reversed());
        ObjectBO objectBO = allocatedObjects.get(0);
        liveObjects.add(objectBO);
        gcSupervisor.mark(objectBO);
    }
}
