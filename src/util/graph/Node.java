package util.graph;

import util.List;
import util.BitSet;

/**
 * A node of a graph containing elements of type X. Those elements flow from
 * node to node following the arcs of the graph and are always kept in a stable
 * state.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Node<X> {

    /**
     * The set of elements contained in this node.
     */

    private BitSet<X> approx;

    /**
     * The set of arcs leaving this node.
     */

    private List<Arc<X>> star;

    /**
     * Builds a node of a graph containing the given elements.
     *
     * @param approx the elements which are going to be used as initial
     *               approximation for this node
     */

    public Node(BitSet<X> approx) {
	this.approx = approx;
    }

    /**
     * Builds a node of a graph with no current approximation.
     */

    protected Node() {}

    /**
     * Yields the current approximation for this node.
     *
     * @return the current approximation for this node
     */

    public BitSet<X> getApprox() {
	return approx;
    }

    /**
     * Sets the approximation inside this node, if it has not been set yet.
     *
     * @param approx the new approximation
     */

    final void setApprox(BitSet<X> approx) {
	if (this.approx == null) this.approx = approx;
    }

    /**
     * Adds the given arc to the out star of this node.
     *
     * @param arc the arc to be added to the out star of this node
     */

    void addStar(Arc<X> arc) {
	if (star == null) star = new List<Arc<X>>(arc);
	else star.addFirst(arc);

	// the destination of the arc might change its approximation
	List<Arc<X>> workSet = new List<Arc<X>>(arc);
	List<BitSet<X>> sets = new List<BitSet<X>>(new BitSet<X>(approx));

	while (!workSet.isEmpty())
	    workSet.removeFirst().propagate(workSet,sets);
    }

    /**
     * Propagates the approximation of this node into that of <tt>sink</tt>.
     *
     * @param sink the node towards which the approximation of this node is
     *             propagated
     * @param workSet a stack of arcs that still need to be propagated. This
     *                stack might be modified by this method, since if the
     *                approximation for the <tt>sink</tt> changed, then all
     *                outgoing arcs from <tt>sink</tt> are pushed into the
     *                <tt>workSet</tt>
     * @param sets the sets of elements which must be propagated for each
     *             arc in <tt>workSet</tt>
     */

    void propagate(Node<X> sink, List<Arc<X>> workSet,
		   List<BitSet<X>> sets) {

	BitSet<X> added;

	if (!(added = sink.addAll(sets.removeFirst(),null)).isEmpty())
	    if (sink.star != null) for (Arc<X> arc: sink.star) {
		workSet.addLast(arc);
		sets.addLast(added);
	    }
    }

    /**
     * Propagates the elements in the approximation of this node and in the
     * given filter into that of <tt>sink</tt>.
     *
     * @param sink the node towards which the approximation of this node is
     *             propagated
     * @param workSet a stack of arcs that still need to be propagated. This
     *                stack might be modified by this method, since if the
     *                approximation for the <tt>sink</tt> changed, then all
     *                outgoing arcs from <tt>sink</tt> are pushed into the
     *                <tt>workSet</tt>
     * @param sets the sets of elements which must be propagated for each
     *             arc in <tt>workSet</tt>
     * @param filter the filter
     */

    void propagate(Node<X> sink, List<Arc<X>> workSet,
		   List<BitSet<X>> sets, BitSet<X> filter) {

	BitSet<X> added;

	if (!(added = sink.addAll(sets.removeFirst(),filter)).isEmpty())
	    if (sink.star != null) for (Arc<X> arc: sink.star) {
		workSet.addLast(arc);
		sets.addLast(added);
	    }
    }

    /**
     * Propagates the elements in the approximation of this node and not in the
     * given filter into that of <tt>sink</tt>.
     *
     * @param sink the node towards which the approximation of this node is
     *             propagated
     * @param workSet a stack of arcs that still need to be propagated. This
     *                stack might be modified by this method, since if the
     *                approximation for the <tt>sink</tt> changed, then all
     *                outgoing arcs from <tt>sink</tt> are pushed into the
     *                <tt>workSet</tt>
     * @param sets the sets of elements which must be propagated for each
     *             arc in <tt>workSet</tt>
     * @param filter the filter
     */

    void propagateNot(Node<X> sink, List<Arc<X>> workSet,
		      List<BitSet<X>> sets, BitSet<X> filter) {

	BitSet<X> added;

	if (!(added = sink.addAllNot(sets.removeFirst(),filter)).isEmpty())
	    if (sink.star != null) for (Arc<X> arc: sink.star) {
		workSet.addLast(arc);
		sets.addLast(added);
	    }
    }

    /**
     * Adds the given set of elements to the approximation for this node,
     * filtered wrt. the given filter.
     *
     * @param set the set of elements to be added
     * @param filter the set of elements which can be added
     * @return the set of elements from <tt>set</tt> which where not in the
     *         approximation of this node
     */

    protected BitSet<X> addAll(BitSet<X> set, BitSet<X> filter) {
	if (filter == null) return approx.addAllAndDifference(set);
	else return approx.addAllAndDifference(set.intersection(filter));
    }

    /**
     * Adds the given set of elements to the approximation for this node,
     * filtered wrt. the given filter.
     *
     * @param set the set of elements to be added
     * @param filter the set of elements which cannot be added
     * @return the set of elements from <tt>set</tt> which where not in the
     *         approximation of this node
     */

    protected BitSet<X> addAllNot(BitSet<X> set, BitSet<X> filter) {
	if (filter == null) return approx.addAllAndDifference(set);
	else return approx.addAllAndDifference(set.minus(filter));
    }

    /**
     * Yields a string describing the current approximation for this node.
     *
     * @return the string
     */

    public String toString() {
	String result = "{";

	for (X e: approx) result += e + ",";

	if (result.endsWith(","))
	    result = result.substring(0,result.length() - 1);

	return result + "}";
    }
}
