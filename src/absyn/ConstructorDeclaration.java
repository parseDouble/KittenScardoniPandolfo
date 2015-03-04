package absyn;

import java.io.FileWriter;
import java.util.HashSet;

import semantical.TypeChecker;
import symbol.Symbol;
import types.ClassMemberSignature;
import types.ClassType;
import types.ConstructorSignature;
import types.TypeList;
import types.VoidType;

/**
 * A node of abstract syntax representing the declaration of a constructor
 * of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ConstructorDeclaration extends CodeDeclaration {

	/**
	 * Constructs the abstract syntax of a constructor declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param formals the abstract syntax of the formal parameters
	 *                of the constructor
	 * @param body the abstract syntax of the body of the constructor
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 */

	public ConstructorDeclaration(int pos, FormalParameters formals,
			Command body, ClassMemberDeclaration next) {
		super(pos,formals,body,next);
	}

	/**
	 * Yields the signature of this constructor declaration.
	 *
	 * @return the signature of this constructor declaration.
	 *         Returns <tt>null</tt> if type-checking has not been
	 *         performed yet
	 */

	public ConstructorSignature getSignature() {
		return (ConstructorSignature)super.getSignature();
	}

	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing the <tt>formal</tt> and <tt>body</tt> fields.
	 *
	 * @param where the file where the dot representation must be written
	 */

	protected void toDot$0(FileWriter where) throws java.io.IOException {
		if (getFormals() != null)
			linkToNode("formals",getFormals().toDot(where),where);

		linkToNode("body",getBody().toDot(where),where);
	}

	/**
	 * Adds the signature of this constructor declaration to the given class.
	 *
	 * @param clazz the class where the signature of this constructor
	 *              declaration must be added
	 */

	@Override
	protected void addMember(ClassType clazz) {
		ConstructorSignature cSig =
				new ConstructorSignature
				(clazz,getFormals() != null ?
						getFormals().toType() : TypeList.EMPTY,this);

		clazz.addConstructor(cSig);

		// we record the signature of this constructor inside
		// this abstract syntax
		setSignature(cSig);
	}

	/**
	 * Type-checks this constructor declaration.
	 * Namely, it builds a type-checker whose only variable in scope is
	 * <tt>this</tt> of type <tt>currentClass</tt> and the parameters of the
	 * constructor, and
	 * where return instructions of type <tt>void</tt> are allowed.
	 * It then type-checks the body of the constructor in that type-checker
	 * and checks that it does not contain any dead-code.
	 *
	 * @param clazz the semantical type of the class where this
	 *              constructor occurs.
	 */

	protected void typeCheck$0(ClassType clazz) {
		TypeChecker checker;

		// we build a type-checker which signals errors for the source code
		// of the class where this constructor is defined,
		// whose only variables in scope is
		// <tt>this</tt> of type <tt>clazz</tt> and
		// the parameters of the constructor, and
		// where return instructions of type <tt>void</tt> are allowed
		checker = new TypeChecker(VoidType.INSTANCE, clazz.getErrorMsg());
		checker = checker.putVar(Symbol.THIS,clazz);

		// we enrich the type-checker with the formal parameters
		checker = getFormals() != null ?
				getFormals().typeCheck(checker) : checker;

				TypeList pars = getFormals() != null ?
						getFormals().typeCheck() : TypeList.EMPTY;

						// we type-check the body of the constructor
						// in the resulting type-checker
						getBody().typeCheck(checker);

						// we check that there is no dead-code in the body of the constructor
						getBody().checkForDeadcode();

						// if our superclass exists, it must contain an empty constructor,
						// which will be chained to this constructor
						if (clazz.getSuperclass() != null &&
								clazz.getSuperclass().constructorLookup(TypeList.EMPTY) == null)
							clazz.getErrorMsg().error
							(getPos(),clazz.getSuperclass() + " has no empty constructor");

						// constructors return nothing, so that we do not check whether
						// a <tt>return</tt> statement is always present at the end of every
						// syntactical execution path in the body of a constructor
						// (see instead <tt>absyn/MethodDeclaration.java</tt>)
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
		if (done.contains(getSignature())) return;

		super.translate(done);
	}
}
