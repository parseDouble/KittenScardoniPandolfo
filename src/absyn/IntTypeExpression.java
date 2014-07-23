package absyn;

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

    protected Type typeCheck$0() {
	return Type.INT;
    }

    protected Type toType$0() {
	return Type.INT;
    }

    public String toString() {
	return "int";
    }
}
