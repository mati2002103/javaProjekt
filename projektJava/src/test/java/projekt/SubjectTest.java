package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testowa dla klasy {@link Subject}, testująca operacje na nazwie
 * przedmiotu oraz zarządzanie kryteriami oceniania.
 * 
 * @author Damian Jokisz
 * 
 */
class SubjectTest {
	// Zmienna przechowująca przedmiot
	private Subject subject;

	/**
	 * Inicjalizacja nowego obiektu {@link Subject} przed każdym testem.
	 */
	@BeforeEach
	void setUp() {
		subject = new Subject("Matematyka");
	}

	/**
	 * Test konstruktora oraz getterów. Sprawdza poprawność inicjalizacji nazwy
	 * przedmiotu i pustych kryteriów.
	 */
	@Test
	void testConstructorAndGetters() {
		assertEquals("Matematyka", subject.getSubjectName());
		assertTrue(subject.getGradingCriteria().isEmpty());
	}

	/**
	 * Test zmiany nazwy przedmiotu za pomocą metody
	 * {@link Subject#setSubjectName(String)}.
	 */
	@Test
	void testSetSubjectName() {
		subject.setSubjectName("Fizyka");
		assertEquals("Fizyka", subject.getSubjectName());
	}

	/**
	 * Test dodania nowego kryterium oceniania.
	 *
	 * @see Subject#updateCriterion(String, int)
	 */
	@Test
	void testUpdateCriterion_AddNew() {
		subject.updateCriterion("Egzamin", 100);
		Map<String, Integer> criteria = subject.getGradingCriteria();
		assertEquals(1, criteria.size());
		assertEquals(100, criteria.get("Egzamin"));
	}

	/**
	 * Test nadpisania istniejącego kryterium oceniania.
	 */
	@Test
	void testUpdateCriterion_UpdateExisting() {
		subject.updateCriterion("Kolokwium", 40);
		subject.updateCriterion("Kolokwium", 50);
		assertEquals(1, subject.getGradingCriteria().size());
		assertEquals(50, subject.getGradingCriteria().get("Kolokwium"));
	}

	/**
	 * Test usuwania kryterium oceniania z mapy kryteriów.
	 */
	@Test
	void testRemoveCriterion() {
		subject.updateCriterion("Projekt", 60);
		subject.removeCriterion("Projekt");
		assertFalse(subject.getGradingCriteria().containsKey("Projekt"));
	}

	/**
	 * Test wyliczania łącznej maksymalnej liczby punktów na podstawie kryteriów
	 * oceniania.
	 *
	 * @see Subject#getTotalMaxPoints()
	 */
	@Test
	void testGetTotalMaxPoints() {
		subject.updateCriterion("Egzamin", 70);
		subject.updateCriterion("Kolokwium", 20);
		subject.updateCriterion("Projekt", 10);
		assertEquals(100, subject.getTotalMaxPoints());
	}

	/**
	 * Test ustawienia nowych kryteriów oceniania poprzez
	 * {@link Subject#setGradingCriteria(Map)}.
	 *
	 */
	@Test
	void testSetGradingCriteria() {
		Map<String, Integer> newCriteria = new LinkedHashMap<>();
		newCriteria.put("Test", 30);
		newCriteria.put("Lab", 20);

		subject.setGradingCriteria(newCriteria);

		assertEquals(2, subject.getGradingCriteria().size());
		assertEquals(50, subject.getTotalMaxPoints());
	}
}
