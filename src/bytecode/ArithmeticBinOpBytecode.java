package bytecode;

import types.CodeSignature;
import types.NumericalType;

/**
 * A bytecode which computes a binary arithmetic operation on the top two
 * elements of the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ArithmeticBinOpBytecode extends BinOpBytecode {

    /**
     * The semantical type of the top two elements of the stack.
     * This can only be a <tt>NumericalType</tt>.
     */

	private NumericalType type;

	/**
	 * Constructs a list of instructions made up of a single bytecode which
	 * computes a binary arithmetic operation on the top two
	 * elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the top two values of the stack
	 */

	protected ArithmeticBinOpBytecode(CodeSignature where, NumericalType type) {
		super(where);

		this.type = type;
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 * This can only be <tt>int</tt> or <tt>float</tt>.
	 *
	 * @return the semantical type of the top two elements of stack
	 */

	public NumericalType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}

	protected int hashCodeAux() {
		return type.hashCode();
	}

	public boolean equalsAux(Object other) {
		return ((ArithmeticBinOpBytecode)other).type == type;
	}
}