package bytecode;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.CodeSignature;

/**
 * A bytecode which does not do anything.
 * <br><br>
 * ... -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NOP extends NonCallingSequentialBytecode {

	/**
	 * Constructs a bytecode which does not do anything.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	public NOP(CodeSignature where) {
		super(where);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>nop</tt> bytecode
	 */

	public InstructionList generateJB(KittenClassGen classGen) {
		return new InstructionList(new org.apache.bcel.generic.NOP());
	}

	protected int hashCode$0() {
		return 1;
	}

	public boolean equals$0(Object other) {
		return true;
	}
}