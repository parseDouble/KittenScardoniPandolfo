package types;

/**
 * A type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Type {

    /**
     * The Boolean type. Always use this constant to refer to the Boolean type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static BooleanType BOOLEAN = new BooleanType();

    /**
     * The float type. Always use this constant to refer to the float type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static FloatType FLOAT = new FloatType();

    /**
     * The integer type. Always use this constant to refer to the integer type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static IntType INT = new IntType();

    /**
     * The nil type. Always use this constant to refer to the nil type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static NilType NIL = new NilType();

    /**
     * The void type. Always use this constant to refer to the void type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static VoidType VOID = new VoidType();

    /**
     * The unused type. Always use this constant to refer to the unused type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static UnusedType UNUSED = new UnusedType();

    /**
     * The top of the hierarchy of the reference types.
     */

    private static ClassType objectType;

    /**
     * Builds a type object.
     */

    protected Type() {
    }

    /**
     * Yields the top type of the hierarchy of the reference types.
     *
     * @return the top type
     */

    public static final ClassType getObjectType() {
	return objectType;
    }

    /**
     * Sets the top type of the hierarchy of the reference types.
     */

    protected static final void setObjectType(ClassType objectType) {
	Type.objectType = objectType;
    }

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a value of this type. This is always 1 for Kitten types which
     * are not <tt>void</tt>.
     *
     * @return 1
     */

    public int getSize() {
	return 1;
    }

    /**
     * Computes a string representation of the type.
     *
     * @return the string representing the type
     */

    abstract public String toString();

    /**
     * Determines whether this type can be assigned to a given type.
     * Type <tt>void</tt> cannot be assigned to any type, not even
     * to itself.
     *
     * @param other what this type should be assigned to
     * @return true if the assignment is possible, false otherwise
     */

    abstract public boolean canBeAssignedTo(Type other);

    /**
     * Determines whether this is a (non-strict) subtype of a given type.
     * It is exactly the same as <tt>canBeAssignedTo</tt>.
     *
     * @param other what this type should be a (non-strict) subtype of
     * @return true if this type is a (non-strict) subtype of <tt>other</tt>,
     *         false otherwise
     */

    public final boolean subtypeOf(Type other) {
	return canBeAssignedTo(other);
    }

    /**
     * Determines whether this type can be assigned to a given type, by
     * assuming that primitive types can only be assigned to the same,
     * identical, primitive type. Moreover, it assumes that <tt>void</tt>
     * is a subtype of <tt>void</tt>.
     *
     * @param other what this type should be assigned to
     * @return true if the assignment is possible, false otherwise
     */

    public boolean canBeAssignedToSpecial(Type other) {
	// primitive types should redefine this
	return canBeAssignedTo(other);
    }

    /**
     * Computes the least common supertype of a given type and this type.
     * That is, a common supertype which is the least amongst all possible
     * supertypes.
     *
     * @param other the type whose least supertype with this type must be found
     * @return the least common supertype of this type and <tt>other</tt>,
     *         if it exists, <tt>null</tt> otherwise (for instance, there
     *         is no least common supertype between <tt>boolean</tt> and
     *         <tt>int</tt>)
     */

    public Type leastCommonSupertype(Type other) {
	// the  least upper bound with an unused type is <tt>this</tt>
	if (other == Type.UNUSED) return this;

	// this works fine for primitive types. Class and array types
	// will have to redefine this behaviour
	if (this.canBeAssignedTo(other)) return other;
	if (other.canBeAssignedTo(this)) return this;

	// there is no least common supertype
	return null;
    }

    /**
     * Checks equality of two types.
     *
     * @param other the type to be compared with this type
     * @return true if this type is the same as <tt>other</tt>, false otherwise
     */

    public final boolean equals(Type other) {
	// this is fine since types can only be created from the constants above
	return this == other;
    }

    /**
     * Translates a Kitten type into its BCEL equivalent.
     *
     * @return the BCEL type corresponding to this Kitten type
     */

    public abstract org.apache.bcel.generic.Type toBCEL();
}