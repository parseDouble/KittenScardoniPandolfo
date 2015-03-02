package testcases.runTime;

import java.io.IOException;


/* La classe che implementa le stringhe in Java bytecode.
   Qui "String" sta per "runTime.String" e quando dobbiamo fare riferimento
   alla classe "java.lang.String" dobbiamo farlo esplicitamente */

public class String {
	private java.lang.String value;

	/* crea una stringa vuota */
	public String() {
		value = new java.lang.String();
	}

	/* clona una stringa */
	public String(java.lang.String other) {
		value = new java.lang.String(other);
	}

	// la lunghezza della stringa */
	public int length() {
		return value.length();
	}

	// l'i-esimo carattere della stringa */
	public char charAt(int i) {
		return value.charAt(i);
	}

	/* converte nell'intero rappresentato dalla stringa */
	public int toInt() {
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			System.out.println("illegal integer format");
			return 0;
		}
	}

	/* converte nel float rappresentato dalla stringa */
	public float toFloat() {
		try {
			return Float.parseFloat(value);
		}
		catch (NumberFormatException e) {
			System.out.println("illegal integer format");
			return 0;
		}
	}

	/* vero se e solo se "this" e' uguale a "other" */
	public boolean equals(String other) {
		return value.equals(other.value);
	}

	/* stampa la stringa a video */
	public void output() {
		System.out.print(value);
	}

	/* legge una sequenza di caratteri (fino al primo newline)
       da tastiera e la memorizza nella stringa */
	public void input() {
		char c;

		value = new java.lang.String();

		try {
			while ((c = (char)System.in.read()) != '\n') value += c;
		}
		catch (IOException e) {
			System.out.println("I/O exception");
		}
	}

	/* ritorna la concatenazione di "this" e poi "s" */
	public String concat(String s) {
		return new String(value + s.value);
	}

	/* ritorna la concatenazione di "this" e poi "f" */
	public String concat(float f) {
		return new String(value + f);
	}

	/* ritorna la concatenazione di "this" e poi "i" */
	public String concat(int i) {
		return new String(value + i);
	}

	/* ritorna la concatenazione di "this" e poi "c" */
	public String concat(char c) {
		return new String(value + c);
	}

	/* ritorna la concatenazione di "this" e poi "b" */
	public String concat(boolean b) {
		return new String(value + b);
	}
}
