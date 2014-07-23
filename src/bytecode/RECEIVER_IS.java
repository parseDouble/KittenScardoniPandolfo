package bytecode;

import generateJB.KittenClassGen;

import org.apache.bcel.generic.InstructionList;

import types.ClassType;
import types.CodeSignature;
import util.List;

/**
 * A bytecode which states that local variable 0 is an instance
 * of a list of classes. It does not modify anything.
 * <br><br>
 * ... -> ...<br>
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class RECEIVER_IS extends NonCallingSequentialBytecode {

	/**
	 * The list of possible instances.
	 */

	private List<? extends ClassType> instances;

	/**
	 * Constructs a bytecode which states that local variable 0 is an instance
	 * of a list of classes.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param instances the possible instances
	 */

	public RECEIVER_IS(CodeSignature where, List<? extends ClassType> instances) {
		super(where);

		this.instances = instances;
	}

	@Override
	public String toString() {
		return "receiver_is " + instances;
	}

	protected int hashCode$0() {
		return instances.hashCode();
	}

	protected boolean equals$0(Object other) {
		return ((RECEIVER_IS)other).instances.equals(instances);
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java bytecode <tt>nop</tt>, since this Kitten bytecode is
	 *         only an annotation
	 */

	public InstructionList generateJB(KittenClassGen classGen) {
		return new InstructionList(new org.apache.bcel.generic.NOP());
	}
}