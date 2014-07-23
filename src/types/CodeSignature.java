package types;

import generateJB.KittenClassGen;

import java.util.HashSet;

import org.apache.bcel.generic.InvokeInstruction;

import symbol.Symbol;
import translate.CodeBlock;
import util.BitSet;
import util.graph.Graph;
import util.graph.Node;
import absyn.CodeDeclaration;
import bytecode.Bytecode;
import bytecode.BytecodeList;
import bytecode.CALL;
import bytecode.FieldWriterBytecode;
import bytecode.LocalModifier;

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
     * An overapproximation of the set of local variables that might have
     * been modified during the execution of the code of this signature.
     */

    private HashSet<Integer> modifiedLocals;

    /**
     * A node whose elements are an overapproximation of the set of bytecodes
     * that might modify a field during the execution of this method or
     * constructor (also considering those inside the methods or
     * constructors that it calls).
     */

    private Node<FieldWriterBytecode> fieldWriters;

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
     * Computes an overapproximation of the set of local variables that
     * might be modified by the execution of this code. It is just the union
     * of the variables <i>i</i> for which a <tt>store i</tt> instruction is
     * contained in the code. It also computes the set of bytecodes that
     * might modify a field during the execution of this method or constructor
     * or of a method or constructor that it calls.
     *
     * @param graph a graph of nodes representing the set of field modifiers
     *              for each method or constructor
     */

    public void computeModified(Graph<FieldWriterBytecode> graph) {
	this.modifiedLocals = new HashSet<Integer>();
	if (fieldWriters == null) fieldWriters = new Node(graph.emptySet());
	computeModifiedLocals(code,new HashSet<CodeBlock>());
	BitSet<FieldWriterBytecode> modifiedLocally = graph.emptySet();
	computeFieldWriters
	    (code,new HashSet<CodeBlock>(),graph,modifiedLocally);

	// all field modifiers occurring locally inside this method or
	// constructor are included
	graph.arc(new Node<FieldWriterBytecode>(modifiedLocally),fieldWriters);
    }

    /**
     * Computes an overapproximation of the set of bytecodes that might
     * modify a field during the execution of this code, considering
     * also the methods or constructors that it might call
     *
     * @param cb the block of code from which the search for the field updates
     *           must start
     * @param done the set of blocks that have been already visited
     * @param graph the graph representing the inclusions between the sets
     *              of modifiers
     * @param modifiedLocally the set of modifiers that occur locally inside
     *                        this method or constructors, without considering
     *                        the methods or constructors that it calls
     */

    private void computeFieldWriters
	(CodeBlock cb, HashSet<CodeBlock> done,
	 Graph<FieldWriterBytecode> graph,
	 BitSet<FieldWriterBytecode> modifiedLocally) {

	if (done.contains(cb)) return;
	else done.add(cb);

	for (BytecodeList bl = cb.getBytecode();
	     bl != null; bl = bl.getTail()) {

	    Bytecode h = bl.getHead();

	    if (h instanceof FieldWriterBytecode)
		modifiedLocally.add((FieldWriterBytecode)h);
	    else if (h instanceof CALL)
		for (CodeSignature target: ((CALL)h).getDynamicTargets()) {
		    if (target.fieldWriters == null)
			target.fieldWriters
			    = new Node<FieldWriterBytecode>(graph.emptySet());

		    // all fields modified by the called method(s) might
		    // be modified by the caller
		    graph.arc(target.fieldWriters,fieldWriters);
		}
	}

	for (CodeBlock f: cb.getFollows())
	    computeFieldWriters(f,done,graph,modifiedLocally);
    }

    /**
     * Computes an overapproximation of the set of local
     * variables that might be modified by the execution of this code. It is
     * just the collection of the variables <i>i</i> for which a
     * <tt>store i</tt> instruction is contained in the code.
     *
     * @param cb the block of code from which the search for the <tt>store</tt>
     *           instructions must start
     * @param done the set of blocks that have been already visited
     */

    private void computeModifiedLocals(CodeBlock cb, HashSet<CodeBlock> done) {
	if (done.contains(cb)) return;
	else done.add(cb);

	for (BytecodeList bl = cb.getBytecode(); bl != null; bl = bl.getTail())
	    if (bl.getHead() instanceof LocalModifier)
		modifiedLocals.add(((LocalModifier)bl.getHead()).getVarNum());

	for (CodeBlock f: cb.getFollows()) computeModifiedLocals(f,done);
    }

    /**
     * Yields an overapproximation of the set of local variables that might
     * be modified by the execution of the code in this signature.
     *
     * @return the overapproximation of the set of local variables that might
     *         be modified by the execution of the code in this signature
     */

    public HashSet<Integer> getModifiedLocals() {
	return modifiedLocals;
    }

    /**
     * Yields an overapproximation of the bytecodes that might modify a field
     * during the execution of this method or constructor (also considering
     * those inside the methods or constructors that it calls).
     *
     * @return the overapproximation
     */

    public BitSet<FieldWriterBytecode> getFieldWriters() {
	return fieldWriters.getApprox();
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
     * Tells if all <tt>putfield</tt> bytecodes in this constructor or method
     * which write into a field of local variable 0 are such that local
     * variable 0 is not accessible from any other variable or stack element
     * there. This is important to improve the precision of sharing analysis.
     *
     * @return true if all <tt>putfield</tt> bytecodes in this constructor
     *         satisfy the above condition
     */

    public boolean getPutfieldTo0() {
	return false; 	// subclasses may redefine
    }

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
