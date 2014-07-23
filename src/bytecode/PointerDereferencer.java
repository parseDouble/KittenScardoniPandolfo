package bytecode;

/**
 * A bytecode which dereferences a pointer, that is, it access the memory
 * pointed by some stack element.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public interface PointerDereferencer {

    /**
     * Yields a description of what this instruction performs.
     *
     * @return the description
     */

    public String description();
}
