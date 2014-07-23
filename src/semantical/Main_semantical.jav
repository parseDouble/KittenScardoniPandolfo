import java.io.IOException;

import errorMsg.ErrorMsg;
import types.ClassType;
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
	KittenClassType.mkFromFileName(args[0]);

	System.out.println("End of the semantical analysis");

	// we dump the set of classes which have been created
	if (!ErrorMsg.anyErrors())
	    for (ClassType clazz: KittenClassType.getAll()) {
	        try { ((KittenClassType)clazz).dumpDot(); }
	        catch (IOException e) {
		    System.out.println("Cannot dump " + clazz + ".dot");
		    continue;
	        }

	        System.out.println("Dumped " + clazz + ".dot");
	    }
    }
}
