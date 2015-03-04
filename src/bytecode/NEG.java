package bytecode;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.BooleanType;
import types.CodeSignature;
import types.NumericalType;
import types.Type;

/**
 * A bytecode which negates the top element of the stack. It works on
 * numerical values as well as on <tt>boolean</tt> values.
 * <br><br>
 * ..., value -> ..., negation of value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEG extends NonCallingSequentialBytecode {

	/**
	 * The type of the element on top of the stack.
	 */

	private Type type;

	/**
	 * Constructs a bytecode which negates the top element of the stack,
	 * which holds a Boolean value.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the Boolean type
	 */

	public NEG(CodeSignature where, BooleanType type) {
		super(where);

		this.type = type;
	}

	/**
	 * Constructs a bytecode which negates the top element of the stack,
	 * which holds a numerical value.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the type of the numerical constant which is negated
	 */

	public NEG(CodeSignature where, NumericalType type) {
		super(where);

		this.type = type;
	}

	/**
	 * Yield sthe type of the value which is negated by this bytecode.
	 * This is whether the Boolean type or a numerical type.
	 *
	 * @return the type of the value which is negated by this bytecode
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "neg " + type;
	}

	/**
	 * Generates the Java bytecode corresponding
	 * to this Kitten bytecode. Namely, it generates an <tt>ineg</tt>
	 * Java bytecode if <tt>type</tt> is <tt>int</tt>,
	 * an <tt>fneg</tt> Java bytecode if <tt>type</tt> is <tt>float</tt> and
	 * the code
	 * <br>
	 * <tt>ifeq</tt> <i>after</i><br>
	 * <tt>iconst 0</tt><br>
	 * <tt>goto</tt> <i>end</i><br>
	 * <i>after:</i> <tt>iconst 1</tt><br>
	 * <i>end:</i> <tt>nop</tt>
	 * <br>
	 * if <tt>type</tt> is <tt>boolean</tt>, since in the Java bytecode
	 * the integer constant <tt>0</tt> is used to represent <tt>false</tt>
	 * and the integer constant <tt>1</tt> is used to represent <tt>true</tt>.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>ineg</tt> or similar bytecode, if <tt>type</tt> is
	 *         the corresponding numerical type, or the sequence seen above
	 *         if <tt>type</tt> is the Boolean type
	 */

	@Override
	public InstructionList generateJB(KittenClassGen classGen) {
		InstructionList il = new InstructionList();

		if (type == BooleanType.INSTANCE) {
			// the negation of a Boolean value: it becomes an alternative
			// which loads 0 or 1 on the stack
			InstructionFactory factory = classGen.getFactory();

			InstructionHandle end =
					il.insert(new org.apache.bcel.generic.NOP());
			InstructionHandle after = il.insert(factory.createConstant(1));
			il.insert(new org.apache.bcel.generic.GOTO(end));
			il.insert(factory.createConstant(0));
			il.insert(new org.apache.bcel.generic.IFEQ(after));
		}
		else ((NumericalType)type).neg(il);

		return il;
	}

	protected int hashCode$0() {
		return type.hashCode();
	}

	public boolean equals$0(Object other) {
		return type == ((NEG)other).type;
	}
}