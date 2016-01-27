package javaBytecodeGenerator;

import java.io.PrintStream;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DDIV;

import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.L2D;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LSUB;
import org.apache.bcel.generic.MethodGen;

import org.apache.bcel.generic.Type;


import types.ClassMemberSignature;
import types.ClassType;
import types.FixtureSignature;
import types.TestSignature;

@SuppressWarnings("serial")
public class TestClassGenerator extends JavaClassGenerator {
	
	/**
	 * Builds a test class generator for the given class type.
	 *
	 * @param clazz the class type
	 * @param sigs a set of class member signatures. These are those that must be translated
	 */
	public TestClassGenerator(ClassType clazz, Set<ClassMemberSignature> sigs) {

		// test class extends only java.lang.Object
		super(clazz.getName()+"Test", "java.lang.Object",
				Constants.ACC_PUBLIC);
		
		
		// we add the fixtures
		for (FixtureSignature fix : clazz.getFixture())
			if (sigs == null || sigs.contains(fix))
				//generate fixture code
				fix.createFixture(this);
		for (Set<TestSignature> tests : clazz.getTest().values()) {
			for (TestSignature test : tests) {
				if (sigs == null || sigs.contains(test)){
					//generate test method code
					test.createTest(this, clazz.getFixture());
					
				}
			}
		}
		
		//create a new instruction list
		InstructionList il = new InstructionList();
		
		
		Type buildType= Type.getType(StringBuilder.class);
		String buildName = StringBuilder.class.getName();
		
		//initialize array of type used for call methods 
		Type[] strArg = {Type.STRING};
		Type[] longArg = {Type.LONG};
		Type[] doubleArg = {Type.DOUBLE};
		Type[] intArg = {Type.INT};
		
		//index of local variables
		int startTime = 3;
		int initTestTime = 5;
		int testRes = 7;
		
		
		String argName[] = {"arg0"};
		//create main method
		MethodGen main = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, Type.VOID, 
				new Type[]{new ArrayType("java.lang.String", 1)},
				argName, "main", getClassName(), il, getConstantPool());
		ConstantPoolGen consts = main.getConstantPool();
		
		//passed
		il.append(InstructionFactory.ICONST_0);
		il.append(InstructionFactory.ISTORE_1);
		//failed
		il.append(InstructionFactory.ICONST_0);
		il.append(InstructionFactory.ISTORE_2);
		
		
		il.append(getFactory().createPrintln("\nTest execution for class "+getClassName()+":\n"));
		il.append(getFactory().createInvoke(System.class.getName(), "nanoTime", Type.LONG,
				Type.NO_ARGS, Constants.INVOKESTATIC));
		
		il.append(InstructionFactory.createStore(Type.LONG, startTime));
		
		il.append(getFactory().createNew(buildName));
		il.append(InstructionConstants.DUP);
		il.append(new LDC(consts.addString("\nResults:\t")));
		//creates the constructor invocation of the tested class
		il.append(getFactory().createInvoke(buildName, Constants.CONSTRUCTOR_NAME, Type.VOID,
				strArg, Constants.INVOKESPECIAL));
		
		//for each test
		for (Set<TestSignature> testSigns : clazz.getTest().values()) {
			for (TestSignature testSign : testSigns) {
				il.append(getFactory().createInvoke(System.class.getName(), "nanoTime", Type.LONG,
						Type.NO_ARGS, Constants.INVOKESTATIC));
				il.append(new LSTORE(initTestTime));
				
				il.append(new LDC(consts.addString("\n - "+testSign.getName()+": ")));
				
				il.append(getFactory().createInvoke(buildName, "append", buildType, strArg,
						Constants.INVOKEVIRTUAL));
				
				//invoke test method
				il.append(getFactory().createInvoke(getClassName(), testSign.getName(),
						Type.STRING, Type.NO_ARGS, Constants.INVOKESTATIC));

				il.append(InstructionFactory.createStore(Type.STRING, testRes));
				
				il.append(InstructionFactory.createLoad(Type.STRING, testRes));
				
				//check if test is passed
				BranchHandle testResHandle = il.append(new IFNONNULL(null));
				//test passed code
				il.append(new LDC(consts.addString("passed [")));
				
				il.append(getFactory().createInvoke(buildName, "append", buildType, strArg, Constants.INVOKEVIRTUAL));
				il.append(new IINC(1, 1));
				
				BranchHandle gotoHandle = il.append(new GOTO(null));
				//test failed code
				InstructionHandle failedHandle = il.append(new LDC(consts.addString("failed [")));
				il.append(getFactory().createInvoke(buildName, "append", buildType, strArg, Constants.INVOKEVIRTUAL));
				il.append(new IINC(2, 1));
				
				testResHandle.setTarget(failedHandle);
				//in both cases
				InstructionHandle afterIf =il.append(
						getFactory().createGetStatic(TimeUnit.class.getName(), 
								"NANOSECONDS", Type.getType(TimeUnit.class)));

				
				gotoHandle.setTarget(afterIf);
				//add test duration
				il.append(getFactory().createInvoke(System.class.getName(), "nanoTime",	Type.LONG, Type.NO_ARGS, Constants.INVOKESTATIC));
				il.append(new LLOAD(initTestTime));
				il.append(new LSUB());
				il.append(getFactory().createInvoke(TimeUnit.class.getName(), "toMicros", Type.LONG, longArg, Constants.INVOKEVIRTUAL));
				il.append(new L2D());
				int index = consts.addDouble(1000);
				il.append(new LDC2_W(index));
				il.append(new DDIV());
				il.append(getFactory().createInvoke(buildName, "append", buildType, doubleArg, Constants.INVOKEVIRTUAL));
				il.append(new LDC(consts.addString("ms] ")));
				il.append(getFactory().createInvoke(buildName, "append", buildType, strArg, Constants.INVOKEVIRTUAL));
				
				il.append(InstructionFactory.createLoad(Type.STRING, testRes));
				//if test is failed print the error message
				BranchHandle nullRes = il.append(new IFNULL(null));
				il.append(InstructionFactory.createLoad(Type.STRING, testRes));
				
				BranchHandle gotoAppend = il.append(new GOTO(null));
				
				InstructionHandle emptyLDC = il.append(new LDC(consts.addString("")));
				
				InstructionHandle append = il.append(getFactory().createInvoke(buildName, 
						"append", buildType, strArg, Constants.INVOKEVIRTUAL));
				nullRes.setTarget(emptyLDC);
				gotoAppend.setTarget(append);
				
				
			}
		}
		//display the number of passed and failed tests
		il.append(getFactory().createGetStatic(System.class.getName(), "out", Type.getType(PrintStream.class)));
		il.append(InstructionConstants.SWAP);
		il.append(new LDC(consts.addString("\n\n")));
		il.append(getFactory().createInvoke(buildName, "append", buildType, strArg, Constants.INVOKEVIRTUAL));
		il.append(InstructionFactory.ILOAD_1);
		il.append(getFactory().createInvoke(buildName, "append", buildType, intArg, Constants.INVOKEVIRTUAL));
		il.append(new LDC(consts.addString(" tests passed, ")));
		il.append(getFactory().createInvoke(buildName, "append", buildType, strArg, Constants.INVOKEVIRTUAL));
		il.append(InstructionFactory.ILOAD_2);
		il.append(getFactory().createInvoke(buildName, "append", buildType, intArg ,Constants.INVOKEVIRTUAL));
		il.append(new LDC(consts.addString(" failed [")));
		il.append(getFactory().createInvoke(buildName, "append", buildType, strArg ,Constants.INVOKEVIRTUAL));
		
		//print total duration of tests
		il.append(getFactory().createGetStatic(TimeUnit.class.getName(), "NANOSECONDS", Type.getType(TimeUnit.class)));
		il.append(getFactory().createInvoke(System.class.getName(), "nanoTime",	Type.LONG, Type.NO_ARGS, Constants.INVOKESTATIC));
		il.append(new LLOAD(startTime));
		il.append(new LSUB());
		il.append(getFactory().createInvoke(TimeUnit.class.getName(), "toMicros", Type.LONG, longArg, Constants.INVOKEVIRTUAL));
		il.append(new L2D());
		int index = consts.addDouble(1000);
		il.append(new LDC2_W(index));
		il.append(new DDIV());
		il.append(getFactory().createInvoke(buildName, "append", buildType, doubleArg, Constants.INVOKEVIRTUAL));
		il.append(new LDC(consts.addString("ms]")));
		il.append(getFactory().createInvoke(buildName, "append", buildType, strArg ,Constants.INVOKEVIRTUAL));
		il.append(getFactory().createInvoke(buildName, "toString", Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		il.append(getFactory().createInvoke(PrintStream.class.getName(), "println", Type.VOID, strArg, Constants.INVOKEVIRTUAL));

		//add return
		il.append(InstructionConstants.RETURN);
		main.setMaxStack();
		main.setMaxLocals();
		addMethod(main.getMethod());	
		
	}

}
