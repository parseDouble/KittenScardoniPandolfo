package semantical;

import symbol.Symbol;
import symbol.Table;
import types.Type;
import errorMsg.ErrorMsg;

/**
 * A type-checker. It maintains a symbol table and auxiliary information
 * needed to type-check Kitten source code.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class TypeChecker {

	/**
	 * The return type expected by this type-checker.
	 */

	private Type returnType;

	/**
	 * The <i>environment</i>, i.e., a symbol table mapping variable names
	 * to their declared type.
	 */

	private Table env;

	/**
	 * The number of local variables seen so far by this type-checker.
	 */

	private int varNum;

	/**
	 * The error reporting utility used to signal errors in the source code.
	 */

	private ErrorMsg errorMsg;

	/**
	 * Constructs a type-checker.
	 *
	 * @param returnType the return type expected by the type-checker
	 * @param env the environment of the type-checker
	 * @param varNum the number of local variables seen by the type-checker
	 * @param errorMsg the error reporting utility of the type-checker
	 */

	private TypeChecker(Type returnType, Table env, int varNum, ErrorMsg errorMsg) {
		this.returnType = returnType;
		this.env = env;
		this.varNum = varNum;
		this.errorMsg = errorMsg;
	}

	/**
	 * Constructs a type-checker having a given expected return type,
	 * a given error reporting utility and an empty symbol table
	 *
	 * @param returnType the expected return type
	 * @param errorMsg the error reporting utility used to signal errors
	 */

	public TypeChecker(Type returnType, ErrorMsg errorMsg) {
		this(returnType,Table.EMPTY,0,errorMsg);
	}

	/**
	 * Yields the type expected by this type-checker for the <tt>return</tt>
	 * commands.
	 *
	 * @return the type expected by this type-checker for the <tt>return</tt>
	 *         commands.
	 */

	public Type getReturnType() {
		return returnType;
	}

	/**
	 * Yields a new type-checker identical to this but where a given variable
	 * has been bound to a given type.
	 *
	 * @param var the variable to be bound
	 * @param type the type to which <tt>var</tt> must be bound
	 * @return the new type-checker where <tt>var</tt> is bound
	 *         to <tt>type</tt>
	 */

	public TypeChecker putVar(Symbol var, Type type) {
		// note that in the new type-checker the number of local
		// variables is one more than in this type-checker
		return new TypeChecker(returnType,
				env.put(var,new TypeAndNumber(type,varNum)),
				varNum + 1,errorMsg);
	}

	/**
	 * Yields the type bound to a given variable by this type-checker.
	 *
	 * @param var the variable
	 * @return the type bound to <tt>var</tt> by this type-checker.
	 *         Returns <tt>null</tt> if <tt>var</tt> is not bound by this
	 *         type-checker
	 */

	public Type getVar(Symbol var) {
		TypeAndNumber tan = (TypeAndNumber)env.get(var);

		if (tan != null) return tan.getType();
		else return null;
	}

	/**
	 * Yields the progressive number assigned to a given variable
	 * by this type-checker.
	 *
	 * @param var the variable
	 * @return the progressive number of <tt>var</tt> in some enumeration
	 *         of the variables seen so far by this type-checker. It is
	 *         guaranteed that progressive numbers are non-negative and that
	 *         two distinct variables have different
	 *         progressive numbers. Returns <tt>-1</tt> if <tt>var</tt>
	 *         is not bound by this type-checker
	 */

	public int getVarNum(Symbol var) {
		TypeAndNumber tan = (TypeAndNumber)env.get(var);

		if (tan != null) return tan.getNumber();
		else return -1;
	}

	/**
	 * Reports an error through this type-checker.
	 *
	 * @param pos the position where the error must be reported
	 *            (number of characters from the beginning of the
	 *            source file)
	 * @param msg the message to be reported
	 */

	public void error(int pos, String msg) {
		errorMsg.error(pos,msg);
	}

	/**
	 * Determines if any error has been reported up to now
	 * by this type-checker.
	 *
	 * @return true if some error has been reported by this type-checker,
	 *         false otherwise
	 */

	public boolean anyErrors() {
		return errorMsg.anyErrors();
	}
}