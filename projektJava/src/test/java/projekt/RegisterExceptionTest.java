package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RegisterExceptionTest {

    // Testujemy konstruktor, który przyjmuje tylko wiadomość
    @Test
    void testRegisterExceptionMessageOnly() {
        String errorMessage = "Błąd rejestracji studenta";
        
        // Tworzymy wyjątek z tylko komunikatem
        RegisterException exception = new RegisterException(errorMessage);
        
        // Sprawdzamy, czy komunikat w wyjątku jest taki sam jak przekazany
        assertEquals(errorMessage, exception.getMessage());
    }

    // Testujemy konstruktor, który przyjmuje wiadomość i inny wyjątek
    @Test
    void testRegisterExceptionWithCause() {
        String errorMessage = "Błąd rejestracji grupy";
        Exception cause = new Exception("Błąd wczytywania danych grupy");
        
        // Tworzymy wyjątek z wiadomością i innym wyjątkiem jako przyczyną
        RegisterException exception = new RegisterException(errorMessage, cause);
        
        // Sprawdzamy, czy komunikat jest poprawny
        assertEquals(errorMessage, exception.getMessage());
        
        // Sprawdzamy, czy przyczyna wyjątku jest zgodna z przekazanym wyjątkiem
        assertEquals(cause, exception.getCause());
    }

    // Testujemy, czy wyjątek jest prawidłowo rzucany w kodzie aplikacji
    @Test
    void testThrowRegisterException() {
        String errorMessage = "Niepoprawna liczba punktów";
        
        // Sprawdzamy, czy metoda rzuci wyjątek, jeśli zajdzie błąd
        assertThrows(RegisterException.class, () -> {
            throw new RegisterException(errorMessage);
        });
    }

    // Testujemy, czy wyjątek z przyczyną jest prawidłowo rzucany
    @Test
    void testThrowRegisterExceptionWithCause() {
        String errorMessage = "Błąd rejestracji";
        Exception cause = new Exception("Nieprawidłowe dane");
        
        // Sprawdzamy, czy wyjątek jest rzucany z przyczyną
        assertThrows(RegisterException.class, () -> {
            throw new RegisterException(errorMessage, cause);
        });
    }
}
