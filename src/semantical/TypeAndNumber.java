package semantical;

import types.Type;

/**
 * A pair made of a type and an integer number.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeAndNumber {

    /**
     * The type.
     */

    private Type type;

    /**
     * The integer number.
     */

    private int number;

    /**
     * Constructs a pair of a type and an integer number.
     *
     * @param type the type
     * @param number the integer number
     */

    public TypeAndNumber(Type type, int number) {
	this.type = type;
	this.number = number;
    }

    /**
     * Yields the type component of this pair.
     *
     * @return the type component of this pair
     */

    public Type getType() {
	return type;
    }

    /**
     * Yields the integer number component of this pair.
     *
     * @return the integer number component of this pair
     */

    public int getNumber() {
	return number;
    }
}
