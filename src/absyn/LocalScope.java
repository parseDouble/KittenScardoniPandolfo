package absyn;

import java.io.FileWriter;

import types.CodeSignature;
import semantical.TypeChecker;
import translate.Block;

/**
 * A node of abstract syntax representing a local scope, <i>i.e.</i>, a block
 * of code whose local declarations are not visible after the block.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LocalScope extends Command {

    /**
     * The body of the local scope, <i>i.e.</i>, the command which is
     * executed in the local scope.
     */

    private Command body;

    /**
     * Constructs the abstract syntax of a local scope, <i>i.e.</i>, a block
     * of code whose local declarations are not visible after the block.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param body the body of the local scope, <i>i.e.</i>, the command
     *             which is executed in the local scope
     */

    public LocalScope(int pos, Command body) {
	super(pos);

	this.body = body;
    }

    /**
     * Yields the body of the local scope.
     *
     * @return the body of the local scope
     */

    public Command getBody() {
	return body;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of a local scope.
     * This amounts to adding arcs from the node for the local
     * scope to the <tt>body</tt> component.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDotAux(FileWriter where) throws java.io.IOException {
	linkToNode("body",body.toDot(where),where);
    }

    /**
     * Performs the type-checking of a local scope.
     * Namely, it type-checks the body of the local scope, and returns the
     * original type-checker, so that local declarations inside the body of the
     * local scope are not visible after.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheckAux(TypeChecker checker) {
	body.typeCheck(checker);

	return checker;
    }

    /**
     * Checks that this local scope does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. This is reduced to the problem
     * of finding dead-code in its body.
     *
     * @return true if and only if every syntactical
     *         execution path in the body of this
     *         local declaration ends with a <tt>return</tt> command
     */

    public boolean checkForDeadcode() {
	return body.checkForDeadcode();
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it translates the <tt>body</tt> of the
     * local scope with the given <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	// we translate the body of the local scope with
	// the given <tt>continuation</tt>
	return body.translate(where,continuation);
    }
}
