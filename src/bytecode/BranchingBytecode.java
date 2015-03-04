package bytecode;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.CodeSignature;

/**
 * A bytecode with two subsequent bytecodes. It checks a condition
 * and consequently routes
 * the computation at the end of a branching block of code.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class BranchingBytecode extends Bytecode implements NonCallingBytecode {

	/**
	 * Constructs a branching bytecode.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 */

	protected BranchingBytecode(CodeSignature where) {
		super(where);
	}

	/**
	 * Yields a branching bytecode which expresses the opposite condition
	 * of this.
	 *
	 * @return a branching bytecode which expresses the opposite condition
	 *         of this
	 */

	public abstract BranchingBytecode negate();

	/**
	 * Generates the Java bytecode which checks the condition expressed by this
	 * bytecode and goes to one of two possible targets depending on the
	 * outcome of that check. Namely, it generates the code
	 * <br><br>
	 * <tt>generateJB$0(...yes)</tt><br>
	 * <tt>goto no</tt><br>
	 *
	 * @param classGen the Java class generator to be used for this code
	 *                 generation
	 * @param yes the target if the check is satisfied
	 * @param no the target if the check in not satisfied
	 * @return the Java bytecode that checks the condition expressed
	 *         by this bytecode and goes to <tt>yes</tt> or <tt>no</tt>
	 *         depending on the outcome of that check
	 */

	public final InstructionList generateJB(KittenClassGen classGen,
			InstructionHandle yes, InstructionHandle no) {

		InstructionList il = new InstructionList();

		// builds the instructions which go to <tt>yes</tt> if the test is true
		generateJB$0(il,classGen,yes);

		il.append(new org.apache.bcel.generic.GOTO(no));

		return il;
	}

	/**
	 * Auxiliary method which adds to the given list of instructions the
	 * code which goes to <tt>yes</tt> if the outcome of the test expressed
	 * by this branching bytecode is true.
	 *
	 * @param il the list of instructions which must be expanded
	 * @param classGen the class generator to be used to generate the code
	 * @param yes the target where one must go if the outcome of the test
	 *            expressed by this branching bytecode is true
	 */

	protected abstract void generateJB$0(InstructionList il, KittenClassGen classGen, InstructionHandle yes);
}