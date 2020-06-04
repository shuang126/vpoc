package io.vizit.vpoc.jvm;

import io.vizit.vpoc.jvm.model.Copy;
import io.vizit.vpoc.jvm.model.ObjectBO;
import io.vizit.vpoc.jvm.model.Sweep;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class Monitor {
    public static final String TOPIC_GC_NEW = "/topic/gc/new";
    public static final String TOPIC_GC_MARK = "/topic/gc/mark";
    public static final String TOPIC_GC_COPY = "/topic/gc/copy";
    public static final String TOPIC_GC_SWEEP = "/topic/gc/sweep";
    private final SimpMessageSendingOperations messagingTemplate;
    private int delay = 1000;

    public Monitor(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void reportNewObject(ObjectBO objectBO) {
        report(objectBO, TOPIC_GC_NEW);
    }

    public void mark(ObjectBO objectBO) {
        report(objectBO, TOPIC_GC_MARK);
    }

    public void copy(Copy copy) {
        report(copy, TOPIC_GC_COPY);
    }

    public void sweep(Sweep sweep) {
        report(sweep, TOPIC_GC_SWEEP);
    }

    private void report(Object object, String topic) {
        try {
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(topic, object);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
