package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.ArrayType;
import types.CodeSignature;
import types.Type;

/**
 * A bytecode which creates an array of a given type and size and then
 * pushes on top of the stack a reference to that array.
 * <br><br>
 * ..., dim_11, ..., dim_n -> ..., array reference
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEWARRAY extends NonCallingSequentialBytecode {

	/**
	 * The type of the elements of the array.
	 */

	private Type elementsType;

	/**
	 * The number of dimensions of the array.
	 */

	private int dimensions;

	/**
	 * Constructs a bytecode which creates an array of the given type and
	 * dimensions. Note that the size of the array
	 * is provided at run-time through the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param elementsType the type of the elements of the array which
	 *                     is created by this bytecode
	 * @param dimensions the number of dimensions of the array
	 */

	public NEWARRAY(CodeSignature where, Type elementsType, int dimensions) {
		super(where);

		this.elementsType = elementsType;
		this.dimensions = dimensions;
	}

	/**
	 * Constructs a bytecode which creates an array of a given type and
	 * one dimension. Note that the size of the array
	 * is provided at run-time through the stack.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param elementsType the type of the elements of the array which
	 *                     is created by this bytecode
	 */

	public NEWARRAY(CodeSignature where, Type elementsType) {
		this(where, elementsType, 1);
	}

	/**
	 * Yields the number of dimensions of the array which is created by
	 * this bytecode.
	 *
	 * @return the number of dimensions
	 */

	public int getDimensions() {
		return dimensions;
	}

	@Override
	public String toString() {
		if (dimensions == 1)
			return "newarray of " + elementsType + " of 1 dimension";
		else
			return "newarray of " + elementsType + " of " + dimensions + " dimensions";
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the <tt>newarray elementsType</tt> Java bytecode, if
	 *         <tt>elementsType</tt> is a primitive types, and the
	 *         <tt>anewarray elementsType</tt> bytecode, if
	 *         <tt>elementsType</tt> is a reference type.
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the choice between
		// the <tt>newarray</tt> and <tt>anewarray</tt> bytecode. Moreover,
		// it automatically puts the type of the elements of the array inside
		// the constant pool

		// this is to cope with a bug in BCEL
		Type t = elementsType;
		if (dimensions > 1) t = ArrayType.mk(t);

		return new InstructionList
				(classGen.getFactory()
						.createNewArray(t.toBCEL(),(short)dimensions));
	}
}