package io.vizit.vpoc.jvm;

import io.vizit.vpoc.jvm.model.Copy;
import io.vizit.vpoc.jvm.model.ObjectBO;
import io.vizit.vpoc.jvm.model.Sweep;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class Monitor {
    public static final String TOPIC_GC_NEW = "/topic/gc/new";
    public static final String TOPIC_GC_MARK = "/topic/gc/mark";
    public static final String TOPIC_GC_COPY = "/topic/gc/copy";
    public static final String TOPIC_GC_SWEEP = "/topic/gc/sweep";
    private final SimpMessageSendingOperations messagingTemplate;

    public Monitor(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void reportNewObject(ObjectBO objectBO) {
        messagingTemplate.convertAndSend(Monitor.TOPIC_GC_NEW, objectBO);
    }

    public void mark(ObjectBO objectBO) {
        messagingTemplate.convertAndSend(Monitor.TOPIC_GC_MARK, objectBO);
    }

    public void copy(Copy copy) {
        messagingTemplate.convertAndSend(Monitor.TOPIC_GC_COPY, copy);
    }

    public void sweep(Sweep objectBO) {
        messagingTemplate.convertAndSend(Monitor.TOPIC_GC_SWEEP, objectBO);
    }
}
