package bytecode;

import generateJB.KittenClassGen;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;

/**
 * A bytecode which performs a logical <i>or</i> operation on the top
 * two elements of the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>or</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class OR extends BooleanBinOpBytecode {

	/**
	 * Constructs a bytecode which performs a logical <i>or</i> operation
	 * on the top two elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public OR(CodeSignature where) {
		super(where);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>ior</tt> bytecode
	 */

	@Override
	public InstructionList generateJB(KittenClassGen classGen) {
		return new InstructionList(new org.apache.bcel.generic.IOR());
	}
}
