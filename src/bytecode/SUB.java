package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.NumericalType;

/**
 * A bytecode which subtracts the top element of the stack from the
 * underlying element.
 * <br><br>
 * ..., value1, value2 -> ..., value1 - value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class SUB extends ArithmeticBinOpBytecode {

	/**
	 * Constructs a bytecode which
	 * subtracts the top element of the stack from the underlying element.
	 *
	 * @param type the semantical type of the values which are subtracted
	 */

	public SUB(NumericalType type) {
		super(type);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the <tt>isub</tt> Java bytecode if <tt>type</tt> is <tt>int</tt>
	 *         and the <tt>fsub</tt> Java bytecode if <tt>type</tt> is
	 *         <tt>float</tt>
	 */

	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		getType().sub(il);

		return il;
	}
}