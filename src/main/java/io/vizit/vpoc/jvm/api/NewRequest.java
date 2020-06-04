package io.vizit.vpoc.jvm.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewRequest {
    private int size;
    private int count = 1;
    private int randomSizeMax;
    private int delay = 100; // ms
}
