package absyn;

import java.io.FileWriter;

import types.Type;
import types.ArrayType;

/**
 * A node of abstract syntax representing a Kitten array type.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ArrayTypeExpression extends TypeExpression {

    /**
     * The abstract syntax of the type of the elements of the array.
     */

    private TypeExpression elementsType;

    /**
     * Constructs the abstract syntax of a Kitten array type.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param elementsType the abstract syntax of the type
     *                     of the elements of the array
     */

    public ArrayTypeExpression(int pos, TypeExpression elementsType) {
	super(pos);

	this.elementsType = elementsType;
    }

    /**
     * Type-checks this array type expression. It type-checks the elements
     * of the array and then returns the array type for such an elements type.
     *
     * @return the semantical array type corresponding to this
     *         array type expression
     */

    protected Type typeCheck$0() {
	return ArrayType.mk(elementsType.typeCheck());
    }

    /**
     * Auxiliary method which yields the semantical type corresponding
     * to this type expression. The difference with
     * <tt>typeCheck$0()</tt> is that class types occurring in this array type
     * expressions are not type-checked themselves.
     *
     * @return the semantical array type corresponding to this
     *         array type expression
     */

    protected Type toType$0() {
	return ArrayType.mk(elementsType.toType());
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the array type. This amounts to an arc from the node
     * for the array type to the abstract syntax for the elements of the
     * array.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("elementsType",elementsType.toDot(where),where);
    }

    public String toString() {
	return "array of " + elementsType;
    }
}
