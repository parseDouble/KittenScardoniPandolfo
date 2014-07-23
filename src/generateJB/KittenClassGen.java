package generateJB;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.TargetLostException;

import util.List;
import symbol.Symbol;
import types.KittenClassType;
import types.FieldSignature;
import types.ConstructorSignature;
import types.MethodSignature;
import types.ClassMemberSignature;
import bytecode.BranchingBytecode;
import translate.CodeBlock;

/**
 * A Java bytecode generator. It transforms the Kitten intermediate language
 * into Java bytecode which can be dumped to <tt>.class</tt> files and run.
 * It uses the BCEL library to represent Java classes and dump them
 * on the file-system.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class KittenClassGen extends ClassGen {

    /**
     * An <tt>org.apache.bcel.generic.InstructionFactory</tt> is a utility
     * class of the BCEL library that helps us generate complex Java bytecodes,
     * which have different forms depending on the type of their operands,
     * or require to store constants in the constant pool. We might
     * generate them without this helper object, but it would be really
     * complex!
     */

    private InstructionFactory factory;

    /**
     * An empty set of interfaces, used to generate the Java class.
     */

    private final static String[] noInterfaces = new String[] {};

    /**
     * Builds a generator of Java bytecode for the given class type.
     *
     * @param className the name of the class which must be generated
     * @param superName the name of the superclass of <tt>className</tt>,
     *                  if any
     * @param interfs the names of the superinterfaces of <tt>className</tt>
     * @param source the name of the source file which must be reported
     *               onthe <tt>.class</tt> file
     * @param access the access modifiers of the class which must be generated
     * @param fields a map from field name to <tt>FieldSignature</tt>
     * @param constructors a set of <tt>ConstructorSignature</tt>'s
     * @param methods a map from method name to set of
     *        <tt>MethodSignature</tt>'s
     * @param sigs a set of <tt>types.ClassMemberSignature</tt>'s. These are
     *             the only class members which must be translated.
     *             If this is <tt>null</tt>, all class members are translated
     */

    protected KittenClassGen
	(String className, String superName, String[] interfs,
	 String source, int access,
	 HashMap<Symbol,FieldSignature> fields,
	 HashSet<? extends ConstructorSignature> constructors,
	 HashMap<Symbol,HashSet<MethodSignature>> methods,
	 HashSet<ClassMemberSignature> sigs) {

	super(className,superName,source,access,interfs,new ConstantPoolGen());

	// and a new instruction factory that places the constants
	// in the previous constant pool. This is useful for generating
	// complex bytecodes that access the constant pool
	this.factory = new InstructionFactory(getConstantPool());

	// we add the fields
	for (FieldSignature f: fields.values())
	    if (sigs == null || sigs.contains(f)) f.createField(this);

	// we add the constructors
	for (ConstructorSignature c: constructors)
	    if (sigs == null || sigs.contains(c)) c.createConstructor(this);

	// we add the methods
	for (HashSet<MethodSignature> s: methods.values())
	    for (MethodSignature m: s)
		if (sigs == null || sigs.contains(m)) m.createMethod(this);
    }

    /**
     * Builds a class generator for the given class type.
     *
     * @param clazz the class type
     * @param sigs a set of <tt>types.ClassMemberSignature</tt>'s. These are
     *             the only class members which must be translated.
     *             If this is <tt>null</tt>, all class members are translated
     */

    public KittenClassGen(KittenClassType clazz,
			  HashSet<ClassMemberSignature> sigs) {

	this(clazz.getName().toString(), // name of the class
	     // the superclass of the Kitten <tt>Object</tt> class
	     // is set to be the Java <tt>java.lang.Object</tt> class
	     clazz.getSuperclass() != null ?
	     clazz.getSuperclass().getName().toString() : "java.lang.Object",
	     noInterfaces, // Java interfaces: none
	     clazz.getName() + ".kit", // source file
	     Constants.ACC_PUBLIC, // Java attributes: everything is public!
	     clazz.getFields(), // fields
	     clazz.getConstructors(), // constructors
	     clazz.getMethods(), // methods
	     sigs);
    }

    /**
     * Yields the instruction factory which can be used to create complex
     * Java bytecodes for this class generator.
     *
     * @return the instruction factory which can be used to create complex
     *         Java bytecodes for this class generator
     */

    public final InstructionFactory getFactory() {
	return factory;
    }

    /**
     * Generates the Java bytecode for the given block of code and for all
     * blocks reachable from it. It calls the auxiliary <tt>generateJB$0()</tt>
     * method and then the <tt>removeRedundanciesJB()</tt> method.
     *
     * @param cb the code from which the generation starts
     * @return the Java bytecode for <tt>cb</tt> and all blocks reachable
     *         from it
     */

    public InstructionList generateJB(CodeBlock cb) {
	InstructionList instructions = new InstructionList();

	generateJB(cb,new HashMap<CodeBlock,InstructionHandle>(),instructions);

	return removeRedundanciesJB(instructions);
    }

    /**
     * Auxiliary method which generates the Java bytecode for the given block
     * of code and for all blocks reachable from it. It uses a set of processed
     * blocks in order to avoid looping.
     *
     * @param cb the block from which the code generation starts
     * @param done the set of blocks which have been already processed
     * @param instructions the Java bytecode which have already been generated.
     *                     It gets modified in order to include the Java
     *                     bytecode generated for <tt>cb</tt> and for those
     *                     that are reachable from it
     * @return a reference to the first instruction of the Java bytecode
     *         generated for this block
     */

    private InstructionHandle generateJB
	(CodeBlock cb, HashMap<CodeBlock,InstructionHandle> done,
	 InstructionList instructions) {

	// we first check if we already processed <tt>cb</tt>
	InstructionHandle result = done.get(cb);
	if (result != null) return result;

	// this is the first time that we process <tt>cb</tt>!

	// we generate the Java bytecode for the code inside <tt>cb</tt>, and
	// we put it at the end of the <tt>instructions</tt> already generated
	result = instructions.append(cb.getBytecode().generateJB(this));

	// we record the beginning of the Java bytecode generated
	// for <tt>cb</tt>, for future lookup
	done.put(cb,result);

	// we process the following blocks
	generateJBFollows(cb,done,instructions);

	// we return the beginning of the Java bytecode generated
	// for <tt>cb</tt>
	return result;
    }

    /**
     * Auxiliary method that generates the Java bytecode for the blocks
     * that follow a given block. That Java bytecode might include some
     * <i>glue</i>, such as the conditional Java bytecode for the
     * branching code blocks.
     *
     * @param cb the block for whose followers the code is being generated
     * @param done the set of blocks which have been already processed
     * @param instructions the Java bytecode which have already been
     *                     generated from the predecessors
     *                     and from the code of <tt>cb</tt>. It gets modified
     *                     in order to include the Java bytecode generated
     *                     for the blocks that follow <tt>cb</tt>, including
     *                     some <i>glue</i>
     */

    private void generateJBFollows
	(CodeBlock cb, HashMap<CodeBlock,InstructionHandle> done,
	 InstructionList instructions) {

	List<CodeBlock> follows = (List<CodeBlock>)cb.getFollows();
	InstructionHandle ourLast, noH, yesH, followJB;
	BranchingBytecode condition;

	// this is where the Java bytecode currently ends
	ourLast = instructions.getEnd();

	if (!follows.isEmpty())
	    if (follows.getFirst().getBytecode().getHead()
		instanceof BranchingBytecode) {

		// we are facing a branch due to a comparison bytecode.
		// That bytecode and its negation are at the beginning of
		// our two following blocks

		// we get the condition of the branching
		condition = (BranchingBytecode)
		    (follows.getFirst().getBytecode().getHead());

		// we append the code for the two blocks that follow <tt>cb</tt>
		noH = generateJB(follows.get(1),done,instructions);
		yesH = generateJB(follows.getFirst(),done,instructions);

		// in between, we put some code that jumps to <tt>yesH</tt> if
		// <tt>condition</tt> holds, and to <tt>noH</tt> otherwise
		instructions.append
		    (ourLast,condition.generateJB(this,yesH,noH));
	    }
	    else {
		// we append the code for the first block after <tt>cb</tt>
		followJB = generateJB(follows.getFirst(),done,instructions);

		// in between, we put a <tt>goto</tt> bytecode. Note that we
		// need this <tt>goto</tt> since we have no guarantee that the
		// code for <tt>follows</tt> will be appended exactly after that
		// for <tt>cb</tt>. The <tt>follows</tt> blocks might indeed
		// have been already translated into Java bytecode, and hence
		// <tt>followJB</tt> might be an internal program point
		// of <tt>instructions</tt>
		instructions.append(ourLast,new GOTO(followJB));
	    }
    }

    /**
     * Simplifies a piece of Java bytecode, by removing:
     * <ul>
     * <li> <tt>nop</tt> bytecodes
     * <li> <tt>goto</tt> bytecodes which jump to their subsequent
     *                    program point
     * </ul>
     *
     * @param il the Java bytecode which must be simplified
     * @return the same Java bytecode, simplified as above
     */

    private InstructionList removeRedundanciesJB(InstructionList il) {
	Iterator it = il.iterator();
	InstructionHandle handle;
	Instruction instruction;

	while(it.hasNext()) {
	    handle = (InstructionHandle)it.next();
	    instruction = handle.getInstruction();

	    if (instruction instanceof org.apache.bcel.generic.NOP ||
		(instruction instanceof GOTO &&
		((GOTO)instruction).getTarget() == handle.getNext()))
		try {
		    il.redirectBranches(handle,handle.getNext());
		    il.delete(handle);
		}
		catch (TargetLostException e) {
		    // impossible
		}
	}

	return il;
    }
}
