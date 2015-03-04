package absyn;

import java.io.FileWriter;

import types.Type;
import types.CodeSignature;
import bytecode.BinOpBytecode;
import translate.Block;

/**
 * A node of abstract syntax representing a binary operation between two
 * expressions.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BinOp extends Expression {

    /**
     * The left-hand side expression of the binary operation.
     */

    private Expression left;

    /**
     * The right-hand side expression of the binary operation.
     */

    private Expression right;

    /**
     * Constructs the abstract syntax of a binary operation between two
     * expressions.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    protected BinOp(int pos, Expression left, Expression right) {
	super(pos);

	this.left = left;
	this.right = right;
    }

    /**
     * Yields the left-hand side expression of the binary operation.
     *
     * @return the left-hand side expression of the binary operation
     */

    public Expression getLeft() {
	return left;
    }

    /**
     * Yields the right-hand side expression of the binary operation.
     *
     * @return the right-hand side expression of the binary operation
     */

    public Expression getRight() {
	return right;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a binary operation between two
     * expressions.
     * This amounts to adding two arcs from the node for the binary operation
     * to the abstract syntax for its two expressions.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("left",left.toDot(where),where);
	linkToNode("right",right.toDot(where),where);
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, the translation of the
     * left and right sides of the binary expression, followed by
     * the binary bytecode returned by <tt>operator()</tt> and
     * which performs a binary operation-specific
     * computation on the values of the left and right-hand sides)
     * followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public final Block translate
	(CodeSignature where, Block continuation) {

	Type type = getLeft().getStaticType()
	    .leastCommonSupertype(getRight().getStaticType());

	return getLeft().translateAs
	    (where,type,getRight().translateAs
	     (where,type,operator(where,type).followedBy(continuation)));
    }

    /**
     * A binary operation-specific bytecode which performs a binary
     * computation on the left and right-hand sides of this binary operation.
     *
     * @param where the method or constructor where this expression occurs
     * @param type the type of the values computed by the left and right-hand
     *             sides of this binary expression
     * @return a binary bytecode which performs a binary operation-specific
     *         computation on the values of the left and right-hand sides
     */

    protected abstract BinOpBytecode operator(CodeSignature where, Type type);
}
