package types;

/**
 * An integral numerical type, that is, a numerical type whose values
 * are discrete.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class IntegralType extends NumericalType {

    /**
     * Builds an integral type.
     */

    protected IntegralType() {}

    /**
     * Determines whether this type can be assigned to a given type.
     *
     * @param other what this type should be assigned to
     * @return true if and only if <tt>other</tt> is <tt>this</tt>
     */

    public boolean canBeAssignedTo(Type other) {
	return other == this;
    }
}
