package bytecode;

/**
 * A bytecode which modified a local variable.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public interface LocalModifier {

    /**
     * Yields the number of the local variable which is modified.
     *
     * @return the number of the local variable which is modified
     */

    public int getVarNum();
}
