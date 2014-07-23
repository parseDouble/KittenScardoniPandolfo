package absyn;

import java.io.FileWriter;
import java.io.IOException;

import types.KittenClassType;
import types.ClassMemberSignature;

/**
 * A node of abstract syntax representing the declaration of a class
 * member <i>i.e.</i>, a field, constructor or method of a Kitten class.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ClassMemberDeclaration extends Absyn {

    /**
     * The declaration of the subsequent class member.
     * It might be <tt>null</tt>.
     */

    private ClassMemberDeclaration next;

    /**
     * Constructs a node of abstract syntax representing a class
     * member declaration (<i>i.e.</i>, the declaration of a field,
     * constructor or method of a Kitten class). By default, there
     * is no subsequent class member declaration.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param next the abstract syntax of the declaration of the
     *             subsequent class member, if any
     */

    protected ClassMemberDeclaration(int pos, ClassMemberDeclaration next) {
	super(pos);

	// there is no subsequent class member declaration
	this.next = next;
    }

    /**
     * Yields the signature of this class member declaration.
     *
     * @return the signature
     */

    public abstract ClassMemberSignature getSignature();

    /**
     * Writes in the specified file a dot representation of this node
     * of abstract syntax. It writes a single dot node for this
     * node of abstract syntax and it calls the auxiliary
     * <tt>toDot$0()</tt> method. Subclasses should redefine the latter
     * in order to consider components of abstract syntax classes.
     * If there is a subsequent class member declaration, it calls itself
     * recursively and builds a bold arc from this declaration to
     * the dot representation of the subsequent one.
     *
     * @param where the file where the dot representation must be written
     * @return the name used to refer to this node in the dot file,
     *         as computed by <tt>dotNodeName()</tt>
     */

    public final String toDot(FileWriter where) throws IOException {
	// dumps in the file the name of the node in the dot file,
	// followed by the label used to show the node to the user of dot.
	// This label is computed by the <tt>label()</tt> method
	where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

	toDot$0(where);

	// if there is a subsequent class member declaration,
	// we link process it and link it through a bold arc
	if (next != null) boldLinkToNode("next",next.toDot(where),where);

	return dotNodeName();
    }

    /**
     * Adds class member-specific information in the dot file
     * representing the abstract syntax. This should usually build
     * arcs between this node and those for the abstract syntax
     * of its components.
     *
     * @param where the file where the dot representation must be written
     */

    protected abstract void toDot$0(FileWriter where) throws IOException;

    /**
     * Adds the signature of this class member (<i>i.e.</i>, field, method
     * or constructor) and of the next class members to the given class.
     *
     * @param clazz the class where the signature of this and the
     *              next class members must be added
     */

    protected final void addMembers(KittenClassType clazz) {
	addMember(clazz);

	// if there is another declaration, we process it
	if (next != null) next.addMembers(clazz);
    }

    /**
     * Adds the signature of this class member (<i>i.e.</i>, field, method
     * or constructor) to the given class.
     *
     * @param clazz the class where the signature of this
     *              class member must be added
     */

    protected abstract void addMember(KittenClassType clazz);

    /**
     * Type-checks this definition of a class member. It calls the auxiliary
     * <tt>typeCheck$0</tt> method and then continues recursively with
     * the subsequent members.
     *
     * @param currentClass the semantical type of the class where this
     *                     member occurs.
     *                     This will be bound to the implicit <tt>this</tt>
     *                     parameter
     */

    final void typeCheck(KittenClassType currentClass) {
	typeCheck$0(currentClass);

	// if there is another declaration, we type-check it
	if (next != null) next.typeCheck(currentClass);
    }

    /**
     * Auxiliary method which type-checks this class member.
     *
     * @param currentClass the semantical type of the class where this
     *                     member occurs.
     *                     This will be bound to the implicit <tt>this</tt>
     *                     parameter
     */

    protected abstract void typeCheck$0(KittenClassType currentClass);
}
