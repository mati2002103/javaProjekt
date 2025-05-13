package projekt;

/**
 * 
 * 
 * Klasa wyjątku, który jest rzucany w przypadku błędów związanych z rejestracją
 * studenta lub grupy oraz operacjami związanymi z ocenami i punktami.
 * 
 * @author Wiśniewski Mateusz
 */
public class RegisterException extends Exception {

	/**
	 * Konstruktor klasy RegisterException.
	 *
	 * @param msg wiadomość opisująca błąd
	 */
	public RegisterException(String msg) {
		super(msg);
	}

	/**
	 * Konstruktor klasy RegisterException z dodatkowym wyjątkiem.
	 *
	 * @param msg wiadomość opisująca błąd
	 * @param ex  wyjątek, który powoduje ten błąd
	 */
	public RegisterException(String msg, Exception ex) {
		super(msg, ex);
	}
}
