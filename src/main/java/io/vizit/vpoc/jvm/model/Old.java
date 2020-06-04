package io.vizit.vpoc.jvm.model;

import io.vizit.vpoc.jvm.Monitor;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Old {
    private final long capacity = JvmConfig.getOldSize();
    private final Monitor monitor;

    public Old(Monitor monitor) {
        this.monitor = monitor;
    }

    public void clear() {

    }
}
