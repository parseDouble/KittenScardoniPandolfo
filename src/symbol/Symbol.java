package symbol;


/**
 * A symbol of the abstract syntax. It represents an identifier in the
 * source code. Two occurrences of the same identifier are represented by the
 * same, unique symbol, by using the singleton design pattern.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Symbol implements Comparable<Symbol> {

	/**
	 * The symbol representing the identifier {@code this}.
	 */

	public static final Symbol THIS = new Symbol("this");

	/**
	 * The symbol representing the identifier {@code Object}.
	 */

	public static final Symbol OBJECT = new Symbol("Object");

	/**
	 * The symbol representing the identifier {@code String}.
	 */

	public static final Symbol STRING = new Symbol("String");

	/**
	 * The symbol of the method where the application starts.
	 */

	public static final Symbol MAIN = new Symbol("main");

	/**
	 * The string representation of the symbol.
	 */

	private final String name;

	/**
	 * Creates a symbol with the given name (identifier).
	 *
	 * @param name the name (identifier)
	 */

	public Symbol(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Symbol other) {
		return name.compareTo(other.name);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Symbol && ((Symbol) other).name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}