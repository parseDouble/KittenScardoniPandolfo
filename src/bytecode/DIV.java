package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.NumericalType;

/**
 * A bytecode which divides the top element of the stack by the
 * underlying element.
 * <br><br>
 * ..., value1, value2 -> ..., value1 / value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class DIV extends ArithmeticBinOpBytecode {

	/**
	 * Constructs a bytecode that
	 * divides the top element of the stack by the underlying element.
	 *
	 * @param type the semantical type of the values which are subtracted
	 */

	public DIV(NumericalType type) {
		super(type);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the <tt>idiv</tt> Java bytecode if <tt>type</tt> is <tt>int</tt>
	 *         and the <tt>fdiv</tt> Java bytecode if <tt>type</tt> is
	 *         <tt>float</tt>
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionList il = new InstructionList();

		getType().div(il);

		return il;
	}
}