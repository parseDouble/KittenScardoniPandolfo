package bytecode;

import generateJB.KittenClassGen;

import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which writes a value into an element of an array.
 * If the reference to the array is <tt>nil</tt>, the computation stops.
 * <br><br>
 * ..., array reference, index, value -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ARRAYSTORE extends NonCallingSequentialBytecode
    implements PointerDereferencer {

    /**
     * The type of the elements of the array which is modified.
     */

    private Type type;

    /**
     * Constructs a bytecode which
     * writes a value into an element of an array. Note that the index, where
     * the array is modified, is provided at run-time through the stack.
     *
     * @param where the method or constructor where this bytecode occurs
     * @param type the type of the elements of the array which
     *             is created by this bytecode
     */

    public ARRAYSTORE(CodeSignature where, Type type) {
	super(where);

	this.type = type;
    }

    /**
     * Yields the type of the elements of the array which is modified by this
     * bytecode.
     *
     * @return the type of the elements of the array which is modified by this
     *         bytecode
     */

    public Type getType() {
	return type;
    }

    @Override
    public String toString() {
	return "store into array of " + type;
    }

    protected int hashCode$0() {
	return type.hashCode();
    }

    public boolean equals$0(Object other) {
	return type == ((ARRAYSTORE)other).type;
    }

    /**
     * Generates the Java bytecode corresponding
     * to this Kitten bytecode. Namely, it generates an <tt>iastore</tt>
     * if <tt>type</tt> is <tt>int</tt>, an <tt>fastore</tt> if
     * <tt>type</tt> is <tt>float</tt> and an <tt>aastore</tt> if
     * <tt>type</tt> is a class or array type.
     *
     * @param classGen the Java class generator to be used for this
     *                 Java bytecode generation
     * @return the Java <tt>iastore</tt>, <tt>fastore</tt> or <tt>aastore</tt>
     *         bytecode, depending on <tt>type</tt>
     */

    public InstructionList generateJB(KittenClassGen classGen) {
	// we use the instruction factory to simplify the choice among
	// the three possible Java bytecodes
	return new InstructionList
	    (InstructionFactory.createArrayStore(type.toBCEL()));
    }

    @Override
    public String description() {
	return "update of array of " + type;
    }
}