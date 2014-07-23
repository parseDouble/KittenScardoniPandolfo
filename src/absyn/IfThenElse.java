package absyn;

import java.io.FileWriter;

import types.CodeSignature;
import semantical.TypeChecker;
import translate.CodeBlock;

/**
 * A node of abstract syntax representing a conditional command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class IfThenElse extends Command {

    /**
     * The guard or condition of the conditional.
     */

    private Expression condition;

    /**
     * The <i>then</i> branch of the conditional.
     */

    private Command then;

    /**
     * The <i>else</i> branch of the conditional.
     * It always exists. If the conditional had no <tt>else</tt> branch,
     * a <tt>Skip()</tt> command is found here.
     */

    private Command _else;

    /**
     * Constructs the abstract syntax of a conditional command.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param condition the guard or condition of the conditional
     * @param then the <i>then</i> branch of the conditional
     * @param _else the <i>else</i> branch of the conditional
     */

    public IfThenElse(int pos, Expression condition,
		      Command then, Command _else) {
	super(pos);

	this.condition = condition;
	this.then = then;
	this._else = _else;
    }

    /**
     * Constructs the abstract syntax of a conditional command without
     * the <tt>else</tt> branch.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param condition the guard or condition of the conditional
     * @param then the <i>then</i> branch of the conditional
     */

    public IfThenElse(int pos, Expression condition, Command then) {
	// we provide a fictitious <tt>else</tt> branch which just skips
	this(pos,condition,then,new Skip(pos));
    }

    /**
     * Yields the condition or guard of the conditional.
     *
     * @return the condition or guard of the conditional
     */

    public Expression getCondition() {
	return condition;
    }

    /**
     * Yields the <i>then</i> branch of the conditional.
     *
     * @return the <i>then</i> branch of the conditional
     */

    public Command getThen() {
	return then;
    }

    /**
     * Yields the <i>else</i> branch of the conditional.
     *
     * @return the <i>else</i> branch of the conditional
     */

    public Command getElse() {
	return _else;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the conditional command.
     * This amounts to adding arcs from the node for the conditional
     * command to the abstract syntax for its <tt>condition</tt>,
     * <tt>then</tt> and <tt>_else</tt> components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("condition",condition.toDot(where),where);
	linkToNode("then",then.toDot(where),where);
	linkToNode("_else",_else.toDot(where),where);
    }

    /**
     * Performs the type-checking of the conditional command
     * by using a given type-checker. It type-checks the condition, <i>then</i>
     * and <i>else</i> branches of the conditional.
     * It checks that the condition is
     * a Boolean expression. It returns the original type-checker passed
     * as a parameter, so that
     * local declarations inside the branches of the conditional are not
     * visible after.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheck$0(TypeChecker checker) {
	condition.mustBeBoolean(checker);
	then.typeCheck(checker);
	_else.typeCheck(checker);

	// we return the original type-checker. Hence local declarations
	// inside the <tt>then</tt> or <tt>_else</tt> are not visible
	// after the conditional
	return checker;
    }

    /**
     * Checks that this conditional does not contain <i>dead-code</i>, that is,
     * commands which can never be executed.
     *
     * @return true if and only if every execution path in both branches of the
     *         conditional ends with a <tt>return</tt> command
     */

    public boolean checkForDeadcode() {
	return then.checkForDeadcode() && _else.checkForDeadcode();
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which evaluates the
     * <tt>condition</tt> of the conditional and then continues with
     * the code for the compilation of the <tt>then</tt> or <tt>_else</tt>
     * components. It the continues with <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	// by making the <tt>continuation</tt> unmergeable with whatever we
	// prefix to it, we avoid duplicating it in the <tt>then</tt> and
	// <tt>else</tt> branch. <b>This is just an optimisation!</b>.
	// Try removing this line: everything will work, but the code
	// will be larger
	continuation.doNotMerge();

	// we compile the condition by using as <tt>yes</tt> and <tt>no</tt>
	// continuations the translations of the <tt>then</tt> and
	// <tt>_else</tt> components
	return condition.translateAsTest
	    (where,then.translate(where,continuation),
	     _else.translate(where,continuation));
    }
}
