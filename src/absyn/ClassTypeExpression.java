package absyn;

import java.io.FileWriter;

import symbol.Symbol;
import types.ClassType;
import types.Type;

/**
 * A node of abstract syntax representing a Kitten class type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ClassTypeExpression extends TypeExpression {

    /**
     * The name (identifier) of the class.
     */

    private Symbol name;

    /**
     * Constructs the abstract syntax of a Kitten class type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param name the name (identifier) of the class
     */

    public ClassTypeExpression(int pos, Symbol name) {
	super(pos);

	this.name = name;
    }

    /**
     * Yields the name of the class.
     *
     * @return the name of the class
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Type-checks this class type expression. It builds the class type
     * with the given name through the <tt>KittenClassType.mk</tt> method.
     * This might trigger a lexical, syntactical and semantical analysis
     * of one (or recursively more) class(es).
     *
     * @return the semantical class type corresponding to this
     *         class type expression
     */

    protected Type typeCheckAux() {
	ClassType result = ClassType.mk(name);

	result.typeCheck();

	return result;
    }

    /**
     * Auxiliary method which yields the semantical type corresponding
     * to this type expression. It builds the class type with the
     * given name through the <tt>KittenClassType.mk</tt> method but does
     * not type-check it.
     *
     * @return the semantical class type corresponding to this
     *         class type expression
     */

    @Override
    protected Type toTypeAux() {
	return ClassType.mk(name);
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing this class type expression. Namely, it builds an arc
     * between the abstract syntax for this class type expression and
     * the abstract syntax for the name of the class.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDotAux(FileWriter where) throws java.io.IOException {
	linkToNode("name",name.toDot(where),where);
    }

    public String toString() {
	return name.toString();
    }
}
