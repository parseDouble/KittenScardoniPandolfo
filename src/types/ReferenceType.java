package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A reference type, <i>i.e.</i>, a type whose values can be <i>referenced</i>
 * from a pointer, such as classes and arrays.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ReferenceType extends ComparableType {

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a value of this type.
     */

    public final static int SIZE = 1;

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a value of this type.
     *
     * @return 1
     */

    public final int getSize() {
	return SIZE;
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are equal.
     * In this case, it adds an <tt>if_acmpeq</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ACMPEQ(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are not equal.
     * In this case, it adds an <tt>if_acmpne</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ACMPNE(yes));
    }
}
