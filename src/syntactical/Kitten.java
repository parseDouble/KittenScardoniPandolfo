package syntactical;
import java.io.*;

import java_cup.runtime.Symbol;

import lexical.Lexer;
import syntactical.Parser;
import absyn.ClassDefinition;

public class Kitten {
    public static void main(String[] args) {
	if (args.length == 0) {
	    System.out.println
                ("You must specify a Kitten class name to compile");
            return;
        }

	String fileName = args[0], prefix, dotName;
	ClassDefinition absyn;
	Symbol symbol;
	Parser parser;

	try {
	    parser = new Parser(new Lexer(fileName));
	    symbol = parser.parse();
	}
	catch (java.io.IOException e) {
	    System.out.println("I/O error during parsing");
	    return;
	}
	catch (Error e) {
	    System.out.println("Unmatched input");
	    return;
	}
	catch (RuntimeException e) {
	    throw e;
	}
	catch (Exception e) {
	    return;
	}

	System.out.println("End of the syntactical analysis");

	absyn = (ClassDefinition)symbol.value;

	if (absyn != null) {
	    try {
		absyn.dumpDot();
	    }
	    catch (IOException e) {
		System.out.println("I/O error while dumping the dot file");
	    }

	    prefix = fileName.substring(0,fileName.length() - ".kit".length());
	    dotName = prefix + ".dot";

	    System.out.println("Abstact syntax saved into " + dotName);
	}
	else System.out.println("Null semantical value");
    }
}
