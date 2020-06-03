package io.vizit.vpoc.jvm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Copy {
    private SpaceEnum from;
    private SpaceEnum to;
    private ObjectBO objectBO;
    private long address;
}
