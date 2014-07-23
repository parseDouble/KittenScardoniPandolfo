package bytecode;

import types.Type;

/**
 * A bytecode which accesses a portion of a data-structure.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public interface DataSelector {

    /**
     * Yields the type of the accessed portion of the data-structure.
     *
     * @return the type of the accessed portion of the data-structure
     */

    public Type accessedType();
}
