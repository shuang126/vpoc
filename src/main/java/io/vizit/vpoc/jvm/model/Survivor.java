package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Component
@Scope("prototype")
public class Survivor {
    private boolean empty = true;
    protected long capacity = JvmConfig.getSurvivorSize();
    protected TreeSet<ObjectBO> allocatedObjects = new TreeSet<>();
    protected TreeSet<ObjectBO> liveObjects = new TreeSet<>();
    private final Monitor monitor;

    public Survivor(Monitor monitor) {
        this.monitor = monitor;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size);
        allocatedObjects.add(objectBO);
        return objectBO;
    }

    public boolean available(int size) {
        return allocatedObjects.size() + size < capacity;
    }

    public void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
    }

    public void mark() {
        int count = ThreadLocalRandom.current().nextInt(1, 3);
        for (int i = 0; i < count; i++) {
            liveObjects.add(allocatedObjects.pollFirst());
        }
    }
}
