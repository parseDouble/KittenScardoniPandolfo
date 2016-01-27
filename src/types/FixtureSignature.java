package types;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import javaBytecodeGenerator.TestClassGenerator;
import absyn.FixtureDeclaration;
import translation.Block;

public class FixtureSignature extends CodeSignature {
	//index of fixture(used for the name)
	private static int index=0;
		
	/**
	 * Constructs the signature of a fixture
	 * 
	 * @param clazz the class where this fixture is defined
	 * @param abstractSyntax the abstract syntax of the declaration of this fixture
	 */
	public FixtureSignature(ClassType clazz, FixtureDeclaration abstractSyntax) {
		super(clazz, VoidType.INSTANCE, TypeList.EMPTY, "Fixture"+ index++, abstractSyntax);
	}

	/**
	 * Adds a prefix to the Kitten bytecode generated for this method.
	 *
	 * @param code the code already compiled for this fixture
	 * @return {@code code} itself
	 */
	@Override
	protected Block addPrefixToCode(Block code) {
		return code;
	}
	
	/**
	 * Adds the the given class generator a Java bytecode method for this fixture.
	 *
	 * @param classGen the generator of the class where the fixture lives
	 */
	public void createFixture(TestClassGenerator tcg){
		InstructionList il = new InstructionList();
		
		il.append(getCode().getBytecode().generateJavaBytecode(tcg));
		
		MethodGen metGen = new MethodGen(Constants.ACC_PRIVATE | Constants.ACC_STATIC, 
				Type.VOID, new Type[]{ (getDefiningClass().toBCEL())},
				null, getName(), tcg.getClassName(), il, tcg.getConstantPool());		
		metGen.setMaxStack();
		metGen.setMaxLocals();
		tcg.addMethod(metGen.getMethod());
	}

}
