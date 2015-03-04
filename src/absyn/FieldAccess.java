package absyn;

import java.io.FileWriter;

import symbol.Symbol;
import types.Type;
import types.ClassType;
import types.FieldSignature;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;
import bytecode.GETFIELD;
import bytecode.PUTFIELD;

/**
 * A node of abstract syntax representing the access to the field of an object.
 * Its concrete syntax is the <tt>receiver.f</tt> notation.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FieldAccess extends Lvalue {

    /**
     * The abstract syntax of the <i>receiver</i> of the field access,
     * that is, the expression which is on the left of the dot in the
     * <tt>receiver.f</tt> notation.
     */

    private Expression receiver;

    /**
     * The name of the field which is accessed, that is, the symbol <tt>f</tt>
     * on the right of the dot in the <tt>receiver.f</tt> notation.
     */

    private Symbol name;

    /**
     * The signature of the field which is accessed. This is <tt>null</tt> if
     * type-checking has not been performed yet.
     */

    private FieldSignature field;

    /**
     * Constructs the abstract syntax of a field access expression.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param receiver the abstract syntax of the <i>receiver</i> of the field
     *                 access, that is, of the expression which is on the left
     *                 of the dot in the <tt>receiver.f</tt> notation
     * @param name the name of the field which is accessed, that is, the
     *             symbol <tt>f</tt> on the right of the dot in the
     *             <tt>receiver.f</tt> notation
     */

    public FieldAccess(int pos, Expression receiver, Symbol name) {
	super(pos);

	this.receiver = receiver;
	this.name = name;
    }

    /**
     * Yields the abstract syntax of the <i>receiver</i> of the field access,
     * that is, of the expression which is on the left
     * of the dot in the <tt>receiver.f</tt> notation.
     *
     * @return the abstract syntax of the receiver of the field access
     */

    public Expression getReceiver() {
	return receiver;
    }

    /**
     * Yields the name of the field which is accessed, that is,
     * the symbol <tt>f</tt> on the right
     * of the dot in the <tt>receiver.f</tt> notation.
     *
     * @return the name of the field which is accessed
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields a the string labelling the class
     * of abstract syntax represented by
     * this node. We redefine this method in order to add the field referenced
     * by this access, if it has already been computed by a type-checker.
     *
     * @return a string describing the kind of this node of abstract syntax,
     *         followed by the static type of the expression, if already
     *         computed through type-checking, and the field referenced by
     *         this access
     */

    protected String label() {
	// if this expression has not been type-checked yet, we do not
	// report the field referenced
	if (field == null) return super.label();
	// otherwise we add the field referenced
	else return super.label() + "\\nreferences " + field;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a field access expression.
     * This amounts to adding arcs from the node for the field access
     * to the abstract syntax for its <tt>receiver</tt> and
     * <tt>name</tt> component.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("receiver",receiver.toDot(where),where);
	linkToNode("name",name.toDot(where),where);
    }

    /**
     * Performs the type-checking of a field access expression,
     * by using a given type-checker. It type-checks the <i>receiver</i>
     * and checks if its static type is a class type and has a field
     * with the given <tt>name</tt>.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the declared type of the field <tt>name</tt> in the class
     *         which is the static type of <tt>receiver</tt>
     */

    protected Type typeCheck$0(TypeChecker checker) {
	Type receiverType = receiver.typeCheck(checker);

	// the receiver must have class type!
	if (!(receiverType instanceof ClassType))
	    return error("class type required");

	ClassType receiverClass = (ClassType)receiverType;

	// we read the signature of a field called <tt>name</tt> in the static
	// class <tt>receiverClass</tt> of the receiver
	if ((field = receiverClass.fieldLookup(name)) == null)
	    // there is no such field!
	    return error("unknown field " + name);

	// we return the static type of the field <tt>name</tt>
	// in the class of the receiver
	return field.getType();
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of a <tt>field</tt> of an object. Namely, the code
     * which is generated is<br>
     * <br>
     * <i>translation of <tt>receiver</tt><br>
     * <tt>getfield field</tt><br>
     * <br>
     * followed by the given <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	return receiver.translate
	    (where,new GETFIELD(where,field).followedBy(continuation));
    }

    /**
     * Generates the intermediate Kitten code which must be executed before
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it translates the <tt>receiver</tt> of this field
     * access.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return the evaluation of <tt>receiver</tt> followed by the given
     *         <tt>continuation</tt>
     */

    public Block translateBeforeAssignment
	(CodeSignature where, Block continuation) {

	return receiver.translate(where,continuation);
    }

    /**
     * Generates the intermediate Kitten code which must be executed after
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it generates a <tt>putfield</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return a <tt>putfield</tt> bytecode followed by <tt>continuation</tt>
     */

    public Block translateAfterAssignment
	(CodeSignature where, Block continuation) {

	return new PUTFIELD(where,field).followedBy(continuation);
    }
}
