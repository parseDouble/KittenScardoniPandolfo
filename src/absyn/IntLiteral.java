package absyn;

import types.Type;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.CodeBlock;
import bytecode.CONST;

/**
 * A node of abstract syntax representing an integer literal.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IntLiteral extends Literal {

    /**
     * The lexical value of the integer literal.
     */

    private int value;

    /**
     * Constructs the abstract syntax of an int literal.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param value the lexical value of the integer literal
     */

    public IntLiteral(int pos, int value) {
	super(pos);

	this.value = value;
    }

    /**
     * Yields the lexical value of the integer literal.
     *
     * @return the lexical value of the integer literal
     */

    public float getValue() {
	return value;
    }

    /**
     * Yields a the string labelling the class
     * of abstract syntax represented by this integer literal.
     * We redefine this method in order to include the lexical value
     * of the integer literal.
     *
     * @return a string describing the kind of this node of abstract syntax,
     *         followed by the lexical value of this integer literal
     */

    protected String label() {
	return super.label() + ": " + value;
    }

    /**
     * Performs the type-checking of an integer literal.
     * There is nothing to check.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical <tt>int</tt> type
     */

    protected Type typeCheck$0(TypeChecker checker) {
	return Type.INT;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, a <tt>const</tt> bytecode
     * which loads on the stack the lexical value of this integer
     * literal expression) followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	return new CONST(where,value).followedBy(continuation);
    }
}
