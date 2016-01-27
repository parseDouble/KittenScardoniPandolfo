package absyn;

import java.io.FileWriter;
import java.io.IOException;

import semantical.TypeChecker;
import types.ClassType;
import types.TestSignature;
import types.VoidType;

public class TestDeclaration extends CodeDeclaration {
	
	/**
	 * The name of the test.
	 */
	private final String name;	
	
	/**
	 * Constructs the abstract syntax of a test declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param name the name of the test
	 * @param body the abstract syntax of the body of the test
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 */
	public TestDeclaration(int pos, String name, Command body,
			ClassMemberDeclaration next) {
		super(pos, null, body, next);
		this.name = name;
	}
	
	/**
	 * Yields the name of this test.
	 *
	 * @return the name of this test
	 */
	public String getName(){
		return this.name;
	}

	/**
	 * Yields the signature of this test declaration.
	 *
	 * @return the signature of this test declaration. Yields {@code null}
	 *         if type-checking has not been performed yet
	 */
	public TestSignature getSignature(){
		return (TestSignature)super.getSignature();
	}
	
	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing [@link {@link #name} and {@link #body}.
	 *
	 * @param where the file where the dot representation must be written
	 */
	@Override
	protected void toDotAux(FileWriter where) throws IOException {
		linkToNode("name", toDot(name,where), where);
		linkToNode("body",getBody().toDot(where), where);
	}

	/**
	 * Adds the signature of this test declaration to the given class.
	 *
	 * @param clazz the class where the signature of this test declaration must be added
	 */
	@Override
	protected void addTo(ClassType clazz) {
		TestSignature tsign=new TestSignature(clazz, name, this);
		clazz.addTest(name, tsign);
		setSignature(tsign);

	}

	/**
	 * Type-checks this test declaration.
	 * It first type-check the body of the test in the resulting type-checker
	 * It then check that there is no dead-code in the body of the test
	 * 
	 * @param clazz the semantical type of the class where this test occurs
	 */
	@Override
	protected void typeCheckAux(ClassType currentClass) {
		TypeChecker tcheck= new TypeChecker(VoidType.INSTANCE, currentClass.getErrorMsg(), true);
		tcheck= tcheck.putVar("this", currentClass);
		
		getBody().typeCheck(tcheck);
		getBody().checkForDeadcode();
	}

}
