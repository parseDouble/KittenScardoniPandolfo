package types;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.InstructionList;

import util.List;
import absyn.MethodDeclaration;
import symbol.Symbol;
import bytecode.RECEIVER_IS;
import translate.CodeBlock;
import generateJB.KittenClassGen;

/**
 * The signature of a method of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class MethodSignature extends CodeSignature {

    /**
     * Constructs the signature of a method with the given name, return type
     * and parameters types.
     *
     * @param clazz the class where this method is defined
     * @param returnType the return type of this method
     * @param parameters the types of the parameters of this method
     * @param name the name of the method
     * @param abstractSyntax the abstract syntax of the declaration
     *                       of this method
     */

    public MethodSignature
	(ClassType clazz, Type returnType,
	 TypeList parameters, Symbol name,
	 MethodDeclaration abstractSyntax) {

	super(clazz,returnType,parameters,name,abstractSyntax);
    }

    /**
     * Generates an <tt>invokevirtual</tt> Java bytecode that calls this
     * method. The Java <tt>invokevirtual</tt> bytecode calls a method by using
     * the run-time class of the receiver to look up for the
     * method's implementation.
     *
     * @param classGen the class generator to be used to generate
     *                 the <tt>invokevirtual</tt> Java bytecode
     * @return an <tt>invokevirtual</tt> Java bytecode that calls this method
     */

    public INVOKEVIRTUAL createINVOKEVIRTUAL(KittenClassGen classGen) {
	return (INVOKEVIRTUAL)createInvokeInstruction
	    (classGen,Constants.INVOKEVIRTUAL);
    }

    /**
     * Adds the the given class generator a Java bytecode method for
     * this method.
     *
     * @param classGen the generator of the class where the method lives
     */

    public void createMethod(KittenClassGen classGen) {
	MethodGen methodGen = new MethodGen
	    (Constants.ACC_PUBLIC, // public
	     getReturnType().toBCEL(), // return type
	     getParameters().toBCEL(), // parameters types, if any
	     null, // parameters names: we do not care
	     getName().toString(), // method's name
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
     * Adds a prefix to the Kitten bytecode generated for this method.
     * This is a <tt>receiver_is</tt> bytecode with a list of the possible
     * receivers of this method.
     *
     * @param code the code already compiled for this method
     * @return <tt>code</tt>, with a <tt>receiver_is</tt> bytecode
     *         at its beginning
     */

    protected CodeBlock addPrefixToCode(CodeBlock code) {
	// we compute the set of instances of the static type of the receiver
	// which might arise at run-time. They are the instances of the
	// receiver such that a call to a method with our signature
	// leads to this method
	List<ClassType> receivers = new List<ClassType>();

	for (ClassType rec: getDefiningClass().getInstances())
	    if (rec.methodLookup(getName(),getParameters()) == this)
		receivers.addFirst(rec);

	// we add an annotation which enumerates the possible dynamic
	// receivers of this method declaration
	return new RECEIVER_IS(this,receivers).followedBy(code);
    }
}
