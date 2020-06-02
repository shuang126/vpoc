package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Young {
    private final long capacity;
    private Eden eden;
    private Survivor from;
    private Survivor to;

    public Young(long capacity) {
        this.capacity = capacity;
        this.eden = new Eden(this.capacity * JvmConfig.SurvivorRatio / (JvmConfig.SurvivorRatio + 2));
        this.from = new Survivor((this.capacity - this.eden.getCapacity()) / 2);
        this.to = new Survivor((this.capacity - this.eden.getCapacity()) / 2);
    }

    public ObjectBO allocate(long id, int size) {
        if (this.eden.available(size)) {
            gc();
        }
        return this.eden.allocate(id, size);
    }

    private synchronized void gc() {
        eden.mark();
        from.mark();
        copyAndSweep();
        swapSurvivor();
    }

    private void copyAndSweep() {
        // copy from eden to <TO Survivor>
        for (ObjectBO objectBO : this.eden.getLiveObjects()) {
            this.to.allocate(objectBO.getId(), objectBO.getSize());
        }
        this.eden.sweep();

        // copy from <FROM Survivor> to <TO Survivor>
        for (ObjectBO objectBO : this.from.getLiveObjects()) {
            this.to.allocate(objectBO.getId(), objectBO.getSize());
        }
        this.from.sweep();
    }

    private void swapSurvivor() {
        Survivor tmp = this.from;
        this.from = this.to;
        this.to = tmp;
    }

}
