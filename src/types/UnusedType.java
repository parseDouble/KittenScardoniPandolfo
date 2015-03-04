package types;

/**
 * A type marking an unused element in a list of types.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class UnusedType extends Type {

	/**
	 * Builds an unused type. This constructor is protected, so that the only
	 * unused type object is the constant in {@code Types.Type}.
	 */

	protected UnusedType() {}

	@Override
	public String toString() {
		return "unused";
	}

	/**
	 * Determines whether this type can be assigned to a given type.
	 * Type {@code unused} cannot be assigned to anything.
	 *
	 * @param other what this type should be assigned to
	 * @return false
	 */

	@Override
	public boolean canBeAssignedTo(Type other) {
		return false;
	}

	/**
	 * Computes the least common supertype of a given type and this type.
	 * That is, a common supertype which is the least amongst all possible
	 * supertypes.
	 *
	 * @param other the type whose least supertype with this type must be found
	 * @return {@code other}
`     */

	@Override
	public Type leastCommonSupertype(Type other) {
		return other;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return null;
	}
}