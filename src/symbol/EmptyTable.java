package symbol;

/**
 * An empty symbol table.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class EmptyTable extends Table {

    EmptyTable() {}

    public Object get(Symbol key) {
	// there is no key in this empty table
	return null;
    }

    public Table put(Symbol key, Object value) {
	// builds a non-empty symbol table with empty subtrees
	return new NonEmptyTable(key,value);
    }
}

