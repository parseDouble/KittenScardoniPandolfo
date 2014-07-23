package bytecode;

import types.CodeSignature;

/**
 * A bytecode that writes into a field.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class FieldWriterBytecode extends FieldAccessBytecode {

    /**
     * Builds a bytecode that writes a field.
     *
     * @param where the method or constructor where this bytecode occurs
     */

    protected FieldWriterBytecode(CodeSignature where) {
	super(where);
    }
}
