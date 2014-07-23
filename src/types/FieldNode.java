package types;

import util.graph.Node;

/**
 * A node of a graph containing types. Those types flow from node to node
 * following the arcs of the graph and are always kept in a stable state.
 * Each field node stands for the types reachable from the fields of a class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class FieldNode extends Node<Type> {

    /**
     * The class for which this node has been built.
     */

    private ClassType clazz;

    /**
     * Builds a node of a graph for the given class. Initially, it is empty.
     *
     * @param clazz the class for which the node is built
     */

    public FieldNode(ClassType clazz) {
	super(Type.getFactory().createEmptySet());

	this.clazz = clazz;
    }

    /**
     * Checks if this node is equal to another, that is, they are built for the
     * same type.
     *
     * @param other the other node
     * @return true if and only if <tt>other</tt> is a <tt>FieldNode</tt> which
     *         has been built for the same class
     */

    public boolean equals(Object other) {
	if (!(other instanceof FieldNode)) return false;
	else return ((FieldNode)other).clazz == this.clazz;
    }

    /**
     * Yields the hashcode of this node. This is consistent
     * with <tt>equals()</tt>.
     *
     * @return the hashcode
     */

    public int hashCode() {
	return clazz.hashCode();
    }
}
