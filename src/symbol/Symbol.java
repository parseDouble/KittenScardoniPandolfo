package symbol;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * A symbol of the abstract syntax. It represents an identifier in the
 * source code. Two occurrences of the same identifier are represented by the
 * same, unique symbol.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Symbol implements Comparable {

    /**
     * A table which binds strings to their corresponding symbol.
     * Two occurrences of the same identifier are represented
     * by the same, unique symbol.
     */

    private static HashMap<String,Symbol> memory
	= new HashMap<String,Symbol>();

    /**
     * The symbol representing the identifier <tt>this</tt>.
     */

    public static final Symbol THIS = mk("this");

    /**
     * The symbol representing the identifier <tt>Object</tt>.
     */

    public static final Symbol OBJECT = mk("Object");

    /**
     * The symbol representing the identifier <tt>String</tt>.
     */

    public static final Symbol STRING = mk("String");

    /**
     * The symbol of the method where the application starts.
     */

    public static final Symbol MAIN = mk("main");

    /**
     * The string representation of the symbol.
     */

    private String name;

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
	// we first look in the memory, to see if we already
	// created a symbol for this name
	Symbol s = memory.get(name);
	if (s != null) return s;

	// if not, we build a new symbol for the name and we store it
	// in memory for future lookup
	s = new Symbol(name);
	memory.put(name,s);

	return s;
    }

    /**
     * Returns a string representation of the symbol.
     *
     * @return a string representation of the symbol
     */

    public String toString() {
	return name;
    }

    /**
     * Lexicographic comparison between this symbol and another symbol.
     *
     * @param other the symbol to compare with this
     * @return a negative integer if this symbol precedes <tt>other</tt>,
     *         0 if they are the same symbol, a positive integer
     *         if this symbol follows <tt>other</tt>
     */

    public int compareTo(Object other) {
	if (!(other instanceof Symbol)) return 0;

	return name.compareTo(((Symbol)other).name);
    }

    /**
     * Writes in a file the node for the dot representation of this symbol.
     *
     * @param where the file where the dot representation must be written
     * @return the string representing this node in the dot file
     */

    public String toDot(FileWriter where) throws IOException {
	where.write("symbol_" + name +
		    " [label = \"" + name + "\"" +
		    " fontname = \"Times-Italic\" shape = box]\n");

	// it must be the same string as above
	return "symbol_" + name;
    }
}
