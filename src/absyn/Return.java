package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import types.Type;
import types.CodeSignature;
import bytecode.NOP;
import bytecode.RETURN;
import translate.CodeBlock;

/**
 * A node of abstract syntax representing a <tt>return</tt> command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Return extends Command {

    /**
     * The abstract syntax of the expression whose value is returned.
     * It might be <tt>null</tt>.
     */

    private Expression returned;

    /**
     * Constructs the abstract syntax of a <tt>return</tt> command.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param returned the abstract syntax of the expression whose value
     *                 is returned. It might be <tt>null</tt>
     */

    public Return(int pos, Expression returned) {
	super(pos);

	this.returned = returned;
    }

    /**
     * Yields the abstract syntax of the expression whose value is returned,
     * if any.
     *
     * @return the abstract syntax of the expression whose value is returned,
     *         if any. Returns <tt>null</tt> if there is no such expression
     */

    public Expression getReturned() {
	return returned;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the <tt>return</tt> command.
     * This amounts to adding an arc from the node for the <tt>return</tt>
     * command to the abstract syntax for its <tt>returned</tt>
     * component, if any.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	if (returned != null)
	    linkToNode("returned",returned.toDot(where),where);
    }

    /**
     * Performs the type-checking of the <tt>return</tt> command
     * by using a given type-checker. It type-checks the expression whose
     * value is returned, if it is not <tt>null</tt>, and checks that
     * its static type can be assigned to the type expected by the
     * type-checker for the <tt>return</tt> instructions.
     * If no returned expression is present, then the it checks that
     * the type-checker expects <tt>void</tt> as a
     * return type. It returns the same type-checker passed as a parameter.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheck$0(TypeChecker checker) {
	// we get from the type-checker the expected type
	// for the <tt>return</tt> instructions
	Type expectedReturnType = checker.getReturnType();

	// a <tt>return</tt> instruction without expression is legal
	// only inside a <tt>void</tt> method
	if (returned == null && expectedReturnType != Type.VOID)
	    error("missing return value");

	// if there is a returned expression, we check that its static
	// type can be assigned to the expected return type
	if (returned != null &&
	    returned.typeCheck(checker) != null &&
	    !returned.typeCheck(checker).canBeAssignedTo(expectedReturnType))
	    error("illegal return type: " + expectedReturnType + " expected");

	return checker;
    }

    /**
     * Checks that this <tt>return</tt> command
     * does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. This is always true for
     * <tt>return</tt> commands.
     *
     * @return true, since this command always terminates with a
     *         <tt>return</tt> command (itself)
     */

    public boolean checkForDeadcode() {
	return true;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which starts with
     * the evaluation of the <tt>returned</tt> expression, if any, and
     * continues with a <tt>RETURN</tt> bytecode for the type returned
     * by the current method.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	// we get the type which must be returned by this the current method
	Type returnType = getTypeChecker().getReturnType();

	// we get a code which is made of a block containing
	// the bytecode <tt>return</tt>
	continuation = new CodeBlock(new RETURN(where,returnType));

	// if there is an initialising expression, we translate it
	if (returned != null)
	    continuation = returned.translateAs(where,returnType,continuation);

	return continuation;
    }
}
