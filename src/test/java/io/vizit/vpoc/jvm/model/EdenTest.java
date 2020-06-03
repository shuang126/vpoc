package io.vizit.vpoc.jvm.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EdenTest {
    @Autowired
    private Eden eden;

    @Test
    void allocate() {
        for (int i = 1; i < 50; i++) {
//            int size = ThreadLocalRandom.current().nextInt(i + 2);
            int size = 1;
            ObjectBO allocate = eden.allocate(i, size);
            assertTrue(eden.getAllocatedObjects().contains(allocate));
            if (i != eden.getAllocatedObjects().size()) {
                assertEquals(i, eden.getAllocatedObjects().size());
            }

        }

    }

    @Test
    void treeMap() {
        PriorityQueue<ObjectBO> allocatedObjects = new PriorityQueue<>(Comparator.comparing(ObjectBO::getSize));
        for (int i = 1; i < 50; i++) {
            ObjectBO e = new ObjectBO(i, 50-i, 1);
            assertTrue(allocatedObjects.add(e));
        }
    }
}