package projekt;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Wiśniewski Mateusz
 * 
 *         Klasa reprezentująca przedmiot akademicki, zawierająca jego nazwę
 *         oraz kryteria oceniania wraz z maksymalnymi punktami.
 */
public class Subject {

	/** Lista przykładowych nazw przedmiotów */
	public static final String[] SUBJECT_NAMES = { "Język Java", "Język C++", "Język C#", "Analiza Matematyczna",
			"Matematyka Dyskretna", "Statystyka" };

	/** Nazwa przedmiotu */
	private String subjectName;

	/** Mapa kryteriów oceniania i ich maksymalnych punktów */
	private Map<Criteria, Integer> gradingCriteria;

	/**
	 * Konstruktor inicjalizujący nowy przedmiot o podanej nazwie i domyślnych
	 * maksymalnych punktach dla każdego kryterium.
	 *
	 * @param subjectName nazwa przedmiotu
	 */
	public Subject(String subjectName) {
		this.subjectName = subjectName;
		this.gradingCriteria = new HashMap<>();
		for (Criteria c : Criteria.values()) {
			gradingCriteria.put(c, c.getmaxPoints());
		}
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
	 * Zwraca mapę kryteriów oceniania i maksymalnych punktów dla tego przedmiotu.
	 *
	 * @return mapa kryteriów oceniania
	 */
	public Map<Criteria, Integer> getGradingCriteria() {
		return gradingCriteria;
	}

	/**
	 * Aktualizuje maksymalną liczbę punktów dla podanego kryterium.
	 *
	 * @param criteria kryterium oceniania
	 * @param newMax   nowa maksymalna liczba punktów
	 */
	public void updateCriterion(Criteria criteria, int newMax) {
		gradingCriteria.put(criteria, newMax);
	}
}
