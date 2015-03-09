package bytecode;

import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.CodeSignature;
import types.FloatType;
import types.IntType;
import types.NumericalType;
import types.ReferenceType;
import types.Type;

/**
 * A bytecode which casts the top of the stack into a given type.
 * If the cast is not possible, the program stops.
 * A reference value can only be cast towards its type or a subtype of
 * its type. In that case, the cast value is original value, unmodified.
 * The value <tt>nil</tt> can be cast towards any reference type and remains
 * unmodified. A numerical type can be cast into any numerical type through
 * a type conversion. No other casts are possible.
 * <br><br>
 * ..., value -> ..., cast value
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CAST extends NonCallingSequentialBytecode {

    /**
     * The original, static type of the top of the stack.
     */

    private Type fromType;

    /**
     * The type the top of the stack is cast into.
     */

    private Type intoType;

    /**
     * Constructs a bytecode which
     * casts the top of the stack into the given type.
     *
     * @param where the method or constructor where this bytecode occurs
     * @param fromType the declared semantical type of the top of the stack
     * @param intoType the semantical type the top of the stack is cast into
     */

    public CAST(CodeSignature where, Type fromType, ReferenceType intoType) {
	super(where);

	this.fromType = fromType;
	this.intoType = intoType;
    }

    /**
     * Constructs a bytecode which
     * casts the top of the stack into the given type.
     * They are both <tt>NumericalType</tt>'s.
     *
     * @param where the method or constructor where this bytecode occurs
     * @param fromType the declared semantical type of the top of the stack
     * @param intoType the semantical type the top of the stack is cast into
     */

    public CAST
	(CodeSignature where, NumericalType fromType, NumericalType intoType) {

	super(where);

	this.fromType = fromType;
	this.intoType = intoType;
    }

    /**
     * Yields the type from which the value is cast.
     *
     * @return the type from which the value is cast
     */

    public Type getFromType() {
	return fromType;
    }

    /**
     * Yields the type towards which the value is cast.
     *
     * @return the type towards which the value is cast
     */

    public Type getIntoType() {
	return intoType;
    }

    @Override
    public String toString() {
	return "cast " + fromType + " into " + intoType;
    }

    protected int hashCodeAux() {
	return fromType.hashCode() * intoType.hashCode();
    }

    public boolean equalsAux(Object other) {
	return ((CAST)other).fromType == fromType &&
	    ((CAST)other).intoType == intoType;
    }

    /**
     * Generates the Java bytecode corresponding to this Kitten bytecode.
     *
     * @param classGen the Java class generator to be used for this
     *                 Java bytecode generation
     * @return the Java <tt>checkcast intoType</tt> bytecode for casts between
     *         <tt>ReferenceType</tt>'s and a type conversion bytecode
     *         such as <tt>i2f</tt> for conversions between
     *         <tt>NumericalType</tt>'s
     */

    public InstructionList generateJB(JavaClassGenerator classGen) {
	if (intoType instanceof ReferenceType)
	    // we use the instruction factory to simplify the addition of
	    // <tt>type</tt> to the constant pool
	    return new InstructionList
		(classGen.getFactory().createCheckCast
		 ((org.apache.bcel.generic.ReferenceType)intoType.toBCEL()));
	else if (fromType == IntType.INSTANCE && intoType == FloatType.INSTANCE)
	    return new InstructionList(new I2F());
	else // it must be float into int
	    return new InstructionList(new F2I());
    }
}