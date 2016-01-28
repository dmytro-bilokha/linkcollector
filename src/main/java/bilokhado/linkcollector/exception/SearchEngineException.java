package bilokhado.linkcollector.exception;

/**
 * An exception class used for signaling run-time failure of reading or parsing
 * data from the search engine.
 */
public class SearchEngineException extends Exception {

	public SearchEngineException() {
		super();
	}

	public SearchEngineException(String message) {
		super(message);
	}

	public SearchEngineException(Throwable cause) {
		super(cause);
	}

	public SearchEngineException(String message, Throwable cause) {
		super(message, cause);
	}

}
