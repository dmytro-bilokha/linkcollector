package bilokhado.linkcollector.exception;

/**
 * An exception class used for signaling run-time failure of converting JSON
 * string to TagsList object.
 */
public class TagsListParsingException extends Exception {

	public TagsListParsingException() {
		super();
	}

	public TagsListParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public TagsListParsingException(String message) {
		super(message);
	}

	public TagsListParsingException(Throwable cause) {
		super(cause);
	}

}
