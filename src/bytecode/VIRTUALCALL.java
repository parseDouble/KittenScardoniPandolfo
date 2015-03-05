package bytecode;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.JavaClassGenerator;
import types.ClassType;
import types.CodeSignature;
import types.MethodSignature;

/**
 * A bytecode which calls a method of an object with dynamic lookup.
 * That object is the <i>receiver</i> of the call. If the receiver is
 * <tt>nil</tt>, the computation stops.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -> ..., returned value<br>
 * if the method return type is non-<tt>void</tt><br><br>
 * ..., receiver, par_1, ..., par_n -> ...<br>
 * if the method's return type is <tt>void</tt>
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class VIRTUALCALL extends CALL implements PointerDereferencer {

	/**
	 * Constructs a bytecode which calls a method of an object with dynamic
	 * lookup. The set of run-time targets is assumed to be that obtained
	 * from every subclass of the static type of the receiver.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param receiverType the static type of the receiver of this call
	 * @param staticTarget the signature of the static target of the call
	 */

	public VIRTUALCALL(CodeSignature where, ClassType receiverType, MethodSignature staticTarget) {
		// we compute the dynamic targets by assuming that the run-time
		// type of the receiver is any subclass of its static type
		super(where, receiverType, staticTarget, dynamicTargets(receiverType.getInstances(), staticTarget));
	}

	/**
	 * Yields the set of run-time receivers of this call. They are all
	 * methods with the same signature of the static target which might
	 * be called from a given set of run-time classes for the receiver.
	 *
	 * @param possibleRunTimeClasses the set of run-time classes for the receiver
	 * @param staticTarget the static target of the call
	 * @return the set of method signatures that might be called
	 *         with the given set of classes as receiver
	 */

	private static Set<CodeSignature> dynamicTargets(List<ClassType> possibleRunTimeClasses, CodeSignature staticTarget) {
		Set<CodeSignature> dynamicTargets = new HashSet<CodeSignature>();

		for (ClassType rec: possibleRunTimeClasses) {
			// we look up for the method from the dynamic receiver
			MethodSignature candidate = rec.methodLookup
					(staticTarget.getName(), staticTarget.getParameters());

			// we add the dynamic target. If it was already there, the set is not modified
			if (candidate != null) dynamicTargets.add(candidate);
		}

		return dynamicTargets;
	}

	/**
	 * Generates the Java bytecode corresponding
	 * to this Kitten bytecode. Namely, it generates an
	 * <tt>invokevirtual staticTarget</tt> Java bytecode.
	 * The Java <tt>invokevirtual</tt> bytecode calls a method by using
	 * the run-time class of the receiver to look up for the
	 * method's implementation.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>invokevirtual staticTarget</tt> bytecode
	 */

	@Override
	public InstructionList generateJB(JavaClassGenerator classGen) {
		// the <tt>MethodSignature</tt> <tt>staticTarget</tt> contains
		// everything which is needed in order to create
		// the Java <tt>invokevirtual staticTarget</tt> bytecode
		return new InstructionList
				(((MethodSignature)getStaticTarget()).createINVOKEVIRTUAL(classGen));
	}

	@Override
	public String description() {
		return "calling method " + getStaticTarget();
	}
}