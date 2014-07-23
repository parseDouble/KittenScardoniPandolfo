package types;

import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;

import java.util.HashSet;

/**
 * The type of the <tt>nil</tt> constant of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NilType extends PrimitiveType {

    /**
     * Builds a <tt>nil</tt> type.
     * This constructor is protected, so that the only <tt>nil</tt> type object
     * is the constant in <tt>Types.Type</tt>.
     */

    protected NilType() {}

    /**
     * Yields a <tt>String</tt> representation of this type.
     *
     * @return the <tt>String</tt> <tt>nil</tt>
     */

    public String toString() {
	return "nil";
    }

    /**
     * Determines whether this nil type can be assigned to a given type.
     * To this purpose, the latter must be a nil or reference type.
     *
     * @param other what this nil type should be assigned to
     * @return true if <tt>other</tt> is a class or array type, false otherwise
     */

    public boolean canBeAssignedTo(Type other) {
	return other == this || other instanceof ReferenceType;
    }

    /**
     * Computes the least common supertype of a given type and this type.
     * Namely, it yields <tt>other</tt> is the latter is a <tt>NilType</tt> or
     * a <tt>ReferenceType</tt>. It yields <tt>null</tt> otherwise.
     *
     * @param other the type whose least supertype with this type must be found
     * @return the least common supertype of this type and <tt>other</tt>,
     *         if it exists, <tt>null</tt> otherwise
     */

    public Type leastCommonSupertype(Type other) {
	if (other == this || other instanceof ReferenceType) return other;
	else return null;
    }

    /**
     * Auxiliary method that adds, to a given set, the set of types
     * <i>reachable</i> from <tt>this</tt>
     * <i>i.e.</i>, the type <tt>this</tt> itself, all types which
     * occur inside <tt>this</tt>, recursively, and their subtypes.
     * Since the set of subtypes of a given type might be infinite,
     * this method adds a finite set of types. Their downwards closure
     * is the set of types reachable from <tt>this</tt>.
     * For the <tt>nil</tt> type, this method just adds the type
     * <tt>this</tt> itself.
     *
     * @param reachable the set where the rechable types must be added
     */

    public void reachableTypes$0(HashSet<Type> reachable) {
	reachable.add(this);
    }

    public org.apache.bcel.generic.Type toBCEL() {
	return org.apache.bcel.generic.Type.NULL;
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
