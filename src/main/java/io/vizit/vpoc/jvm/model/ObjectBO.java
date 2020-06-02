package io.vizit.vpoc.jvm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectBO {
    private long id;
    private int size;

    public ObjectBO(long id, int size) {
        this.id = id;
        this.size = size;
    }
}
