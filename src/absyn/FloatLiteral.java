package absyn;

import java.io.FileWriter;

import types.FloatType;
import types.Type;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;
import bytecode.CONST;

/**
 * A node of abstract syntax representing a float literal.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FloatLiteral extends Literal {

    /**
     * The lexical value of the float literal.
     */

    private float value;

    /**
     * Constructs the abstract syntax of a float literal.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param value the lexical value of the float literal
     */

    public FloatLiteral(int pos, float value) {
	super(pos);

	this.value = value;
    }

    /**
     * Yields the lexical value of the float literal.
     *
     * @return the lexical value of the float literal
     */

    public float getValue() {
	return value;
    }

    /**
     * Yields a the string labelling the class
     * of abstract syntax represented by this float literal.
     * We redefine this method in order to include the lexical value
     * of the float literal.
     *
     * @return a string describing the kind of this node of abstract syntax,
     *         followed by the lexical value of this float literal
     */

    protected String label() {
	return super.label() + ": " + value;
    }

    /**
     * Performs the type-checking of a float literal.
     * There is nothing to check.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical <tt>float</tt> type
     */

    protected Type typeCheckAux(TypeChecker checker) {
	return FloatType.INSTANCE;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, a <tt>const</tt> bytecode
     * which loads on the stack the lexical value of this float
     * literal expression) followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	return new CONST(where,value).followedBy(continuation);
    }
}
