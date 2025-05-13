package projekt;

/**
 * Klasa {@code AppConfig} służy do przechowywania globalnych parametrów
 * konfiguracyjnych aplikacji. Parametry te mogą być używane w różnych częściach
 * systemu, umożliwiając centralne zarządzanie ustawieniami aplikacji.
 * <p>
 * Obecnie klasa przechowuje konfigurację dotyczącą liczby wątków w puli wątków
 * używanej w aplikacji.
 * </p>
 *
 * <p>
 * Przykład użycia:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	int liczbaWatkow = AppConfig.THREAD_POOL_SIZE;
 * }
 * </pre>
 * </p>
 *
 * @author Wiśniewski Mateusz
 * 
 */
public class AppConfig {

	/**
	 * Liczba wątków w puli {@link java.util.concurrent.ExecutorService}
	 * wykorzystywanej przez aplikację do obsługi operacji w tle.
	 * <p>
	 * Wartość domyślna: {@code 4}.
	 * </p>
	 */
	public static final int THREAD_POOL_SIZE = 4;

}
