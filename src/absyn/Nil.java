package absyn;

import types.Type;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.CodeBlock;
import bytecode.CONST;

/**
 * A node of abstract syntax representing the <tt>nil</tt> constant.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Nil extends Literal {

    /**
     * Constructs the abstract syntax of the <tt>nil</tt> constant.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     */

    public Nil(int pos) {
	super(pos);
    }

    /**
     * Performs the type-checking of the <tt>nil</tt> constant
     * by using a given type-checker.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical <i>nil</i> type
     */

    protected Type typeCheck$0(TypeChecker checker) {
	return Type.NIL;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, a <tt>const</tt> bytecode
     * which loads on the stack the value <tt>nil</tt>)
     * followed by the given <tt>continuation</tt>.
     * The original stack elements are not modified.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	return new CONST(where).followedBy(continuation);
    }
}
