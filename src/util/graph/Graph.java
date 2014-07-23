package util.graph;

import java.util.HashMap;

import util.BitSet;
import util.BitSetFactory;

/**
 * A graph of nodes and arcs without repetitions. Each node represents a set of
 * elements of type <tt>X</tt> which can flow along the arcs. The graph is
 * always kept in a consistent state, that is, all flows are propagated.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Graph<X> {

    /**
     * The factory used to create bitsets of things in this graph.
     */

    private final BitSetFactory<X> factory;

    /**
     * A cache mapping nodes into nodes. This is used to avoid duplicate nodes.
     */

    private final HashMap<Node<X>,Node<X>> cacheNodes;

    /**
     * A cache mapping arcs into arcs. This is used to avoid duplicate arcs.
     */

    private final HashMap<Arc<X>,Arc<X>> cacheArcs;

    /**
     * Builds a graph.
     */

    public Graph() {
	this(new BitSetFactory<X>());
    }

    /**
     * Builds a graph with the given factory.
     *
     * @param factory the factory
     */

    protected Graph(BitSetFactory<X> factory) {
	this.cacheNodes = new HashMap<Node<X>,Node<X>>();
	this.cacheArcs = new HashMap<Arc<X>,Arc<X>>();
	this.factory = factory;
    }

    /**
     * Yields an empty set of things.
     *
     * @return the empty set of things
     */

    public BitSet<X> emptySet() {
        return factory.createEmptySet();
    }

    /**
     * Yields the unique representant of the given node in this graph.
     * It first checks if an equivalent node belongs to this graph. If this is
     * the case, the equivalent node is returned. Otherwise, the given node
     * is added to this graph.
     *
     * @param node the node that must be added to the graph
     * @return the same <tt>node</tt>, added to this graph, if it did not
     *         belong to this graph. Otherwise, it yields an equivalent node
     *         which already belongs to this graph
     */

    protected final Node<X> mkUnique(Node<X> node) {
	// we first check to see if an equivalent node was already in the graph
	Node<X> cached = cacheNodes.get(node);
	if (cached != null) return cached;
	else {
	    // if not, we add the node to the graph. It will stand in the future
	    // for all equivalent nodes that will be added to this graph
	    cacheNodes.put(node,node);
	    node.setApprox(factory.createEmptySet());
	    return node;
	}
    }

    /**
     * Builds an arc between two nodes. It uses a cache to avoid duplication.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     */

    public final void arc(Node<X> source, Node<X> sink) {
	// an arc between a node and itself is useless
	if (source == sink) return;

	Arc<X> result = new Arc<X>(source,sink), cached = cacheArcs.get(result);

	if (cached == null) {
	    // the arc did not exist yet in this graph: we put it in the cache
	    cacheArcs.put(result,result);

	    // we also say that this arc belongs to the out star
	    // of <tt>source</tt>
	    source.addStar(result);
	}
    }

    /**
     * Builds a filter arc between two nodes. The filter is provided
     * explicitly. It uses a cache to avoid duplication. Only the elements
     * which are in the filter can flow along the arc.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     * @param filter the filter
     */

    public final void filterArc
	(Node<X> source, Node<X> sink, BitSet<X> filter) {

	FilterArc<X> result = new FilterArc<X>(source,sink,filter);
	Arc<X> cached = cacheArcs.get(result);

	if (cached == null) {
	    // the arc did not exist yet in this graph: we put it in the cache
	    cacheArcs.put(result,result);

	    // we also say that this arc belongs to the out star
	    // of <tt>source</tt>
	    source.addStar(result);
	}
    }

    /**
     * Builds a filter arc between two nodes. The filter is provided
     * explicitly. It uses a cache to avoid duplication. Only the elements
     * which are <i>not</i> in the filter can flow along the arc.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     * @param filter the filter
     */

    public final void filterArcNot
	(Node<X> source, Node<X> sink, BitSet<X> filter) {

	FilterArcNot<X> result = new FilterArcNot<X>(source,sink,filter);
	Arc<X> cached = cacheArcs.get(result);

	if (cached == null) {
	    // the arc did not exist yet in this graph: we put it in the cache
	    cacheArcs.put(result,result);

	    // we also say that this arc belongs to the out star
	    // of <tt>source</tt>
	    source.addStar(result);
	}
    }

    /**
     * Builds a filter arc between two nodes. The filter is provided
     * explicitly. It uses a cache to avoid duplication.
     *
     * @param source the origin of the arc
     * @param sink the destination of the arc
     * @param filter the filter. Its approximation is the filtering set
     */

    public final void filterArc
	(Node<X> source, Node<X> sink, Node<X> filter) {

	filterArc(source,sink,filter.getApprox());
    }

    /**
     * Prints the approximations for the nodes of this graph.
     */

    public void printAll() {
	for (Node<X> n: cacheNodes.values()) System.out.println(n);
    }

    /**
     * Prints the approximations for the nodes of this graph.
     */

    public void printAllArcs() {
	for (Arc<X> a: cacheArcs.values()) System.out.println(a);
    }


    /**
     * Yields the statistics about this graph.
     *
     * @return a string describing the statistics about this graph
     */

    public String statistics() {
	return "nodes: " + cacheNodes.size() + ", arcs: " + cacheArcs.size()
	    + ", items: " + factory.size();
    }
}
