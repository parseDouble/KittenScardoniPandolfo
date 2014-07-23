package types;

import java.util.HashSet;

/**
 * The void type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VoidType extends Type {

    /**
     * Builds a <tt>void</tt> type.
     * This constructor is protected, so that the only void type object
     * is the constant in <tt>Types.Type</tt>.
     */

    protected VoidType() {}

    /**
     * Yields a <tt>String</tt> representation of this type.
     *
     * @return the <tt>String</tt> <tt>void</tt>
     */

    public String toString() {
	return "void";
    }

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a value of this type.
     *
     * @return 0
     */

    public int getSize() {
	return 0;
    }

    /**
     * Determines whether this void type can be assigned to a given type.
     * This is always false since type <tt>void</tt> cannot be assigned
     * to any type, not even to itself.
     *
     * @param other what this type should be assigned to
     * @return false
     */

    public boolean canBeAssignedTo(Type other) {
	return false;
    }

    /**
     * Determines whether this void type can be assigned to a given type, by
     * assuming that primitive types can only be assigned to the same,
     * identical, primitive type. Moreover, it assumes that <tt>void</tt>
     * can be assigned to <tt>void</tt>.
     *
     * @param other what this type should be assigned to
     * @return true if <tt>other</tt> is a <tt>VoidType</tt>, false otherwise
     */

    public boolean canBeAssignedToSpecial(Type other) {
	return this == other;
    }

    /**
     * Translates a Kitten type into its BCEL equivalent.
     *
     * @return the Kitten <tt>VOID</tt> type
     */

    public org.apache.bcel.generic.Type toBCEL() {
	return org.apache.bcel.generic.Type.VOID;
    }
}
