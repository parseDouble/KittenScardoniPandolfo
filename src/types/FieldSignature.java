package types;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.FieldGen;

import absyn.FieldDeclaration;
import symbol.Symbol;
import translate.CodeBlock;
import generateJB.KittenClassGen;

/**
 * The signature of a field of a Kitten class.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class FieldSignature extends ClassMemberSignature {

    /**
     * The type of the field.
     */

    private Type type;

    /**
     * The name of the field.
     */

    private Symbol name;

    /**
     * Constructs the signature of a field with the given type and name,
     * declared in the given class.
     *
     * @param clazz the class where this field is defined
     * @param type the type of the field
     * @param name the name of the field
     * @param abstractSyntax the abstract syntax of this field declaration
     */

    public FieldSignature
	(ClassType clazz, Type type, Symbol name,
	 FieldDeclaration abstractSyntax) {

	super(clazz,abstractSyntax);

	this.type = type;
	this.name = name;
    }

    public boolean equals(Object other) {
	if (other instanceof FieldSignature) {
	    FieldSignature otherF = (FieldSignature)other;

	    return otherF.getDefiningClass() == getDefiningClass() &&
		otherF.name == name && otherF.type == type;
	}
	else return false;
    }

    public int hashCode() {
	return getDefiningClass().hashCode()
	    + name.hashCode() + type.hashCode();
    }

    /**
     * Yields the type of the field.
     *
     * @return the type of the field
     */

    public Type getType() {
	return type;
    }

    /**
     * Yields the name of the field.
     *
     * @return the name of the field
     */

    public Symbol getName() {
	return name;
    }

    /**
     * Yields a <tt>String</tt> representation of the field, of the form
     * <i>Class.name</i>.
     *
     * @return a <tt>String</tt> representation of this field
     */

    public String toString() {
	return getDefiningClass() + "." + name + ":" + type;
    }

    /**
     * Generates a <tt>getfield</tt> Java bytecode that reads the
     * value of this field.
     *
     * @param classGen the class generator to be used to generate
     *                 the <tt>getfield</tt> Java bytecode
     * @return a <tt>getfield</tt> Java bytecode that reads the
     *         value of this field
     */

    public FieldInstruction createGETFIELD(KittenClassGen classGen) {
	// we use the instruction factory in order to simplify the
	// creation of the <tt>getfield</tt> Java bytecode. The factory
	// automatically puts in the constant pool a reference to the Java
	// signature of the field that is being read
	return classGen.getFactory().createGetField
	    (getDefiningClass().toBCEL().toString(),
	     name.toString(),type.toBCEL());
    }

    /**
     * Generates a <tt>putfield</tt> Java bytecode that writes a
     * value inside this field.
     *
     * @param classGen the class generator to be used to generate
     *                 the <tt>putfield</tt> Java bytecode
     * @return a <tt>putfield</tt> Java bytecode that writes a value
     *         inside this field
     */

    public FieldInstruction createPUTFIELD(KittenClassGen classGen) {
	// we use the instruction factory in order to simplify the
	// creation of the <tt>putfield</tt> Java bytecode. The factory
	// automatically puts in the constant pool a reference to the Java
	// signature of the field that is being written
	return classGen.getFactory().createPutField
	    (getDefiningClass().toBCEL().toString(),
	     name.toString(),type.toBCEL());
    }

    /**
     * Creates a Java bytecode field and adds it the the given class with the
     * given constant pool.
     *
     * @param classGen the generator of the class where the field lives
     */

    public void createField(KittenClassGen classGen) {
	classGen.addField(new FieldGen
			  (Constants.ACC_PUBLIC, // the field is public
			   getType().toBCEL(), // type
			   getName().toString(), // name
			   // constant pool where it must be stored
			   classGen.getConstantPool())
			  .getField());
    }
}
