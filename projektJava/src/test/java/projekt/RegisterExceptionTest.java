package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Klasa testowa sprawdzająca działanie klasy {@link RegisterException}. Testuje
 * konstruktory oraz rzucanie wyjątków.
 *
 *
 *
 * @author Damian Jokisz
 *
 * @see RegisterException
 * 
 * 
 * 
 * 
 */
class RegisterExceptionTest {

	/**
	 * Testuje konstruktor {@link RegisterException#RegisterException(String)}.
	 * Sprawdza, czy przekazany komunikat błędu jest poprawnie ustawiany.
	 *
	 * @see RegisterException#getMessage()
	 */
	@Test
	void testRegisterExceptionMessageOnly() {
		String errorMessage = "Błąd rejestracji studenta";

		RegisterException exception = new RegisterException(errorMessage);

		assertEquals(errorMessage, exception.getMessage());
	}

	/**
	 * Testuje konstruktor
	 * {@link RegisterException#RegisterException(String, Throwable)}. Sprawdza, czy
	 * komunikat oraz przyczyna wyjątku są poprawnie przypisane.
	 *
	 * @see RegisterException#getMessage()
	 * @see RegisterException#getCause()
	 */
	@Test
	void testRegisterExceptionWithCause() {
		String errorMessage = "Błąd rejestracji grupy";
		Exception cause = new Exception("Błąd wczytywania danych grupy");

		RegisterException exception = new RegisterException(errorMessage, cause);

		assertEquals(errorMessage, exception.getMessage());
		assertEquals(cause, exception.getCause());
	}

	/**
	 * Testuje rzucanie wyjątku {@link RegisterException} z komunikatem. Sprawdza,
	 * czy wyjątek jest poprawnie zgłaszany przez metodę.
	 *
	 * @throws RegisterException gdy wystąpi błąd rejestracji
	 */
	@Test
	void testThrowRegisterException() {
		String errorMessage = "Niepoprawna liczba punktów";

		assertThrows(RegisterException.class, () -> {
			throw new RegisterException(errorMessage);
		});
	}

	/**
	 * Testuje rzucanie wyjątku {@link RegisterException} z komunikatem i przyczyną.
	 * Sprawdza, czy wyjątek jest prawidłowo rzucany i zawiera zadaną przyczynę.
	 *
	 * @throws RegisterException gdy wystąpi błąd rejestracji z przyczyną
	 */
	@Test
	void testThrowRegisterExceptionWithCause() {
		String errorMessage = "Błąd rejestracji";
		Exception cause = new Exception("Nieprawidłowe dane");

		assertThrows(RegisterException.class, () -> {
			throw new RegisterException(errorMessage, cause);
		});
	}
}
