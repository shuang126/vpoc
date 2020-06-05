package io.vizit.vpoc.jvm.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ObjectBO implements Cloneable {
    @EqualsAndHashCode.Include
    private long id;
    private int size;
    private long address;
    private int age;

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void increaseAge() {
        this.age++;
    }
}
