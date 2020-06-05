package io.vizit.vpoc.jvm.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A single local variable can hold a value of type boolean,
 * byte, char, short, int, float, reference, or returnAddress.
 * A pair of local variables can hold a value of type long or double.
 */
@Component
@Scope("prototype")
public class LocalVariable {
}
