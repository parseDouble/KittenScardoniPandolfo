package types;

/**
 * A list of stack Kitten types. It is assumed that each type
 * uses as many elements as its size.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeList {

    /**
     * The empty list of types. This is used to terminate all lists of types,
     * so that no list of types is <tt>null</tt>.
     */

    public final static TypeList EMPTY = new TypeList(Type.UNUSED,null);

    /**
     * The head of the list.
     */

    private Type head;

    /**
     * The tail of the list.
     */

    private TypeList tail;

    /**
     * Builds a list of types.
     *
     * @param head the first type in the list
     * @param tail the tail of the list
     */

    private TypeList(Type head, TypeList tail) {
	this.head = head;
	this.tail = tail;
    }

    /**
     * Yields the first element of this list of types.
     *
     * @return the first element
     */

    public Type getHead() {
	return head;
    }

    /**
     * Yields the tail of this list of types.
     *
     * @return the tail
     */

    public TypeList getTail() {
	return tail;
    }

    /**
     * Computes a comma-separated string of the types in this list.
     *
     * @return the comma-separated string of the types in this list
     */

    public String toString() {
	TypeList cursor;
	String s = "";

	for (cursor = this; cursor != EMPTY; cursor = cursor.tail) {
	    s += cursor.head.toString();
	    if (cursor.tail != EMPTY) s += ",";
	}

	return s;
    }

    /**
     * Determines if this list of type is equal to another.
     *
     * @param other the other list of types
     * @return true if and only if this list of type is the same
     *              as <tt>other</tt>
     */

    public boolean equals(Object other) {
	TypeList otherTL = (TypeList)other, cursor = this;

	while (cursor != EMPTY && otherTL != EMPTY)
	    if (cursor.head != otherTL.head) return false;
	    else {
		cursor = cursor.tail;
		otherTL = otherTL.tail;
	    }

	// trailing unused elements do not affect the equality
	while (cursor != null && cursor.head == Type.UNUSED)
	    cursor = cursor.tail;
	while (otherTL != null && otherTL.head == Type.UNUSED)
	    otherTL = otherTL.tail;

	return cursor == otherTL;
    }

    /**
     * Yields the hash code of this list of types. This is consistent with
     * <tt>equals()</tt>.
     *
     * @return the hash code
     */

    public int hashCode() {
	TypeList cursor = this;
	Type t;
	int code = 0;

	for (int i = 0; cursor != EMPTY; cursor = cursor.tail, i++)
	    if ((t = cursor.head) != Type.UNUSED)
		code += (t.hashCode() << i);

	return code;
    }

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a list of values of the types contained in this list.
     *
     * @return the number of stack elements
     */

    public int getSize() {
	int s = 0;

	for (TypeList cursor = this; cursor != EMPTY; cursor = cursor.tail)
	    s++;

	return s;
    }

    /**
     * Checks if this list of types can be element-wise assigned to another.
     * This entails that they have the same length.
     *
     * @param others the other list of types
     * @return true if and only if this list of types has the same
     *         length as <tt>other</tt> and can be element-wise
     *         assigned to <tt>other</tt>. Returns false otherwise
     */

    public boolean canBeAssignedTo(TypeList others) {
	TypeList cursor = this;

	while (cursor != EMPTY && others != EMPTY)
	    if (!cursor.head.canBeAssignedTo(others.head)) return false;
	    else {
		cursor = cursor.tail;
		others = others.tail;
	    }

	return cursor == others;
    }

    /**
     * Yields a list of types equal to this but first <tt>count</tt> elements
     * have been removed.
     *
     * @param count the number of elements which must be removed from this list
     * @return the resulting list of types
     */

    private TypeList pop(int count) {
	TypeList result = this;

	while (count-- > 0) result = result.tail;

	// this should not happen
	//if (result == null) throw null;

	return result;
    }

    /**
     * Yields a list of types equal to this but whose head has been removed.
     *
     * @return the resulting list of types
     */

    public TypeList pop() {
	return pop(1);
    }

    /**
     * Yields a list of types equal to this without as many elements at its
     * beginning as the size of <tt>type</tt>.
     *
     * @param type the type which must be popped from this list of types
     * @return the resulting list of types
     */

    public TypeList pop(Type type) {
	pop2(type);
	return pop(type.getSize());
    }

    public TypeList pop2(Type type) {
	return pop(2);
    }

    /**
     * Yields a list of types equal to this without as many elements at its
     * beginning as the size of <tt>types</tt>.
     *
     * @param types the types which must be popped from this list of types
     * @return the resulting list of types
     */

    public TypeList pop(TypeList types) {
	return pop(types.getSize());
    }

    /**
     * Yields a list of types equal to this but beginning with the given type.
     *
     * @param type the type which must be pushed
     * @return the resulting list of types
     */

    public TypeList push(Type type) {
	TypeList result = this;
	int s = type.getSize();

	while (s-- > 0) result = new TypeList(type,result);

	return result;
    }

    /**
     * Yields the <tt>n</tt>th element in this list.
     *
     * @param n the index of the element which must be accessed
     * @return the <tt>n</tt>th element in this list. If the list is too short
     *         to contain the <tt>n</tt>th element, it yields
     *         <tt>Type.UNUSED</tt>
     */

    public Type getNth(int n) {
	TypeList cursor = this;

	while (n-- > 0 && cursor != EMPTY) cursor = cursor.tail;

	// we check if the list is too short
	if (cursor == EMPTY) return Type.UNUSED;
	else return cursor.head;
    }

    /**
     * Yields the first element in this list.
     *
     * @return the first element in this list. If the list is too short
     *         to contain a first element, it yields <tt>null</tt>
     */

    public Type getTop() {
	return getNth(0);
    }

    /**
     * Yields a list of types similar to <tt>this</tt> but
     * where the <tt>n</tt>th element has been changed into <tt>type</tt>
     *
     * @param n the index of the element which must be changed
     * @param type the type to be written at the <tt>n</tt>th position of
     *             this list
     * @return the resulting list of types. If the list <tt>this</tt> was too
     *         short to hold an <tt>n</tt>th element, the result is extended
     *         with as many <tt>UnusedType</tt>'s as needed.
     */

    public TypeList setNth(int n, Type type) {
	int s, ss;

	if (n == 0) {
	    TypeList result = this;

	    s = ss = type.getSize();
	    while (s-- > 0 && result != EMPTY) result = result.tail;

	    s = ss;
	    while (s-- > 0) result = new TypeList(type,result);

	    return result;
	}
	else
	    if (this == EMPTY)
		return new TypeList(Type.UNUSED,setNth(n - 1,type));
	    else
		return new TypeList(head,tail.setNth(n - 1,type));
    }

    /**
     * Computes an element-wise least upper bound of the types in this
     * list <i>wrt.</i> to the types in the <tt>other</tt> list.
     *
     * @param other the other list
     * @return the result of the least upper bound
     */

    public TypeList lub(TypeList other) {
	if (this == other) return this;
	else if (this == EMPTY) return other;
        else if (other == EMPTY) return this;
        else {
            Type h1 = head, h2 = other.head, lcs;

            // in the bytecode, two integral numerical types can be joined at
            // the same program point. Moreover, Booleans are seen as integers
            if (h1 == h2) lcs = h1;
	    else if (h1 instanceof IntegralType && h2 instanceof IntegralType)
                lcs = Type.INT;
            else if (h1 instanceof IntegralType && h2 == Type.BOOLEAN)
                lcs = Type.INT;
            else if (h1 == Type.BOOLEAN && h2 instanceof IntegralType)
		lcs = Type.INT;
            else
                lcs = h1.leastCommonSupertype(h2);

	    if (lcs == null) lcs = Type.UNUSED;

            return new TypeList(lcs,tail.lub(other.tail));
        }
    }

    /**
     * Merges element-wise this list of types with another.
     * If the other list contains unused elements, they are replaced by the
     * corresponding element in <tt>this</tt>. Otherwise, the element from
     * the other list is used.
     *
     * @param other the other list
     * @param diff the number of elements by which <tt>other</tt> mus be
     *             considered longer than <tt>this</tt>
     * @return the merged list
     */

    public TypeList merge(TypeList other, int diff) {
        if (diff > 0)
	    return new TypeList(other.head,merge(other.tail,diff - 1));
        else if (diff < 0)
            if (this == EMPTY) return other;
            else return tail.merge(other,diff + 1);
        else if (this == EMPTY) return other;
        else if (other == EMPTY) return this;
        else if (other.head == Type.UNUSED)
            return new TypeList(head,tail.merge(other.tail,0));
        else
            return new TypeList(other.head,tail.merge(other.tail,0));
    }

    /**
     * Converts this list of types into an array of BCEL types.
     *
     * @return an array of BCEL types corresponding to this list
     *         of Kitten types
     */

    public org.apache.bcel.generic.Type[] toBCEL() {
	TypeList cursor;
	int s, i;

	// we first count them :-(
	for (i = 0, cursor = this; cursor != TypeList.EMPTY; i++)
	    cursor = cursor.pop(cursor.head);

	org.apache.bcel.generic.Type[] result =
	    new org.apache.bcel.generic.Type[i];

	// then we translate each of them into the corresponding BCEL type
	for (i = 0, cursor = this; cursor != TypeList.EMPTY; i++) {
	    result[i] = cursor.head.toBCEL();	 
	    cursor = cursor.pop(cursor.head);
	}

	return result;
    }
}
