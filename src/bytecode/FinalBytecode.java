package bytecode;

/**
 * A bytecode of the intermediate Kitten language with no subsequent bytecode.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

import types.CodeSignature;

public abstract class FinalBytecode extends NonBranchingBytecode implements NonCallingBytecode {

	/**
	 * Constructs a final bytecode.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected FinalBytecode(CodeSignature where) {
		super(where);
	}
}