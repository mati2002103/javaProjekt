package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasa testowa jednostkowa dla klasy {@link Student}. Testuje poprawność
 * tworzenia obiektów, przypisywania punktów i obsługi wyjątków.
 * 
 * @author Damian Jokisz
 * 
 */
class StudentTest {

	private Student student;
	private Group group;
	private Subject subject;

	/**
	 * Inicjalizuje dane testowe przed każdym testem. Tworzy przykładowego studenta,
	 * grupę i przedmiot z kryterium oceniania.
	 */
	@BeforeEach
	void setUp() {
		group = new Group("G01", "Informatyka", "Grupa Informatyki");
		student = new Student("Jan", "Kowalski", "12345", group);
		subject = new Subject("Matematyka");

		// Dodajemy przykładowe kryterium oceniania do przedmiotu
		subject.getGradingCriteria().put("Zaliczenie", 100);
	}

	/**
	 * Test sprawdzający poprawne utworzenie obiektu {@link Student}.
	 */
	@Test
	void testStudentCreation() {
		assertNotNull(student);
		assertEquals("Jan", student.getName());
		assertEquals("Kowalski", student.getSurname());
		assertEquals("12345", student.getAlbumNumber());
		assertEquals(group, student.getGroup());
	}

	/**
	 * Test metody {@link Student#addPoints(Subject, String, int)} w przypadku
	 * poprawnych danych.
	 *
	 * @throws RegisterException jeśli wystąpi błąd przypisania punktów (nie
	 *                           powinien wystąpić tutaj)
	 */
	@Test
	void testAddPoints() throws RegisterException {
		assertTrue(student.addPoints(subject, "Zaliczenie", 85));
		assertEquals(85, student.getGrades().get(subject).get("Zaliczenie"));
	}

	/**
	 * Test sprawdzający, czy metoda {@link Student#addPoints(Subject, String, int)}
	 * rzuca wyjątek dla ujemnej liczby punktów.
	 */
	@Test
	void testAddPointsThrowsException() {
		try {
			student.addPoints(subject, "Zaliczenie", -5);
			fail("Oczekiwano wyjątku RegisterException");
		} catch (RegisterException e) {
			assertEquals("Niepoprawna liczba punktów lub brak kryterium w przedmiocie", e.getMessage());
		}
	}

	/**
	 * Test sprawdzający, czy metoda {@link Student#addPoints(Subject, String, int)}
	 * rzuca wyjątek, jeśli kryterium oceniania nie istnieje w danym przedmiocie.
	 */
	@Test
	void testAddPointsThrowsExceptionForMissingCriteria() {
		try {
			student.addPoints(subject, "NieistniejąceKryterium", 50);
			fail("Oczekiwano wyjątku RegisterException");
		} catch (RegisterException e) {
			assertEquals("Niepoprawna liczba punktów lub brak kryterium w przedmiocie", e.getMessage());
		}
	}

	/**
	 * Test metody fabrycznej
	 * {@link Student#createStudent(String, String, String, Group)}, która tworzy
	 * nowy obiekt studenta.
	 */
	@Test
	void testCreateStudent() {
		Student newStudent = student.createStudent("Anna", "Nowak", "67890", group);
		assertNotNull(newStudent);
		assertEquals("Anna", newStudent.getName());
		assertEquals("Nowak", newStudent.getSurname());
		assertEquals("67890", newStudent.getAlbumNumber());
		assertEquals(group, newStudent.getGroup());
	}

	/**
	 * Test sprawdzający, czy metoda {@link Student#getGrades()} poprawnie zwraca
	 * mapę ocen.
	 *
	 * @throws RegisterException jeśli wystąpi błąd przy przypisywaniu punktów
	 */
	@Test
	void testGetGrades() throws RegisterException {
		student.addPoints(subject, "Zaliczenie", 75);
		assertNotNull(student.getGrades().get(subject));
		assertEquals(75, student.getGrades().get(subject).get("Zaliczenie"));
	}
}
