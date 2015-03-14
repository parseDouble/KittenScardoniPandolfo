package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which duplicates the top element of the stack.
 * <br><br>
 * ..., value -> ..., value, value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class DUP extends NonCallingSequentialBytecode {

	/**
	 * The type of the element which is duplicated.
	 */

	private Type type;

	/**
	 * Constructs a bytecode which duplicates the top element of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the type of the element which is duplicated
	 */

	public DUP(CodeSignature where, Type type) {
		super(where);

		this.type = type;
	}

	@Override
	public String toString() {
		return "dup " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>dup</tt> bytecode
	 */

	public InstructionList generateJB(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.DUP());
	}

	protected int hashCodeAux() {
		return type.hashCode();
	}

	public boolean equalsAux(Object other) {
		return ((DUP)other).type == type;
	}
}