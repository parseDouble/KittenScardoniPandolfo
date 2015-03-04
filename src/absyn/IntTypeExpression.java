package absyn;

import types.IntType;
import types.Type;

/**
 * A node of abstract syntax representing the Kitten <tt>int</tt> type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IntTypeExpression extends TypeExpression {

    /**
     * Constructs the abstract syntax of the Kitten <tt>int</tt> type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

    public IntTypeExpression(int pos) {
	super(pos);
    }

    protected Type typeCheckAux() {
	return IntType.INSTANCE;
    }

    protected Type toTypeAux() {
	return IntType.INSTANCE;
    }

    public String toString() {
	return "int";
    }
}
