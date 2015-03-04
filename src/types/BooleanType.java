package types;

/**
 * The Boolean type of the Kitten language.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BooleanType extends PrimitiveType {

	protected BooleanType() {
		// this constructor is protected, the only Boolean type object is Types.Type
	}

	@Override
	public String toString() {
		return "boolean";
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other == Type.BOOLEAN;
	}

	@Override
	public org.apache.bcel.generic.Type toBCEL() {
		return org.apache.bcel.generic.Type.BOOLEAN;
	}
}