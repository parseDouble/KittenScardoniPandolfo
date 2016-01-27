package types;

import java.util.Set;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import javaBytecodeGenerator.TestClassGenerator;
import absyn.TestDeclaration;
import translation.Block;

public class TestSignature extends CodeSignature {

	/**
	 * Constructs the signature of a test with the given name.
	 *
	 * @param clazz the class where this test is defined
	 * @param name the name of the test
	 * @param abstractSyntax the abstract syntax of the declaration of this test
	 */
	public TestSignature(ClassType clazz, String name, 
			TestDeclaration abstractSyntax) {

		super(clazz, VoidType.INSTANCE, TypeList.EMPTY, name, abstractSyntax);
	}
	
	/**
	 * Adds the the given class generator a Java bytecode method for this test.
	 *
	 * @param classGen the generator of the class where the test lives
	 */
	public void createTest(TestClassGenerator tcg, Set<FixtureSignature> fix) {
		InstructionList il = new InstructionList();
		il.append(tcg.getFactory().createNew(getDefiningClass().getName()));
		il.append(InstructionConstants.DUP);
		il.append(tcg.getFactory().createInvoke(getDefiningClass().getName(), 
				Constants.CONSTRUCTOR_NAME, Type.VOID, 
				Type.NO_ARGS, Constants.INVOKESPECIAL));
		il.append(InstructionFactory.ASTORE_0);
		
		//invocation of the fixtures
		for (FixtureSignature fixtureSignature : fix) {
			il.append(InstructionConstants.ALOAD_0);
			il.append(tcg.getFactory().createInvoke(tcg.getClassName(),
					fixtureSignature.getName(), Type.VOID,
					new Type[]{getDefiningClass().toBCEL()},
					Constants.INVOKESTATIC));
		}
		
		il.append(tcg.generateJavaBytecode(getCode()));
		MethodGen mg=new MethodGen(Constants.ACC_PRIVATE | Constants.ACC_STATIC,
				Type.STRING, Type.NO_ARGS, null, getName(), 
				tcg.getClassName(),	il, tcg.getConstantPool());
		
		mg.setMaxStack();
		mg.setMaxLocals();
		
		tcg.addMethod(mg.getMethod());
	}
	
	/**
	 * Adds a prefix to the Kitten bytecode generated for this test.
	 *
	 * @param code the code already compiled for this test
	 * @return {@code code} itself
	 */

	@Override
	protected Block addPrefixToCode(Block code) {
		return code;
	}

}
