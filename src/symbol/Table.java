package symbol;

/**
 * A table mapping symbols to objects.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Table {

    /**
     * An empty table.
     */

    public final static EmptyTable EMPTY = new EmptyTable();

    /**
     * Returns the object bound to a given symbol.
     *
     * @param key the symbol to look up for in the table
     * @return the object bound to that symbol.
     *         Returns <tt>null</tt> if no object is bound to that symbol
     */

    public abstract Object get(Symbol key);

    /**
     * Builds a new table, identical to this, but where a given symbol is
     * bound to a given value. This table is not modified.
     *
     * @param key the symbol to be bound to the given value
     * @param value to value to be bound to the symbol
     * @return a symbol table identical to this except for <tt>key</tt> which
     *         is bound to <tt>value</tt>
     */

    public abstract Table put(Symbol key, Object value);
}

