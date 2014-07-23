package bytecode;

import types.CodeSignature;
import types.ReferenceType;

/**
 * A bytecode that creates an object.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CreationBytecode extends NonCallingSequentialBytecode {

	/**
	 * Builds a bytecode that creates an object.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected CreationBytecode(CodeSignature where) {
		super(where);
	}

	/**
	 * Yields the type of the value which is created by this bytecode.
	 *
	 * @return the type of the value created by this bytecode
	 */

	public abstract ReferenceType createdType();
}