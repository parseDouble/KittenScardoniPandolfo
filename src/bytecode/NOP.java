package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;

/**
 * A bytecode which does not do anything.
 * <br><br>
 * ... -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NOP extends NonCallingSequentialBytecode {

	/**
	 * Constructs a bytecode which does not do anything.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public NOP(CodeSignature where) {
		super(where);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>nop</tt> bytecode
	 */

	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.NOP());
	}
}