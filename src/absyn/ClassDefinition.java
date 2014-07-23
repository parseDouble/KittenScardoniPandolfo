package absyn;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import symbol.Symbol;
import types.KittenClassType;
import types.MethodSignature;
import types.ClassMemberSignature;
import types.TypeList;
import translate.Program;

/**
 * The abstract syntax of the definition of a Kitten class.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ClassDefinition extends Absyn {

    /**
     * The name of the class.
     */

    private Symbol name;

    /**
     * The name of the superclass.
     */

    private Symbol superclassName;

    /**
     * The sequence of fields or methods declarations.
     * This might be <tt>null</tt>.
     */

    private ClassMemberDeclaration declarations;

    /**
     * The class type of this class definition. It is <tt>null</tt> if
     * type-checking has not been performed yet.
     */

    private KittenClassType staticType;

    /**
     * Constructs the abstract syntax of the definition of a Kitten class.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param name the name of the class
     * @param superclassName the name of the superclass
     * @param declarations the sequence of fields or methods declarations.
     *                     This might be <tt>null</tt>
     */

    public ClassDefinition(int pos, Symbol name, Symbol superclassName,
			   ClassMemberDeclaration declarations) {
	super(pos);

	this.name = name;
	this.superclassName = superclassName;
	this.declarations = declarations;
    }

    /**
     * Yields the name of the class defined with this abstract syntax.
     *
     * @return the name of the class defined with this abstract syntax
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields the name of the superclass of the class
     * defined with this abstract syntax.
     *
     * @return the name of the superclass of the class
     *         defined with this abstract syntax
     */

    public Symbol getSuperclassName() {
	return superclassName;
    }

    /**
     * Yields the abstract syntax of the sequence of fields and methods
     * declarations in this class definition, if any.
     *
     * @return the abstract syntax of the sequence of fields and methods
     *         declarations in this class definition, if any. Returns
     *         <tt>null</tt> if there is no declaration inside this class
     *         definition
     */

    public ClassMemberDeclaration getDeclarations() {
	return declarations;
    }

    /**
     * Yields the static type of this class definition.
     *
     * @return the static type of thie class definition. It returns
     *         <tt>null</tt> if type-checking has not been performed yet
     */

    public KittenClassType getStaticType() {
	return staticType;
    }

    /**
     * Writes in the specified file a dot representation of the abstract
     * syntax of this class. It writes the preamble specifying the paper
     * size and orientation, then puts a node for this class definition,
     * with three children corresponding to the <tt>name</tt>,
     * <tt>superclassName</tt> and <tt>declarations</tt> fields, if any. Then
     * it builds the subtrees rooted at those children.
     *
     * @param where the file where the dot representation must be written
     * @throws IOException if there is an error while writing in <tt>where</tt>
     */

    public final void toDot(FileWriter where) throws IOException {
	where.write("digraph " + name + " {\n");

	// the size of a standard A4 sheet (in inches)
	where.write("size = \"11,7.5\";\n");

	// landscape mode
	where.write("rotate = 90\n");

	// dumps in the file the name of the node in the dot file,
	// followed by the label used to show the node to the user of dot.
	// This label is computed by the <tt>label()</tt> method
	where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

	linkToNode("name",name.toDot(where),where);
	if (superclassName != null)
	    linkToNode("superclassName",superclassName.toDot(where),where);
	if (declarations != null)
	    linkToNode("declarations",declarations.toDot(where),where);

	where.write("}");
    }

    /**
     * Writes in a file named as this class (plus the trailing
     * <tt>.dot</tt>) a dot representation of the abstract syntax of this
     * class.
     *
     * @throws IOException if the dot file cannot be created
     */

    public final void dumpDot() throws IOException {
	FileWriter dot = new FileWriter(name + ".dot");

	toDot(dot);
	dot.flush();
	dot.close();
    }

    /**
     * Adds, to the given class, the signatures of the fields,
     * constructors and methods of this class definition.
     *
     * @param clazz the class where the signatures of fields,
     *              constructors and methods must be added
     */

    public void addMembers(KittenClassType clazz) {
	if (declarations != null) declarations.addMembers(clazz);
    }

    /**
     * Type-checks this class definition. Namely, it type-checks its members
     * and checks that the empty constructor exists.
     *
     * @param currentClass the semantical type of this class.
     *                     This will be bound to the implicit <tt>this</tt>
     *                     parameter of all its constructors and methods
     */

    public void typeCheck(KittenClassType currentClass) {
	staticType = currentClass;

	if (declarations != null) declarations.typeCheck(currentClass);
    }

    /**
     * Translates this class definition into intermediate Kitten code.
     * Only the methods reachable from <tt>main</tt> are compiled, if any.
     *
     * @return the set of <tt>ClassMemberSignature</tt>'s which are reachable
     *         from the empty constructor or the <tt>main</tt> method of the
     *         class compiled by Kitten
     */

    public Program translate() {
	HashSet<ClassMemberSignature> done
	    = new HashSet<ClassMemberSignature>();

	// we look up for the main method, if any
	MethodSignature main = staticType.methodLookup
	    (Symbol.MAIN,TypeList.EMPTY);

	// we translate everything which is reachable from the main
	// method of this class (if any)
	if (main != null) main.getAbstractSyntax().translate(done);

	return new Program(done,name.toString(),main);
    }
}
