package io.vizit.vpoc.jvm.model;

import lombok.*;

import javax.annotation.concurrent.Immutable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Immutable
public class ObjectBO   {
    @EqualsAndHashCode.Include
    private long id;
    private int size;
    private long address;
    private int age;
}
