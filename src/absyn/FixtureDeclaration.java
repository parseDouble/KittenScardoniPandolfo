package absyn;

import java.io.FileWriter;
import java.io.IOException;



import semantical.TypeChecker;
import types.ClassType;
import types.FixtureSignature;
import types.VoidType;

public class FixtureDeclaration extends CodeDeclaration {
	
	/**
	 * Constructs the abstract syntax of a fixture declaration.
	 *
	 * @param pos the starting position in the source file of
	 *            the concrete syntax represented by this abstract syntax
	 * @param body the abstract syntax of the body of the fixture
	 * @param next the abstract syntax of the declaration of the
	 *             subsequent class member, if any
	 *             
	 */
	public FixtureDeclaration(int pos,
			Command body, ClassMemberDeclaration next) {
		super(pos, null, body, next);
	}
	
	/**
	 * Yields the signature of this fixture declaration.
	 *
	 * @return the signature of this fixture declaration. Yields {@code null}
	 *         if type-checking has not been performed yet
	 */
	public FixtureSignature getSignature(){
		return (FixtureSignature)super.getSignature();
	}
	
	/**
	 * Adds arcs between the dot node for this piece of abstract syntax
	 * and those representing [@link {@link #body}.
	 *
	 * @param where the file where the dot representation must be written
	 */
	@Override
	protected void toDotAux(FileWriter where) throws IOException {
		linkToNode("body", getBody().toDot(where), where);

	}
	
	
	/**
	 * Adds the signature of this fixture declaration to the given class.
	 *
	 * @param clazz the class where the signature of this fixture declaration must be added
	 */
	@Override
	protected void addTo(ClassType clazz) {
		FixtureSignature fixsign=new FixtureSignature(clazz, this);
		
		clazz.addFix(fixsign);
		setSignature(fixsign);
	}

	
	/**
	 * Type-checks this fixture declaration.
	 * It first type-check the body of the fixture in the resulting type-checker
	 * It then check that there is no dead-code in the body of the fixture
	 * 
	 * @param clazz the semantical type of the class where this fixture occurs
	 */
	@Override
	protected void typeCheckAux(ClassType currentClass) {
		TypeChecker tcheck= new TypeChecker(VoidType.INSTANCE, currentClass.getErrorMsg());
		
		tcheck= tcheck.putVar("this", currentClass);
		getBody().typeCheck(tcheck);
		getBody().checkForDeadcode();

	}

}
