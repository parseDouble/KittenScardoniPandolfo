package types;

import util.graph.Node;

/**
 * A node of a graph containing types. Those types flow from node to node
 * following the arcs of the graph and are always kept in a stable state.
 * This node represent the set of all types ever generated.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class AllNode extends Node<Type> {

    /**
     * Builds a node of a graph representing all types ever generated.
     */

    public AllNode() {
	super(Type.getFactory().createEmptySet());
    }

    /**
     * Checks if this node is equal to another, that is, they are both
     * <tt>AllNode</tt>'s.
     *
     * @param other the other node
     * @return true if and only if <tt>other</tt> is an <tt>AllNode</tt>
     */

    public boolean equals(Object other) {
	return other instanceof AllNode;
    }

    /**
     * Yields the hashcode of this node. This is consistent
     * with <tt>equals()</tt>.
     *
     * @return the hashcode
     */

    public int hashCode() {
	return 1;
    }
}
