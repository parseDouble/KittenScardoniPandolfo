package bytecode;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.BooleanType;
import types.CodeSignature;
import types.Type;

/**
 * A branching bytecode which checks if the top of the stack is the
 * Boolean value <i>true</i>. It routes accordingly the computation
 * at the end of a branching block of code.
 * <br><br>
 * ..., value -> ...<br>
 * (checks if value is true)
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IF_TRUE extends BranchingConstantComparisonBytecode {

	/**
	 * Constructs a branching
	 * bytecode which checks if the top of the stack is the
	 * Boolean value <i>true</i>. It routes accordingly the computation
	 * at the end of a conditional block of code.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public IF_TRUE(CodeSignature where) {
		// we are comparing with a <tt>boolean</tt> constant
		super(where, BooleanType.INSTANCE);
	}

	/**
	 * Yields a <tt>String</tt> representation of this instruction.
	 * Namely, it yields the <tt>if_true</tt> <tt>String</tt>.
	 *
	 * @return the <tt>if_true</tt> <tt>String</tt>
	 */

	public String toString() {
		return "if_true";
	}

	/**
	 * Yields a branching bytecode which expresses the opposite condition
	 * of this.
	 *
	 * @return an <tt>IF_FALSE</tt> bytecode
	 */

	public BranchingBytecode negate() {
		return new IF_FALSE(getWhere());
	}

	/**
	 * Auxiliary method which adds to the given list of instructions the
	 * code which goes to <tt>yes</tt> if the outcome of the test expressed
	 * by this branching bytecode is true.
	 * Namely, it generates the Java bytecode<br>
	 * <br>
	 * <tt>ifne yes</tt><br>
	 * <br>
	 * Note that in Java bytecodes Boolean values are represented as
	 * integers (0 = <i>false</i>, 1 = <i>true</i>),
	 * so that we use <tt>ifne</tt> to check if the Boolean
	 * value on top of the stack is <i>true</i>.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param classGen the class generator to be used to generate the code
	 * @param yes the target where one must go if the outcome of the test
	 *            expressed by this branching bytecode is true
	 */

	@Override
	protected void generateJB$0
	(InstructionList il, KittenClassGen classGen, InstructionHandle yes) {

		// builds the instructions which go to <tt>yes</tt> if the test is true
		il.append(new org.apache.bcel.generic.IFNE(yes));
	}
}