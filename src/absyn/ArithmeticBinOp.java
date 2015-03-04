package absyn;

import semantical.TypeChecker;
import types.Type;

/**
 * A node of abstract syntax representing an arithmetic binary operation
 * between two expressions, that is, a binary operation, such as addition
 * or subtraction,
 * which returns an <tt>int</tt> or <tt>float</tt> value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ArithmeticBinOp extends BinOp {

    /**
     * Constructs the abstract syntax of an arithmetic binary operation
     * between two expressions.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    protected ArithmeticBinOp(int pos, Expression left, Expression right) {
	super(pos,left,right);
    }

    /**
     * Performs the type-checking of an arithmetic binary operation
     * by using a given type-checker. It type-checks both sides of the
     * binary operation and then checks that
     * they have <tt>int</tt> or <tt>float</tt> type.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the least common supertype of the static types of the two sides
     *         of the binary expression
     */

    protected Type typeCheckAux(TypeChecker checker) {
	Type leftType = getLeft().typeCheck(checker);
	Type rightType = getRight().typeCheck(checker);

	// both sides of the operation must have <tt>int</tt> or
	// <tt>float</tt> type. The type of the binary operation will then
	// be the least common supertype of the types of the two sides
	if ((leftType == Type.INT || leftType == Type.FLOAT) &&
	    (rightType == Type.INT || rightType == Type.FLOAT))
	    return leftType.leastCommonSupertype(rightType);
	else return error("numerical argument required");
    }
}
