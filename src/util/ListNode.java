package util;

/**
 * A node of a list.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class ListNode<E> {

    /**
     * The element inside the node.
     */

    E head;

    /**
     * The subsequent node.
     */

    ListNode<E> tail;

    /**
     * Builds a node with a sgiven successor.
     *
     * @param head the element inside the node
     * @param tail the successor of the node
     */

    ListNode(E head, ListNode<E> tail) {
	this.head = head;
	this.tail = tail;
    }

    /**
     * Builds a node with no successor.
     *
     * @param the element inside the node
     */

    ListNode(E head) {
	this.head = head;
    }
}