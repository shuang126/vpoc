package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Component
public class Eden {
    protected long capacity = JvmConfig.getEdenSize();
    protected TreeSet<ObjectBO> allocatedObjects = new TreeSet<>();
    protected TreeSet<ObjectBO> liveObjects = new TreeSet<>();
    private final SimpMessageSendingOperations messagingTemplate;

    public Eden(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public synchronized ObjectBO allocate(long id, int size) {
        ObjectBO objectBO = new ObjectBO(id, size);
        allocatedObjects.add(objectBO);
        return objectBO;
    }

    public boolean available(int size) {
        return allocatedObjects.size() + size < capacity;
    }

    public void sweep() {
        allocatedObjects.clear();
        liveObjects.clear();
    }

    public void mark() {
        int count = ThreadLocalRandom.current().nextInt(1, 5);
        for (int i = 0; i < count; i++) {
            liveObjects.add(allocatedObjects.pollFirst());
        }
    }
}
