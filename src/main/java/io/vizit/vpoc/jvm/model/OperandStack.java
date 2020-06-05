package io.vizit.vpoc.jvm.model;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Each frame (§2.6) contains a last-in-first-out (LIFO) stack known as its operand stack.
 * The maximum depth of the operand stack of a frame is determined at compile-time
 * and is supplied along with the code for the method associated with the frame (§4.7.3).
 *
 * The operand stack is empty when the frame that contains it is created.
 * The Java Virtual Machine supplies instructions to load constants or values
 * from local variables or fields onto the operand stack.
 * Other Java Virtual Machine instructions take operands from the operand stack,
 * operate on them, and push the result back onto the operand stack.
 * The operand stack is also used to prepare parameters to be passed to methods
 * and to receive method results.
 */

@Component
public class OperandStack {
    Deque<Operand> operandStack = new ArrayDeque<>();

}
