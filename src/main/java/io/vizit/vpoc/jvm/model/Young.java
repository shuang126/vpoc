package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Young {
    private final long capacity = JvmConfig.getYoungSize();
    private final Eden eden;
    private Survivor from;
    private Survivor to;
    private final GcSupervisor gcSupervisor;

    public Young(Eden eden, Survivor from, Survivor to, SimpMessageSendingOperations messagingTemplate, GcSupervisor gcSupervisor) {
        this.eden = eden;
        this.from = from;
        this.from.setName(SpaceEnum.S0);
        this.to = to;
        this.to.setName(SpaceEnum.S1);
        this.gcSupervisor = gcSupervisor;
    }

    public ObjectBO allocate(long id, int size) {
        if (!this.eden.available(size)) {
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
            this.to.copy(SpaceEnum.EDEN, objectBO);
        }
        // copy from <FROM Survivor> to <TO Survivor>
        for (ObjectBO objectBO : this.from.getLiveObjects()) {
            this.to.copy(this.from.getName(), objectBO);
        }

        this.eden.sweep();
        this.from.sweep();
    }

    private void swapSurvivor() {
        Survivor tmp = this.from;
        this.from = this.to;
        this.to = tmp;
    }

    public void clear() {
        this.eden.sweep();
        this.from.sweep();
        this.to.sweep();
    }
}
