package absyn;

import java.util.HashSet;

import symbol.Symbol;
import types.Type;
import types.CodeSignature;
import types.ClassMemberSignature;
import translate.CodeBlock;
import bytecode.Bytecode;
import bytecode.BytecodeList;
import bytecode.RETURN;
import bytecode.CALL;
import bytecode.GETFIELD;
import bytecode.PUTFIELD;

/**
 * A node of abstract syntax representing the declaration of a constructor
 * or method of a Kitten class.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CodeDeclaration extends ClassMemberDeclaration {

    /**
     * The abstract syntax of the formal parameters of the constructor
     * or method. This might be <tt>null</tt>.
     */

    private FormalParameters formals;

    /**
     * The abstract syntax of the body of the constructor or method.
     */

    private Command body;

    /**
     * The signature of this constructor or method.
     * This is <tt>null</tt> if this constructor or method has not been
     * type-checked yet.
     */

    private CodeSignature sig;

    /**
     * Constructs the abstract syntax of a constructor or method declaration.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param formals the abstract syntax of the formal parameters
     *                of the constructor or method
     * @param body the abstract syntax of the body of the constructor or method
     * @param next the abstract syntax of the declaration of the
     *             subsequent class member, if any
     */

    protected CodeDeclaration(int pos, FormalParameters formals,
			      Command body, ClassMemberDeclaration next) {
	super(pos,next);

	this.formals = formals;
	this.body = body;
    }

    /**
     * Yields the abstract syntax of the formal parameters of the constructor
     * or method.
     *
     * @return the abstract syntax of the formal parameters of the constructor
     *         or method
     */

    public FormalParameters getFormals() {
	return formals;
    }

    /**
     * Yields the abstract syntax of the body of the constructor or method.
     *
     * @return the abstract syntax of the body of the constructor or method
     */

    public Command getBody() {
	return body;
    }

    /**
     * Specifies the code signature of this declaration.
     *
     * @param sig the code signature of this declaration
     */

    protected void setSignature(CodeSignature sig) {
	this.sig = sig;
    }

    /**
     * Yields the signature of this method or constructor declaration.
     *
     * @return the signature of this method or constructor declaration.
     *         Returns <tt>null</tt> if type-checking has not been
     *         performed yet
     */

    public CodeSignature getSignature() {
	return sig;
    }

    /**
     * Translates this constructor or method into intermediate Kitten code.
     * This amounts to translating its body with a continuation containing
     * a <tt>return</tt> bytecode. This way, if a method does not have an
     * explicit <tt>return</tt> statement, it is automatically put at its end.
     *
     * @param done the set of <tt>CodeSignature</tt>'s which have been
     *             already translated
     */

    public void translate(HashSet<ClassMemberSignature> done) {
	if (done.contains(sig)) return;
	else done.add(sig);

	// we translate the body of the constructor or
	// method with a block containing RETURN as continuation. This way,
	// all methods returning <tt>void</tt> and
	// with some missing <tt>return</tt> command are correctly
	// terminated anyway. If the method is not <tt>void</tt>, this
	// precaution is useless since we know that every execution path
	// ends with a <tt>return</tt> command, as guaranteed by
	// <tt>checkForDeadCode()</tt> (see <tt>typeCheck()</tt> in
	// <tt>MethodDeclaration.java</tt>)
	sig.setCode(getBody().translate
		    (sig,new CodeBlock(new RETURN(sig,Type.VOID))));

	// we translate all methods and constructors which are referenced
	// from the code we have generated
	translateReferenced(sig.getCode(),done,new HashSet<CodeBlock>());
    }

    /**
     * Auxiliary method which
     * translates into Kitten bytecode all class members which are
     * referenced from the given block and the blocks reachable from it.
     *
     * @param cb the block
     * @param done the <tt>ClassMemberSignature</tt>'s already translated
     * @param blocksDone the blocks which have been already processed
     */

    private void translateReferenced
	(CodeBlock cb,
	 HashSet<ClassMemberSignature> done, HashSet<CodeBlock> blocksDone) {

	// if we already processed the block, we return immediately
	if (blocksDone.contains(cb)) return;

	// we have processed the block
	blocksDone.add(cb);

	for (BytecodeList cursor = cb.getBytecode(); cursor != null;
	     cursor = cursor.getTail()) {
	    Bytecode h = cursor.getHead();

	    if (h instanceof GETFIELD) done.add(((GETFIELD)h).getField());
	    else if (h instanceof PUTFIELD) done.add(((PUTFIELD)h).getField());
	    else if (h instanceof CALL)
		for (CodeSignature callee: ((CALL)h).getDynamicTargets())
		    callee.getAbstractSyntax().translate(done);
	}

	// we continue with the following blocks
	for (CodeBlock f: cb.getFollows())
	    translateReferenced(f,done,blocksDone);
    }
}