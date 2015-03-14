package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which terminates the code of a method or constructor,
 * and gives back the control to the caller. If there is a return
 * value on top of the stack, the stack elements which are under that value
 * are removed.
 * <br><br>
 * ..., value -> value
 * <br>
 * if this <tt>return</tt> instruction returns a non-<tt>void</tt> value, and
 * <br><br>
 * ... -> emptystack
 * <br>
 * if this <tt>return</tt> instruction returns <tt>void</tt>.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class RETURN extends FinalBytecode {

	/**
	 * The semantical type of the value which is returned.
	 */

	private Type type;

	/**
	 * Constructs a <tt>return</tt> bytecode
	 * which returns a value of the given type.
	 *
	 * @param type the type of the value returned
	 *             by the <tt>return</tt> bytecode
	 */

	public RETURN(CodeSignature where, Type type) {
		super(where);

		this.type = type;
	}

	/**
	 * Yields the type of the value returned by this bytecode.
	 *
	 * @return the type of the value returned by this bytecode
	 */

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "return " + type;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the <tt>return</tt> Java bytecode if <tt>type</tt> is
	 *         <tt>void</tt>, the <tt>ireturn</tt> Java bytecode if
	 *         <tt>type</tt> is <tt>int</tt> or <tt>boolean</tt> (Booleans are
	 *         represented as integers in Java bytecode, with the assumption
	 *         that 0 = <i>false</i> and 1 = <i>true</i>), the <tt>freturn</tt>
	 *         Java bytecode if <tt>type</tt> is <tt>float</tt> and the
	 *         <tt>areturn</tt> Java bytecode if <tt>type</tt> is a
	 *         <tt>ReferenceType</tt>
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the selection of the
		// right <tt>return</tt> bytecode, depending on <tt>type</tt>
		return new InstructionList(InstructionFactory.createReturn(type.toBCEL()));
	}
}