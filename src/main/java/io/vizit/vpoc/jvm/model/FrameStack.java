package io.vizit.vpoc.jvm.model;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class FrameStack {
    Deque<Frame> frameStack = new ArrayDeque<>();
}
