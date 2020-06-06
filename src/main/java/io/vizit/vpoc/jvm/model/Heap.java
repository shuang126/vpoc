package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Component
public class Heap {
    private int capacity = 240000;
    private final Young young;
    private final Old old;
    private AtomicLong sequence = new AtomicLong(1);
    private final GcSupervisor gcSupervisor;

    public Heap(Young young, Old old, GcSupervisor gcSupervisor) {
        this.young = young;
        this.old = old;
        this.gcSupervisor = gcSupervisor;
    }

    public ObjectBO allocate(int size) {
        return young.allocate(sequence.getAndIncrement(), size);
    }

    public void clear() {
        young.clear();
        old.clear();
        sequence = new AtomicLong(1);
        gcSupervisor.go();
    }

}
