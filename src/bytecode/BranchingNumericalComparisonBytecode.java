package bytecode;

import types.NumericalType;
import types.CodeSignature;

/**
 * A bytecode which applies a numerical comparison on the top two elements of
 * the stack. It is used to route the computation at the end of a branching
 * block of code.
 * <br><br>
 * ..., value1, value2 -> ...
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingNumericalComparisonBytecode
    extends BranchingComparisonBytecode {

    /**
     * Constructs a bytecode which compares the top two elements of the
     * stack to decide where to branch.
     *
     * @param where the method or constructor where this bytecode occurs
     * @param type the semantical type of the values which are compared
     */

    protected BranchingNumericalComparisonBytecode
	(CodeSignature where, NumericalType type) {

	super(where,type);
    }

    /**
     * Yields the semantical type of the top two elements of the stack.
     */

    public NumericalType getType() {
	return (NumericalType)super.getType();
    }
}
