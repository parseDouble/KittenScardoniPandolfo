package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * A numerical type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class NumericalType extends PrimitiveType {

    /**
     * Builds a numerical type.
     */

    protected NumericalType() {}

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is less than the element
     * at the top of the stack.
     * In this case, it adds an <tt>if_icmplt</tt> Java bytecode.
     * This method is redefined for the float type.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmplt(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPLT(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is greater than the element
     * at the top of the stack.
     * In this case, it adds an <tt>if_icmpgt</tt> Java bytecode.
     * This method is redefined for the float type.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpgt(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPGT(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is less than or equal to
     * the element at the top of the stack.
     * In this case, it adds an <tt>if_icmple</tt> Java bytecode.
     * This method is redefined for the float type.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmple(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPLE(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is greater than or equal to
     * the element at the top of the stack.
     * In this case, it adds an <tt>if_icmpge</tt> Java bytecode.
     * This method is redefined for the float type.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpge(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPGE(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which add two values of this
     * type.
     *
     * @param il the list of instructions which must be expanded
     */

    public abstract void add(InstructionList il);

    /**
     * Adds to <tt>il</tt> the Java bytecodes which multiply two values of this
     * type.
     *
     * @param il the list of instructions which must be expanded
     */

    public abstract void mul(InstructionList il);

    /**
     * Adds to <tt>il</tt> the Java bytecodes which divide two values of this
     * type.
     *
     * @param il the list of instructions which must be expanded
     */

    public abstract void div(InstructionList il);

    /**
     * Adds to <tt>il</tt> the Java bytecodes which subtract two values of this
     * type.
     *
     * @param il the list of instructions which must be expanded
     */

    public abstract void sub(InstructionList il);

    /**
     * Adds to <tt>il</tt> the Java bytecodes which negate a value of this
     * type.
     *
     * @param il the list of instructions which must be expanded
     */

    public abstract void neg(InstructionList il);
}
