package types;

import util.BitSet;
import util.BitSetFactory;
import util.graph.Node;
import util.graph.Graph;
import symbol.Symbol;
import java.util.HashSet;

/**
 * A type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class Type {

    /**
     * A factory to generate sets of bitsets of types.
     */

    private final static BitSetFactory<Type> factory
	= new BitSetFactory<Type>();

    /**
     * A graph of nodes representing the set of reachable types for each type.
     */

    private final static Graph<Type> reachGraph = new Graph<Type>();

    /**
     * The node of the graph of reachable types that contains the set of all
     * types ever generated.
     */

    private final static AllNode allTypes = new AllNode();

    /**
     * The Boolean type. Always use this constant to refer to the Boolean type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static BooleanType BOOLEAN = new BooleanType();

    /**
     * The float type. Always use this constant to refer to the float type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static FloatType FLOAT = new FloatType();

    /**
     * The integer type. Always use this constant to refer to the integer type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static IntType INT = new IntType();

    /**
     * The nil type. Always use this constant to refer to the nil type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static NilType NIL = new NilType();

    /**
     * The void type. Always use this constant to refer to the void type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static VoidType VOID = new VoidType();

    /**
     * The unused type. Always use this constant to refer to the unused type,
     * so that type comparison can be carried out by simple == tests.
     */

    public final static UnusedType UNUSED = new UnusedType();

    /**
     * The top of the hierarchy of the reference types.
     */

    private static ClassType objectType;

    /**
     * A set of types that share with this class. This is used in
     * the method canShareWith.
     */

    private final HashSet<Type> cacheShare = new HashSet<Type>();

    /**
     * A set of types that do not share with this class. This is used in
     * the method canShareWith.
     */

    private final HashSet<Type> cacheDoNotShare = new HashSet<Type>();

    /**
     * A cache of last call to <tt>isCyclical()</tt>. It holds
     * 0 if <tt>isCyclical()</tt> has never been called, -1 if last call
     * has found this type to be non-cyclical and 1 if it has found it to be
     * cyclical.
     */

    private int isCyclical;

    /**
     * The node of the graph of reachable types that contains the set
     * of types that are reachable from this type.
     */

    private Node<Type> reachNode;

    /**
     * Builds a type object.
     */

    protected Type() {
	this.reachNode = createReachNode();

	// this is one of the types ever created
	reachGraph.arc(reachNode,allTypes);
    }

    /**
     * Yields the node that contains the set of all types reachable from
     * this type.
     *
     * @return a node initially containing this type only. Subclasses may
     *         redefine
     */

    protected Node<Type> createReachNode() {
	// we initialise the node that represents the set of all types
	// reachable from this type. At the beginning, it will contain
	// this type only
	return new TypeNode(this);
    }

    /**
     * Yields the set of all types ever generated.
     *
     * @return the set of all types ever generated
     */

    public final static BitSet<Type> allTypes() {
	return allTypes.getApprox();
    }

    /**
     * Yields the node of the graph of types representing the set of
     * all types ever created.
     *
     * @return the node
     */

    protected final static AllNode getAllNode() {
	return allTypes;
    }

    /**
     * Yields the top type of the hierarchy of the reference types.
     *
     * @return the top type
     */

    public static final ClassType getObjectType() {
	return objectType;
    }

    /**
     * Sets the top type of the hierarchy of the reference types.
     */

    protected static final void setObjectType(ClassType objectType) {
	Type.objectType = objectType;
    }

    /**
     * Yields the factory used to build bitsets of types.
     *
     * @return the factory used to build bitsets of types
     */

    public static BitSetFactory<Type> getFactory() {
	return factory;
    }

    /**
     * The number of stack elements used on the Kitten abstract machine
     * to hold a value of this type. This is always 1 for Kitten types which
     * are not <tt>void</tt>.
     *
     * @return 1
     */

    public int getSize() {
	return 1;
    }

    /**
     * Yields the set of types that can be reached from this type.
     *
     * @return the set of types reachable from this type
     */

    public BitSet<Type> getReachable() {
	return reachNode.getApprox();
    }

    /**
     * Yields the node representing the set of types reachable from this type.
     *
     * @return the node
     */

    protected Node<Type> getReachNode() {
	return reachNode;
    }

    /**
     * Yields a graph of nodes representing the reachable types for each type.
     *
     * @return the graph
     */

    protected static Graph<Type> getReachGraph() {
	return reachGraph;
    }

    /**
     * Takes note that the types reachable from this type include all the
     * types reachable from the fields of a given class.
     *
     * @param clazz the class
     */

    protected void containsAllTypesReachableFromFieldsOf(ClassType clazz) {
	reachGraph.arc(clazz.getReachFieldNode(),reachNode);
    }

    /**
     * Computes a string representation of the type.
     *
     * @return the string representing the type
     */

    abstract public String toString();

    /**
     * Determines whether this type can be assigned to a given type.
     * Type <tt>void</tt> cannot be assigned to any type, not even
     * to itself.
     *
     * @param other what this type should be assigned to
     * @return true if the assignment is possible, false otherwise
     */

    abstract public boolean canBeAssignedTo(Type other);

    /**
     * Determines whether this is a (non-strict) subtype of a given type.
     * It is exactly the same as <tt>canBeAssignedTo</tt>.
     *
     * @param other what this type should be a (non-strict) subtype of
     * @return true if this type is a (non-strict) subtype of <tt>other</tt>,
     *         false otherwise
     */

    public final boolean subtypeOf(Type other) {
	return canBeAssignedTo(other);
    }

    /**
     * Determines whether this type can be assigned to a given type, by
     * assuming that primitive types can only be assigned to the same,
     * identical, primitive type. Moreover, it assumes that <tt>void</tt>
     * is a subtype of <tt>void</tt>.
     *
     * @param other what this type should be assigned to
     * @return true if the assignment is possible, false otherwise
     */

    public boolean canBeAssignedToSpecial(Type other) {
	// primitive types should redefine this
	return canBeAssignedTo(other);
    }

    /**
     * Computes the least common supertype of a given type and this type.
     * That is, a common supertype which is the least amongst all possible
     * supertypes.
     *
     * @param other the type whose least supertype with this type must be found
     * @return the least common supertype of this type and <tt>other</tt>,
     *         if it exists, <tt>null</tt> otherwise (for instance, there
     *         is no least common supertype between <tt>boolean</tt> and
     *         <tt>int</tt>)
     */

    public Type leastCommonSupertype(Type other) {
	// the  least upper bound with an unused type is <tt>this</tt>
	if (other == Type.UNUSED) return this;

	// this works fine for primitive types. Class and array types
	// will have to redefine this behaviour
	if (this.canBeAssignedTo(other)) return other;
	if (other.canBeAssignedTo(this)) return this;

	// there is no least common supertype
	return null;
    }

    /**
     * Checks equality of two types.
     *
     * @param other the type to be compared with this type
     * @return true if this type is the same as <tt>other</tt>, false otherwise
     */

    public final boolean equals(Type other) {
	// this is fine since types can only be created from the constants above
	return this == other;
    }

    /**
     * Checks if this type <i>shares</i> with another type, <i>i.e.</i>,
     * if there is a class or array type <i>t1</i> reachable from
     * <tt>this</tt> and a class or array type <i>t2</i> reachable from
     * <tt>other</tt> such that <i>t1</i> can be assigned to <i>t2</i> or
     * vice versa. If at least one between <tt>this</tt> and <tt>other</tt>
     * is an open type, this method returns <tt>true</tt>, conservatively.
     *
     * @param other the other type
     * @return true if and only if this type <i>shares</i> with <tt>other</tt>
     */

    public final boolean canShareWith(Type other) {
	// we first check if we already checked this
	if (cacheShare.contains(other)) return true;
	if (cacheDoNotShare.contains(other)) return false;

	// we consider the set of types reachable from <tt>this</tt>
	// and of those reachable from <tt>other</tt>
	for (Type t1: this.getReachable()) {
	    // we look for class and array types only
	    if (!(t1 instanceof ReferenceType)) continue;

	    for (Type t2: other.getReachable()) {
		// we look for class and array types only
		if (!(t2 instanceof ReferenceType)) continue;

		if (t1.subtypeOf(t2) || t2.subtypeOf(t1)) {
		    // we found what we were looking for: the types
		    // share and we take note in the caches
		    cacheShare.add(other);
		    other.cacheShare.add(this);

		    return true;
		}
	    }
	}

	// the types do not share. We take note in the caches
	cacheDoNotShare.add(other);
	other.cacheDoNotShare.add(this);

	//if (this instanceof ReferenceType && other instanceof ReferenceType)
	//  System.out.println(this + " vs " + other + " :no");

	return false;
    }

    /**
     * Checks if this type is <i>cyclical</i>, that is, a value of the same
     * type can be reached from it or it can reach a cyclical type.
     * If first consults a cache for the previous call.
     *
     * @return true if and only if this type is cyclical
     */


    public final boolean isCyclical() {
	if (isCyclical == -1) return false;
	else if (isCyclical == 1) return true;
	else {
	    boolean result;

	    isCyclical = (result = isCyclical$0()) ? 1 : -1;

	    //if (!result) System.out.println("non-cyclical: " + this);
	    //else System.out.println("cyclical: " + this);

	    return result;
	}
    }

    /**
     * Auxiliary method that
     * checks if this type is <i>cyclical</i>, that is, a value of the same
     * type can be reached from it or it can reach a cyclical type.
     *
     * @return <tt>false</tt>. Subclasses may redefine
     */

    protected boolean isCyclical$0() {
	return false;
    }

    /**
     * Determines if <tt>this</tt> can be <i>reached</i> from <tt>other</tt>.
     *
     * @param other the other type
     * @return true if and only if <tt>this</tt> can be <i>reached</i>
     *         from <tt>other</tt>
     */

    public boolean isReachableFrom(Type other) {
	// we first try an inexpensive test
	if (other.getReachable().contains(this)) return true;

	// otherwise we apply the full test
	for (Type t: other.getReachable()) if (this.subtypeOf(t)) return true;

	return false;
    }

    /**
     * Translates a Kitten type into its BCEL equivalent.
     *
     * @return the BCEL type corresponding to this Kitten type
     */

    public abstract org.apache.bcel.generic.Type toBCEL();

    /**
     * Yields the Kitten type equivalent to the given BCEL type.
     *
     * @param type the BCEL type
     * @return the Kitten type equivalent to <tt>type</tt>
     */

    public Type fromBCEL(org.apache.bcel.generic.Type type) {
	if (type == org.apache.bcel.generic.Type.VOID)
	    return Type.VOID;
	else if (type == org.apache.bcel.generic.Type.INT)
	    return Type.INT;
	else if (type == org.apache.bcel.generic.Type.FLOAT)
	    return Type.FLOAT;
	else if (type == org.apache.bcel.generic.Type.BOOLEAN)
	    return Type.BOOLEAN;
	else if (type instanceof org.apache.bcel.generic.ObjectType)
	    return KittenClassType.mk(Symbol.mk(type.toString()));
	else {
	    // it must be an array type
	    org.apache.bcel.generic.ArrayType at =
		(org.apache.bcel.generic.ArrayType)type;

	    // Kitten array types have only one dimension
	    ArrayType result = ArrayType.mk(fromBCEL(at.getBasicType()));

	    for (int dim = at.getDimensions(); dim > 1; dim--)
		result = ArrayType.mk(result);

	    return result;
	}
    }

    /**
     * Yields the list of Kitten types equivalent to the given array of
     * BCEL types, as it would be allovcated on the stack of the Kitten
     * Virtual Machine.
     *
     * @param types the array of BCEL type
     * @return the list of Kitten types equivalent to <tt>types</tt>
     */

    public final TypeList fromBCEL(org.apache.bcel.generic.Type[] types) {
	TypeList result = TypeList.EMPTY;

	for (int pos = types.length - 1; pos >= 0; pos--)
	    result = result.push(fromBCEL(types[pos]));

	return result;
    }
}

