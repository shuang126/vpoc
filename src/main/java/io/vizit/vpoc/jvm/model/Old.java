package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Old {
    private final long capacity = JvmConfig.getOldSize();
    private final SimpMessageSendingOperations messagingTemplate;

    public Old(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}
