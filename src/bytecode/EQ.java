package bytecode;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.BooleanType;
import types.CodeSignature;
import types.ComparableType;
import types.FloatType;
import types.IntType;

/**
 * A bytecode which checks if the top two elements of the stack
 * are the same. It leaves on the stack the Boolean result
 * of the comparison.
 * <br><br>
 * ..., value1, value2 -> ..., value1 = value2
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class EQ extends ComparisonBinOpBytecode {

	/**
	 * Constructs a bytecode that
	 * checks if the top two elements of the stack are the same.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param type the semantical type of the values which are added
	 */

	public EQ(CodeSignature where, ComparableType type) {
		super(where,type);
	}

	/**
	 * Yields a branching version of this bytecode.
	 *
	 * @return an <tt>if_cmpeq</tt> bytecode for the same type as <tt>this</tt>
	 */

	public BranchingComparisonBytecode toBranching() {
		return new IF_CMPEQ(getWhere(),getType());
	}

	/**
	 * Generates the Java bytecode which leaves on the stack the
	 * <tt>boolean</tt> value resulting from an equality
	 * comparison of two values.
	 * Namely, it generates the Java bytecode<br>
	 * <br>
	 * <tt>if_icmpeq after</tt><br>
	 * <tt>iconst 0</tt><br>
	 * <tt>goto follow</tt><br>
	 * <tt>after: iconst 1</tt><br>
	 * <tt>follow: nop</tt><br>
	 * <br>
	 * if <tt>type</tt> is <tt>int</tt> or <tt>boolean</tt>
	 * (Booleans are represented as
	 * integers in Java bytecode, with the assumption that
	 * 0 = <i>false</i> and 1 = <i>true</i>),<br>
	 * <br>
	 * <tt>if_acmpeq after</tt><br>
	 * <tt>iconst 0</tt><br>
	 * <tt>goto follow</tt><br>
	 * <tt>after: iconst 1</tt><br>
	 * <tt>follow: nop</tt><br>
	 * <br>
	 * if <tt>type</tt> is a class or array type, and<br>
	 * <br>
	 * <tt>fcmpl</tt><br>
	 * <tt>ifeq after</tt><br>
	 * <tt>iconst 0</tt><br>
	 * <tt>goto follow</tt><br>
	 * <tt>after: iconst 1</tt><br>
	 * <tt>follow: nop</tt><br>
	 * <br>
	 * if <tt>type</tt> is <tt>float</tt>.
	 * The <tt>fcmpl</tt> Java bytecode operates over
	 * two <tt>float</tt> values on top of the stack and produces an
	 * <tt>int</tt> value at their place, as it follows:<br>
	 * <br>
	 * ..., value1, value2 -> ..., 1   if value1 &gt; value2<br>
	 * ..., value1, value2 -> ..., 0   if value1 = value2<br>
	 * ..., value1, value2 -> ..., -1  if value1 &lt; value2
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java bytecode as above, depending on <tt>type</tt>
	 */

	@Override
	public InstructionList generateJB(KittenClassGen classGen) {
		InstructionList il = new InstructionList
				(new org.apache.bcel.generic.NOP());
		InstructionHandle after, follow;

		follow = il.getStart();
		after = il.insert(new org.apache.bcel.generic.ICONST(1));
		il.insert(new org.apache.bcel.generic.GOTO(follow));
		il.insert(new org.apache.bcel.generic.ICONST(0));

		if (getType() == IntType.INSTANCE || getType() == BooleanType.INSTANCE)
			il.insert(new org.apache.bcel.generic.IF_ICMPEQ(after));
		else if (getType() == FloatType.INSTANCE) {
			il.insert(new org.apache.bcel.generic.IFEQ(after));
			il.insert(new org.apache.bcel.generic.FCMPL());
		}
		else // classes or arrays
			il.insert(new org.apache.bcel.generic.IF_ACMPEQ(after));

		return il;
	}
}