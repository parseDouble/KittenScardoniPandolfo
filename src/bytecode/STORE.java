package bytecode;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.CodeSignature;
import types.Type;

/**
 * A bytecode which stores the top of the stack inside a local variable.
 * <br><br>
 * ..., value -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class STORE extends NonCallingSequentialBytecode implements LocalModifier {

	/**
	 * The number of the local variable which is modified.
	 */

	private int varNum;

	/**
	 * The type of the value stored in the local variable.
	 */

	private Type type;

	/**
	 * Constructs a bytecode
	 * which stores the value on top of the stack inside a local variable,
	 * followed by the given list of instructions.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param varNum the number of the local variable which is modified
	 * @param type the type of the value stored inside the local variable
	 */

	public STORE(CodeSignature where, int varNum, Type type) {
		super(where);

		this.varNum = varNum;
		this.type = type;
	}

	@Override
	public int getVarNum() {
		return varNum;
	}

	/**
	 * Yields the type of the local variable which is modified.
	 *
	 * @return the type of the local variable which is modified
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "store " + varNum + " of type " + type;
	}

	protected int hashCodeAux() {
		return varNum * type.hashCode();
	}

	public boolean equalsAux(Object other) {
		return ((STORE)other).varNum == varNum && ((STORE)other).type == type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>istore varNum</tt>, <tt>fstore varNum</tt> or
	 *         <tt>astore varNum</tt> bytecode, if <tt>type</tt> is
	 *         <tt>int</tt>, <tt>float</tt> or a class or array type,
	 *         respectively.
	 */

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice
		// between the three Java bytecode
		return new InstructionList(InstructionFactory.createStore(type.toBCEL(), varNum));
	}
}