package absyn;

import java.io.FileWriter;

import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;

/**
 * A node of abstract syntax representing a <tt>while</tt> command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class While extends Command {

    /**
     * The guard or condition of the loop.
     */

    private Expression condition;

    /**
     * The body of the loop.
     */

    private Command body;

    /**
     * Constructs the abstract syntax of a <tt>while</tt> command.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param condition the guard or condition of the loop
     * @param body the body of the loop
     */

    public While(int pos, Expression condition, Command body) {
	super(pos);

	this.condition = condition;
	this.body = body;
    }

    /**
     * Yields the abstract syntax of the guard or condition
     * of the <tt>while</tt> command.
     *
     * @return the abstract syntax of the guard or condition of the
     *         <tt>while</tt> command
     */

    public Expression getCondition() {
	return condition;
    }

    /**
     * Yields the abstract syntax of the body of the <tt>while</tt> command.
     *
     * @return the abstract syntax of the body of the <tt>while</tt> command
     */

    public Command getBody() {
	return body;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the <tt>while</tt> command.
     * This amounts to adding two arcs from the node for the <tt>while</tt>
     * command to the abstract syntax for its <tt>condition</tt>
     * and <tt>body</tt> components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("condition",condition.toDot(where),where);
	linkToNode("body",body.toDot(where),where);
    }

    /**
     * Performs the type-checking of the <tt>while</tt> command
     * by using a given type-checker. It type-checks the condition and body
     * of the <tt>while</tt> command. It checks that the condition is
     * a Boolean expression. Returns the original type-checker
     * passed as a parameter, so that local declarations in the body of
     * the loop are not visible after.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheck$0(TypeChecker checker) {
	// the condition of the loop must be a Boolean expression
	condition.mustBeBoolean(checker);

	// we type-check the body of this command.
	// Note that the resulting type-checker is not used
	body.typeCheck(checker);

	// local declarations in the body of the loop are lost after the loop
	return checker;
    }

    /**
     * Checks that this <tt>while</tt>
     * does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. It calls itself recursively
     * on the <tt>body</tt> component.
     *
     * @return false, since there is no guarantee that the loop will ever be
     *         entered once
     */

    public boolean checkForDeadcode() {
	body.checkForDeadcode();

	return false;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which evaluates the
     * <tt>condition</tt> of the loop and then continues with
     * the compilation of its <tt>body</tt>
     * or with the given <tt>continuation</tt>.
     * After the <tt>body</tt>, the <tt>condition</tt> of the loop is
     * checked again.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {

	/* The idea is to translate a <tt>while</tt> command into the code

	   condition -> (no) <tt>continuation</tt>
	   | (yes) ^
	   V       |
	   body ----
	*/

	// we create an empty block which is used to close the loop
	Block pivot = new Block(where);

	// we translate the condition of the loop. If the condition is true,
	// we execute the translation of the body. Otherwise we execute
	// what follows this command
	Block result = condition.translateAsTest
	    (where,body.translate(where,pivot),continuation);

	result.doNotMerge();

	// we link the pivot to the code which tests the condition, so that
	// we close the loop
	pivot.linkTo(result);

	return result;
    }
}
