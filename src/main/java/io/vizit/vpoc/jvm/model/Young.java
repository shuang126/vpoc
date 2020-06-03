package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
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
    private final Monitor monitor;

    public Young(Eden eden, Survivor from, Survivor to, SimpMessageSendingOperations messagingTemplate, Monitor monitor) {
        this.eden = eden;
        this.from = from;
        this.to = to;
        this.monitor = monitor;
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
