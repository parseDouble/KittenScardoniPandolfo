package absyn;

import semantical.TypeChecker;
import types.Type;
import types.CodeSignature;
import bytecode.BinOpBytecode;
import bytecode.AND;

/**
 * A node of abstract syntax representing a logical <i>and</i> operation.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class And extends BooleanBinOp {

    /**
     * Constructs the abstract syntax of a binary <i>and</i> logical operation.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    public And(int pos, Expression left, Expression right) {
	super(pos,left,right);
    }

    /**
     * A binary operation-specific bytecode which performs a binary
     * computation on the left and right sides of this binary operation.
     * Namely, an <tt>and</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param type the type of the values of the left and right sides of this
     *             binary expression
     * @return an <tt>and</tt> bytecode
     */

    protected BinOpBytecode operator(CodeSignature where, Type type) {
	return new AND(where);
    }
}
