package absyn;

import types.Type;
import types.CodeSignature;
import semantical.TypeChecker;
import bytecode.ComparisonBinOpBytecode;
import translate.CodeBlock;

/**
 * A node of abstract syntax representing a comparison binary operation
 * between two expressions, that is, a binary operation, such as
 * =, &lt; or &le;, which compares two values and returns a Boolean value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ComparisonBinOp extends BinOp {

    /**
     * Constructs the abstract syntax of a comparison
     * binary operation between two expressions.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param left the abstract syntax of the left-hand side expression
     * @param right the abstract syntax of the right-hand side expression
     */

    protected ComparisonBinOp(int pos, Expression left, Expression right) {
	super(pos,left,right);
    }

    /**
     * Translates this expression by routing control to one of two possible
     * destinations, through an <tt>ifcmp</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param yes the continuation which is the <i>yes</i> destination
     * @param no the continuation which is the <i>no</i> destination
     * @return the code which evaluates the expression and on the basis
     *         of its Boolean value routes the computation to the
     *         <tt>yes</tt> or <tt>no</tt> continuation, respectively
     */

    public CodeBlock translateAsTest
	(CodeSignature where, CodeBlock yes, CodeBlock no) {

	// we compute the least common supertype of the two sides of this
	// binary expression
	Type type = getLeft().getStaticType().leastCommonSupertype
	    (getRight().getStaticType());

	return getLeft().translateAs
	    (where,type,getRight().translateAs
	     (where,type,
	      (new CodeBlock
	       // we transform the operator into its branching version
	       (((ComparisonBinOpBytecode)operator(where,type)).toBranching(),
		yes,no))));
    }
}
