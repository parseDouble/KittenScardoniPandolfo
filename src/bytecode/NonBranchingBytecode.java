package bytecode;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.CodeSignature;

/**
 * A bytecode of the intermediate Kitten language with one or no
 * subsequent bytecode.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class NonBranchingBytecode extends Bytecode {

	/**
	 * Constructs a non-branching bytecode.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected NonBranchingBytecode(CodeSignature where) {
		super(where);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 * Sometimes there is a direct correspondence between the two bytecode
	 * languages. Other times, instead, one needs to generate more than one
	 * Java bytecode to emulate the semantics of a single Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java bytecode(s) corresponding to this Kitten bytecode
	 */

	public abstract InstructionList generateJB(KittenClassGen classGen);
}
