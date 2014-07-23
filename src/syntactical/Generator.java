package syntactical;

import java.io.FileInputStream;

import java_cup.internal_error;

public class Generator {

	public static void main(String[] args) throws internal_error, Exception {
		FileInputStream fis = new FileInputStream("resources/Kitten.cup");
		System.setIn(fis);
		java_cup.Main.main(new String[] { "-parser", "Parser", "-dump_grammar", "-dump_states"});
		fis.close();
	}
}