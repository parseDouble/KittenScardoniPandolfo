package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.FieldSignature;

/**
 * A bytecode which writes a value into a given field of an object,
 * called <i>receiver</i>. If the receiver is <tt>nil</tt>, the computation stops.
 * <br><br>
 * ..., receiver object, value -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class PUTFIELD extends FieldAccessBytecode {

	/**
	 * The signature of the field which is written by this bytecode.
	 */

	private FieldSignature field;

	/**
	 * Constructs a bytecode which writes into a field of an object.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param field the signature of the field which is written
	 */

	public PUTFIELD(CodeSignature where, FieldSignature field) {
		super(where);

		this.field = field;
	}

	/**
	 * Yields the field signature of this field access bytecode.
	 *
	 * @return the field signature
	 */

	public FieldSignature getField() {
		return field;
	}

	/**
	 * Sets the field signature of this bytecode.
	 *
	 * @param field the field signature
	 */

	protected void setField(FieldSignature field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "putfield " + field;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>putfield field</tt> bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// the <tt>FieldSignature</tt> <tt>field</tt>
		// contains everything which is needed
		// in order to create the Java <tt>putfield field</tt> bytecode
		return new InstructionList(field.createPUTFIELD(classGen));
	}
}