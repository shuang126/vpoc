package io.vizit.vpoc.jvm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Copy {
    private SpaceEnum from;
    private SpaceEnum to;
    private ObjectBO objectBO;
}
