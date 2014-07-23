package absyn;

import java.io.FileWriter;

import types.Type;
import types.CodeSignature;
import symbol.Symbol;
import semantical.TypeChecker;
import translate.CodeBlock;
import bytecode.LOAD;
import bytecode.STORE;

/**
 * A node of abstract syntax representing a reference to a program variable.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Variable extends Lvalue {

    /**
     * The name of the variable.
     */

    private Symbol name;

    /**
     * Constructs the abstract syntax of a reference to a program variable.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param name the name of the variable
     */

    public Variable(int pos, Symbol name) {
	super(pos);

	this.name = name;
    }

    /**
     * Yields the name of the variable.
     *
     * @return the name of the variable
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields the unique number of this variable inside the method where
     * it occurs. Type-checking must have been completed before calling
     * this method.
     *
     * @return the unique number of this variable inside the method where
     *         it occurs
     */

    public int getVarNum() {
	return getTypeChecker().getVarNum(name);
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the reference to a variable.
     * This amounts to adding an arc from the node for the variable
     * to the node for its <tt>name</tt> component.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("name",name.toDot(where),where);
    }

    /**
     * Performs the type-checking of the reference to a program variable,
     * by using a given type-checker.
     * It just checks that the variable is known to the type-checker.
     *
     * @param checker the type-checker to be used for type-checking
     * @return the static type assigned by <tt>checker</tt> to this variable
     */

    protected Type typeCheck$0(TypeChecker checker) {
	// we ask the type-checker for the type of this variable
	Type result = checker.getVar(name);

	if (result == null)
	    // if the type-checker does not know this variable,
	    // we issue an error
	    return error("undefined variable " + name);

	// we return the type assigned by the type-checker to this variable
	return result;
    }

    /**
     * Translates this expression into its intermediate Kitten code.
     * The result is a piece of code which starts with a <tt>load</tt>
     * bytecode which loads on the stack the value of this variable, and
     * continues with the given <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code executed after this expression
     * @return the code which evaluates this expression and continues
     *         with <tt>continuation</tt>
     */

    public CodeBlock translate(CodeSignature where, CodeBlock continuation) {
	return new LOAD
	    (where,getVarNum(),getStaticType()).followedBy(continuation);
    }

    /**
     * Generates the intermediate Kitten code which must be executed before
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. There is nothing to generate in this case.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return <tt>continuation</tt> itself
     */

    public CodeBlock translateBeforeAssignment
	(CodeSignature where, CodeBlock continuation) {

	return continuation;
    }

    /**
     * Generates the intermediate Kitten code which must be executed after
     * the evaluation of the rightvalue which is going to be assigned to this
     * variable. Namely, it generates a <tt>store</tt> bytecode.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the code which must be executed after this
     *                     expression
     * @return a <tt>store</tt> bytecode followed by <tt>continuation</tt>
     */

    public CodeBlock translateAfterAssignment
	(CodeSignature where, CodeBlock continuation) {

	return new STORE(where,getVarNum(),getStaticType())
	    .followedBy(continuation);
    }
}
