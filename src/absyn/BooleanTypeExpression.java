package absyn;

import types.BooleanType;
import types.Type;

/**
 * A node of abstract syntax representing the Kitten <tt>boolean</tt> type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BooleanTypeExpression extends TypeExpression {

    /**
     * Constructs the abstract syntax of the Kitten <tt>boolean</tt> type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

    public BooleanTypeExpression(int pos) {
	super(pos);
    }

    protected Type typeCheckAux() {
	return BooleanType.INSTANCE;
    }

    protected Type toTypeAux() {
	return BooleanType.INSTANCE;
    }

    public String toString() {
	return "boolean";
    }
}
