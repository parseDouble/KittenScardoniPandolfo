package types;

import util.graph.Node;

/**
 * A node of a graph containing types. Those types flow from node to node
 * following the arcs of the graph and are always kept in a stable state.
 * Each node is built for a type.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class TypeNode extends Node<Type> {

    /**
     * The type from which this node has been built.
     */

    private Type type;

    /**
     * Builds a node of a graph for the given <tt>type</tt>. Initially, it
     * contains only <tt>type</tt>.
     *
     * @param type the type for which this node is built
     */

    public TypeNode(Type type) {
	super(Type.getFactory().createEmptySet());

	this.type = type;

	// initially, only the type itself is used as approximation
	getApprox().add(type);
    }

    /**
     * Checks if this node is equal to another, that is, they are built for the
     * same type.
     *
     * @param other the other node
     * @return true if and only if <tt>other</tt> is a <tt>TypeNode</tt> which
     *         has been built for the same type
     */

    public boolean equals(Object other) {
	if (!(other instanceof TypeNode)) return false;
	else return ((TypeNode)other).type == this.type;
    }

    /**
     * Yields the hashcode of this node. This is consistent
     * with <tt>equals()</tt>.
     *
     * @return the hashcode
     */

    public int hashCode() {
	return type.hashCode();
    }
}
