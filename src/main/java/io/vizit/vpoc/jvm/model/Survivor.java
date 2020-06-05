package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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

    public Survivor(GcSupervisor gcSupervisor) {
        this.gcSupervisor = gcSupervisor;
    }

    public synchronized ObjectBO allocate(long id, int size, int age) {
        ObjectBO objectBO = new ObjectBO(id, size, allocatedPointer.get(), age);
        allocatedObjects.add(objectBO);
        allocatedPointer.addAndGet(objectBO.getSize());
        return objectBO;
    }

    public boolean available(int size) {
        return allocatedPointer.get() + size < capacity;
    }

    public void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        gcSupervisor.sweep(new Sweep(this.name));
    }

    public void mark() {
        if (allocatedObjects.size() == 0) {
            return;
        }
        IntStream ids = ThreadLocalRandom.current().ints(
                1,
                0,
                allocatedObjects.size());
        ids.forEach(i -> {
            ObjectBO objectBO = allocatedObjects.get(i);
            liveObjects.add(objectBO);
            gcSupervisor.mark(objectBO);
        });
    }
}
