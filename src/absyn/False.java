package absyn;

import semantical.TypeChecker;
import types.Type;
import types.CodeSignature;
import translate.Block;
import bytecode.CONST;

/**
 * A node of abstract syntax representing a <tt>false</tt> Boolean constant.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class False extends Literal {

    /**
     * Constructs the abstract syntax of a <tt>false</tt> Boolean constant.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     */

    public False(int pos) {
	super(pos);
    }

    /**
     * Performs the type-checking of the <tt>false</tt> Boolean constant
     * by using a given type-checker.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the semantical Boolean type
     */

    protected Type typeCheck$0(TypeChecker checker) {
	return Type.BOOLEAN;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (namely, a <tt>const</tt> bytecode
     * which loads on the stack the <tt>boolean</tt> value <i>false</i>)
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

	return new CONST(where,false).followedBy(continuation);
    }
}
