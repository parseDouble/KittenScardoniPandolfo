package bytecode;

import javaBytecodeGenerator.JavaClassGenerator;

import org.apache.bcel.generic.InstructionList;

/**
 * A list of Kitten bytecodes.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class BytecodeList {

    /**
     * The bytecode contained at the head of this list.
     */

    private Bytecode head;

    /**
     * The next instruction of this list of bytecodes.
     * It might be <tt>null</tt> if this is the last element of the list.
     */

    private BytecodeList tail;

    /**
     * Constructs a list of bytecodes which starts with the
     * given bytecode and continues with the given list of bytecodes.
     *
     * @param head the first bytecode of the list to be constructed
     * @param tail the list of bytecodes that follows <tt>bytecode</tt>
     */

    public BytecodeList(Bytecode head, BytecodeList tail) {
    	this.head = head;
    	this.tail = tail;
    }

    /**
     * Constructs a list of bytecodes containing a single bytecode.
     *
     * @param head the only element of the list
     */

    public BytecodeList(Bytecode head) {
    	this(head, null);
    }

    /**
     * Yields the head of this list.
     *
     * @return the head of this list
     */

    public Bytecode getHead() {
    	return head;
    }

    /**
     * Yields the tail of this list.
     *
     * @return the tail of this list
     */

    public BytecodeList getTail() {
    	return tail;
    }

    /**
     * Computes the concatenation of this list of bytecodes
     * and another given one. <tt>nop</tt> instructions in the middle of the
     * list are removed.
     *
     * @param other the list of bytecodes which must be appended
     *              to <tt>this</tt>
     * @return the result of the concatenation of this list of
     *         bytecodes with <tt>other</tt>. The lists <tt>this</tt> and
     *         <tt>other</tt> are not modified
     */

    public BytecodeList append(BytecodeList other) {
    	if (other == null) return this;
    	else if (other.head instanceof NOP) return append(other.tail);
    	else if (tail == null) return new BytecodeList(head,other);
    	else return new BytecodeList(head,tail.append(other));
    }

    /**
     * Yields a <tt>String</tt> representation of this list of bytecodes.
     *
     * @return a <tt>String</tt> representation of this list of
     *         bytecodes. This just calls <tt>toString()</tt> on each
     *         bytecode and concatenates their results
     */

    @Override
    public String toString() {
    	String s = head.toString();
    	if (s.length() > 100) s = s.substring(0,100) + "...";

    	if (tail != null)
    		// if some bytecode yields the empty string, we do no print
    		// a new line. This can be useful for bytecodes which disappear
    		// from the print-out
    		if (s.length() > 0) return s + "\n" + tail.toString();
    		else return tail.toString();
    	else return s;
    }

    /**
     * Generates the Java bytecode corresponding to this list of
     * bytecodes. This just calls <tt>generateJB()</tt> on each
     * non branching bytecode in the list and appends the results.
     *
     * @param classGen the Java class generator to be used for this
     *                 Java bytecode generation
     * @return the Java bytecode corresponding to this list of bytecodes
     */

    public InstructionList generateJB(JavaClassGenerator classGen) {
    	InstructionList result;

    	if (head instanceof NonBranchingBytecode)
    		// we generate the Java bytecode for the first bytecode
    		// if it is not a condition of a branch
    		result = ((NonBranchingBytecode)head).generateJB(classGen);
    	else
    		result = new InstructionList();

    	// and for its followers, if any
    	if (tail != null) result.append(tail.generateJB(classGen));

    	// if we added no instruction, we add a fictitious one so that
    	// we never return an empty list, and an InstructionHandle to the
    	// first instruction always exists
    	if (result.isEmpty()) result.append(new org.apache.bcel.generic.NOP());

    	return result;
    }

    /**
     * Yields the last bytecode in this list.
     *
     * @return the last bytecode in this list
     */

    public Bytecode getLast() {
    	if (tail == null) return head;
    	else return tail.getLast();
    }
}