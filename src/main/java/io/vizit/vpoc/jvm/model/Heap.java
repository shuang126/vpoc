package io.vizit.vpoc.jvm.model;

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
    private final SimpMessageSendingOperations messagingTemplate;

    public Heap(Young young, Old old, SimpMessageSendingOperations messagingTemplate) {
        this.young = young;
        this.old = old;
        this.messagingTemplate = messagingTemplate;
    }

    public ObjectBO allocate(int size) {
        return young.allocate(sequence.getAndIncrement(), size);
    }
}
