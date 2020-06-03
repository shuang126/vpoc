package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ObjectBO {
    private long id;
    private int size;

    public ObjectBO(long id, int size) {
        this.id = id;
        this.size = size;
    }

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
