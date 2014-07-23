package components;

import util.List;

/**
 * A node of a graph.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Node {

    /**
     * The number of the strongly connected component of this node.
     * This is 0 if the method <tt>computeSCCs()</tt> has not been called.
     * It is strictly positive otherwise.
     */

    private int component;

    /**
     * The set of predecessors of this node. This is only valid after the
     * computation of the strongly connected components.
     */

    private List<Node> previous;

    /**
     * Yields the successors of this node.
     *
     * @return the successors of this node
     */

    public abstract List<Node> forward();

    /**
     * Yields the set of predecessors of this node. This is only valid after
     * the computation of the strongly connected components.
     *
     * @return the set of predecessors of this node, assuming that strongly
     *         connected components have been already computed
     */

    public List<Node> getPrevious() {
	return previous;
    }

    /**
     * Sets the set of predecessors of this node.
     *
     * @param previous the new set of predecessors of this node
     */

    public void setPrevious(List<Node> previous) {
	this.previous = previous;
    }

    /**
     * Computes the strongly connected components of the graph made up of
     * the nodes reachable from this node.
     *
     * @return all nodes reachable from this node, in decreasing component
     *         order
     */

    public List<Node> computeSCCs() {
	return secondDFS(firstDFS(new List<Node>()));
    }

    /**
     * Yields the number of the strongly connected component of this node.
     *
     * @return the number of the strongly connected component of this node.
     *         Returns 0 if the method <tt>computeSCCs()</tt> has not been
     *         called yet. It is strictly positive otherwise
     */

    public int getComponent() {
	return component;
    }

    /**
     * Sets the component of this node.
     *
     * @param component the component
     */

    public void setComponent(int component) {
	this.component = component;
    }

    /**
     * Applies the first phase of the computation of the strongly connected
     * components.
     *
     * @param orderedNodes the nodes which have been already processed,
     *                     in decreasing order wrt. time of visit
     * @return <tt>orderedNodes</tt> itself, enriched with all the nodes
     *         reachable from this one in decreasing order wrt. time of visit
     */

    public List<Node> firstDFS(List<Node> orderedNodes) {
	// mark this node as already visited
	component = -1;

	// we reset the set of its predecessors
	previous = new List<Node>();

	// for every successor v of this node...
	for (Node v: forward()) {
	    // ...which has not been seen before, we visit this successor
	    if (v.component >= 0) v.firstDFS(orderedNodes);

	    // mark this node as one of the predecessors of v
	    v.previous.addFirst(this);
	}

	// nodes are ordered on the basis of the time they have been visited
	orderedNodes.addFirst(this);

	return orderedNodes;
    }

    /**
     * The second phase of the computation of the strongly connected
     * components.
     *
     * @param orderedNodes all nodes, in decreasing order wrt. time of visit
     * @return a new list of all nodes, in decreasing component number
     */

    public List<Node> secondDFS(List<Node> orderedNodes) {
	List<Node> result = new List<Node>();
	int component = 1;

	// we consider the nodes in decreasing component order
	// and we put in the same component its predecessors not yet visited
	for (Node v: orderedNodes)
	    // if the node has not been visited in this phase yet...
	    if (v.component < 0) v.includeComponent(component++,result);

	return result;
    }

    /**
     * Gives the component number <tt>component</tt> to our predecessors
     * not yet visited during the second phase of the computation of
     * the strongly connected component.
     *
     * @param component the component number to be given
     * @param seen the nodes already seen during the second phase, in
     *             decreasing component order. It gets prefixed with the nodes
     *             of this component whose number is <tt>component</tt>
     */

    public void includeComponent(int component, List<Node> seen) {
	seen.addFirst(this);
	this.component = component;

	for (Node p: previous) if (p.component < 0)
	    p.includeComponent(component,seen);
    }
}
