package absyn;

import types.Type;

/**
 * A node of abstract syntax representing the Kitten <tt>void</tt> type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VoidTypeExpression extends TypeExpression {

    /**
     * Constructs the abstract syntax of the Kitten <tt>void</tt> type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

    public VoidTypeExpression(int pos) {
	super(pos);
    }

    protected Type typeCheckAux() {
	return types.Type.VOID;
    }

    protected Type toTypeAux() {
	return types.Type.VOID;
    }

    public String toString() {
	return "void";
    }
}
