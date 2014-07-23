package translate;

import generateJB.KittenClassGen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import types.ClassMemberSignature;
import types.CodeSignature;
import types.KittenClassType;
import bytecode.Bytecode;
import bytecode.CALL;
import bytecode.FieldAccessBytecode;
import bytecode.FieldWriterBytecode;

/**
 * A program, that is, a set of <tt>ClassMemberSignature</tt>'s.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Program {

    /**
     * The set of <tt>ClassMemberSignature</tt>'s making up this program.
     */

    private HashSet<ClassMemberSignature> sigs;

    /**
     * The name of the program. This is usually the name
     * of the class passed at compile line to Kitten.
     */

    private String name;

    /**
     * The starting code of this program. This is usually the
     * main() method of this program.
     */

    private CodeSignature start;

    /**
     * Builds a program, that is, a set of <tt>ClassMemberSignature</tt>'s.
     *
     * @param sigs the set of signatures
     * @param name the name of the program
     * @param start the code where the program starts
     */

    public Program(HashSet<ClassMemberSignature> sigs, String name,
		   CodeSignature start) {

	this.sigs = sigs;
	this.name = name;
	this.start = start;

	// we clean-up the code, in order to remove useless nop's
	// and merge blocks whenever possible
	if (start != null) cleanUp();
    }

    /**
     * Yields the <tt>ClassMemberSignature</tt>'s which make up this program.
     *
     * @return the <tt>ClassMemberSignature</tt>'s which make up this program
     */

    public HashSet<ClassMemberSignature> getSigs() {
	return sigs;
    }

    /**
     * Yields the name of the program. This is usually the name
     * of the class passed at compile line to Kitten.
     */

    public String getName() {
	return name;
    }

    /**
     * Yields the method from which the program starts.
     *
     * @return the method where the program starts
     */

    public CodeSignature getStart() {
	return start;
    }

    /**
     * Yields the first block of code from which the program starts.
     *
     * @return the first block of code from which the program starts
     */

    public CodeBlock firstBlock() {
	return getStart().getCode();
    }

    /**
     * Cleans-up the code of this progran. This amounts to removing useless
     * nop's or methods or constructors that are not called.
     */

    public void cleanUp() {
	sigs.clear();
	start.getCode().cleanUp(this);
    }

    /**
     * Dumps the Kitten code of the signatures in this set into dot files.
     * It is assumed that all these signatures have already been translated
     * into Kitten code.
     */

    public void dumpCodeDot() {
	for (ClassMemberSignature sig: sigs)
	    if (sig instanceof CodeSignature)
		try {
		    dumpCodeDot((CodeSignature)sig,"./");
		}
		catch (IOException e) {
		    System.out.println("Could not dump Kitten code for " + sig);
		}
    }

    /**
     * Writes a dot file containing a representation of the graph of blocks
     * for the code of the given code signature (method or constructor).
     *
     * @param sig the signature
     * @param dir the directory where the file must be written
     * @throws IOException if an input/output error occurs
     */

    protected void dumpCodeDot(CodeSignature sig, String dir)
	throws IOException {

	FileWriter dot = new FileWriter(dir + sig + ".dot");

	// the name of the graph
	dot.write("digraph \"" + sig + "\" {\n");

	// the size of a standard A4 sheet (in inches)
	dot.write("size = \"11,7.5\";\n");

	// landscape mode
	//dot.write("rotate = 90\n");

	toDot(sig.getCode(),dot,new HashSet<CodeBlock>());

	dot.write("}");

	dot.flush();
	dot.close();
    }

    /**
     * Auxiliary method which writes in the dot file a box standing for the
     * given block, linked to the following blocks, if any.
     *
     * @param cb the block
     * @param where the file where the dot representation must be written
     * @param done the set of blocks which have been processed up to now
     * @return the identifier of <tt>cb</tt> in the dot file
     * @throws IOException if an input/output error occurs
     */

    private String toDot
	(CodeBlock cb, FileWriter where, HashSet<CodeBlock> done)
	throws IOException {

	String name = cb.dotNodeName();

	// did we already dumped the given block in the file?
	if (!done.contains(cb)) {
	    // we never dumped the block before: we add it to the set
	    // of already dumped blocks
	    done.add(cb);

	    // we add a box to the dot file
		where.write(name + " [ shape = box, label = \"block " + cb.getId() + "\\n");

	    // in the middle there is a dump of the bytecode inside the block
	    where.write(cb.getBytecode().toString().replaceAll("\n","\\\\n"));

	    // end of the label of the node
	    where.write("\"];\n");

	    // we add a dot representation for the follows of the block,
	    // except for the blocks containing a single <tt>makescope</tt>
	    // bytecode, which lead to the code of a method or constructor
	    // which is not that whose code we are printing. This only happens
	    // for the termination transformation of the Kitten code
	    for (CodeBlock f: cb.getFollows())
		where.write(name + "->" + toDot(f,where,done) +
				" [color = blue label = \"\" fontsize = 8]\n");
	}

	// we return the unique identifier of the block in the dot file
	return name;
    }

    /**
     * Generates the Java bytecode for all the class types and
     * dumps the relative <tt>.class</tt> files on the file system.
     */

    public void generateJB() {
	// we consider one class at the time and we generate its Java bytecode
	for (KittenClassType clazz: KittenClassType.getAll())
	    try {
		new KittenClassGen(clazz,sigs).getJavaClass()
		    .dump(clazz + ".class");
	    }
	    catch (IOException e) {
		System.out.println
		    ("Could not dump the Java bytecode for class " + clazz);
	    }
    }

    /**
     * Takes note that this program contains the given bytecode. This amounts
     * to adding some signature to the set of signatures for the program.
     *
     * @param bytecode the bytecode
     */

    protected void storeBytecode(Bytecode bytecode) {
	if (bytecode instanceof FieldAccessBytecode)
	    sigs.add(((FieldAccessBytecode)bytecode).getField());
	else if (bytecode instanceof CALL)
	    // a call instruction might call many methods
	    // or constructors at run-time
	    sigs.addAll(((CALL)bytecode).getDynamicTargets());
    }
}
