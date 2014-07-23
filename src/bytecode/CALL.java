package bytecode;

import java.util.HashSet;
import java.util.Set;

import types.CodeSignature;
import types.Type;

/**
 * A bytecode which calls a method of a <i>receiver</i>.
 * <br><br>
 * ..., receiver, par_1, ..., par_n -> ..., returned value<br>
 * if the method return type is non-<tt>void</tt><br><br>
 * ..., receiver, par_1, ..., par_n -> ...<br>
 * if the method's return type is <tt>void</tt>.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public abstract class CALL extends SequentialBytecode {

	/**
	 * The static type of the receiver of this call. The dynamic type
	 * might be this type or every subclass of this type.
	 */

	private Type receiverType;

	/**
	 * The signature of the static target method or constructor
	 * of the call. The dynamic target
	 * of the call might be every redefinition of this
	 * in <tt>receiverType</tt> and its subclasses.
	 */

	private CodeSignature staticTarget;

	/**
	 * The signatures of the dynamic target methods or constructors
	 * of this call. They are redefinitions of <tt>staticTarget</tt>
	 * in <tt>receiverType</tt> and its subclasses.
	 */

	private Set<CodeSignature> dynamicTargets;

	/**
	 * An overapproximation of the set of local variables that might be modified
	 * during the execution of one of the dynamic targets of this call.
	 */

	private HashSet<Integer> modifiedLocals;

	/**
	 * Constructs a bytecode which calls a method.
	 *
	 * @param where the method or constructor where this bytecode occurs
	 * @param receiverType the static type of the receiver of this call
	 * @param staticTarget the signature of the static target of the call
	 * @param dynamicTargets the set of dynamic targets for the call
	 */

    protected CALL(CodeSignature where,
    		Type receiverType, CodeSignature staticTarget,
    		Set<CodeSignature> dynamicTargets) {

    	super(where);

    	this.receiverType = receiverType;
    	this.staticTarget = staticTarget;
    	this.dynamicTargets = dynamicTargets;
    }

    /**
     * Yields the type of the receiver of this call.
     *
     * @return the type of the receiver of this call
     */

    public final Type getReceiverType() {
    	return receiverType;
    }

    /**
     * Yields the static target of this method call instruction.
     *
     * @return the static target
     */

    public CodeSignature getStaticTarget() {
    	return staticTarget;
    }

    /**
     * Yields the set of dynamic targets of this instruction.
     *
     * @return the set of dynamic targets
     */

    public Set<CodeSignature> getDynamicTargets() {
    	return dynamicTargets;
    }

    @Override
    public String toString() {
    	return "call " + staticTarget + " " + dynamicTargets;
    }

    protected int hashCode$0() {
    	return staticTarget.hashCode();
    }

    protected boolean equals$0(Object other) {
    	CALL otherCALL = (CALL)other;

    	return receiverType == otherCALL.receiverType &&
    			staticTarget == otherCALL.staticTarget &&
    			dynamicTargets.equals(otherCALL.dynamicTargets);
    }
}