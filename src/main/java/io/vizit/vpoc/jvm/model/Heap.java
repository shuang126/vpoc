package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class Heap {
    private static Heap singleton = new Heap();
    private long capacity = 240000;
    private Young young;
    private Old old;
    private AtomicLong sequence = new AtomicLong(1);

    private Heap() {
        this.young = new Young(capacity * (1 / (JvmConfig.NewRatio + 1)));
        this.old = new Old(capacity - this.young.getCapacity());
    }

    public static Heap getInstance() {
        return singleton;
    }

    public ObjectBO allocate(int size) {
        return young.allocate(sequence.getAndIncrement(), size);
    }
}
