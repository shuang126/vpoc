package io.vizit.vpoc.jvm.model;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Eden extends Space {

    public Eden(long capacity) {
        super(capacity);
    }

    public void mark() {
        int count = ThreadLocalRandom.current().nextInt(1, 5);
        for (int i = 0; i < count; i++) {
            liveObjects.add(allocatedObjects.pollFirst());
        }
    }
}
