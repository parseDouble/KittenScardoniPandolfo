package symbol;

/**
 * A non-empty symbol table. It is organised as a binary search tree.
 *
 * @author  <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

class NonEmptyTable extends Table {
    /**
     * the key on top of the tree.
     */

    private Symbol key;

    /**
     * the value bound on key at the top of the tree.
     */

    private Object value;

    /**
     * the left subtree.
     */

    private Table left;

    /**
     * the right subtree.
     */

    private Table right;

    /**
     * Builds a non-empty table.
     *
     * @param key the key in the root of the tree
     * @param value the value bound to <tt>key</tt>
     * @param left the left subtree
     * @param right the right subtree
     */

    private NonEmptyTable(Symbol key, Object value, Table left, Table right) {
	this.key = key;
	this.value = value;
	this.left = left;
	this.right = right;
    }

    /**
     * Builds a non-empty table having empty subtrees.
     *
     * @param key the key in the root of the tree
     * @param value the value bound to <tt>key</tt>
     */

    NonEmptyTable(Symbol key, Object value) {
	this(key,value,EMPTY,EMPTY);
    }

    public Object get(Symbol key) {
	int comp = this.key.compareTo(key);

	if (comp < 0) return left.get(key);
	else if (comp == 0) return value;
	else return right.get(key);
    }

    public Table put(Symbol key, Object value) {
	Table temp;
	int comp = this.key.compareTo(key);

	if (comp < 0) {
	    temp = left.put(key,value);
	    if (temp == left) return this;
	    else return new NonEmptyTable(this.key,this.value,temp,right);
	}
	else if (comp == 0)
	    if (value == this.value) return this;
	    else return new NonEmptyTable(this.key,value,left,right);
	else {
	    temp = right.put(key,value);
	    if (temp == right) return this;
	    else return new NonEmptyTable(this.key,this.value,left,temp);
	}
    }
}

