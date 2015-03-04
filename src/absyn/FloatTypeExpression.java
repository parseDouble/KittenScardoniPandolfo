package absyn;

import types.Type;

/**
 * A node of abstract syntax representing the Kitten <tt>float</tt> type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FloatTypeExpression extends TypeExpression {

    /**
     * Constructs the abstract syntax of the Kitten <tt>float</tt> type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

    public FloatTypeExpression(int pos) {
	super(pos);
    }

    protected Type typeCheckAux() {
	return Type.FLOAT;
    }

    protected Type toTypeAux() {
	return Type.FLOAT;
    }

    public String toString() {
	return "float";
    }
}
