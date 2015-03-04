package absyn;

import java.io.FileWriter;

import types.Type;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;
import bytecode.NEG;

/**
 * A node of abstract syntax representing the unary minus of an expression.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Minus extends Expression {

    /**
     * The abstract syntax of the expression which is minus'ed.
     */

    private Expression expression;

    /**
     * Constructs the abstract syntax of a unary minus expression.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param expression the abstract syntax of the expression which is
     *                   minus'ed
     */

    public Minus(int pos, Expression expression) {
	super(pos);

	this.expression = expression;
    }

    /**
     * Yields the abstract syntax of the expression which is minus'ed.
     *
     * @return the abstract syntax of the expression which is minus'ed
     */

    public Expression getExpression() {
	return expression;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a unary minus of an expression.
     * This amounts to adding an arc from the node for the unary minus
     * to the abstract syntax for its <tt>expression</tt> component.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDotAux(FileWriter where) throws java.io.IOException {
	linkToNode("expression",expression.toDot(where),where);
    }

    /**
     * Performs the type-checking of the unary minus of an expression
     * by using a given type-checker. It type-checks the expression and
     * then checks that its static type is <tt>int</tt> or <tt>float</tt>.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the static type of the minus'ed expression
     */

    protected Type typeCheckAux(TypeChecker checker) {
	Type expressionType = expression.typeCheck(checker);

	// we can only negate integers or floats
	if (expressionType != Type.INT && expressionType != Type.FLOAT)
	    error("integer or float expected");

	return expressionType;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, the translation of the
     * <tt>expression</tt> which is minus'ed, followed by the
     * <tt>neg</tt> bytecode) followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	return expression.translate
	    (where,new NEG(where,Type.INT).followedBy(continuation));
    }
}
