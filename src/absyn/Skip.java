package absyn;

import types.CodeSignature;
import semantical.TypeChecker;
import translate.CodeBlock;

/**
 * A node of abstract syntax representing a skip command.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Skip extends Command {

    /**
     * Constructs the abstract syntax of a skip command.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     */

    public Skip(int pos) {
	super(pos);
    }

    /**
     * Performs the type-checking of this skip command
     * by using a given type-checker. A skip command is always type-checked.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the type-checker <tt>checker</tt>
     */

    protected TypeChecker typeCheck$0(TypeChecker checker) {
	// there is nothing to do, it is always type-checked

	// the type-checker is not modified
	return checker;
    }

    /**
     * Checks
     * that this command does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. This is always true for
     * the skip command.
     *
     * @return false, since this command never terminates with a
     *         <tt>return</tt> command
     */

    public boolean checkForDeadcode() {
	return false;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which runs the <tt>Skip</tt>
     * command and then continues with the given <tt>continuation</tt>,
     * that is, it returns the <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	// yields the continuation, unchanged
	return continuation;
    }
}
