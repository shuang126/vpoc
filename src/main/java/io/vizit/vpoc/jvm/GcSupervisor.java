package io.vizit.vpoc.jvm;

import com.google.common.collect.ImmutableMap;
import io.vizit.vpoc.jvm.model.Copy;
import io.vizit.vpoc.jvm.model.ObjectBO;
import io.vizit.vpoc.jvm.model.Sweep;
import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Setter
@Getter
public class GcSupervisor {
    public static final String TOPIC_GC_NEW = "/topic/gc/new";
    public static final String TOPIC_GC_MARK = "/topic/gc/mark";
    public static final String TOPIC_GC_COPY = "/topic/gc/copy";
    public static final String TOPIC_GC_SWEEP = "/topic/gc/sweep";
    public static final String TOPIC_GC_PROMOTION = "/topic/gc/promotion";
    public static final String TOPIC_SUPERVISOR_PAUSE = "/topic/supervisor/pause";
    private final SimpMessageSendingOperations messagingTemplate;
    private int delay = 1000;
    private boolean debug; // pause before mark, copy, sweep
    private Lock lock = new ReentrantLock();
    private Condition go = lock.newCondition();

    public GcSupervisor(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void reportNewObject(ObjectBO objectBO) {
        report(objectBO, TOPIC_GC_NEW);
    }

    public void mark(ObjectBO objectBO) {
        try {
            debug(String.format("mark %s", objectBO));
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(TOPIC_GC_MARK, objectBO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void debug(String msg) {
        if (debug) {
            lock.lock();
            try {
                messagingTemplate.convertAndSend(TOPIC_SUPERVISOR_PAUSE, ImmutableMap.of("msg", msg));
                go.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public void copy(Copy copy) {
        debug(String.format("copy %s", copy.getObjectBO()));
        report(copy, TOPIC_GC_COPY);
    }

    public void promotion(Copy copy) {
        debug(String.format("promotion %s", copy.getObjectBO()));
        report(copy, TOPIC_GC_PROMOTION);
    }

    public void sweep(Sweep sweep) {
        try {
            debug(String.format("sweep %s", sweep));
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(TOPIC_GC_SWEEP, sweep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void report(Object object, String topic) {
        try {
            Thread.sleep(delay);
            messagingTemplate.convertAndSend(topic, object);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean go() {
        step();
        debug = false;
        return true;
    }

    public Boolean step() {
        if (debug) {
            lock.lock();
            go.signal();
            lock.unlock();
        }
        return debug;
    }
}
