package symbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A symbol of the abstract syntax. It represents an identifier in the
 * source code. Two occurrences of the same identifier are represented by the
 * same, unique symbol, by using the singleton design pattern.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Symbol implements Comparable<Symbol> {

	/**
	 * A table that binds names to their corresponding symbol.
	 * Two occurrences of the same identifier are represented
	 * by the same, unique symbol.
	 */

	private static Map<String,Symbol> memory = new HashMap<String,Symbol>();

	/**
	 * The symbol representing the identifier {@code this}.
	 */

	public static final Symbol THIS = mk("this");

	/**
	 * The symbol representing the identifier {@code Object}.
	 */

	public static final Symbol OBJECT = mk("Object");

	/**
	 * The symbol representing the identifier {@code String}.
	 */

	public static final Symbol STRING = mk("String");

	/**
	 * The symbol of the method where the application starts.
	 */

	public static final Symbol MAIN = mk("main");

	/**
	 * The string representation of the symbol.
	 */

	private final String name;

	/**
	 * Creates a symbol with the given name (identifier).
	 *
	 * @param name the name (identifier)
	 */

	private Symbol(String name) {
		this.name = name;
	}

	/**
	 * Returns the unique symbol representing the given string (identifier).
	 *
	 * @param name the name of the symbol (identifier)
	 * @return the unique symbol representing that name
	 */

	public static Symbol mk(String name) {
		// we first look in the memory, to see if we already created a symbol for this name
		Symbol s = memory.get(name);
		if (s == null)
			// if not, we build a new symbol for the name and store it in memory
			memory.put(name, s = new Symbol(name));

		return s;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Symbol other) {
		return name.compareTo(other.name);
	}
}