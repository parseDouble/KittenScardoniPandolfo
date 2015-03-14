package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.NumericalType;

/**
 * A bytecode which adds the top two elements of the stack.
 * <br><br>
 * ..., value1, value2 -> ..., value1 + value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ADD extends ArithmeticBinOpBytecode {

	/**
	 * Constructs a bytecode which adds the top two elements of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the values which are added
	 */

	public ADD(CodeSignature where, NumericalType type) {
		super(where,type);
	}

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		getType().add(il);

		return il;
	}
}