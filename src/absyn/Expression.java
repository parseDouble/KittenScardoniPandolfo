package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import types.Type;
import types.CodeSignature;
import translate.CodeBlock;
import bytecode.CAST;
import bytecode.IF_TRUE;

/**
 * A node of abstract syntax representing a Kitten expression.
 * Expressions are syntactical structures which yield a value.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Expression extends Absyn {

    /**
     * The static type of this expression, as computed during type-checking.
     */

    private Type staticType;

    /**
     * The type-checker used during the last type-checking of this
     * expression. This is <tt>null</tt> if this expression has never
     * been type-checked before.
     */

    private TypeChecker checker;

    /**
     * Constructs the abstract syntax of an expression.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     */

    protected Expression(int pos) {
	super(pos);
    }

    /**
     * Yields a the string labelling the class
     * of abstract syntax represented by
     * this node. We redefine this method in order to add the static type
     * of the expression, if it has already been computed by a type-checker.
     *
     * @return a string describing the label of this node of abstract syntax,
     *         followed by the static type of the expression, if already
     *         computed through type-checking
     */

    protected String label() {
	// if this expression has not been type-checked yet, we do not
	// report its static type
	if (staticType == null) return super.label();
	// otherwise we add its static type
	else return super.label() + " [" + staticType + "]";
    }

    /**
     * Type-checks this expression and checks that it has Boolean type.
     * Signals an error otherwise.
     *
     * @param checker the type-checker to be used for type-checking
     */

    protected void mustBeBoolean(TypeChecker checker) {
	if (typeCheck(checker) != Type.BOOLEAN) error("boolean expected");
    }

    /**
     * Type-checks this expression and checks that it has integer type.
     * Signals an error otherwise.
     *
     * @param checker the type-checker to be used for type-checking
     */

    protected void mustBeInt(TypeChecker checker) {
	if (typeCheck(checker) != Type.INT) error("integer expected");
    }

    /**
     * Returns the static semantical
     * type of this expression, as computed during
     * last type-checking.
     *
     * @return the static semantical type of this expression, if it has
     *         already been type-checked. Returns <tt>null</tt> otherwise
     */

    public final Type getStaticType() {
	return staticType;
    }

    /**
     * Yields the type-checker used during the last type-checking
     * of this expression.
     *
     * @return the type-checker used during the last type-checking of this
     *         expression. Returns <tt>null</tt> if this expression has not
     *         been type-checked yet
     */

    public final TypeChecker getTypeChecker() {
	return checker;
    }

    /**
     * Writes in the specified file a dot representation of the abstract syntax
     * of this expression. By default, it writes a single dot node for this
     * node of abstract syntax and it calls the auxiliary
     * <tt>toDot$0()</tt> method. Subclasses should redefine the latter
     * in order to consider components of expressions.
     *
     * @param where the file where the dot representation must be written
     * @return the name used to refer to this node in the dot file,
     *         as computed by <tt>dotNodeName()</tt>
     */

    public final String toDot(FileWriter where) throws java.io.IOException {
	// dumps in the file the name of the node in the dot file,
	// followed by the label used to show the node to the user of dot.
	// This label is computed by the <tt>label()</tt> method
	where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

	toDot$0(where);

	return dotNodeName();
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax. This should usually build
     * arcs between this node and those for the abstract syntax
     * of its components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	// nothing, by default
    }

    /**
     * Wrapper function which
     * performs the type-checking of this expression by using a given
     * type-checker. It calls the expression-specific type-checking method
     * <tt>typeCheck$0</tt> and then stores the static type of the expression
     * in the <tt>staticType</tt> field. It records the type-checker
     * int the <tt>checker</tt> field of this expression.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the static type of the expression, as computed
     *         by the type-checker
     */

    public final Type typeCheck(TypeChecker checker) {
	return staticType = typeCheck$0(this.checker = checker);
    }

    /**
     * Performs the type-checking of this expression by using a given
     * type-checker.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the static type of the expression, as computed
     *         by the type-checker
     */

    protected abstract Type typeCheck$0(TypeChecker checker);

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which pushes onto the stack
     * the value of the expression (the original stack elements are not
     * modified) followed by a <tt>continuation</tt>
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public abstract CodeBlock translate
	(CodeSignature where, CodeBlock continuation);

    /**
     * Translates this expression by requiring that it leaves onto the
     * stack a value of the given type. The only difference with
     * <tt>translate</tt> is that it checks if a <tt>float</tt> is required
     * while an <tt>int</tt> expression is being translated. In such a case,
     * a type promotion bytecode <tt>i2f</tt> is added.
     *
     * @param where the method or constructor where this expression occurs
     * @param type the type of the value which must be left onto the
     *             stack by the translation of this expression
     * @param continuation the continuation to be executed after this
     *                     expression
     * @return the code which evaluates this expression, followed by a
     *         possible <tt>i2f</tt> bytecode and then by <tt>continuation</tt>
     */

    public final CodeBlock translateAs
	(CodeSignature where, Type type, CodeBlock continuation) {

	if (staticType == Type.INT && type == Type.FLOAT)
	    // type promotion
	    continuation = new CAST(where,Type.INT,Type.FLOAT)
		.followedBy(continuation);

	return translate(where,continuation);
    }

    /**
     * Translates this expression by assuming that it has <tt>boolean</tt>
     * value. This must have been guaranteed by a previous type-checking.
     * Depending on the truth of that <tt>boolean</tt> value, control is routed
     * to one of two possible destinations, through a <tt>if_true</tt>
     * bytecode. Subclasses may redefine to get more improved code.
     *
     * @param where the method or constructor where this expression occurs
     * @param yes the continuation which is the <i>yes</i> destination
     * @param no the conitnuation which is the <i>no</i> destination
     * @return the code which evaluates the expression and on the basis
     *         of its <tt>boolean</tt> value routes the computation to the
     *         <tt>yes</tt> or <tt>no</tt> continuation, respectively
     */

    public CodeBlock translateAsTest
	(CodeSignature where, CodeBlock yes, CodeBlock no) {

	return translate(where,new CodeBlock(new IF_TRUE(where),yes,no));
    }

    /**
     * Outputs an error message to the user, by using the type-checker
     * used during the last type-checking. Returns a default type
     * which is used to continue the type-checking anyway.
     *
     * @param msg the message to be output
     * @return <tt>Type.INT</tt>
     */

    protected Type error(String msg) {
	error(checker,msg);

	// this type is fine for most of the cases
	return Type.INT;
    }
}
