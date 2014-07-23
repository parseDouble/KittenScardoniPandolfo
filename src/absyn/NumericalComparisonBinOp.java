package absyn;

import semantical.TypeChecker;
import types.Type;

/**
 * A node of abstract syntax representing a comparison binary operation
 * between two expressions, that is, a binary operation, such as
 * &lt; or &le;, which compares two numerical values and returns
 * a Boolean value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class NumericalComparisonBinOp extends ComparisonBinOp {

    /**
     * Constructs the abstract syntax of a numerical comparison
     * binary operation between two expressions.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    protected NumericalComparisonBinOp
	(int pos, Expression left, Expression right) {

	super(pos,left,right);
    }

    /**
     * Performs the type-checking of a numerical comparison binary operation
     * by using a given type-checker. It type-checks both sides of the
     * binary operation and then checks that
     * they have <tt>int</tt> or <tt>float</tt> type.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical <tt>boolean</tt> type
     */

    protected Type typeCheck$0(TypeChecker checker) {
        Type leftType = getLeft().typeCheck(checker);
        Type rightType = getRight().typeCheck(checker);
 
        if ((leftType != Type.INT && leftType != Type.FLOAT) ||
            (rightType != Type.INT && rightType != Type.FLOAT))
            error("numerical arguments required");
 
        return Type.BOOLEAN;
    }
}
