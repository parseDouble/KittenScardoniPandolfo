package absyn;

import java.io.FileWriter;

import types.Type;
import types.TypeList;
import types.CodeSignature;
import semantical.TypeChecker;
import translate.CodeBlock;

/**
 * A node of abstract syntax representing a sequence (list)
 * of Kitten expressions.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ExpressionSeq extends Absyn {

    /**
     * The head of the list.
     */

    private Expression head;

    /**
     * The tail of the list.
     */

    private ExpressionSeq tail;

    /**
     * Constructs the abstract syntax of a non-empty
     * sequence (list) of expressions.
     *
     * @param pos the starting position in the source file of
     *            the concrete syntax represented by this abstract syntax
     * @param head the head of the list
     * @param tail the tail of the list
     */

    public ExpressionSeq(int pos, Expression head, ExpressionSeq tail) {
	super(pos);

	this.head = head;
	this.tail = tail;
    }

    /**
     * Yields the head of this sequence of expressions.
     *
     * @return the head of this sequence of expressions
     */

    public Expression getHead() {
	return head;
    }

    /**
     * Yields the tail of this sequence of expressions, if any.
     *
     * @return the tail of this sequence of expressions, if any. Returns
     *         <tt>null</tt> if there is no tail
     */

    public ExpressionSeq getTail() {
	return tail;
    }

    /**
     * Writes in the specified file a dot representation of the abstract syntax
     * of this sequence of expressions.
     *
     * @param where the file where the dot representation must be written
     * @return the name used to refer to this node in the dot file,
     *         as computed by <tt>dotNodeName()</tt>
     */

    public final String toDot(FileWriter where) throws java.io.IOException {
	// dumps in the file the name of the node in the dot file,
	// followed by the label used to show the node to the user of dot.
	// This label is computed by the <tt>label()</tt> method
	where.write(dotNodeName() + " [ label = \"" + label() + "\"];\n");

	// links this node with the node for the abstract syntax of the head
	linkToNode("head",head.toDot(where),where);

	// links this node to the node for the abstract syntax of the tail
	if (tail != null) boldLinkToNode("tail",tail.toDot(where),where);

	return dotNodeName();
    }

    /**
     * Type-checks the expressions in this sequence.
     *
     * @param checker the type-checker used for type-checking
     * @return the list of the Kitten types of the elements in this sequence
     */

    public TypeList typeCheck(TypeChecker checker) {
	TypeList result;

	if (tail != null) result = tail.typeCheck(checker);
	else result = TypeList.EMPTY;

	return result.push(head.typeCheck(checker));
    }

    /**
     * Translates this sequence of expressions into intermediate Kitten
     * code, by requiring that each
     * expression leaves on the stack a value of the corresponding type
     * in the list of types passed as a parameter. The result
     * is an intermediate Kitten code which loads on the stack the values
     * of the expressions, with the value of the last expression on top, and
     * then continues with the given <tt>continuation</tt>.
     * This methods calls itself recursively on the <tt>tail</tt>
     * of the sequence and then calls <tt>translateAs</tt>
     * (of class <tt>Expression.java</tt>) on its <tt>head</tt>.
     *
     * @param where the method or constructor where this expression occurs
     * @param types the list of types of the values which must be left onto the
     *              stack by the translation of this sequence of expressions
     * @param continuation the code which must follow the translation of
     *                     these expressions
     * @return the code which evaluates the expressions in sequence and then
     *         continues with <tt>continuation</tt>
     */

    public CodeBlock translateAs
	(CodeSignature where, TypeList types, CodeBlock continuation) {

	return head.translateAs
	    (where,types.getHead(),tail != null ?
	     tail.translateAs(where,types.getTail(),continuation)
	     : continuation);
    }
}
