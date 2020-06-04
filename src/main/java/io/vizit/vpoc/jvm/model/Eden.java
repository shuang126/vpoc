package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Getter
@Component
public class Eden {
    private int capacity = JvmConfig.getEdenSize();
    private AtomicInteger allocatedPointer = new AtomicInteger(0);
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    List<ObjectBO> liveObjects = new ArrayList<>();
    private final Monitor monitor;

    public Eden(Monitor monitor) {
        this.monitor = monitor;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size, allocatedPointer.get());
        allocatedObjects.add(objectBO);
        allocatedPointer.addAndGet(objectBO.getSize());
        monitor.reportNewObject(objectBO);
        return objectBO;
    }

    public boolean available(int size) {
        return allocatedPointer.get() + size <= capacity;
    }

    public void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
        allocatedPointer.set(0);
        monitor.sweep(new Sweep(SpaceEnum.EDEN));
    }

    public void mark() {
        IntStream ids = ThreadLocalRandom.current().ints(
                3,
                0,
                allocatedObjects.size());
        ids.forEach(i -> {
            ObjectBO objectBO = allocatedObjects.get(i);
            liveObjects.add(objectBO);
            monitor.mark(objectBO);
        });
    }
}
