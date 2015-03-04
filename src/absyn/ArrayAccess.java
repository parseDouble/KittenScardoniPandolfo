package absyn;

import java.io.FileWriter;

import types.Type;
import types.ArrayType;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;
import bytecode.ARRAYLOAD;
import bytecode.ARRAYSTORE;

/**
 * A node of abstract syntax representing the access to an element of an array.
 * Its concrete syntax is the <tt>array[index]</tt> notation.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ArrayAccess extends Lvalue {

    /**
     * The abstract syntax of the <tt>array</tt> expression in the
     * <tt>array[index]</tt> notation.
     */

    private Expression array;

    /**
     * The abstract syntax of the <tt>index</tt> expression in the
     * <tt>array[index]</tt> notation.
     */

    private Expression index;

    /**
     * Constructs the abstract syntax of an access to an array element.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param array the abstract syntax of the <tt>array</tt> expression in the
     *              <tt>array[index]</tt> notation
     * @param index the abstract syntax of the <tt>index</tt> expression in the
     *              <tt>array[index]</tt> notation
     */

    public ArrayAccess(int pos, Expression array, Expression index) {
	super(pos);

	this.array = array;
	this.index = index;
    }

    /**
     * Yields the abstract syntax of the <tt>array</tt> expression in the
     * <tt>array[index]</tt> notation.
     *
     * @return the abstract syntax of the <tt>array</tt> expression in the
     *         <tt>array[index]</tt> notation
     */

    public Expression getArray() {
	return array;
    }

    /**
     * Yields the abstract syntax of the <tt>index</tt> expression in the
     * <tt>array[index]</tt> notation.
     *
     * @return the abstract syntax of the <tt>index</tt> expression in the
     *         <tt>array[index]</tt> notation
     */

    public Expression getIndex() {
	return index;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of an access to an array element.
     * This amounts to adding arcs from the node for the array access
     * to the abstract syntax for its <tt>array</tt> and
     * <tt>index</tt> components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("array",array.toDot(where),where);
	linkToNode("index",index.toDot(where),where);
    }

    /**
     * Performs the type-checking of an array access expression,
     * by using a given type-checker. It type-checks the <tt>array</tt> and
     * <tt>index</tt> expressions of the <tt>array[index]</tt> notation.
     * It requires the former to have array type and the latter to have
     * <tt>int</tt> type.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the static type of the elements of the array type
     *         which is the static type of <tt>array</tt>
     */

    protected Type typeCheck$0(TypeChecker checker) {
	// we type-check the reference to the array
	Type arrayType = array.typeCheck(checker);

	// we type-check the index and require it to have integer type
	index.mustBeInt(checker);

	// the array expression must have array type
	if (!(arrayType instanceof ArrayType))
	    return error("array type required");

	// we return the static type of the elements of the array
	return ((ArrayType)arrayType).getElementsType();
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of an element of an array. Namely, the code which is generated
     * is<br>
     * <br>
     * <i>translation of <tt>array</tt><br>
     * <i>translation of <tt>index</tt><br>
     * <tt>arrayload</tt> type of the elements of the array<br>
     * <br>
     * followed by the given <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	return array.translate
	    (where,index.translate
	     (where,new ARRAYLOAD(where,getStaticType()).followedBy
	      (continuation)));
    }

    /**
     * Generates the intermediate Kitten code which must be executed before
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it translates the <tt>array</tt> and
     * the <tt>index</tt> of this array element access.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return the evaluation of <tt>array</tt> followed by that of
     *         <tt>index</tt> followed by the given <tt>continuation</tt>
     */

    public Block translateBeforeAssignment
	(CodeSignature where, Block continuation) {

	return array.translate(where,index.translate(where,continuation));
    }

    /**
     * Generates the intermediate Kitten code which must be executed after
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it generates a <tt>arraystore</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return a <tt>putfield</tt> bytecode followed by <tt>continuation</tt>
     */

    public Block translateAfterAssignment
	(CodeSignature where, Block continuation) {

	return new ARRAYSTORE(where,getStaticType()).followedBy(continuation);
    }
}
