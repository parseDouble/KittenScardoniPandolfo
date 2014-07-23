package types;

import generateJB.KittenClassGen;

import org.apache.bcel.generic.InvokeInstruction;

import symbol.Symbol;
import translate.CodeBlock;
import absyn.CodeDeclaration;

/**
 * The signature of a piece of code of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CodeSignature extends ClassMemberSignature {

    /**
     * The name of this code object.
     */

    private Symbol name;

    /**
     * The return type of this code object.
     */

    private Type returnType;

    /**
     * The parameters of this code object.
     */

    private TypeList parameters;

    /**
     * The intermediate Kitten code for this constructor or method.
     * This is <tt>null</tt> if this constructor or method has not been
     * translated yet.
     */

    private CodeBlock code;

    /**
     * Builds a signature for a code object.
     *
     * @param clazz the class where this code is defined
     * @param returnType the return type of this code
     * @param parameters the types of the parameters of this code
     * @param name the name of this code
     * @param abstractSyntax the abstract syntax of the declaration
     *                       of this code
     */

    protected CodeSignature
	(ClassType clazz, Type returnType,
	 TypeList parameters, Symbol name,
	 CodeDeclaration abstractSyntax) {

	super(clazz,abstractSyntax);

	this.parameters = parameters;
	this.name = name;
	this.returnType = returnType;
    }

    /**
     * Determines if this signature is equal to another.
     *
     * @param other the other signature
     * @return true if and only if this signature if equal to <tt>other</tt>
     */

    public boolean equals(Object other) {
	if (getClass() == other.getClass()) {
	    CodeSignature otherM = (CodeSignature)other;

	    return otherM.getDefiningClass() == getDefiningClass() &&
		otherM.name == name &&
		otherM.parameters.equals(parameters) &&
		otherM.returnType == returnType;
	}
	else return false;
    }

    /**
     * Yields the hash code of this signature. This is consistent with
     * <tt>equals()</tt>.
     *
     * @return the hash code of this signature
     */

    public int hashCode() {
	return getDefiningClass().hashCode()
	    + name.hashCode() + parameters.hashCode() + returnType.hashCode();
    }

    /**
     * Yields a <tt>String</tt> representation of this code signature,
     * of the form <i>Class.name(parametersTypes):returnType</i>.
     *
     * @return a <tt>String</tt> representation of this method signature
     */

    public String toString() {
	return getDefiningClass() + "."
	    + getName() + "(" + getParameters() + "):" + getReturnType();
    }

    /**
     * Yields the types of the parameters.
     *
     * @return the types of the parameters
     */

    public TypeList getParameters() {
	return parameters;
    }

    /**
     * Yields the return type of this code object.
     *
     * @return the return type of this code object
     */

    public Type getReturnType() {
	return returnType;
    }

    /**
     * Yields the name of this code object.
     *
     * @return the name of this code object
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields the abstract syntax of this constructor or method declaration.
     *
     * @return the abstract syntax of this constructor or method declaration
     */

    public CodeDeclaration getAbstractSyntax() {
	return (CodeDeclaration)super.getAbstractSyntax();
    }

    /**
     * Yields the types of the stack elements which must be on top of the
     * stack when one calls this method or constructor.
     *
     * @return the types of the top stack elements at the moment of the call
     *         to this method or constructor
     */

    public TypeList requiredStackTypes() {
	return parameters.push(getDefiningClass());
    }

    /**
     * Yields the block where the Kitten bytecode of this method or
     * constructor starts.
     *
     * @return the block where the Kitten bytecode of this method or
     *         constructor starts
     */

    public CodeBlock getCode() {
	return code;
    }

    /**
     * Sets the Kitten code of this constructor or method to the given
     * code, prefixed as required by <tt>addPrefixToCode()</tt>.
     *
     * @param code the Kitten code
     */

    public void setCode(CodeBlock code) {
	this.code = addPrefixToCode(code);
    }

    /**
     * Adds a prefix to the Kitten bytecode generated for this constructor or
     * method. Constructors add a call to the constructor to the superclass.
     * Constructors and methods add a <tt>receiver_is</tt> bytecode which
     * qualifies the type of the receiver.
     *
     * @param code the code already compiled for this constructor or method
     * @return <tt>code</tt> with a prefix
     */

    protected abstract CodeBlock addPrefixToCode(CodeBlock code);

    /**
     * Generates an invocation instruction that calls this method or
     * constructor. The kind of invocation is specified by using the constants
     * inside <tt>org.apache.bcel.Constants</tt>.
     *
     * @param classGen the class generator to be used to generate the
     *                 invocation instruction
     * @param invocationType the type of invocation required, as enumerated
     *                       inside <tt>org.apache.bcel.Constants</tt>
     * @return an invocation instruction that calls this method or constructor
     */

    protected InvokeInstruction createInvokeInstruction
	(KittenClassGen classGen, short invocationType) {

	// we use the instruction factory in order to put automatically inside
	// the constant pool a reference to the Java signature of this method
	// or constructor
	return classGen.getFactory().createInvoke
	    (getDefiningClass().toBCEL().toString(), // name of the class
	     getName().toString(), // name of the method or constructor
	     getReturnType().toBCEL(), // return type
	     getParameters().toBCEL(), // parameters types
	     invocationType); // the type of invocation (static, special, ecc.)
    }
}
