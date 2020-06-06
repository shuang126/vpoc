package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Component
public class Eden {
    private int capacity = JvmConfig.getEdenSize();
    private AtomicInteger allocatedPointer = new AtomicInteger(0);
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    List<ObjectBO> liveObjects = new ArrayList<>();
    private final GcSupervisor gcSupervisor;

    public Eden(GcSupervisor gcSupervisor) {
        this.gcSupervisor = gcSupervisor;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size, allocatedPointer.get(), 0);
        allocatedObjects.add(objectBO);
        allocatedPointer.addAndGet(objectBO.getSize());
        gcSupervisor.reportNewObject(objectBO);
        return objectBO;
    }

    public synchronized boolean available(int size) {
        return allocatedPointer.get() + size <= capacity;
    }

    public synchronized void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        gcSupervisor.sweep(new Sweep(SpaceEnum.EDEN));
    }

    public synchronized void mark() {
        Set<Integer> set = new HashSet<>();
        while (set.size() < 3) {
            int nextInt = ThreadLocalRandom.current().nextInt(allocatedObjects.size());
            set.add(nextInt);
        }
        set.forEach(i -> {
            ObjectBO objectBO = allocatedObjects.get(i);
            liveObjects.add(objectBO);
            gcSupervisor.mark(objectBO);
        });
    }
}
