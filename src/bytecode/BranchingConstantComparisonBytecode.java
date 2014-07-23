package bytecode;

import types.CodeSignature;
import types.ComparableType;

/**
 * A bytecode which compares the top element of the stack with a constant.
 * It is used to route the computation at the end of a branching block of code.
 * <br><br>
 * ..., value -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingConstantComparisonBytecode extends BranchingBytecode {

	/**
	 * The semantical type of the top element of the stack, which is compared
	 * with a constant, and of this constant itself.
	 */

	private ComparableType type;

	/**
	 * Constructs a bytecode which compares the top element of the
	 * stack with a constant.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the value and of the constant
	 *             which are compared
	 */

	protected BranchingConstantComparisonBytecode(CodeSignature where, ComparableType type) {
		super(where);

		this.type = type;
	}

	/**
	 * Yields the semantical type of the top element of the stack.
	 */

	public ComparableType getType() {
		return type;
	}

	/**
	 * Yields a <tt>String</tt> representation of this instruction.
	 * Namely, it yields the name of the instruction
	 * followed by the type of the top element of the stack.
	 *
	 * @return a <tt>String</tt> made up of the name of the instruction
	 *         followed by the type of the top element of the stack
	 */

	public String toString() {
		return getClass().getSimpleName().toLowerCase() + " " + type;
	}

	protected int hashCode$0() {
		return type.hashCode();
	}

	public boolean equals$0(Object other) {
		return type == ((BranchingConstantComparisonBytecode)other).type;
	}
}