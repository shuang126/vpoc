package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
public class Young {
    private final long capacity;
    private Eden eden;
    private Supplier s0;
    private Supplier s1;

    public Young(long capacity) {
        this.capacity = capacity;
        this.eden = new Eden(this.capacity * JvmConfig.SurvivorRatio/(JvmConfig.SurvivorRatio+2));
    }

    public ObjectBO allocate(long id, int size) {
        return this.eden.allocate(id, size);
    }
}
