package projekt;

public class RegisterException extends Exception {

	public RegisterException(String msg) {
		super(msg);
	}

	public RegisterException(String msg, Exception ex) {
		super(msg, ex);
	}

}
