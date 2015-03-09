package bytecode;

import types.CodeSignature;

/**
 * A bytecode which computes a binary Boolean operation on two Boolean
 * elements on top of the stack and yields the result on the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BooleanBinOpBytecode extends BinOpBytecode {

	/**
	 * Constructs a list of instructions made up of a single bytecode which
	 * computes a binary Boolean operation on the top two Boolean
	 * elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected BooleanBinOpBytecode(CodeSignature where) {
		super(where);
	}

	protected int hashCodeAux() {
		return 1;
	}

	public boolean equalsAux(Object other) {
		return true;
	}
}
