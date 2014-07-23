package types;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;

import absyn.MethodDeclaration;
import symbol.Symbol;
import translate.CodeBlock;
import generateJB.KittenClassGen;

/**
 * The signature of the starting <tt>main()</tt> method of a Kitten program.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class StartMethodSignature extends MethodSignature {

    /**
     * Constructs the signature of the starting method of a Kitten application.
     *
     * @param clazz the class where this method is defined
     * @param abstractSyntax the abstract syntax of the declaration
     *                       of this method
     */

    public StartMethodSignature
	(ClassType clazz, MethodDeclaration abstractSyntax) {

	// the starting method is void and has no parameters
	super(clazz,Type.VOID,TypeList.EMPTY,Symbol.MAIN,abstractSyntax);
    }

    /**
     * Creates a Java bytecode <tt>static</tt> method whose name is
     * <tt>main</tt>, with void as return type and an array of Java
     * <tt>String</tt>'s as parameters.
     *
     * @param classGen the generator of the class where the method lives
     */

    public void createMethod(KittenClassGen classGen) {
	MethodGen methodGen = new MethodGen
	    (Constants.ACC_PUBLIC | Constants.ACC_STATIC, // public and static
	     org.apache.bcel.generic.Type.VOID, // return type
	     new org.apache.bcel.generic.Type[] // parameters
		{new org.apache.bcel.generic.ArrayType("java.lang.String",1)},
	     null, // parameters names: we do not care
	     "main", // method's name
	     classGen.getClassName(), // defining class
	     classGen.generateJB(getCode()), // bytecode of the method
	     classGen.getConstantPool()); // constant pool

	// we must always call these methods before the <tt>getMethod()</tt>
	// method below. They set the number of local variables and stack
	// elements used by the code of the method
	methodGen.setMaxStack();
	methodGen.setMaxLocals();

	// we add a method to the class that we are generating
	classGen.addMethod(methodGen.getMethod());
    }

    /**
     * Adds a prefix to the Kitten code generated for this method.
     * This starting method has no receiver. It is a sort of Java
     * <tt>static</tt> method. Hence there is no <tt>receiver_is</tt>
     * bytecode to add and we just return <tt>code</tt>.
     *
     * @param code the code already compiled for this method
     * @return <tt>code</tt>
     */

    protected CodeBlock addPrefixToCode(CodeBlock code) {
	return code;
    }
}
