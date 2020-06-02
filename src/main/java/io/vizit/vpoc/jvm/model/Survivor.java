package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class Survivor extends Space {
    private boolean empty = true;

    public Survivor(long capacity) {
        super(capacity);
    }

    public void mark() {
        int count = ThreadLocalRandom.current().nextInt(1, 3);
        for (int i = 0; i < count; i++) {
            liveObjects.add(allocatedObjects.pollFirst());
        }
    }
}
