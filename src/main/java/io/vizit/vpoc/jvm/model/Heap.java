package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Component
public class Heap {
    private long capacity = 240000;
    private final Young young;
    private final Old old;
    private AtomicLong sequence = new AtomicLong(1);
    private final Monitor monitor;

    public Heap(Young young, Old old, SimpMessageSendingOperations messagingTemplate, Monitor monitor) {
        this.young = young;
        this.old = old;
        this.monitor = monitor;
    }

    public ObjectBO allocate(int size) {
        return young.allocate(sequence.getAndIncrement(), size);
    }
}
