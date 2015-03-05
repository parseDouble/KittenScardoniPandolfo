package bytecode;

import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.CodeSignature;
import types.IntType;
import types.NumericalType;

/**
 * A bytecode which checks if the one but last element of the stack
 * is greater than or equal to its last element.
 * It leaves on the stack the Boolean result of the comparison.
 * <br><br>
 * ..., value1, value2 -> ..., value1 &ge; value2
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class GE extends ComparisonNumericalBinOpBytecode {

    /**
     * Constructs a bytecode which
     * checks if the element under the top of the stack
     * is greater than or equal to the element on top of the stack.
     *
     * @param where the method or constructor where this bytecode occurs
     * @param type the semantical type of the values which are added
     */

    public GE(CodeSignature where, NumericalType type) {
	super(where,type);
    }

    /**
     * Yields a branching version of this bytecode.
     *
     * @return an <tt>if_cmpge</tt> bytecode for the same type as <tt>this</tt>
     */

    public BranchingComparisonBytecode toBranching() {
	return new IF_CMPGE(getWhere(),getType());
    }

    /**
     * Generates the Java bytecode which leaves on the stack the
     * <tt>boolean</tt> value resulting from a &ge;
     * comparison of two values.
     * Namely, it generates the Java bytecode<br>
     * <br>
     * <tt>if_icmpge after</tt><br>
     * <tt>iconst 0</tt><br>
     * <tt>goto follow</tt><br>
     * <tt>after: iconst 1</tt><br>
     * <tt>follow: nop</tt><br>
     * <br>
     * if <tt>type</tt> is <tt>int</tt>, and<br>
     * <br>
     * <tt>fcmpl</tt><br>
     * <tt>ifge after</tt><br>
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
       
    public InstructionList generateJB(JavaClassGenerator classGen) {
	InstructionList il = new InstructionList
	    (new org.apache.bcel.generic.NOP());
	InstructionHandle after, follow;

	follow = il.getStart();
	after = il.insert(new org.apache.bcel.generic.ICONST(1));
	il.insert(new org.apache.bcel.generic.GOTO(follow));
	il.insert(new org.apache.bcel.generic.ICONST(0));

	if (getType() == IntType.INSTANCE)
	    il.insert(new org.apache.bcel.generic.IF_ICMPGE(after));
	else {
	    il.insert(new org.apache.bcel.generic.IFGE(after));
	    il.insert(new org.apache.bcel.generic.FCMPL());
	}

	return il;
    }
}
