package bytecode;

import types.FieldSignature;
import types.CodeSignature;

/**
 * A bytecode that accesses a field.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class FieldAccessBytecode extends NonCallingSequentialBytecode {

    /**
     * Builds a bytecode that accesses a field.
     *
     * @param where the method or constructor where this bytecode occurs
     */

    protected FieldAccessBytecode(CodeSignature where) {
	super(where);
    }

    /**
     * Yields the field signature of this field access bytecode.
     *
     * @return the field signature
     */

    public abstract FieldSignature getField();
}
