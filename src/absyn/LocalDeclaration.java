package absyn;

import java.io.FileWriter;

import symbol.Symbol;
import semantical.TypeChecker;
import types.Type;
import types.CodeSignature;
import translate.Block;
import bytecode.STORE;

/**
 * A node of abstract syntax representing the declaration of a local variable.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class LocalDeclaration extends Command {

    /**
     * The abstract syntax of the type of the variable which is declared.
     */

    private TypeExpression type;

    /**
     * The name of the variable which is declared.
     */

    private Symbol name;

    /**
     * The abstract syntax of the initialising expression
     * for the variable which is declared
     */

    private Expression initialiser;

    /**
     * Constructs the abstract syntax of the declaration of a local variable.
     *
     * @param pos the position in the source file where it starts
     *            the concrete syntax represented by this abstract syntax
     * @param type the abstract syntax of the type of the variable which
     *             is declared
     * @param name the name of the variable which is declared
     * @param initialiser the abstract syntax of the initialising expression
     *                    for the variable which is declared
     */

    public LocalDeclaration(int pos, TypeExpression type, Symbol name,
			    Expression initialiser) {
	super(pos);

	this.type = type;
	this.name = name;
	this.initialiser = initialiser;
    }

    /**
     * Yields the abstract syntax of the type of the variable
     * which is declared.
     *
     * @return the abstract syntax of the type of the variable
     *         which is declared
     */

    public TypeExpression getType() {
	return type;
    }

    /**
     * Yields the name of the variable which is declared.
     *
     * @return the name of the variable which is declared
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields the abstract syntax of the initialising expression
     * for the variable which is declared.
     *
     * @return the abstract syntax of the initialising expression
     *         for the variable which is declared
     */

    public Expression getInitialiser() {
	return initialiser;
    }

    /**
     * Adds abstract syntax class-specific information in the dot file
     * representing the abstract syntax of the local declaration of a
     * variable. This amounts to adding arcs from the node for the local
     * declaration to the <tt>type</tt>, <tt>name</tt> and
     * <tt>initialiser</tt> components.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDotAux(FileWriter where) throws java.io.IOException {
	linkToNode("type",type.toDot(where),where);
	linkToNode("name",name.toDot(where),where);
	linkToNode("initialiser",initialiser.toDot(where),where);
    }

    /**
     * Performs the type-checking of the declaration of a local variable,
     * by using a given type-checker. It type-checks the declared type of
     * the variable. If there is an initialising expression, it type-checks it
     * and then checks that it can be assigned to the declared type of
     * the variable. It returns a type-checker identical to that
     * passed as a parameter but where the local variable is bound
     * to its declared type.
     *
     * @param checker the type-checker to be used for type-checking
     * @return a type-checker identical to <tt>checker</tt> but where
     *         <tt>name</tt> is bound to <tt>type</tt>
     */

    protected TypeChecker typeCheckAux(TypeChecker checker) {
	// we type check the declared type of the variable
	Type left = type.typeCheck();

	// we type-check the initialising expression and require
	// that it can be assigned to the declared type of the variable
	Type right = initialiser.typeCheck(checker);

	if (!right.canBeAssignedTo(left))
	    error(right + " cannot be assigned to " + left);

	// we return a type-checker where the variable is bound
	// to its declared type
	return checker.putVar(name,left);
    }

    /**
     * Checks that this local declaration
     * does not contain <i>dead-code</i>, that is,
     * commands which can never be executed. This is always true for
     * local declarations.
     *
     * @return false, since this command never terminates with a
     *         <tt>return</tt> command
     */

    public boolean checkForDeadcode() {
	return false;
    }

    /**
     * Translates this command into intermediate
     * Kitten bytecode. Namely, it returns a code which starts with<br>
     * <br>
     * <i>translation of <tt>initialiser</tt></i><br>
     * <tt>store varNum of type varType</tt><br>
     * <br>
     * and continues with the <tt>continuation</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param continuation the continuation to be executed after this command
     * @return the code executing this command and then
     *         the <tt>continuation</tt>
     */

    public Block translate(CodeSignature where, Block continuation) {
	// we get the number and type of the variable
	int varNum = getTypeChecker().getVarNum(name);
	Type staticType = type.getStaticType();

	// we return a code which starts with the translation of the
	// initialising expression, followed by
	// the <tt>STORE</tt> bytecode, followed by the <tt>continuation</tt>
	return initialiser.translateAs
	    (where,staticType,
	     new STORE(where,varNum,staticType).followedBy(continuation));
    }
}
