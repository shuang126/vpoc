package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.TreeSet;
@Setter
@Getter
public abstract class Space {
    protected long capacity;
    protected TreeSet<ObjectBO> allocatedObjects = new TreeSet<>();
    protected TreeSet<ObjectBO> liveObjects = new TreeSet<>();

    public Space(long capacity) {
        this.capacity = capacity;
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
}
