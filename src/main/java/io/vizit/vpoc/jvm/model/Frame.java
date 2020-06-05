package io.vizit.vpoc.jvm.model;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * https://docs.oracle.com/javase/specs/jvms/se14/html/jvms-2.html#jvms-2.5
 * A frame is used to store data and partial results, as well as to perform dynamic linking,
 * return values for methods, and dispatch exceptions.
 *
 * A new frame is created each time a method is invoked.
 * A frame is destroyed when its method invocation completes,
 * whether that completion is normal or abrupt (it throws an uncaught exception).
 * Frames are allocated from the Java Virtual Machine stack (§2.5.2) of the thread
 * creating the frame. Each frame has its own array of local variables (§2.6.1),
 * its own operand stack (§2.6.2), and a reference to the run-time constant pool (§2.5.5)
 * of the class of the current method.
 *
 */

@Component
public class Frame {
    /**
     * The Java Virtual Machine uses local variables to pass parameters on method invocation.
     * On class method invocation, any parameters are passed in consecutive local variables starting from local variable 0.
     * On instance method invocation, local variable 0 is always used to pass a reference to the object
     * on which the instance method is being invoked (this in the Java programming language).
     * Any parameters are subsequently passed in consecutive local variables starting from local variable 1.
     */
    private List<LocalVariable> localVariables;
    /**
     * The operand stack is empty when the frame that contains it is created.
     */
    private OperandStack operandStack;
    /**
     * Each frame (§2.6) contains a reference to the run-time constant pool (§2.5.5) for
     * the type of the current method to support dynamic linking of the method code.
     */
    private DynamicLinking dynamicLinking;
}
