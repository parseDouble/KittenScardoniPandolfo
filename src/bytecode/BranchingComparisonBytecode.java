package bytecode;

import types.ComparableType;

/**
 * A bytecode which compares the top two elements of
 * the stack. It is used to route the computation at the end of a branching
 * block of code.
 * <br><br>
 * ..., value1, value2 -> ...
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingComparisonBytecode extends BranchingBytecode {

	/**
	 * The semantical type of the top two elements of the stack, which are
	 * compared.
	 */

	private ComparableType type;

	/**
	 * Constructs a bytecode which compares the top two elements of the
	 * stack to decide where to branch.
	 *
	 * @param type the semantical type of the values which are compared
	 */

	protected BranchingComparisonBytecode(ComparableType type) {
		this.type = type;
	}

	/**
	 * Yields the semantical type of the top two elements of the stack.
	 */

	public ComparableType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString() + " " + type;
	}

	protected int hashCodeAux() {
		return type.hashCode();
	}

	public boolean equalsAux(Object other) {
		return other instanceof BranchingComparisonBytecode &&
			((BranchingComparisonBytecode)other).type == type;
	}
}