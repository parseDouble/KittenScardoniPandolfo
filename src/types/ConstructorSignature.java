package types;

import generateJB.KittenClassGen;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import symbol.Symbol;
import translate.CodeBlock;
import util.List;
import absyn.ConstructorDeclaration;
import bytecode.CONSTRUCTORCALL;
import bytecode.LOAD;
import bytecode.RECEIVER_IS;

/**
 * The signature of a constructor of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ConstructorSignature extends CodeSignature {

	/**
	 * Constructs a signature for a constructor, given its parameters types
	 * and the class it belongs to.
	 *
	 * @param clazz the class this constructor belongs to
	 * @param parameters the types of the parameters of the constructor
	 * @param abstractSyntax the abstract syntax of the declaration
	 *                       of this constructor
	 */

	public ConstructorSignature(ClassType clazz, TypeList parameters, ConstructorDeclaration abstractSyntax) {
		// a constructor always returns <tt>void</tt> and its name is by default <tt>init</tt>
		super(clazz, Type.VOID, parameters, Symbol.mk("<init>"), abstractSyntax);
	}

	/**
	 * Yields a <tt>String</tt> representation of this constructor signature,
	 * of the form <i>Class(parametersTypes)</i>.
	 *
	 * @return a <tt>String</tt> representation of this constructor signature
	 *
	 */

	public String toString() {
		return getDefiningClass() + "(" + getParameters() + ")";
	}

	/**
	 * Generates an <tt>invokespecial</tt> Java bytecode that calls this
	 * constructor. The Java <tt>invokespecial</tt> bytecode calls a method by
	 * using a hard-wired class name to look up for the method's implementation
	 * and has a receiver.
	 *
	 * @param classGen the class generator to be used to generate
	 *                 the <tt>invokespecial</tt> Java bytecode
	 * @return an <tt>invokespecial</tt> Java bytecode that calls this
	 *         constructor
	 */

	public INVOKESPECIAL createINVOKESPECIAL(KittenClassGen classGen) {
		return (INVOKESPECIAL)createInvokeInstruction
				(classGen,Constants.INVOKESPECIAL);
	}

	/**
	 * Adds the the given class generator a Java bytecode constructor for
	 * this constructor.
	 *
	 * @param classGen the generator of the class where the constructor lives
	 */

	public void createConstructor(KittenClassGen classGen) {
		InstructionList il = classGen.generateJB(getCode());

		// we add the following code at the beginning of the empty constructor
		// for the Kitten <tt>Object</tt> class:<br>
		// <br>
		// <tt>aload 0   [ load "this" ]</tt><br>
		// <tt>invokespecial java.lang.Object.<init>()</tt><br>
		// <br>
		// In this way we respect the constraint of the Java bytecode
		// that each constructor must call a constructor of the superclass
		if (getDefiningClass().getName() == Symbol.OBJECT) {
			il.insert(classGen.getFactory().createInvoke
					("java.lang.Object", // the name of the class
							Constants.CONSTRUCTOR_NAME, // <init>
							org.apache.bcel.generic.Type.VOID, // return type
							new org.apache.bcel.generic.Type[] {}, // parameters
							Constants.INVOKESPECIAL)); // the type of call
			il.insert(new ALOAD(0));
		}

		// we create a method generator: constructors are just methods
		// in Java bytecode, with special name <tt><init></tt>
		MethodGen methodGen = new MethodGen
				(Constants.ACC_PUBLIC, // public
						org.apache.bcel.generic.Type.VOID, // return type
						getParameters().toBCEL(), // parameters types, if any
						null, // parameters names: we do not care
						Constants.CONSTRUCTOR_NAME, // <tt><init></tt>
						classGen.getClassName(), // name of the class
						il, // bytecode of the constructor
						classGen.getConstantPool()); // constant pool

		// we must always call these methods before the <tt>getMethod()</tt>
		// method below. They set the number of local variables and stack
		// elements used by the code of the method
		methodGen.setMaxStack();
		methodGen.setMaxLocals();

		// we add a method (actually, constructor) to the class that we are
		// generating
		classGen.addMethod(methodGen.getMethod());
	}

	/**
	 * Adds a prefix to the Kitten bytecode generated for this constructor.
	 * This is a call to the constructor of the superclass (if any)
	 * and a <tt>receiver_is</tt> bytecode with a unique possible
	 * receiver: the class which is being constructed. However, if this
	 * constructor is the default constructor, all instances of the class
	 * of the receiver are considered as possible receivers, because of
	 * constructor chaining from the subclasses' constructors.
	 *
	 * @param code the code already compiled for this constructor
	 * @return <tt>code</tt>, with a call to the constructor of the
	 *         superclass and a <tt>receiver_is</tt> bytecode at its beginning
	 */

	protected CodeBlock addPrefixToCode(CodeBlock code) {
		// we prefix a piece of code that calls the constructor of
		// the superclass (if any)
		if (getDefiningClass().getName() != Symbol.OBJECT) {
			ClassType superclass = getDefiningClass().getSuperclass();

			code = new LOAD(this,0, getDefiningClass()).followedBy
				(new CONSTRUCTORCALL(this, superclass.constructorLookup(TypeList.EMPTY))
				.followedBy(code));
		}

		// we add an annotation which enumerates the possible dynamic
		// receivers of this constructor declaration: just one!
		// Except for the empty constructor, which is chained to the
		// constructors of all its subclasses
		List<ClassType> instances = new List<ClassType>();
		if (getParameters() == TypeList.EMPTY)
			instances.addAll(getDefiningClass().getInstances());
		else
			instances.addFirst(getDefiningClass());

		return new RECEIVER_IS(this,instances).followedBy(code);
	}
}