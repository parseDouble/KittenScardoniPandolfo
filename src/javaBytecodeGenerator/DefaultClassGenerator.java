package javaBytecodeGenerator;

import java.util.Set;

import org.apache.bcel.Constants;


import types.ClassMemberSignature;
import types.ClassType;
import types.ConstructorSignature;
import types.FieldSignature;
import types.MethodSignature;

@SuppressWarnings("serial")
public class DefaultClassGenerator extends JavaClassGenerator {
	
	/**
	 * Builds a class generator for the given class type.
	 *
	 * @param clazz the class type
	 * @param sigs a set of class member signatures. These are those that must be translated
	 */
	public DefaultClassGenerator(ClassType clazz, Set<ClassMemberSignature> sigs) {
		super(clazz.getName(), clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "java.lang.Object",
				Constants.ACC_PUBLIC);
		// we add the fields
		for (FieldSignature field: clazz.getFields().values())
			if (sigs.contains(field))
				field.createField(this);

		// we add the constructors
		for (ConstructorSignature constructor: clazz.getConstructors())
			if (sigs.contains(constructor))
				constructor.createConstructor(this);

		// we add the methods
		for (Set<MethodSignature> s: clazz.getMethods().values())
			for (MethodSignature method: s)
				if (sigs.contains(method))
					method.createMethod(this);
	}

}
