package bytecode;

import types.CodeSignature;

/**
 * A bytecode which performs a binary operation on the top two elements of
 * the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>op</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BinOpBytecode extends NonCallingSequentialBytecode {

	/**
	 * Constructs a list of instructions made up of a single bytecode which
	 * performs a binary operation on the top two elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected BinOpBytecode(CodeSignature where) {
		super(where);
	}
}