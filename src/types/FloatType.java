package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

/**
 * The float type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FloatType extends NumericalType {

    /**
     * Builds a float type. This constructor is protected, so that the only
     * float type object is the constant in <tt>Types.Type</tt>.
     */

    protected FloatType() {}

    /**
     * Yields a <tt>String</tt> representation of this type.
     *
     * @return the <tt>String</tt> <tt>float</tt>
     */

    public String toString() {
	return "float";
    }

    /**
     * Determines if this type can be assigned to <tt>other</tt>.
     * This is only true if <tt>other</tt> is a <tt>float</tt>.
     *
     * @param other the other type
     * @return true if and only if <tt>other</tt> is <tt>float</tt>
     */

    public boolean canBeAssignedTo(Type other) {
	// a float can only be assigned to another float
	return this == other;
    }

    public org.apache.bcel.generic.Type toBCEL() {
	return org.apache.bcel.generic.Type.FLOAT;
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are equal.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>ifeq</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFEQ(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are not equal.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>ifne</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFNE(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is less than the element
     * at the top of the stack.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>iflt</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmplt(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFLT(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is greater than the element
     * at the top of the stack.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>ifgt</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpgt(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFGT(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is less than or equal to
     * the element at the top of the stack.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>ifle</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmple(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFLE(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the element under the top of the stack is greater than or equal to
     * the element at the top of the stack.
     * In this case, it adds an <tt>fcmpl</tt> Java bytecode followed
     * by an <tt>ifge</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpge(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.FCMPL());
	il.append(new org.apache.bcel.generic.IFGE(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which add two values of this
     * type, namely an <tt>fadd</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void add(InstructionList il) {
	il.append(new org.apache.bcel.generic.FADD());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which multiply two values of this
     * type, namely an <tt>fmul</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void mul(InstructionList il) {
	il.append(new org.apache.bcel.generic.FMUL());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which divide two values of this
     * type, namely an <tt>fdiv</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void div(InstructionList il) {
	il.append(new org.apache.bcel.generic.FDIV());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which subtract two values of this
     * type, namely an <tt>fsub</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void sub(InstructionList il) {
	il.append(new org.apache.bcel.generic.FSUB());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which negate a value of this
     * type, namely, an <tt>fneg</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void neg(InstructionList il) {
	il.insert(new org.apache.bcel.generic.FNEG());
    }
}
