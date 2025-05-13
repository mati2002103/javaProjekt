package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Klasa testowa dla {@link Score}. Testuje metodę {@link Score#toString()} dla
 * różnych przypadków danych.
 *
 * @author Damian Jokisz
 *
 *
 * @see Score
 * @see Student
 * @see Subject
 */
class ScoreTest {

	private Student student;
	private Subject subject;
	private Score score;

	/**
	 * Inicjalizuje dane testowe przed każdym testem. Tworzy obiekt {@link Student},
	 * {@link Subject} oraz {@link Score}.
	 *
	 * @see Student#Student(String, String, String, Group)
	 * @see Subject#Subject(String)
	 * @see Score#Score(Student, Subject, String, int)
	 */
	@BeforeEach
	void setUp() {
		student = new Student("Jan", "Kowalski", "12345", null);
		subject = new Subject("Matematyka");
		score = new Score(student, subject, "Zaliczenie", 80);
	}

	/**
	 * Testuje metodę {@link Score#toString()} dla przykładowego zestawu danych.
	 * Sprawdza poprawność formatowania wyniku.
	 *
	 * @see Score#toString()
	 */
	@Test
	void testToString() {
		String expected = "Jan Kowalski | Matematyka | Zaliczenie | 80 pkt";
		assertEquals(expected, score.toString());
	}

	/**
	 * Testuje metodę {@link Score#toString()} dla innego zestawu danych. Sprawdza,
	 * czy metoda poprawnie generuje reprezentację tekstową.
	 *
	 * @see Score#toString()
	 */
	@Test
	void testToStringWithDifferentData() {
		Student anotherStudent = new Student("Anna", "Nowak", "67890", null);
		Subject anotherSubject = new Subject("Informatyka");
		Score anotherScore = new Score(anotherStudent, anotherSubject, "Projekt", 95);

		String expected = "Anna Nowak | Informatyka | Projekt | 95 pkt";
		assertEquals(expected, anotherScore.toString());
	}
}
