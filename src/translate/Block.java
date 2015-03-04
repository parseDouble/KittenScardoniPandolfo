package translate;

import java.util.HashSet;

import types.CodeSignature;
import util.List;
import bytecode.BranchingBytecode;
import bytecode.Bytecode;
import bytecode.BytecodeList;
import bytecode.CALL;
import bytecode.FinalBytecode;
import bytecode.NOP;
import bytecode.SequentialBytecode;

/**
 * A block of code of the Kitten intermediate language. There is no jump
 * to or from internal points of the code inside the block.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class Block {

    /**
     * The successors of this block. This should not be <tt>null</tt>.
     */

    private List<Block> follows;

    /**
     * The bytecode inside this block.
     */

    private BytecodeList bytecode;

    /**
     * The unique identifier of this block.
     */

    private int id;

    /**
     * True if this block can be merged when prefixed with another block.
     */

    private boolean mergeable;

    /**
     * The number of blocks created so far.
     */

    private static int counter = 0;

    /**
     * Builds a block of code with no predecessors and with the
     * given bytecode and successors.
     *
     * @param bytecode the code inside the block
     * @param follows the list of successors of this block
     */

    public Block(BytecodeList bytecode, List<Block> follows) {
	this.bytecode = bytecode;
	this.follows = follows;
	this.mergeable = true;

	// we assign a unique identifier to this block
	this.id = counter++;
    }

    /**
     * Builds a block of code containing a final bytecode and having no
     * predecessors nor successors.
     *
     * @param bytecode the final bytecode
     */

    public Block(FinalBytecode bytecode) {
	this(new BytecodeList(bytecode),new List<Block>());
    }

    /**
     * Builds a block of code containing <tt>nop</tt>, with no predecessors
     * and two successors. The branching to the two successors is decided
     * on the basis of a Boolean <tt>condition</tt>. The <tt>condition</tt>
     * and its negation are prefixed to the code of the two following blocks.
     *
     * @param condition the branching bytecode which decides the branching
     * @param yes the block of code to be executed if the <tt>condition</tt>
     *            holds. This should not be <tt>null</tt>
     * @param no the block of code to be executed if the <tt>condition</tt>
     *           does not hold. This should not be <tt>null</tt>
     */

    public Block(BranchingBytecode condition, Block yes, Block no) {
	this(new BytecodeList(new NOP(condition.getWhere())),
	     new List<Block>());

	// we prefix the <tt>condition</tt> and its negation to the
	// code of the following blocks
	follows.addFirst(no.prefixedBy(condition.negate()));
	follows.addFirst(yes.prefixedBy(condition));
    }

    /**
     * Builds a block of code containing <tt>nop</tt> and with no
     * successors nor predecessors.
     *
     * @param where the method or constructor where the block will be put
     */

    public Block(CodeSignature where) {
	// we use <tt>nop</tt> for the initial code of a pivot
	this(new BytecodeList(new NOP(where)),new List<Block>());

	// a pivot cannot be merged, otherwise cycles cannot be built
	mergeable = false;
    }

    /**
     * Builds a new block of code with no predecessors, no successors and with
     * the given bytecode.
     *
     * @param bytecode the code inside the block
     */

    public Block(BytecodeList bytecode) {
	this(bytecode,new List<Block>());
    }

    /**
     * Builds a new block of code with no predecessors and with the
     * given bytecode and successor.
     *
     * @param bytecode the code inside the block
     * @param follow the only successor of this block
     */

    public Block(BytecodeList bytecode, Block follow) {
	this(bytecode,new List<Block>(follow));
    }

    /**
     * Builds a new block of code with the given sequential bytecode,
     * only one successor and no predecessors.
     *
     * @param bytecode the code inside this block
     * @param follow the unique successor of this block. This should
     *               not be <tt>null</tt>
     */

    public Block(SequentialBytecode bytecode, Block follow) {
	this(new BytecodeList(bytecode),new List<Block>(follow));
    }

    /**
     * Yields the unique identifier of this block.
     *
     * @return the unique identifier of this block
     */

    public int getId() {
	return id;
    }

    /**
     * Yields the successors of this block.
     *
     * @return the list <tt>follows</tt>
     */

    public List<Block> getFollows() {
	return follows;
    }

    /**
     * Adds a successor to this block.
     *
     * @param follow the successor to be added to this block.
     *               This should not be <tt>null</tt>
     */

    public void linkTo(Block follow) {
	follows.addFirst(follow);
    }

    /**
     * Specifies that this block cannot be merged when prefixed with
     * a bytecode. See <tt>prefixedBy()</tt>.
     */

    public void doNotMerge() {
	mergeable = false;
    }

    /**
     * Yields the bytecode inside this block.
     *
     * @return the bytecode inside this block
     */

    public BytecodeList getBytecode() {
	return bytecode;
    }

    /**
     * Removes the first instruction inside this block.
     */

    public void removeFirstInstruction() {
	if (bytecode.getTail() == null) {
	    Bytecode h = bytecode.getHead();
	    bytecode = new BytecodeList(new NOP(h.getWhere()),null);
	}
	else bytecode = bytecode.getTail();
    }

    /**
     * Adds a bytecode before this block. This results in the same
     * block being modified or in a new block linked to <tt>this</tt>.
     *
     * @param bytecode the bytecode which must be prefixed to this block
     * @return the result of prefixing <tt>bytecode</tt> to this block
     */

    public Block prefixedBy(Bytecode bytecode) {
	// we can expand our code if we have no predecessors,
	// or otherwise we will also affect the view that our predecessors
	// have of us
	if (mergeable) {
	    this.bytecode = new BytecodeList(bytecode).append(this.bytecode);
	    return this;
	}
	else
	    return new Block(new BytecodeList(bytecode),this);
    }

    /**
     * Yields a <tt>String</tt> identifying this node in a dot file.
     *
     * @return a <tt>String</tt> identifying this node in a dot file
     */

    public String dotNodeName() {
	return "codeblock_" + id;
    }

    /**
     * Cleans-up this block and all those reachable from it, also in the
     * methods called from this block.
     * It removes useless <tt>nop</tt>'s and merges a block, with only
     * one successor which has only one predecessor, with that successor.
     *
     * @param program the program which is being cleaned-up
     */

    public void cleanUp(Program program) {
	// the start method of the program is definitely called
	program.getSigs().add(program.getStart());

	cleanUp$0(new HashSet<Block>(),program);
    }

    /**
     * Auxiliary method that cleans-up this block and all those reachable
     * from it. It removes useless <tt>nop</tt>'s and merges a block,
     * with only one successor which has only one predecessor, with that
     * successor.
     *
     * @param done the set of blocks which have been already cleaned-up
     * @param program the program which is being cleaned-up
     */

    private void cleanUp$0(HashSet<Block> done, Program program) {
	if (!done.contains(this)) {
	    done.add(this);

	    List<Block> newFollows = new List<Block>();

	    // we consider each successor and remove isolated nop's
	    for (Block cb: follows)
		if (cb != this && cb.bytecode.getHead() instanceof NOP &&
		    cb.bytecode.getTail() == null) {
		    newFollows.addAll(cb.follows);
		}
		else
			newFollows.addLast(cb);

	    follows = newFollows;

	    // we continue with the successors
	    for (Block cb: follows) cb.cleanUp$0(done,program);

	    // if the bytecode contains a reference to a field or to a
	    // constructor or to a method, we add it to the signatures
	    // for the program and update its statistics
	    for (BytecodeList bs = bytecode; bs != null; bs = bs.getTail()) {
		Bytecode b = bs.getHead();

		// we take note that the program contains the bytecodes in the block
		program.storeBytecode(b);

		if (b instanceof CALL)
		    // we continue by cleaning the dynamic targets
		    for (CodeSignature target: ((CALL)b).getDynamicTargets())
			target.getCode().cleanUp$0(done,program);
	    }
	}
    }
}
