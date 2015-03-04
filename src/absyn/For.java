package absyn;

import java.io.FileWriter;

import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;

/**
 * A node of abstract syntax representing a <tt>for</tt> command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class For extends Command {

    /**
     * The code to be executed before the loop starts.
     */

    private Command initialisation;

    /**
     * The guard or condition of the loop.
     */

    private Expression condition;

    /**
     * The code to be executed after each iteration.
     */

    private Command update;

    /**
     * The body of the loop.
     */

    private Command body;

    /**
     * Constructs the abstract syntax of a <tt>for</tt> command.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param initialisation the code to be executed before the loop starts
     * @param condition the guard or condition of the loop
     * @param update the code to be executed after each iteration
     * @param body the body of the loop
     */

    public For(int pos, Command initialisation,
	       Expression condition, Command update, Command body) {
	super(pos);

	this.initialisation = initialisation;
	this.condition = condition;
	this.update = update;
	this.body = body;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the <tt>for</tt> command.
     * This amounts to adding two arcs from the node for the <tt>for</tt>
     * command to the abstract syntax for its <tt>initialisation</tt>,
     * <tt>condition</tt>, <tt>update</tt> and <tt>body</tt> components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDotAux(FileWriter where) throws java.io.IOException {
	linkToNode("initialisation",initialisation.toDot(where),where);
	linkToNode("condition",condition.toDot(where),where);
	linkToNode("update",update.toDot(where),where);
	linkToNode("body",body.toDot(where),where);
    }

    /**
     * Performs the type-checking of the <tt>for</tt> command
     * by using a given type-checker. It type-checks the initialisation,
     * condition, update and body
     * of the <tt>for</tt> command. It checks that the condition is
     * a Boolean expression. Note that local declarations inside
     * the <tt>initialisation</tt> component are visible in the
     * <tt>condition</tt>, <tt>update</tt> and <tt>body</tt> components.
     * Returns the original type-checker
     * passed as a parameter, so that local declarations in the
     * <tt>initialisation</tt>, <tt>update</tt> and <tt>body</tt>
     * components are not visible after the <tt>for</tt> command.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheckAux(TypeChecker checker) {
	// we consider the type-checker resulting from type-checking
	// the <tt>initialisation</tt> component. By using this
	// new type-checker in the following checks, we allow local variables
	// defined in the <tt>initialisation</tt> component to be visible
	// in the other components of the <tt>for</tt> command
	TypeChecker initChecker = initialisation.typeCheck(checker);

	// we check that the <tt>condition</tt> component has Boolean type
	condition.mustBeBoolean(initChecker);

	// we type-check the <tt>update</tt> component. Note that the
	// resulting type-checker is not used, so that local declarations
	// in the <tt>update</tt> field are not visible outside that field
	update.typeCheck(initChecker);

	// we type-check the body of this command.
	// Note that the resulting type-checker is not used
	body.typeCheck(initChecker);

	// we return the original type-checker, so that local declarations in
	// the <tt>initialisation</tt>, <tt>update</tt> and <tt>body</tt>
	// components are not visible after the loop
	return checker;
    }

    /**
     * Checks that this <tt>for</tt>
     * does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. It calls itself recursively
     * on the <tt>initialisation</tt>, <tt>update</tt> and <tt>body</tt>
     * components. There is dead-code only if every syntactical execution
     * path in the <tt>initialisation</tt> component ends with a
     * <tt>return</tt>, <tt>break</tt> or <tt>continue</tt> command.
     *
     * @return true if and only if every syntactical execution path in
     *         <tt>initialisation</tt> ends with a <tt>return</tt>,
     *         <tt>break</tt> or <tt>continue</tt> command.
     *         Note that it returns <tt>false</tt> otherwise, since we
     *         have no guarantee that the loop will ever be entered once
     */

    public boolean checkForDeadcode() {
	update.checkForDeadcode();
	body.checkForDeadcode();

	if (initialisation.checkForDeadcode()) {
	    error("dead-code after for loop initialisation");

	    return true;
	}

	return false;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which evaluates the
     * <tt>initialisation</tt> of the loop, then its
     * <tt>condition</tt> and then continues with
     * the compilation of its <tt>body</tt> and <tt>update</tt>
     * or with the given <tt>continuation</tt>.
     * After the <tt>update</tt>, the <tt>condition</tt> of the loop is
     * checked again.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {

	/* The idea is to translate a <tt>for</tt> command into the code

            initialisation -> condition -> (no) continuation
                              | (yes) ^
                              V       |
                            body -> update
	*/

	// we create an empty block which is used to close the loop
	Block pivot = new Block(where);

	// we translate the condition of the loop. If the condition is true,
	// we execute the translation of the body and then the update.
	// Otherwise we execute what follows this command. This code will be
	// used to translate the <tt>initialisation</tt> component
	Block test = condition.translateAsTest
	    (where,body.translate(where,update.translate(where,pivot)),
	     continuation);

	test.doNotMerge();

	// we link the pivot to the code for the test,
	// so that we close the loop
	pivot.linkTo(test);

	return initialisation.translate(where,test);
    }
}
