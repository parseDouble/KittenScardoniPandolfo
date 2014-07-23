package util.graph;

import util.List;
import util.BitSet;

/**
 * An arc between two nodes of a graph. Each node contains elements and
 * those elements flow along the arcs. The graph is always kept in a stable
 * state. An arc represents the fact that the set of elements for the first node
 * is included in the set of elements for the second node, provided they
 * do <i>not</i> belong to a given <i>filtering</i> set.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

final class FilterArcNot<X> extends Arc<X> {

    /**
     * The set of classes which <i>filter</i> the elements from the source
     * into the sink.
     */

    private BitSet<X> filter;

    /**
     * Builds a filtering arc between two nodes.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     * @param filter the set of elements which cannot be propagated from the
     *        <tt>source</tt> to the <tt>sink</tt>
     */

    FilterArcNot(Node<X> source, Node<X> sink, BitSet<X> filter) {
	super(source,sink);

	this.filter = filter;
    }

    /**
     * Determines if this arc represents the same arc the <tt>other</tt>.
     *
     * @param other the other arc
     * @return true if and only if this arc represents the same arc as
     *         <tt>other</tt>
     */

    public boolean equals(Object other) {
	if (!(other instanceof FilterArcNot)) return false;

	return super.equals(other) && filter == ((FilterArcNot)other).filter;
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
	getSource().propagateNot(getSink(),workSet,sets,filter);
    }

    /**
     * Yields a string representation of this arc.
     *
     * @return a string representation of this arc
     */

    public String toString() {
	return super.toString() + " but not in " + filter;
    }
}
