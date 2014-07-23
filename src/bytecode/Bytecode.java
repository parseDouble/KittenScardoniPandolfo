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

    private CodeSignature where;

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

    /**
     * Yields a <tt>String</tt> representation of this bytecode.
     * By default, it yields the name of the class of the bytecode.
     * Subclasses may redefine.
     *
     * @return a <tt>String</tt> representation of this bytecode
     */

    @Override
    public String toString() {
    	return getClass().getSimpleName().toLowerCase();
    }

    /**
     * Yields the hashcode of this bytecode, which only depends on the class of
     * the bytecode and on the parameters of the bytecode.
     *
     * @return the hashcode of this bytecode
     */

    public final int hashCodeNoTyping() {
    	return getClass().hashCode() ^ hashCode$0();
    }

    /**
     * Yields a hashcode of this bytecode that only depends on its parameters.
     *
     * @return the hashcode
     */

    protected abstract int hashCode$0();

    /**
     * Determines if this bytecode is equal to another. It only uses the class
     * and the parameters of the bytecode.
     *
     * @param other the other bytecode
     * @return true if and only if <tt>this</tt> bytecode and the
     *         <tt>other</tt> bytecode belong to the same class and
     *         have equal parameters
     */

    public final boolean equalsNoTyping(Bytecode other) {
    	return getClass() == other.getClass() && equals$0(other);
    }

    /**
     * Determines if this bytecode is equal to another. It only uses the
     * parameters of the bytecodes since they are guaranteed to belong to the
     * same class.
     *
     * @param other the other bytecode
     * @return true if and only if <tt>this</tt> bytecode and the
     *         <tt>other</tt> bytecode have equal parameters
     */

    protected abstract boolean equals$0(Object other);
}