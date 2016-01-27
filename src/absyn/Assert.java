package absyn;

import java.io.FileWriter;
import java.io.IOException;

import bytecode.ERRORSTRING;
import bytecode.RETURN;

import semantical.TypeChecker;
import translation.Block;
import types.ClassType;
import types.CodeSignature;

public class Assert extends Command {
	
	private final Expression exp; //esp of the assert
	
	private String err; //Error string "at row::column"
	
	/**
	 * Constructs the abstract syntax of an assert
	 * 
	 * @param pos position of the assert in the source file
	 * @param exp expression of the assert
	 */
	public Assert(int pos, Expression exp) {
		super(pos);
		this.exp=exp;
		this.err="";
	}

	public Expression getExp(){
		return this.exp;
	}
	
	/**
	 * Type-checking of an assert using a given type-checker
	 */
	@Override
	protected TypeChecker typeCheckAux(TypeChecker checker) {
		
		if(!checker.getIsInTest()){
			error("assert must be used in a test");
		}
		
		int pos=getPos();
		
		this.err=checker.getAssertError(pos, "\ttest failed");
		
		exp.mustBeBoolean(checker);
		exp.typeCheck(checker);
		
		
		return checker;
	}

	/**
	 * Checks if this not contain dead code
	 */
	@Override
	public boolean checkForDeadcode() {
		
		return false;
	}
	/**
	 * This method adds information in the dot file
	 * representing the abstract syntax of the assert command adding args from the node
	 * to the abstract syntax
	 * 
	 * @return write where the dot file must be written
	 */
	public void toDotAux(FileWriter write) throws IOException{
		
		linkToNode("assert", getExp().toDot(write), write);
		
	}
	

	@Override
	public Block translate(CodeSignature code, Block continuation) {
		
		ClassType ct=ClassType.mk("String");
		
		continuation.doNotMerge();
		
		Block block=new ERRORSTRING(err).followedBy(new Block(new RETURN(ct)));
		
		block.doNotMerge();
		
		Block block1= exp.translateAsTest(code, continuation, block);
		
		return block1;
	}
	
	
	/**
	 * Unused method
	 */
	@Override
	public Block translate(Block continuation) {
		// TODO Auto-generated method stub
		return null;
	}

}
