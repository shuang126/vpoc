package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.GcSupervisor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Old {
    private final long capacity = JvmConfig.getOldSize();
    private final GcSupervisor gcSupervisor;

    public Old(GcSupervisor gcSupervisor) {
        this.gcSupervisor = gcSupervisor;
    }

    public void clear() {

    }
}
