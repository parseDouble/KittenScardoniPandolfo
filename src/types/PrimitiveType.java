package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

import java.util.HashSet;

/**
 * A primitive (non-reference) type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class PrimitiveType extends ComparableType {
    protected PrimitiveType() {}

    /**
     * Determines whether this type can be assigned to a given type, by
     * assuming that primitive types can only be assigned to the same,
     * identical, primitive type. Moreover, it assumes that <tt>void</tt>
     * is a subtype of <tt>void</tt>.
     *
     * @param other what this type should be assigned to
     * @return true if this type is the same as <tt>other</tt>, false otherwise
     */

    public boolean canBeAssignedToSpecial(Type other) {
	return this == other;
    }

    /**
     * Determines if this type is open, that is, it is a class type
     * whose code cannot be accessed, or can reach an open type.
     *
     * @return <tt>false</tt>
     */

    public boolean isOpen() {
	return false;
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are equal.
     * In this case, it adds an <tt>if_icmpeq</tt> Java bytecode, since
     * Booleans are considered integers in the Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpeq(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPEQ(yes));
    }

    /**
     * Adds to <tt>il</tt> the Java bytecodes which go to <tt>yes</tt>
     * if the the top two elements of the stack are not equal.
     * In this case, it adds an <tt>if_icmpne</tt> Java bytecode, since
     * Booleans are considered integers in the Java bytecode.
     *
     * @param il the list of instructions which must be expanded
     * @param yes to place where to jump
     */

    public void JB_if_cmpne(InstructionList il, InstructionHandle yes) {
	il.append(new org.apache.bcel.generic.IF_ICMPNE(yes));
    }
}
