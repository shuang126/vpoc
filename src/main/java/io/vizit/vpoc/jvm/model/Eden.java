package io.vizit.vpoc.jvm.model;


import lombok.Getter;

@Getter
public class Eden {

    private final long capacity;

    public synchronized ObjectBO allocate(long id, int size) {
        return new ObjectBO(id, size);
    }

    public Eden(long capacity) {
        this.capacity = capacity;
    }
}
