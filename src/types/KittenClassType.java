package types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import lexical.Lexer;
import symbol.Symbol;
import syntactical.Parser;
import translate.Program;
import absyn.ClassDefinition;
import errorMsg.ErrorMsg;

/**
 * A type representing a class type ofthe Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class KittenClassType extends ClassType {

    /**
     * The parser used to parse the source file of this class.
     */

    private Parser parser;

    /**
     * The abstract syntax of this class.
     */

    private ClassDefinition abstractSyntax;

    /**
     * True if and only if this class has been already type-checked.
     */

    private boolean typeChecked;

    /**
     * A table which binds each symbol to its corresponding
     * <tt>KittenClassType</tt>.
     * This lets us have a unique <tt>KittenClassType</tt> with a given name.
     */

    private static HashMap<Symbol,KittenClassType> memory
	= new HashMap<Symbol,KittenClassType>();

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
	this.memory.put(name,this);

	// we have not type-checked this class yet
	this.typeChecked = false;

	// we perform lexical and syntactical analysis. The result is
	// the abstract syntax of this class definition
	try {
	    this.parser = new Parser(new Lexer(name));
	}
	catch (FileNotFoundException e) {
	    // there is a syntax error in the class text or the same class
	    // cannot be found on the file system: we build a fictitious
	    // syntax for the class, so that the processing can go on
	    if (name == Symbol.OBJECT)
		this.abstractSyntax = new ClassDefinition(0,name,null,null);
	    else
		this.abstractSyntax
		    = new ClassDefinition(0,name,Symbol.OBJECT,null);
	}

	if (this.parser != null) try {
	    this.abstractSyntax = (ClassDefinition)parser.parse().value;
	}
	catch (Exception e) {
	    // there is a syntax error in the class text or the same class
	    // cannot be found on the file system: we build a fictitious
	    // syntax for the class, so that the processing can go on
	    if (name == Symbol.OBJECT)
		this.abstractSyntax = new ClassDefinition(0,name,null,null);
	    else
		this.abstractSyntax
		    = new ClassDefinition(0,name,Symbol.OBJECT,null);
	}

	// we add the fields, constructors and methods of the class
	abstractSyntax.addMembers(this);

	if (name != Symbol.OBJECT)
	    // if this is not <tt>Object</tt>, we create its superclass also
	    addSuperclass(abstractSyntax.getSuperclassName());
	else
	    //otherwise we take note of the top of the hierarchy
	    // of the reference types
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
	return parser.getErrorMsg();
    }

    /**
     * Type-checks this class type, <i>i.e.</i>, its abstract syntax.
     *
     * @return this class type itself
     */

    public KittenClassType typeCheck() {
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

	return this;
    }

    /**
     * Translates this class into intermediate Kitten code.
     * It is assumed that this class has been already type-checked.
     *
     * @return the set of <tt>ClassMemberSignature</tt>'s which are reachable
     *         from the empty constructor or the <tt>main</tt> method of the
     *         class compiled by Kitten, translated into Kitten code
     */

    public Program translate() {
	return abstractSyntax.translate();
    }

    /**
     * Writes in a file named as this class (plus the trailing <tt>.dot</tt>)
     * a dot representation of the abstract syntax of this class.
     *
     * @throws IOException if the dot file cannot be created
     */

    public void dumpDot() throws IOException {
	abstractSyntax.dumpDot();
    }
}
