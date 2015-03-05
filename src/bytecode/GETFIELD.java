package bytecode;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.CodeSignature;
import types.FieldSignature;
import types.Type;

/**
 * A bytecode which reads the value of a given field of an object,
 * called <i>receiver</i>. If the receiver is <tt>nil</tt>, the computation
 * stops.
 * <br><br>
 * ..., receiver object -> ..., value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class GETFIELD extends FieldReaderBytecode implements PointerDereferencer, DataSelector {

	/**
	 * The signature of the field which is read by this bytecode.
	 */

	private FieldSignature field;

	/**
	 * Constructs a bytecode which reads a field of an object.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param field the signature of the field which is read
	 */

	public GETFIELD(CodeSignature where, FieldSignature field) {
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
		return "getfield " + field;
	}

	@Override
	public Type accessedType() {
		return field.getType();
	}

	protected int hashCode$0() {
		return field.hashCode();
	}

	public boolean equals$0(Object other) {
		return ((GETFIELD)other).field == field;
	}

	@Override
	public String description() {
		return "access to field " + field;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>getfield field</tt> bytecode
	 */

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		// the <tt>FieldSignature</tt> <tt>field</tt>
		// contains everything which is needed
		// in order to create the Java <tt>getfield field</tt> bytecode
		return new InstructionList(field.createGETFIELD(classGen));
	}
}