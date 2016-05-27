package kopkaj.rattham.exception;

@SuppressWarnings("serial")
public class RatthamException extends RuntimeException {
	public RatthamException(String message) {
		super(message);
	}
	public RatthamException(String message, Throwable cause) {
		super(message, cause);
	}
}
