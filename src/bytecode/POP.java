package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which pops the top value of the stack.
 * <br><br>
 * ..., value -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class POP extends NonCallingSequentialBytecode {

	/**
	 * The type of the value which is popped from the stack.
	 */

	private Type type;

	/**
	 * Constructs a bytecode which pops the top value of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the type of the value which is popped from the stack
	 */

	public POP(CodeSignature where, Type type) {
		super(where);

		this.type = type;
	}

	/**
	 * Yields the type of the value which is popped off the stack.
	 *
	 * @return the type of the value which is popped off the stack
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "pop " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return a Java <tt>pop</tt> bytecode
	 */

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		return new InstructionList(new org.apache.bcel.generic.POP());
	}

	protected int hashCodeAux() {
		return type.hashCode();
	}

	public boolean equalsAux(Object other) {
		return ((POP)other).type == type;
	}
}