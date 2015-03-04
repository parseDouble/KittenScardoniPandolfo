package types;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import lexical.Lexer;
import symbol.Symbol;
import syntactical.Parser;
import translate.Program;
import absyn.ClassDefinition;
import errorMsg.ErrorMsg;

/**
 * A type representing a class type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class KittenClassType extends ClassType {

	/**
	 * The utility for issuing errors about this class.
	 */

	private ErrorMsg errorMsg;

	/**
	 * The abstract syntax of this class.
	 */

	private final ClassDefinition abstractSyntax;

	/**
	 * True if and only if this class has been already type-checked.
	 */

	private boolean typeChecked;

	/**
	 * A table which binds each symbol to its corresponding {@code KittenClassType}.
	 * This lets us have a unique {@code KittenClassType} for a given name.
	 */

	private final static Map<Symbol, KittenClassType> memory = new HashMap<>();

	/**
	 * Constructs a Kitten class type with the given name. If the class
	 * cannot be found or contains a syntactical error, a fictitious class
	 * with no fields, no constructors and no methods is created.
	 *
	 * @param name the name of the class
	 */

	protected KittenClassType(Symbol name) {
		super(name);

		// we record this object for future lookup
		memory.put(name, this);

		// we have not type-checked this class yet
		this.typeChecked = false;

		ClassDefinition abstractSyntax;

		// we perform lexical and syntactical analysis. The result is
		// the abstract syntax of this class definition
		try {
			Parser parser = new Parser(new Lexer(name));
			errorMsg = parser.getErrorMsg();
			abstractSyntax = (ClassDefinition) parser.parse().value;
			// we add the fields, constructors and methods of the class
			abstractSyntax.addMembers(this);
		}
		catch (Exception e) {
			// there is a syntax error in the class text or the same class
			// cannot be found on the file system or cannot be type-checked:
			// we build a fictitious syntax for the class, so that the processing can go on
			if (name == Symbol.OBJECT)
				abstractSyntax = new ClassDefinition(0, name, null, null);
			else
				abstractSyntax = new ClassDefinition(0, name, Symbol.OBJECT, null);
		}

		this.abstractSyntax = abstractSyntax;

		if (name != Symbol.OBJECT)
			// if this is not Object, we create its superclass also
			addSuperclass(abstractSyntax.getSuperclassName());
		else
			// otherwise we take note of the top of the hierarchy of the reference types
			setObjectType(this);
	}

	/**
	 * Yields a class type with the given name. If a class type object named
	 * <tt>name</tt> already exists, that object is returned. Otherwise, if a
	 * Kitten class named <tt>name.kit</tt> exists and contains no error, a
	 * <tt>KittenClassType</tt> is returned. Otherwise, a fictitious
	 * <tt>KittenClassType</tt> is returned, whose code has no fields nor
	 * constructors nor methods.
	 *
	 * @param name the name of the class
	 * @return the unique class type object for the class with the given name
	 */

	public static KittenClassType mk(Symbol name) {
		KittenClassType result;

		// we first check to see if we already built this class type
		if ((result = memory.get(name)) != null) return result;
		else return new KittenClassType(name);
	}

	/**
	 * Yields a class type with the given file name. If a class type object
	 * with this name already exists, that object is returned. Otherwise, if a
	 * Kitten class named <tt>name</tt> exists and contains no syntax error, a
	 * type-checked <tt>KittenClassType</tt> is returned. Otherwise, a
	 * type-checked fictitious <tt>KittenClassType</tt> is returned, whose code
	 * contains no fields, nor constructors nor methods.
	 *
	 * @param fileName the name of the file of the class, including the
	 *                 <tt>.kit</tt> termination
	 * @return the unique Kitten class type object for the (type-checked)
	 *         class with the given name
	 */

	public static KittenClassType mkFromFileName(String fileName) {
		if (fileName.endsWith(".kit"))
			fileName = fileName.substring(0,fileName.length() - 4);

		KittenClassType result = mk(Symbol.mk(fileName));

		result.typeCheck();

		return result;
	}

	/**
	 * Yields a Kitten class type with the given <tt>name</tt>.
	 *
	 * @param name the name of the class type
	 * @return a new Kitten class type with the given <tt>name</tt>
	 */

	protected ClassType make(Symbol name) {
		return KittenClassType.mk(name);
	}

	/**
	 * Yields the set of all <tt>KittenClassType</tt>'s which have been created
	 * up to now.
	 *
	 * @return the set of all <tt>KittenClassType</tt>'s created so far
	 */

	public final static Collection<KittenClassType> getAll() {
		return memory.values();
	}

	/**
	 * Yields the error reporting utility for this class.
	 *
	 * @return the error reporting utility for this class
	 */

	public ErrorMsg getErrorMsg() {
		return errorMsg;
	}

	/**
	 * Type-checks this class type, <i>i.e.</i>, its abstract syntax.
	 */

	public void typeCheck() {
		// this check is just to avoid repeated error messages
		if (!typeChecked) {
			// we are going to type-check this class now
			typeChecked = true;

			// we type-check the abstract syntax of this class
			abstractSyntax.typeCheck(this);

			// we continue by type-checking our superclass, if any
			ClassType superclass = getSuperclass();

			if (superclass != null && superclass instanceof KittenClassType)
				((KittenClassType)superclass).typeCheck();
		}
	}

	/**
	 * Translates this class into intermediate Kitten code.
	 * It is assumed that this class has been already type-checked.
	 *
	 * @return the program reachable from the empty constructor or the main of
	 *         this class, translated into Kitten code
	 */

	public Program translate() {
		return abstractSyntax.translate();
	}
}