package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;

/**
 * A bytecode which creates an object of class <tt>String</tt> and
 * pushes it on top of the stack.
 * <br><br>
 * ... -> ..., new string
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class NEWSTRING extends NonCallingSequentialBytecode {

	/**
	 * The lexical value of the string that is created.
	 */

	private final String value;

	/**
	 * Constructs a bytecode which creates a <tt>String</tt> object.
	 *
	 * @param value the lexical value of the <tt>String</tt> which is created
	 */

	public NEWSTRING(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "newstring " + value.replaceAll("\n","\\\\\\\\n");
	}

	/**
	 * Generates the Java bytecode corresponding
	 * to this Kitten bytecode. Kitten <tt>String</tt>s are emulated through
	 * <tt>runTime.String</tt>
	 * wrappers of Java bytecode <tt>String</tt>s. This way, all methods
	 * over Kitten <tt>String</tt>s can be emulated through Java methods
	 * inside the <tt>runTime.String</tt> class (see there).
	 * Namely, this method generates the Java bytecode<br>
	 * <br>
	 * <tt>new runTime.String</tt><br>
	 * <tt>dup</tt><br>
	 * <tt>ldc value</tt><br>
	 * <tt>invokespecial runTime.String.&lt;init&gt;</tt><br>
	 * <br>
	 * which creates a <tt>runTime.String</tt> objects and initialises it with
	 * the lexical value <tt>value</tt> of the Kitten <tt>String</tt> we want
	 * to create.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return a Java bytecode that creates a <tt>runTime.String</tt>
	 *         object initialised with the lexical <tt>value</tt> of the
	 *         Kitten <tt>String</tt> we want to create.
	 */

	@Override
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen) {
		InstructionFactory factory = classGen.getFactory();
		InstructionList il = new InstructionList();

		// we create the <tt>invokespecial</tt>
		il.insert(factory.createInvoke
				("runTime.String", // class name of the method
						Constants.CONSTRUCTOR_NAME, // name of the method
						org.apache.bcel.generic.Type.VOID, // return type
						new org.apache.bcel.generic.Type[] // parameters types
								{ org.apache.bcel.generic.Type.getType
						("Ljava/lang/String;") },
						Constants.INVOKESPECIAL)); // invokespecial

		// we prepend the rest of the code
		il.insert(factory.createConstant(value));
		il.insert(new org.apache.bcel.generic.DUP());
		il.insert(factory.createNew("runTime.String"));

		return il;
	}
}