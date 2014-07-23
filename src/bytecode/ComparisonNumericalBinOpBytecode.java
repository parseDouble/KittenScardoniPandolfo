package bytecode;

import types.CodeSignature;
import types.NumericalType;

/**
 * A bytecode which computes a numerical binary comparison operation between
 * the top two elements of the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>comp</i> value2
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparisonNumericalBinOpBytecode extends ComparisonBinOpBytecode {

	/**
	 * Constructs a list of instructions made up of a single bytecode which
	 * computes a numerical binary comparison operation on the top two
	 * elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the top two values of the stack
	 */

	protected ComparisonNumericalBinOpBytecode(CodeSignature where, NumericalType type) {
		super(where,type);
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 *
	 * @return the semantical type of the top two elements of stack
	 */

	public NumericalType getType() {
		return (NumericalType)super.getType();
	}
}