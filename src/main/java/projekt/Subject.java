package projekt;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Klasa reprezentująca przedmiot akademicki, zawierająca jego nazwę
 * oraz dynamiczne kryteria oceniania wraz z maksymalnymi punktami.
 */
public class Subject {

	/** Nazwa przedmiotu */
	private String subjectName;

	/** Mapa dynamicznych kryteriów oceniania i ich maksymalnych punktów */
	private Map<String, Integer> gradingCriteria;

	/**
	 * Konstruktor inicjalizujący nowy przedmiot o podanej nazwie.
	 *
	 * @param subjectName nazwa przedmiotu
	 */
	public Subject(String subjectName) {
		this.subjectName = subjectName;
		this.gradingCriteria = new LinkedHashMap<>();
	}

	/**
	 * Zwraca nazwę przedmiotu.
	 *
	 * @return nazwa przedmiotu
	 */
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * Ustawia nazwę przedmiotu.
	 *
	 * @param subjectName nowa nazwa przedmiotu
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	/**
	 * Ustawia mapę kryteriów oceniania.
	 *
	 * @param gradingCriteria nowa mapa kryteriów i ich maksymalnych punktów
	 */
	public void setGradingCriteria(Map<String, Integer> gradingCriteria) {
		this.gradingCriteria = gradingCriteria;
	}

	/**
	 * Zwraca mapę kryteriów oceniania i maksymalnych punktów.
	 *
	 * @return mapa kryteriów oceniania
	 */
	public Map<String, Integer> getGradingCriteria() {
		return gradingCriteria;
	}

	/**
	 * Dodaje lub aktualizuje kryterium oceniania.
	 *
	 * @param criteriaName nazwa kryterium
	 * @param maxPoints maksymalna liczba punktów
	 */
	public void updateCriterion(String criteriaName, int maxPoints) {
		gradingCriteria.put(criteriaName, maxPoints);
	}

	/**
	 * Usuwa kryterium oceniania.
	 *
	 * @param criteriaName nazwa kryterium do usunięcia
	 */
	public void removeCriterion(String criteriaName) {
		gradingCriteria.remove(criteriaName);
	}

	/**
	 * Zwraca łączną maksymalną liczbę punktów ze wszystkich kryteriów.
	 *
	 * @return suma punktów
	 */
	public int getTotalMaxPoints() {
		return gradingCriteria.values().stream().mapToInt(Integer::intValue).sum();
	}
}
