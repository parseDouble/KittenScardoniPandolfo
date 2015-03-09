package bytecode;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
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

public class PUTFIELD extends FieldWriterBytecode implements PointerDereferencer {

	/**
	 * The signature of the field which is written by this bytecode.
	 */

	private FieldSignature field;

	/**
	 * True if and only if the receiver of this bytecode can only be reached
	 * from itself. This information is useful to improve the precision of
	 * sharing analysis.
	 */

	private boolean receiverUnreachable;

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

	/**
	 * Takes note that the receiver of this bytecode can only be reached from
	 * itself. This information is useful to improve the precision of sharing
	 * analysis.
	 */

	public void setReceiverUnreachable() {
		receiverUnreachable = true;
	}

	@Override
	public String toString() {
		return "putfield " + field;
	}

	protected int hashCodeAux() {
		return field.hashCode();
	}

	public boolean equalsAux(Object other) {
		return ((PUTFIELD)other).field == field
				&& ((PUTFIELD)other).receiverUnreachable == receiverUnreachable;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>putfield field</tt> bytecode
	 */

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		// the <tt>FieldSignature</tt> <tt>field</tt>
		// contains everything which is needed
		// in order to create the Java <tt>putfield field</tt> bytecode
		return new InstructionList(field.createPUTFIELD(classGen));
	}

	@Override
	public String description() {
		return "update of field " + field;
	}
}