package symbol;

/**
 * An empty symbol table.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

final class EmptyTable<E> extends Table<E> {

	EmptyTable() {}

	@Override
	public E get(Symbol key) {
		return null;  // there is no key in this empty table
	}

	@Override
	public Table<E> put(Symbol key, E value) {
		// builds a non-empty symbol table with empty subtrees
		return new NonEmptyTable<E>(key, value);
	}
}