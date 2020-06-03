package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Component
@Scope("prototype")
public class Survivor {
    private boolean empty = true;
    protected long capacity = JvmConfig.getSurvivorSize();
    private AtomicLong allocatedPointer = new AtomicLong(0);
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    List<ObjectBO> liveObjects = new ArrayList<>();
    private final Monitor monitor;
    private SpaceEnum name;
    public Survivor(Monitor monitor) {
        this.monitor = monitor;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size, allocatedPointer.get());
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
        monitor.sweep(new Sweep(this.name));
    }

    public void mark() {
        int count = ThreadLocalRandom.current().nextInt(1, 5);
        for (ObjectBO objectBO : allocatedObjects) {
            liveObjects.add(objectBO);
            monitor.mark(objectBO);
            if (count-- == 0) {
                break;
            }
        }
    }
}
