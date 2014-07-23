package util;

/**
 * A simple linked list.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class List<E> implements Iterable<E> {

    /**
     * The first element of this list, if any.
     */

    private ListNode<E> first;

    /**
     * The last element of this list, if any.
     */

    private ListNode<E> last;

    /**
     * The size of this list.
     */

    private int size;

    /**
     * Builds an empty list.
     */

    public List() {}

    /**
     * Builds a list containing the given element only.
     *
     * @param e the only element which must be contained in the list
     */

    public List(E e) {
	addFirst(e);
    }

    /**
     * Builds a list containing the same elements as the given one.
     *
     * @param c the list which is used as a model to build the new list
     */

    public List(List<? extends E> c) {
	for (E e: c) addLast(e);
    }

    /**
     * Yields the size of this list.
     *
     * @return the size of this list
     */

    public int size() {
	return size;
    }

    /**
     * Determines if this list is empty.
     *
     * @return true if and only if this list is empty.
     */

    public boolean isEmpty() {
	return size == 0;
    }

    /**
     * Adds the given element at the end of this list.
     *
     * @param e the element
     */

    public void addLast(E e) {
	if (last == null) first = last = new ListNode<E>(e);
	else last = last.tail = new ListNode<E>(e);

	size++;
    }

    /**
     * Adds the given element at the beginning of this list.
     *
     * @param e the element
     */

    public void addFirst(E e) {
	if (first == null) first = last = new ListNode<E>(e);
	else first = new ListNode<E>(e,first);

	size++;
    }

    /**
     * Adds the given element at a specified position in this list.
     *
     * @param pos the position where the element must be added. 0 stands for
     *            the beginning of the list. The list must be at least
     *            <tt>pos</tt> elements long
     * @param e the element to add
     */

    public void add(int pos, E e) {
	if (pos <= 0) addFirst(e);
	else {
	    ListNode<E> cursor = first;

	    while (--pos > 0) cursor = cursor.tail;

	    cursor.tail = new ListNode<E>(e,cursor.tail);

	    if (cursor == last) last = cursor.tail;

	    size++;
	}
    }

    /**
     * Adds the elements of another list at the end of this list.
     *
     * @param c the other list
     */

    public void addAll(List<? extends E> c) {
	for (E e: c) addLast(e);
    }

    /**
     * Yields the first element of this list.
     *
     * @return the first element of this list
     */

    public E getFirst() {
	return first.head;
    }

    /**
     * Yields a given element of this list.
     *
     * @param pos the position of the lement which must be returned.
     *            0 stands for the first element
     * @return the element
     */

    public E get(int pos) {
	ListNode<E> cursor = first;
	while (pos-- > 0) cursor = cursor.tail;

	return cursor.head;
    }

    /**
     * Removes the first element from this list. The list must not be empty.
     *
     * @return the removed element
     */

    public E removeFirst() {
	E removed = first.head;

	if (first == last) first = last = null;
	else first = first.tail;

	size--;

	return removed;
    }

    /**
     * Removes the given element from this list.
     *
     * @param e the element to remove
     */

    public void remove(Object e) {
	if (first == null) return;
	else if (first.head == e) {
	    size--;
	    first = first.tail;
	    if (last.head == e) last = null;
	} else {
	    ListNode<E> cursor = first;

	    while (cursor.tail != null)
		if (cursor.tail.head == e) {
		    cursor.tail = cursor.tail.tail;
		    if (last.head == e) last = cursor;
		    size--;
		    return;
		}
		else cursor = cursor.tail;
	}
    }

    /**
     * Removes all the elements from this list.
     */

    public void clear() {
	first = last = null;
	size = 0;
    }

    /**
     * Determines if the given element belongs to this list.
     *
     * @param e the element
     * @return true if and only if <tt>e</tt> belongs to this list
     */

    public boolean contains(E e) {
	for (E ee: this) if (ee.equals(e)) return true;

	return false;
    }

    /**
     * Yields an iterator over the elements of this list, from the first
     * towards the last one.
     *
     * @return the iterator
     */

    public ListIterator<E> iterator() {
	return new ListIterator<E>(first);
    }

    /**
     * Yields the has code of this list.
     *
     * @return the hash code
     */

    public int hashCode() {
	int code = 0;
	for (E e: this) code += e.hashCode();

	return code;
    }

    /**
     * Determines if this list is equal to <tt>other</tt>.
     *
     * @param other the other list
     * @return true if and only if <tt>other</tt> is a list which has equal
     *         elements in the same position as this list
     */
 
    public boolean equals(Object other) {
	if (!(other instanceof List)) return false;

	List otherL = (List)other;

	if (size != otherL.size) return false;

	ListNode cursor1 = first, cursor2 = otherL.first;
	while (cursor1 != null) {
	    if (!cursor1.head.equals(cursor2.head)) return false;
	    cursor1 = cursor1.tail;
	    cursor2 = cursor2.tail;
	}

	return true;
    }

    /**
     * Yields a string describing this list.
     *
     * @return a comma-separated description of the elements of this list
     */

    public String toString() {
	ListNode<E> cursor = first;
	String result = "";

	while (cursor != null) {
	    if (cursor.tail != null) result += cursor.head + ",";
	    else result += cursor.head;
	    cursor = cursor.tail;
	}

	return result;
    }
}
