package absyn;

import java.io.FileWriter;

import semantical.TypeChecker;
import symbol.Symbol;
import types.ClassType;
import types.MethodSignature;
import types.StartMethodSignature;
import types.Type;
import types.TypeList;

/**
 * A node of abstract syntax representing the declaration of a method
 * of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class MethodDeclaration extends CodeDeclaration {

    /**
     * The abstract syntax of the return type of the method.
     */

    private TypeExpression returnType;

    /**
     * The name of the method.
     */

    private Symbol name;

    /**
     * Constructs the abstract syntax of a method declaration.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param returnType the abstract syntax of the return type of the method
     * @param name the name of the method
     * @param formals the abstract syntax of the formal parameters
     *                of the method
     * @param body the abstract syntax of the body of the method
     * @param next the abstract syntax of the declaration of the
     *             subsequent class member, if any
     */

    public MethodDeclaration(int pos, TypeExpression returnType, Symbol name,
			     FormalParameters formals, Command body,
			     ClassMemberDeclaration next) {
	super(pos,formals,body,next);

	this.name = name;
	this.returnType = returnType;
    }

    /**
     * Yields the abstract syntax of the return type of the method.
     *
     * @return the abstract syntax of the return type of the method
     */

    public TypeExpression getReturnType() {
	return returnType;
    }

    /**
     * Yields the name of this method.
     *
     * @return the name of this method
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields the signature of this method declaration.
     *
     * @return the signature of this method declaration.
     *         Returns <tt>null</tt> if type-checking has not been
     *         performed yet
     */

    public MethodSignature getSignature() {
	return (MethodSignature)super.getSignature();
    }

    /**
     * Adds arcs between the dot node for this piece of abstract syntax
     * and those representing the <tt>returnType</tt>, <tt>name</tt>,
     * <tt>formals</tt> and <tt>body</tt> fields.
     *
     * @param where the file where the dot representation must be written
     */

    protected void toDot$0(FileWriter where) throws java.io.IOException {
	linkToNode("returnType",returnType.toDot(where),where);
	linkToNode("name",name.toDot(where),where);

	if (getFormals() != null)
	    linkToNode("formals",getFormals().toDot(where),where);

	linkToNode("body",getBody().toDot(where),where);
    }

    /**
     * Adds the signature of this method declaration to the given class.
     *
     * @param clazz the class where the signature of this method
     *              declaration must be added
     */

    @Override
    protected void addMember(ClassType clazz) {
	Type rt = returnType.toType();
	TypeList pars = getFormals() != null ?
	    getFormals().toType() : TypeList.EMPTY;
	MethodSignature mSig;

	// the main method, from which starts the execution of a Kitten class,
	// uses a <tt>StartMethodSignature</tt>
	if (name == Symbol.MAIN) mSig = new StartMethodSignature(clazz,this);
	else mSig = new MethodSignature(clazz,rt,pars,name,this);

	clazz.addMethod(name,mSig);

	// we record the signature of this method inside this abstract syntax
	setSignature(mSig);	
    }

    /**
     * Type-checks this method declaration.
     * It first checks that if this method overrides a method of a superclass
     * then the return type is a subtype of that of the overridden method.
     * Then it builds a type-checker whose only variable in scope is
     * <tt>this</tt> of type <tt>clazz</tt> and the parameters of the method,
     * and where return instructions of type <tt>returnType</tt> are allowed.
     * It then type-checks the body of the method in that type-checker.
     * It finally checks that if this method dos not return <tt>void</tt>,
     * then every execution path ends with a <tt>return</tt> command.
     *
     * @param clazz the semantical type of the class where this method occurs
     */

    @Override
    protected void typeCheck$0(ClassType clazz) {
	TypeChecker checker;
	ClassType superclass;
	MethodSignature overridden;
	Type rt = returnType.typeCheck();

	// we build a type-checker which signals errors for the source code
	// of the class where this method is defined,
	// whose only variables in scope is <tt>this</tt> of type
	// <tt>clazz</tt> and the parameters of the method, and
	// where return instructions of type <tt>returnType</tt> are allowed
	checker = new TypeChecker(rt,clazz.getErrorMsg());

	// the main method is the only <i>static</i> method, where there
	// is no <tt>this</tt> variable
	if (!(getSignature() instanceof StartMethodSignature))
	    checker = checker.putVar(Symbol.THIS,clazz);

	// we enrich the type-checker with the formal parameters
	checker = getFormals() != null ?
	    getFormals().typeCheck(checker) : checker;

	TypeList pars = getFormals() != null ? getFormals().typeCheck() : null;

	// we check if this method overrides a method of some superclass
	superclass = clazz.getSuperclass();
	if (superclass != null) {
	    overridden = superclass.methodLookup(name,pars);

	    if (overridden != null)
		// it does override a method of a superclass. We check
		// that its return type has been refined. We use the
		// <tt>canBeAssignedToSpecial</tt> method so that
		// <tt>void</tt> can be refined into <tt>void</tt>
		if (!rt.canBeAssignedToSpecial(overridden.getReturnType()))
		    error(checker,
			  "illegal return type for overriding method \"" +
			  name + "\". Was " + overridden.getReturnType());
	}

	// we type-check the body of the method
	// in the resulting type-checker
	getBody().typeCheck(checker);

	// we check that there is no dead-code in the body of the method
	boolean stopping = getBody().checkForDeadcode();

	// we check that if the method does not return <tt>void</tt> then
	// every syntactical execution path in the method ends with
	// a <tt>return</tt> command (<tt>continue</tt> and <tt>break</tt>
	// are forbidden in this position)
	if (rt != Type.VOID && !stopping)
	    error(checker,"missing return statement");
    }
}
