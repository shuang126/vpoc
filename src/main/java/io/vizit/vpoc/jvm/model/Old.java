package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class Old {
    private final long capacity = JvmConfig.getOldSize();
    private final GcSupervisor gcSupervisor;
    private AtomicInteger allocatedPointer = new AtomicInteger(0);
    List<ObjectBO> allocatedObjects = new ArrayList<>();
    List<ObjectBO> liveObjects = new ArrayList<>();

    public Old(GcSupervisor gcSupervisor) {
        this.gcSupervisor = gcSupervisor;
    }

    public synchronized void promotion(SpaceEnum from, ObjectBO copy) {
        copy.setAddress(allocatedPointer.get());
        allocatedObjects.add(copy);
        allocatedPointer.addAndGet(copy.getSize());
        gcSupervisor.promotion(new Copy(from, SpaceEnum.OLD, copy));
    }

    public synchronized void clear() {

    }
}
