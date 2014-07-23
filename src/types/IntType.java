package types;

import org.apache.bcel.generic.InstructionList;

/**
 * The int type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IntType extends IntegralType {

    /**
     * Builds an int type. This constructor is protected, so that the only
     * int type object is the constant in <tt>Types.Type</tt>.
     */

    protected IntType() {}

    /**
     * Yields a <tt>String</tt> representation of this type.
     *
     * @return the <tt>String</tt> <tt>int</tt>
     */

    public String toString() {
	return "int";
    }

    /**
     * Determines whether this type can be assigned to a given type.
     * Type <tt>int</tt> can only be assigned to itself and to type
     * <tt>float</tt>.
     *
     * @param other what this type should be assigned to
     * @return true if and only if <tt>other</tt> is <tt>int</tt> or
     *         <tt>float</tt>
     */

    public boolean canBeAssignedTo(Type other) {
	// an int can only be assigned to an int or to a float
	return other == Type.INT || other == Type.FLOAT;
    }

    public org.apache.bcel.generic.Type toBCEL() {
	return org.apache.bcel.generic.Type.INT;
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which add two values of this
     * type, namely an <tt>iadd</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void add(InstructionList il) {
	il.append(new org.apache.bcel.generic.IADD());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which multiply two values of this
     * type, namely an <tt>imul</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void mul(InstructionList il) {
	il.append(new org.apache.bcel.generic.IMUL());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which divide two values of this
     * type, namely an <tt>idiv</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void div(InstructionList il) {
	il.append(new org.apache.bcel.generic.IDIV());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which subtract two values of this
     * type, namely an <tt>isub</tt> bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void sub(InstructionList il) {
	il.append(new org.apache.bcel.generic.ISUB());
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which negate a value of this
     * type, namely, an <tt>ineg</tt> Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     */

    public void neg(InstructionList il) {
	il.insert(new org.apache.bcel.generic.INEG());
    }
}