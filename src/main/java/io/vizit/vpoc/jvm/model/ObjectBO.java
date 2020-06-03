package io.vizit.vpoc.jvm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class ObjectBO {
    private long id;
    private int size;
    private long address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectBO objectBO = (ObjectBO) o;
        return id == objectBO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
