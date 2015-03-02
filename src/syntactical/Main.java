package syntactical;

import java.io.File;
import java.io.IOException;

import java_cup.runtime.Symbol;
import lexical.Lexer;
import syntactical.Parser;
import absyn.ClassDefinition;

public class Main {

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			System.out.println("You must specify a Kitten class name to compile");
		else
			try {
				String fileName = args[0];
				Parser parser = new Parser(new Lexer(fileName));
				Symbol symbol = parser.parse();
				System.out.println("End of the syntactical analysis");

				ClassDefinition absyn = (ClassDefinition) symbol.value;
				if (absyn != null) {
					int index = fileName.lastIndexOf(File.separatorChar);
					String dir = index > 0 ? fileName.substring(0, index) : "";
					absyn.dumpDot(dir);
					String dotName = fileName.substring(0, fileName.length() - ".kit".length()) + ".dot";
					System.out.println("Abstract syntax saved into " + dotName);
				}
				else
					System.out.println("Null semantical value");
			}
			catch (IOException e) {
				System.out.println("I/O error");
			}
			catch (Error e) {
				System.out.println("Unmatched input");
			}
	}
}