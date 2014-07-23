package errorMsg;

import util.List;

/**
 * An error reporting utility. It allows one to print error messages
 * referring to a given position inside a source Kitten program.
 *
 * @author <A HREF="mailto:fausto.spoto@univr.it">Fausto Spoto</A>
 */

public class ErrorMsg {

	/**
	 * The sequence of newline positions in the source <tt>fileName</tt>.
	 */

	private List<Integer> linePos;

	/**
	 * The number of lines in the source <tt>fileName</tt>.
	 */

	private int lineNum;

	/**
	 * The name of the file to which this error reporting utility
	 * is associated.
	 */

	private String fileName;

	/**
	 * Has any error occurred up to now? By using a <tt>static</tt> field,
	 * this information is global for the whole set of program classes.
	 */

	private static boolean anyErrors = false;

	/**
	 * Creates an error reporting utility for the specified source file.
	 *
	 * @param fileName the name of the source file
	 */

	public ErrorMsg(String fileName) {
		this.linePos = new List<Integer>();
		this.lineNum = 1;
		this.fileName = fileName;
	}

	/**
	 * Yields the name of the source file.
	 *
	 * @return the name of the source file
	 */

	public String getFileName() {
		return fileName;
	}

	/**
	 * Determines if any error has been reported up to now with
	 * some <tt>ErrorMsg</tt> utility.
	 *
	 * @return true if some error has been reported, false otherwise
	 */

	public static boolean anyErrors() {
		return anyErrors;
	}

	/**
	 * Records that a new line character has been found at the given position.
	 *
	 * @param pos the position of the new line character in the source file
	 *            (number of characters from the beginning of the file)
	 */

	public void newline(int pos) {
		lineNum++;
		linePos.addLast(pos);
	}

	/**
	 * Reports an error message occurring at the given position
	 * in the source file.
	 *
	 * @param pos the position where the error must be reported
	 *            (number of characters from the beginning of the file).
	 *            If this is negative, the message is printed without
	 *            any line number reference
	 * @param msg the message to be reported
	 */

	public void error(int pos, String msg) {
		// an error has been reported at least
		anyErrors = true;

		String sayPos = "";

		if (pos >= 0) {
			int last = 0, n = 1;

			// we look for the last new line before position <tt>pos</tt>
			for (int line: linePos) {
				if (line >= pos) break;

				last = line;
				n++;
			}

			sayPos = n + "." + (pos - last);
		}

		System.out.println(fileName + "::" + sayPos + ": " + msg);
	}
}