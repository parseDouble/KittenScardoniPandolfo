package bytecode;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.CodeSignature;
import types.ComparableType;

/**
 * A branching bytecode which compares the top two elements of the stack
 * to check if they are the same.
 * It is used to route the computation at the end of a branching
 * block of code.
 * <br><br>
 * ..., value1, value2 -> ...<br>
 * (checks if value1 = value2)
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IF_CMPEQ extends BranchingComparisonBytecode {

	/**
	 * Constructs a bytecode which compares the top two elements of the
	 * stack to check if they are the same.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the values which are compared
	 */

	public IF_CMPEQ(CodeSignature where, ComparableType type) {
		super(where,type);
	}

	/**
	 * Yields a branching bytecode which expresses the opposite condition
	 * of this.
	 *
	 * @return an <tt>IF_CMPNE</tt> bytecode of the same type as this
	 */

	public BranchingBytecode negate() {
		return new IF_CMPNE(getWhere(),getType());
	}

	/**
	 * Auxiliary method which adds to the given list of instructions the
	 * code which goes to <tt>yes</tt> if the outcome of the test expressed
	 * by this branching bytecode is true.
	 * Namely, it generates the Java bytecode<br>
	 * <br>
	 * <tt>if_icmpeq yes</tt><br>
	 * <br>
	 * for <tt>int</tt> and <tt>boolean</tt> (Booleans are represented as
	 * integers in Java bytecode, with the assumption that
	 * 0 = <i>false</i> and 1 = <i>true</i>),<br>
	 * <br>
	 * <tt>if_acmpeq yes</tt><br>
	 * <br>
	 * for objects and arrays, and<br>
	 * <br>
	 * <tt>fcmpl</tt><br>
	 * <tt>ifeq yes</tt><br>
	 * <br>
	 * for <tt>float</tt>. The <tt>fcmpl</tt> Java bytecode operates over
	 * two <tt>float</tt> values on top of the stack and produces an
	 * <tt>int</tt> value at their place, as it follows:<br>
	 * <br>
	 * ..., value1, value2 -> ..., 1   if value1 &gt; value2<br>
	 * ..., value1, value2 -> ..., 0   if value1 = value2<br>
	 * ..., value1, value2 -> ..., -1  if value1 &lt; value2
	 *
	 * @param il the list of instructions which must be expanded
	 * @param classGen the class generator to be used to generate the code
	 * @param yes the target where one must go if the outcome of the test
	 *            expressed by this branching bytecode is true
	 */

	protected void generateJB$0
	(InstructionList il, JavaClassGenerator classGen, InstructionHandle yes) {

		// builds the instructions which go to <tt>yes</tt> if the test is true
		getType().JB_if_cmpeq(il,yes);
	}
}