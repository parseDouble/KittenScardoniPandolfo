package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.BooleanType;
import types.FloatType;
import types.IntType;
import types.NilType;
import types.Type;

/**
 * A bytecode which loads a <tt>nil</tt>, <tt>boolean</tt>, <tt>int</tt> or
 * <tt>float</tt> constant on top of the stack.
 * <br><br>
 * ... -> ..., constant
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CONST extends NonCallingSequentialBytecode {

	/**
	 * The constant which is loaded on top of the stack.
	 */

	private Object constant;

	/**
	 * Constructs a bytecode which loads the given constant on top of the stack.
	 *
	 * @param constant the constant to be loaded on top of the stack
	 */

	private CONST(Object constant) {
		this.constant = constant;
	}

	/**
	 * Constructs a bytecode that loads a <tt>nil</tt> constant on top
	 * of the stack.
	 */

	public CONST() {
		this(null);
	}

	/**
	 * Constructs a bytecode which
	 * loads a <tt>boolean</tt> constant on top of the stack.
	 *
	 * @param constant the <tt>boolean</tt> constant which is loaded on top
	 *                 of the stack
	 */

	public CONST(boolean constant) {
		this(new Boolean(constant));
	}

	/**
	 * Constructs a bytecode which
	 * loads an <tt>int</tt> constant on top of the stack.
	 *
	 * @param constant the <tt>int</tt> constant which is loaded on top of the stack
	 */

	public CONST(int constant) {
		this(new Integer(constant));
	}

	/**
	 * Constructs a bytecode which
	 * loads a <tt>float</tt> constant on top of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constant the <tt>float</tt> constant which is loaded on top
	 *                 of the stack
	 */

	public CONST(float constant) {
		this(new Float(constant));
	}

	/**
	 * Yields the constant which is loaded on top of the stack.
	 *
	 * @return the constant which is loaded on top of the stack
	 */

	protected Object getConstant() {
		return constant;
	}

	/**
	 * Yields the type of the constant which is loaded on top of the stack.
	 *
	 * @return the semantical type of the constant which is loaded on top
	 *         of the stack
	 */

	public Type getType() {
		if (constant == null) return NilType.INSTANCE;
		else if (constant instanceof Boolean) return BooleanType.INSTANCE;
		else if (constant instanceof Integer) return IntType.INSTANCE;
		else return FloatType.INSTANCE;
	}

	@Override
	public String toString() {
		return "const " + constant;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>iconst</tt>, <tt>fconst</tt>, <tt>ldc</tt>,
	 *         <tt>aconst_null</tt> or <tt>bipush</tt> Java bytecode,
	 *         on the basis of the type and size of <tt>constant</tt>
	 */

	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		if (constant == null)
			return new InstructionList
					(new org.apache.bcel.generic.ACONST_NULL());
		else
			// the instruction factory will create the appropriate instruction
			// among <tt>iconst</tt>, <tt>fconst</tt>, <tt>bipush</tt> and
			// <tt>ldc</tt>. Moreover, it will put the constant inside
			// the constant pool, if needed
			return new InstructionList(classGen.getFactory().createConstant(constant));
	}
}