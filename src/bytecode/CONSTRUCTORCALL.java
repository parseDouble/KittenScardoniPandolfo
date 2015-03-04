package bytecode;

import java.util.HashSet;

import org.apache.bcel.generic.InstructionList;

import bytecodeGenerator.KittenClassGen;
import types.CodeSignature;
import types.ConstructorSignature;

/**
 * A bytecode which calls a constructor of an object.
 * That object is the <i>receiver</i> of the call. If the receiver is
 * <tt>nil</tt>, the computation stops.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -> ...
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class CONSTRUCTORCALL extends CALL implements PointerDereferencer {

	/**
	 * Constructs a bytecode which calls a constructor of an object.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param constructor the signature of the constructor which is called
	 */

	public CONSTRUCTORCALL(CodeSignature where, ConstructorSignature constructor) {
		// there is only only dynamic target: the constructor itself
		super(where,constructor.getDefiningClass(), constructor, singleton(constructor));
	}

	/**
	 * Builds a singleton set containing the given target method.
	 *
	 * @param target the target method
	 * @return the singleton set conatining <tt>target</tt>
	 */

	private static HashSet<CodeSignature> singleton(CodeSignature target) {
		HashSet<CodeSignature> result = new HashSet<CodeSignature>();
		result.add(target);
		return result;
	}

	/**
	 * Generates the Java bytecode corresponding to this Kitten bytecode.
	 *
	 * @param classGen the Java class generator to be used for this
	 *                 Java bytecode generation
	 * @return the Java <tt>invokespecial method</tt> bytecode, which calls
	 *         a method by using a hard-wired class name to look up for the
	 *         method's implementation
	 */

	public InstructionList generateJB(KittenClassGen classGen) {
		// the <tt>ConstructorSignature</tt> <tt>constructor</tt>
		// contains everything which is needed in order to create
		// the Java <tt>invokespecial constuctor</tt> bytecode
		return new InstructionList
				(((ConstructorSignature)getStaticTarget())
						.createINVOKESPECIAL(classGen));
	}

	@Override
	public String description() {
		return "calling constructor " + getStaticTarget();
	}
}
