package components;

import util.List;

/**
 * A node of a graph.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Node {

    /**
     * The set of predecessors of this node. This is only valid after the
     * computation of the strongly connected components.
     */

    private List<Node> previous;

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
}