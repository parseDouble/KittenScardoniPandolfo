package bytecode;

import types.CodeSignature;

/**
 * A bytecode that reads a field.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class FieldReaderBytecode extends FieldAccessBytecode {

    /**
     * Builds a bytecode that reads a field.
     *
     * @param where the method or constructor where this bytecode occurs
     */

    protected FieldReaderBytecode(CodeSignature where) {
	super(where);
    }
}
