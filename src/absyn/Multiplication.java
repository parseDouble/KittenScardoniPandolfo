package absyn;

import types.Type;
import types.NumericalType;
import types.CodeSignature;
import bytecode.BinOpBytecode;
import bytecode.MUL;

/**
 * A node of abstract syntax representing the multiplication
 * of two expressions.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Multiplication extends ArithmeticBinOp {

    /**
     * Constructs the abstract syntax of the multiplication of two expressions.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    public Multiplication(int pos, Expression left, Expression right) {
	super(pos,left,right);
    }

    /**
     * A binary operation-specific bytecode which performs a binary
     * computation on the left and right sides of this binary operation.
     * Namely, a <tt>mul</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param type the type of the values of the left and right sides of this
     *             binary expression
     * @return a <tt>mul</tt> bytecode
     */

    protected BinOpBytecode operator(CodeSignature where, Type type) {
	return new MUL(where,(NumericalType)type);
    }
}
