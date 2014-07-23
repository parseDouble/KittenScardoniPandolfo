package types;

import java.util.HashMap;
import java.util.Map;

/**
 * A (mono-dimensional) array type of the Kitten language.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ArrayType extends ReferenceType {

	/**
	 * A map from elements type to the unique array type
	 * for that elements type.
	 * It is used in order to avoid duplication of array types for
	 * the same elements type. In this way, comparison of array types
	 * can be performed through simple == tests.
	 */

	private static Map<Type, ArrayType> memory = new HashMap<>();

	/**
	 * The type of the elements of the array.
	 */

	private Type elementsType;

	/**
	 * Builds an array type for the given type of elements.
	 *
	 * @param elementsType the type of the elements of the array
	 */

	private ArrayType(Type elementsType) {
		this.elementsType = elementsType;
	}

	/**
	 * Returns the unique <tt>ArrayType</tt> object
	 * with the given elements type.
	 *
	 * @param elementsType the type of the elements of the array
	 * @return the unique <tt>ArrayType</tt> with that type of elements
	 */

	public static ArrayType mk(Type elementsType) {
		ArrayType result = memory.get(elementsType);
		if (result != null) return result;

		result = new ArrayType(elementsType);
		memory.put(elementsType,result);

		return result;
	}

	/**
	 * Returns the unique <tt>ArrayType</tt> object for the given elements
	 * type and dimensions.
	 *
	 * @param elementsType the type of the elements of the array
	 * @return the unique <tt>ArrayType</tt> with elements of type
	 *         <tt>elementsType</tt> and dimensions <tt>dimensions</tt>
	 */

	public static ArrayType mk(Type elementsType, int dimensions) {
		if (dimensions == 1) return mk(elementsType);
		else return ArrayType.mk(ArrayType.mk(elementsType,dimensions - 1));
	}

	/**
	 * Returns the type of the elements of this array.
	 *
	 * @return the type of the elements of this array
	 */

	public Type getElementsType() {
		return elementsType;
	}

	/**
	 * Yields a <tt>String</tt> representation of this array type.
	 *
	 * @return the <tt>String</tt> representation of the type of the elements
	 *         of this array followed by the <tt>String</tt> <tt>[]</tt>
	 */

	public String toString() {
		return elementsType + "[]";
	}

	/**
	 * Determines whether this array type can be assigned to a given type.
	 * An array type can be assigned to another array type provided their
	 * elements types are the same, or rather those of <tt>other</tt>
	 * are superclasses of those of <tt>this</tt>. Note that
	 * if the elements are primitive types, they must be <i>the same type</i>.
	 * We also allow every array to be assigned to <tt>Object</tt>.
	 *
	 * @param other what this type should be assigned to
	 * @return true if the assignment is possible, false otherwise
	 */

	public boolean canBeAssignedTo(Type other) {
		if (other instanceof ArrayType)
			return elementsType.canBeAssignedToSpecial
					(((ArrayType)other).elementsType);
		else return other == getObjectType();
	}

	/**
	 * Computes the least common supertype of a given type and this array type.
	 * That is, a common supertype which is the least amongst all possible
	 * supertypes.
	 * <ul>
	 * <li> If <tt>other</tt> is a class type, then class type
	 * <tt>Object</tt> is returned;
	 * <li> Otherwise, if <tt>other</tt> is an array of primitive types and
	 * <tt>this</tt> is not the same array type, <tt>Object</tt> is returned;
	 * <li> Otherwise, if <tt>other</tt> is an array type and the least
	 * common supertype <i>lcs</i> between its elements and the elements
	 * of <tt>this</tt> exists, an array type of <i>lcs</i> is returned;
	 * <li> Otherwise, if <tt>other</tt> is an array type, <tt>Object</tt> is
	 * returned;
	 * <li> Otherwise, if <tt>other</tt> is a <tt>NilType</tt> or an
	 * <tt>UnusedType</tt>, then <tt>this</tt> is returned;
	 * <li> Otherwise, <tt>null</tt> is returned.
	 * </ul>
	 *
	 * @param other the type whose least supertype with this array
	 *              type must be found
	 * @return the least common supertype of this array
	 *         type and <tt>other</tt>,
	 *         if it exists, <tt>null</tt> otherwise (for instance, there
	 *         is no least common supertype between <tt>int</tt> and
	 *         an array of <tt>boolean</tt>)
	 */

	public Type leastCommonSupertype(Type other) {
		// between array and class, the least common supertype is Object
		if (other instanceof ClassType) return getObjectType();
		else if (other instanceof ArrayType)
			// an array of primitive types can only be compared with itself.
			// Otherwise, the least common supertype is Object
			if (elementsType instanceof PrimitiveType)
				if (this == other) return this;
				else return getObjectType();
			else {
				Type lcs = elementsType.leastCommonSupertype 
						(((ArrayType)other).elementsType);


				if (lcs == null) return getObjectType();
				else return mk(lcs);
			}

		// the least common supertype of an array and <tt>null</tt>
		// or an <tt>UnusedType</tt> is the array
		if (other == Type.NIL || other == Type.UNUSED) return this;

		// no common supertype exists
		return null;
	}

	public org.apache.bcel.generic.Type toBCEL() {
		return new org.apache.bcel.generic.ArrayType(elementsType.toBCEL(),1);
	}
}