package types;

/**
 * The Boolean type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BooleanType extends PrimitiveType {
    protected BooleanType() {
	// this constructor is protected, so that the only Boolean type object
	// is the constant in <tt>Types.Type</tt>
    }

    public String toString() {
	return "boolean";
    }

    // un valore booleano puo' essere assegnato solo a una variabile booleana
    public boolean canBeAssignedTo(Type other) {
	return other == Type.BOOLEAN;
    }

    public org.apache.bcel.generic.Type toBCEL() {
        return org.apache.bcel.generic.Type.BOOLEAN;
    }
}
