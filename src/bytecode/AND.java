package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;

/**
 * A bytecode which performs a logical <i>and</i> operation on the top
 * two elements of the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 <i>and</i> value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class AND extends BooleanBinOpBytecode {

	/**
	 * Constructs a bytecode which performs a logical <i>and</i> operation
	 * on the top two elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public AND(CodeSignature where) {
		super(where);
	}

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.IAND());
	}
}