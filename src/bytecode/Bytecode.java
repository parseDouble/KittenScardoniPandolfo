package bytecode;

import types.CodeSignature;

/**
 * A bytecode of the intermediate Kitten language. It is a more typed,
 * simplified version of the Java bytecode.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Bytecode {

    /**
     * The method or constructor where this bytecode occurs.
     */

    private final CodeSignature where;

    /**
     * Constructs a bytecode.
     *
     * @param where the method or constructor where this bytecode occurs
     */

    protected Bytecode(CodeSignature where) {
    	this.where = where;
    }

    /**
     * Yields the method or constructor where this bytecode occurs.
     *
     * @return the method or constructor where this bytecode occurs
     */

    public CodeSignature getWhere() {
    	return where;
    }

    @Override
    public String toString() {
    	// the name of the class. Subclasses may redefine
    	return getClass().getSimpleName().toLowerCase();
    }

    /**
     * Yields a hashcode of this bytecode that only depends on its parameters.
     *
     * @return the hashcode
     */

    protected abstract int hashCodeAux();

    /**
     * Determines if this bytecode is equal to another. It only uses the class
     * and the parameters of the bytecode.
     *
     * @param other the other bytecode
     * @return true if and only if {@code this} and {@code other} belong to the same class and
     *         have equal parameters
     */

    public final boolean equalsNoTyping(Bytecode other) {
    	return getClass() == other.getClass() && equalsAux(other);
    }

    /**
     * Determines if this bytecode is equal to another. It only uses the parameters
     * of the bytecodes since they are guaranteed to belong to the same class.
     *
     * @param other the other bytecode
     * @return true if and only if {@code this} and {@code other} have equal parameters
     */

    protected abstract boolean equalsAux(Object other);
}