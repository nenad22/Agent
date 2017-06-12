package exceptions;

@SuppressWarnings("serial")
public class AliasExistsException extends Exception {
	public AliasExistsException(String e) {
		super("Alias "+e+" already exists.");
	}
}