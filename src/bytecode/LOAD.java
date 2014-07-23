package bytecode;

import generateJB.KittenClassGen;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which loads the value of a local variable on top of the stack.
 * <br><br>
 * ... -> ..., value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LOAD extends NonCallingSequentialBytecode {

	/**
	 * The number of the local variable which is loaded on the stack.
	 */

	private int varNum;

	/**
	 * The type of the value contained in the local variable.
	 */

	private Type type;

	/**
	 * Constructs a bytecode
	 * which loads the value of a local variable on top of the stack,
	 * followed by the given list of instructions.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param varNum the number of the local variable which is read
	 * @param type the type of the value loaded on top of the stack
	 */

	public LOAD(CodeSignature where, int varNum, Type type) {
		super(where);

		this.varNum = varNum;
		this.type = type;
	}

	/**
	 * Yields the number of the variable which is modified by this bytecode.
	 *
	 * @return the number of the variable modified by this bytecode
	 */

	public int getVarNum() {
		return varNum;
	}

	/**
	 * Yields the type of the loaded value.
	 *
	 * @return the type
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "load " + varNum + " of type " + type;
	}

	protected int hashCode$0() {
		return varNum * type.hashCode();
	}

	public boolean equals$0(Object other) {
		return ((LOAD)other).varNum == varNum &&
				((LOAD)other).type == type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>iload varNum</tt>, <tt>fload varNum</tt> and
	 *         <tt>aload varNum</tt> Java bytecode, if <tt>type</tt> is
	 *         <tt>int</tt>, <tt>float</tt> or a <tt>ReferenceType</tt>,
	 *         respectively
	 */

	public InstructionList generateJB(KittenClassGen classGen) {
		// we use the instruction factory to simplify the choice
		// between the three Java bytecode
		return new InstructionList(InstructionFactory.createLoad(type.toBCEL(),varNum));
	}
}