package bytecode;

/**
 * A sequential bytecode of the intermediate Kitten language which does not call
 * any piece of code.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

import types.CodeSignature;

public abstract class NonCallingSequentialBytecode extends SequentialBytecode implements NonCallingBytecode {

	/**
	 * Constructs a bytecode that does not call anye piece of code.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected NonCallingSequentialBytecode(CodeSignature where) {
		super(where);
	}
}