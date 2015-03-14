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
}