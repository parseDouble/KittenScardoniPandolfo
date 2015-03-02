import translate.Program;
import types.KittenClassType;

public class Kitten {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println
			("You must specify a Kitten class name to compile");
			return;
		}

		// we build the class type for the file name passed as a parameter.
		// This triggers type-checking of that class and all classes
		// referenced from it
		long totalTime = System.currentTimeMillis();
		long time = System.currentTimeMillis();
		KittenClassType clazz = KittenClassType.mkFromFileName(args[0]);
		System.out.println("Parsing and type-checking completed     \t\t["
				+ (System.currentTimeMillis() - time) + "ms]");

		// if this class did not parse and type-check correctly,
		// we cannot translate the program into intermediate Kitten code
		if (!clazz.getErrorMsg().anyErrors()) {
			time = System.currentTimeMillis();
			// we translate this class into Kitten bytecode
			Program program = ((KittenClassType)clazz).translate();

			System.out.println
			("Translation into Kitten bytecode completed \t\t["
					+ (System.currentTimeMillis() - time) + "ms]");

			if (program.getStart() == null) {
				System.out.println("There is no main() method for starting the analyses");
				return;
			}

			// we translate, into Java bytecode, the Kitten code
			// of every class member which
			// is reachable from the class we translated.
			// This also generates the <tt>.class</tt> files
			time = System.currentTimeMillis();
			program.generateJB();

			System.out.println
			("Java bytecode generation completed       \t\t[" +
					+ (System.currentTimeMillis() - time) + "ms]");
		}

		System.out.println("Total compilation time was " +
				(System.currentTimeMillis() - totalTime) + "ms");
	}
}