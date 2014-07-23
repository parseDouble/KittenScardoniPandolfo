package types;

import org.apache.bcel.generic.ClassGen;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Collection;

import util.List;
import symbol.Symbol;

/**
 * The type of a class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class ClassType extends ReferenceType {

    /**
     * The name of this class.
     */

    private Symbol name;

    /**
     * The superclass of this class, if any.
     */

    private ClassType superclass;

    /**
     * The direct subclasses of this class, if any.
     */

    private List<ClassType> subclasses;

    /**
     * The set of instances of this class. This is a cache for the method
     * <tt>getInstances()</tt>.
     */

    private List<ClassType> instances;

    /**
     * A map from field symbols to their signature.
     */

    private HashMap<Symbol,FieldSignature> fields
	= new HashMap<Symbol,FieldSignature>();

    /**
     * The set of constructor signatures in this class.
     */

    private HashSet<ConstructorSignature> constructors
	= new HashSet<ConstructorSignature>();

    /**
     * A map from method symbols to the set of signatures of the methods
     * having that name. Because of overloading,
     * more than one method might have a given name.
     */

    private HashMap<Symbol,HashSet<MethodSignature>> methods
	= new HashMap<Symbol,HashSet<MethodSignature>>();

    /**
     * The node of the graph of reachable types that contains the set
     * of types that are reachable from the fields of this class.
     */

    private FieldNode reachFieldNode;

    /**
     * Constructs a class type with the given name.
     *
     * @param name the name of the class
     */

    protected ClassType(Symbol name) {
	// we record its name
	this.name = name;

	// there are no subclasses at the moment
	this.subclasses = new List<ClassType>();

	// we initialise the node that represents the set of all types
	// reachable from the fields of this type. At the beginning, it is empty
	reachFieldNode = new FieldNode(this);

	// all types reachable from the fields are also reachable from the class
	containsAllTypesReachableFromFieldsOf(this);
    }

    /**
     * Sets the superclass of this class type to the class type of
     * name <tt>superclassName</tt>.
     *
     * @param superclassName the name of the superclass
     */

    protected void addSuperclass(Symbol superclassName) {
	// we are a direct subclass of our superclass
	(superclass = make(superclassName)).subclasses.addFirst(this);

	// the types reachable from this class are also
	// reachable from its superclass
	getReachGraph().arc(getReachNode(),superclass.getReachNode());

	// the types reachable from the fields of the superclass are also
	// reachable from the fields of this class
	includeFieldsOf(superclass);
    }

    /**
     * Takes note that all types reachable from the fields of the given class
     * are also reachable from the fields of this class.
     *
     * @param clazz the given class
     */

    protected void includeFieldsOf(ClassType clazz) {
	getReachGraph().arc(clazz.reachFieldNode,reachFieldNode);
    }

    /**
     * Takes note that this class has a field of the given type.
     *
     * @param type the type
     */

    protected void hasFieldOfType(Type type) {
	getReachGraph().arc(type.getReachNode(),reachFieldNode);
    }

    /**
     * Creates a class type with the given name.
     *
     * @param name the name of the class type
     * @return the new class type with the given <tt>name</tt>
     */

    protected abstract ClassType make(Symbol name);

    /**
     * Yields the superclass of this class type, if any.
     *
     * @return the superclass of this class type.
     */

    public ClassType getSuperclass() {
	return superclass;
    }

    /**
     * Yields the direct subclasses of this class.
     *
     * @return the direct subclasses of this class, if any
     */

    public List<ClassType> getSubclasses() {
	return subclasses;
    }

    /**
     * Yields the name of this class.
     */

    public final Symbol getName() {
	return name;
    }

    /**
     * Yields a <tt>String</tt> representation of this class type, namely,
     * its name.
     *
     * @return the name of this class type, as a <tt>String</tt> object
     */

    public String toString() {
	return name.toString();
    }

    /**
     * Yields the node of the graph representing the set of types reachable
     * from the fields of this class.
     *
     * @return the node
     */

    protected FieldNode getReachFieldNode() {
	return reachFieldNode;
    }

    /**
     * Determines whether this class type can be assigned to a given type.
     * The latter must be a class type, and it must be a (non-necessarily
     * strict) superclass of this class.
     *
     * @param other what this class should be assigned to
     * @return true if the assignment is possible, false otherwise
     */

    public final boolean canBeAssignedTo(Type other) {
	return other instanceof ClassType && this.subclass((ClassType)other);
    }

    /**
     * Checks if this class is a (non-necessarily strict) subclass of another.
     *
     * @param other the other class
     * @return true if this class is a (non-necessarily strict) subclass
     *         of <tt>other</tt>, false otherwise
     */

    public boolean subclass(ClassType other) {
	return this == other ||
	    (superclass != null && superclass.subclass(other));
    }

   /**
     * Computes the least common supertype of a given type and this class type.
     * That is, a common supertype which is the least amongst all possible
     * supertypes.
     * <ul>
     * <li> If <tt>other</tt> is an array type, then class
     * type <tt>Object</tt> is returned;
     * <li> Otherwise, if <tt>other</tt> is a class type then the least common
     * superclass of this and <tt>other</tt> is returned;
     * <li> Otherwise, if <tt>other</tt> is a <tt>NilType</tt> or an
     * <tt>UnusedType</tt>, then <tt>this</tt> is returned;
     * <li> Otherwise, <tt>null</tt> is returned.
     * </ul>
     *
     * @param other the type whose least supertype with this class
     *              type must be found
     * @return the least common supertype of this class
     *         type and <tt>other</tt>,
     *         if it exists, <tt>null</tt> otherwise (for instance, there
     *         is no least common supertype between <tt>int</tt> and
     *         any class type)
     */

    public Type leastCommonSupertype(Type other) {
	// between a class type and an array type, the least common
	// supertype is Object
	if (other instanceof ArrayType) return getObjectType();
	else if (other instanceof ClassType) {
	    // we look in our superclasses for a superclass of <tt>other</tt>
	    for (ClassType cursor = this; cursor != null;
		 cursor = cursor.getSuperclass())
		if (other.canBeAssignedTo(cursor)) return cursor;

	    // last chance, always valid
	    return getObjectType();
	}
	// the supertype of a class type and <tt>null</tt> or an unused type
	// is the class type
	else if (other == Type.NIL || other == Type.UNUSED) return this;
	// otherwise, there is no common supertype
	else return null;
    }

    /**
     * Yields the set of strict and non-strict, direct and indirect
     * subclasses of this class.
     *
     * @return the set of strict and non-strict, direct and indirect
     *         subclasses of this class. This list is never empty
     *         since this class is always an instance of itself
     */

    public List<? extends ClassType> getInstances() {
	// we first check to see if we already computed the set of instances
	// of this class
	if (instances != null) return instances;

	// we add this class itself
	List<ClassType> result = new List<ClassType>(this);

	// we add the instances of our subclasses
	for (ClassType sub: subclasses) result.addAll(sub.getInstances());

	// we take note of the set of instances, so that we do not recompute it
	// next time
	return instances = result;
    }

    /**
     * Adds a field to this class. If a field with the given name
     * already existed, it is overwritten.
     *
     * @param name the name of the field
     * @param sig the signature of the field
     */

    public void addField(Symbol name, FieldSignature sig) {
	fields.put(name,sig);

	// the types reachable from the field are also
	// reachable from the fields of this class
	hasFieldOfType(sig.getType());
    }

    /**
     * Adds a constructor to this class. If a constructor with the given
     * signature already existed, it is overwritten.
     *
     * @param sig the signature of the constructor
     */

    public final void addConstructor(ConstructorSignature sig) {
	constructors.add(sig);
    }

    /**
     * Adds a method to this class. If a method with the given name
     * and signature already existed, it is overwritten.
     *
     * @param name the name of the method
     * @param sig the signature of the method
     */

    public final void addMethod(Symbol name, MethodSignature sig) {
	// we read all methods, in this class, with the given name
	HashSet<MethodSignature> set = methods.get(name);
	if (set == null)
	    methods.put(name,set = new HashSet<MethodSignature>());

	// we add this new method
	set.add(sig);
    }

    /**
     * Yields the set of fields of this class.
     *
     * @return the set of <tt>FieldSignature</tt>'s of this class
     */

    public HashMap<Symbol,FieldSignature> getFields() {
	return fields;
    }

    /**
     * Yields the set of constructors of this class.
     *
     * @return the set of <tt>ConstructorSignature</tt>'s of this class
     */

    public HashSet<? extends ConstructorSignature> getConstructors() {
	return constructors;
    }

    /**
     * Yields the set of methods of this class.
     *
     * @return the set of <tt>MethodSignature</tt>'s of this class
     */

    public HashMap<Symbol,HashSet<MethodSignature>> getMethods() {
	return methods;
    }

    /**
     * Looks up from this class for the signature of the field
     * with the given name, if any.
     *
     * @param name the name of the field to look up for
     * @return the signature of the field, as defined in this class or
     *         in one of its superclasses. Returns <tt>null</tt> if no
     *         such field has been found
     */

    public final FieldSignature fieldLookup(Symbol name) {
	FieldSignature result;

	// we first look in this signature
	if ((result = fields.get(name)) != null) return result;

	// otherwise we look in the signature of the superclass
	if (superclass == null) return null;
	else return superclass.fieldLookup(name);
    }

    /**
     * Looks up in this class for the signatures of all
     * constructors with parameters types compatible with those
     * provided, if any. It is guaranteed that in the resulting set no
     * constructor signature is more specific than another, that is, they are
     * not comparable.
     *
     * @param formals the types the formal parameters of the constructors
     *                should be more general of
     * @return the signatures of the resulting constructors.
     *         Returns an empty set if no constructor has been found
     */

    public final HashSet<ConstructorSignature> constructorsLookup
	(TypeList formals) {

	// we return the most specific constructors amongst those
	// available for this class and whose formal parameters are
	// compatible with <tt>formals</tt>
	return mostSpecific(constructors,formals);
    }

    /**
     * Looks up in this class for the signature of the constructor
     * with exactly the given parameters types, if any.
     *
     * @param formals the types of the formal parameters of the constructor
     * @return the signature of the constructor, as defined in this class.
     *         Returns <tt>null</tt> if no such constructor has been found
     */

    public ConstructorSignature constructorLookup(TypeList formals) {
	// we check all constructors in this class signature
	for (ConstructorSignature constructor: constructors)
	    // we check if they have the same parameters types
	    if (constructor.getParameters().equals(formals))
		// found!
		return constructor;

	// otherwise, we return <tt>null</tt>
	return null;
    }

    /**
     * Looks up from this class for the signature of the method
     * with exactly the given name and parameters types, if any.
     *
     * @param name the name of the method to look up for
     * @param formals the types of the formal parameters of the method
     * @return the signature of the method, as defined in this class or
     *         in one of its superclasses. Returns <tt>null</tt> if no
     *         such method has been found
     */

    public final MethodSignature methodLookup(Symbol name, TypeList formals) {
	// we check all methods in this signature having the given name
	HashSet<MethodSignature> candidates = methods.get(name);
	if (candidates != null)
	    for (MethodSignature method: candidates)
		// we check if they have the same parameters types
		if (method.getParameters().equals(formals))
		    // found!
		    return method;

	// otherwise, we look up in the superclass, if any
	if (superclass == null) return null;
	else return superclass.methodLookup(name,formals);
    }

    /**
     * Looks up from this class for the signatures of all methods
     * with the given name and parameters types compatible with those
     * provided, if any. It is guaranteed that in the resulting set no
     * method signature is more specific than another, that is, they are
     * not comparable.
     *
     * @param name the name of the method to look up for
     * @param formals the types the formal parameters of the methods
     *                should be more general of
     * @return the signatures of the resulting methods.
     *         Returns an empty set if no method has been found
     */

    public final HashSet<MethodSignature> methodsLookup
	(Symbol name, TypeList formals) {

	// the set of candidates is initially the set of all methods
	// called <tt>name</tt> and defined in this class
	HashSet<MethodSignature> candidates = methods.get(name);
	if (candidates == null) candidates = new HashSet<MethodSignature>();

	if (superclass != null) {
	    // if this class extends another class, we consider all possible
	    // candidate targets in the superclass, so that we allow method
	    // inheritance
	    HashSet<MethodSignature> superCandidates
		= superclass.methodsLookup(name,formals);

	    // we remove from the inherited candidates those which are
	    // redefined in this class, in order to model method overriding
	    HashSet<MethodSignature> toBeRemoved
		= new HashSet<MethodSignature>();

	    TypeList sigFormals, sig2Formals;
	    for (MethodSignature sig: superCandidates) {
		sigFormals = sig.getParameters();

		for (MethodSignature sig2: candidates) {
		    sig2Formals = sig2.getParameters();

		    if (sigFormals.equals(sig2Formals)) toBeRemoved.add(sig);
		}
	    }

	    superCandidates.removeAll(toBeRemoved);

	    // we add the inherited and not overridden candidates
	    candidates.addAll(superCandidates);
	}

	// we return the most specific methods amongst those called
	// <tt>name</tt> and whose formal parameters are
	// compatible with <tt>formals</tt>
	return mostSpecific(candidates,formals);
    }

    /**
     * Yields the subset of a set of code signatures whose parameters
     * are compatible with those provided and such that no two signatures in
     * the subset are one more general than the other.
     *
     * @param sigs the original set of code signatures
     * @param formals the parameters which are used to select the signatures
     * @return the subset of <tt>sigs</tt> whose parameters
     *         are compatible with <tt>formals</tt> and such that no two
     *         signatures in this subset are one more general than the other
     */

    private static <T extends CodeSignature> HashSet<T> mostSpecific
	(HashSet<T> sigs, TypeList formals) {

	HashSet<T> result = new HashSet<T>();
	HashSet<T> toBeRemoved = new HashSet<T>();

	// we scan all codes of this class
	for (T sig: sigs)
	    // if the parameters of <tt>sig</tt> are compatible with
	    // <tt>formals</tt>, we add it to the set of candidates
	    if (formals.canBeAssignedTo(sig.getParameters())) result.add(sig);

	// we remove a candidate if it is less general than another
	for (T sig: result)
	    for (T sig2: result)
		if (sig != sig2 &&
		    sig.getParameters().canBeAssignedTo(sig2.getParameters()))
		    toBeRemoved.add(sig2);

	result.removeAll(toBeRemoved);

	return result;
    }

    /**
     * Translates a Kitten type into its BCEL equivalent. It generates an
     * <tt>org.apache.bcel.generic.ObjectType</tt> for the name of this class.
     * For <tt>String</tt>, it generates one for <tt>runTime.String</tt>.
     *
     * @return the BCEL type corresponding to this Kitten type
     */

    public final org.apache.bcel.generic.Type toBCEL() {
	// we transform "String" into "runTime.String"
	if (name == Symbol.STRING)
	    return new org.apache.bcel.generic.ObjectType("runTime.String");
	else
	    return new org.apache.bcel.generic.ObjectType(name.toString());
    }

   /**
     * Yields a <tt>String</tt> which describes the <i>structure</i>
     * of this class, such as its superclass, subclasses, field, constructors
     * and methods.
     *
     * @return a <tt>String</tt> providing name, superclass and subclasses
     *         of this class, as well as its fields, constructors and methods
     */

    public String structure() {
	String result = "class " + name + "\n" +
	    "  fields: " + fields.values() + "\n" +
	    "  constructors: " + constructors + "\n" +
	    "  methods: ";

	// we merge the set of methods
	HashSet<MethodSignature> union = new HashSet<MethodSignature>();
	for (HashSet<MethodSignature> ms: methods.values()) union.addAll(ms);

	return result + union;
    }

    /**
     * Auxiliary method that
     * checks if this type is <i>cyclical</i>, that is, a value of the same
     * type can be reached from it or it can reach a cyclical type.
     *
     * @return true if and only if the superclass of this class is cyclical or
     *         one of the types of the fields of this class is cyclical or
     *         if the set of reachable types from the fields of
     *         this class (even inherited) or of one of its subclasses
     *         contain a subclass this type
     */

    protected boolean isCyclical$0() {
	ClassType cursor;

	for (cursor = superclass; cursor != null; cursor = cursor.superclass)
	    for (FieldSignature field: cursor.getFields().values())
		if (this.isReachableFrom(field.getType())) return true;

	for (ClassType ins: getInstances())
	    for (FieldSignature field: ins.getFields().values())
		if (this.isReachableFrom(field.getType())) return true;

	for (cursor = superclass; cursor != null; cursor = cursor.superclass)
	    for (FieldSignature field: cursor.getFields().values())
		if (field.getType().isCyclical()) return true;

	for (ClassType ins: getInstances())
	    for (FieldSignature field: ins.getFields().values())
		if (field.getType().isCyclical()) return true;

	return false;
    }

    /**
     * Checks if this class is a <i>one-selector data structure</i>, that is,
     * a data structure with at most one field of reference type. Note that
     * the fields of all subclasses are considered also.
     *
     * @return true if and only if this is a one-selector data structure
     */

    public boolean isOneSelector() {
	int count = 0;

	for (ClassType cl: getInstances())
	    for (FieldSignature f: cl.getFields().values())
		if (f.getType() instanceof ReferenceType) {
		    if (count > 0) return false;
		    else count++;
		}

	return true;
    }
}