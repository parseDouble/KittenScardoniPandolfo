package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;

public class ERRORSTRING extends NEWSTRING {

	/**
	 * Constructs a bytecode that invoke method toString on a error String.
	 *
	 * @param the error message
	 */
	public ERRORSTRING(String value) {
		super(value);	
	}
	
	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 * 
	 * @param classGen the Java class generator to be used for this generation
	 * @return the Java bytecode for invoke toString
	 */
	public InstructionList generateJavaBytecode(JavaClassGenerator classGen){
		InstructionFactory inf= classGen.getFactory();
		InstructionList il=super.generateJavaBytecode(classGen);
		
		String str=runTime.String.class.getName();
		il.append(inf.createInvoke(str, "toString", Type.STRING, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		
		
		return il;
	}

}
