package bytecode;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.BooleanType;
import types.CodeSignature;
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
	 * Constructs a bytecode which loads the given constant on top
	 * of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constant the constant to be loaded on top of the stack
	 */

	protected CONST(CodeSignature where, Object constant) {
		super(where);

		this.constant = constant;
	}

	/**
	 * Constructs a bytecode which loads a <tt>nil</tt> constant on top
	 * of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public CONST(CodeSignature where) {
		this(where, null);
	}

	/**
	 * Constructs a bytecode which
	 * loads a <tt>boolean</tt> constant on top of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constant the <tt>boolean</tt> constant which is loaded on top
	 *                 of the stack
	 */

	public CONST(CodeSignature where, boolean constant) {
		this(where, new Boolean(constant));
	}

	/**
	 * Constructs a bytecode which
	 * loads an <tt>int</tt> constant on top of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constant the <tt>int</tt> constant which is loaded on top
	 *                 of the stack
	 */

	public CONST(CodeSignature where, int constant) {
		this(where, new Integer(constant));
	}

	/**
	 * Constructs a bytecode which
	 * loads a <tt>float</tt> constant on top of the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constant the <tt>float</tt> constant which is loaded on top
	 *                 of the stack
	 */

	public CONST(CodeSignature where, float constant) {
		this(where, new Float(constant));
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

	protected int hashCode$0() {
		return constant == null ? 1 : constant.hashCode();
	}

	public boolean equals$0(Object other) {
		Object oc = ((CONST)other).constant;

		return constant == null ? oc == null :
			(oc != null && constant.getClass() == oc.getClass() &&
			constant.equals(oc));
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

	public InstructionList generateJB(JavaClassGenerator classGen) {
		if (constant == null)
			return new InstructionList
					(new org.apache.bcel.generic.ACONST_NULL());
		else
			// the instruction factory will create the appropriate instruction
			// among <tt>iconst</tt>, <tt>fconst</tt>, <tt>bipush</tt> and
			// <tt>ldc</tt>. Moreover, it will put the constant inside
			// the constant pool, if needed
			return new InstructionList
					(classGen.getFactory().createConstant(constant));
	}
}