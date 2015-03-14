package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

import types.ClassType;

/**
 * A bytecode which creates an object of a given class and
 * pushes it on top of the stack. Note that no constructor is called
 * on the newly created object.
 * <br><br>
 * ... -> ..., new object
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEW extends NonCallingSequentialBytecode {

	/**
	 * The class type which is instantiated by this bytecode.
	 */

	private ClassType clazz;

	/**
	 * Constructs a bytecode which creates an object of a given class.
	 *
	 * @param clazz the class type which is instantiated by this bytecode
	 */

	public NEW(ClassType clazz) {
		this.clazz = clazz;
	}

	/**
	 * Yields the class which is instantiated by this bytecode.
	 *
	 * @return the class instantiated by this bytecode
	 */

	public ClassType getType() {
		return clazz;
	}

	@Override
	public String toString() {
		return "new " + clazz;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>new clazz</tt> bytecode
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		// we use the instruction factory to simplify the addition of the
		// class name into the constant pool
		return new InstructionList
				(classGen.getFactory().createNew(clazz.toBCEL().toString()));
	}
}