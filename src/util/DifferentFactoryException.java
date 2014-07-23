package util;

/**
 * An excpetion thrown when a binary operation is applied to two bitsets
 * generated by different factories.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class DifferentFactoryException extends RuntimeException {

    /**
     * Builds an exception thrown when a binary operation is applied to
     * two bitsets generated by different factories.
     */

    public DifferentFactoryException(String message) {
	super(message);
    }
}
