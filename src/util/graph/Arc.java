package util.graph;

import util.List;
import util.BitSet;

/**
 * An arc between two nodes of a graph. Each node contains elements of type
 * <tt>X</tt> which flow along the arcs. The graph is always kept in a stable
 * state. An arc represents the fact that the set of elements for the first node
 * is included in the set of elements for the second node.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class Arc<X> {

    /**
     * The origin of the arc.
     */

    private Node<X> source;

    /**
     * The destination of the arc.
     */

    private Node<X> sink;

    /**
     * Builds an arc between two nodes.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     */

    Arc(Node<X> source, Node<X> sink) {
	this.source = source;
	this.sink = sink;
    }

    /**
     * Yields the source of this arc.
     *
     * @return the source of this arc
     */

    protected Node<X> getSource() {
	return source;
    }

    /**
     * Yields the sink of this arc.
     *
     * @return the sink of this arc
     */

    protected Node<X> getSink() {
	return sink;
    }

    /**
     * Determines if this arc represents the same arc the <tt>other</tt>.
     *
     * @param other the other arc
     * @return true if and only if this arc represents the same arc as
     *         <tt>other</tt>
     */

    public boolean equals(Object other) {
	if (getClass() != other.getClass())
	    return false;

	Arc otherA = (Arc)other;

	return source == otherA.source && sink == otherA.sink;
    }

    /**
     * Yields the hash code of this arc. This is consistent with
     * <tt>equals()</tt>.
     *
     * @return the hash code
     */

    public int hashCode() {
	return source.hashCode() + sink.hashCode();
    }

    /**
     * Propagates the approximation of the <tt>source</tt> of this arc
     * towards its <tt>sink</tt>.
     *
     * @param workSet a stack of arcs that still need to be propagated. This
     *                stack might be modified by this method, since if the
     *                approximation for the <tt>sink</tt> changed, then all
     *                outgoing arcs from <tt>sink</tt> are pushed into the
     *                <tt>workSet</tt>
     * @param sets the sets of elements which must be propagated for each
     *             arc in <tt>workSet</tt>
     */

    void propagate(List<Arc<X>> workSet, List<BitSet<X>> sets) {
	source.propagate(sink,workSet,sets);
    }

    /**
     * Yields a string representation of this arc.
     *
     * @return a string representation of this arc
     */

    public String toString() {
	return source + " -> " + sink;
    }
}
