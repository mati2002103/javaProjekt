package projekt;

/**
 * Klasa reprezentująca przypisanie punktów studenta z danego kryterium danego
 * przedmiotu.
 * <p>
 * Obiekt tej klasy zawiera informację o studencie, przedmiocie, nazwie
 * kryterium oceniania oraz liczbie przyznanych punktów.
 * </p>
 * 
 * @author Wiśniewski Mateusz
 */
public class Score {

	/** Student, któremu przypisano punkty */
	private Student student;

	/** Przedmiot, którego dotyczy ocena */
	private Subject subject;

	/** Nazwa kryterium oceniania (np. "Zaliczenie") */
	private String criteriaName;

	/** Liczba przyznanych punktów */
	private int points;

	/**
	 * Tworzy nowy obiekt typu {@code Score}.
	 *
	 * @param student      student, któremu przypisano punkty
	 * @param subject      przedmiot, którego dotyczy ocena
	 * @param criteriaName nazwa kryterium oceniania
	 * @param points       liczba przyznanych punktów
	 */
	public Score(Student student, Subject subject, String criteriaName, int points) {
		this.student = student;
		this.subject = subject;
		this.criteriaName = criteriaName;
		this.points = points;
	}

	/**
	 * Zwraca tekstową reprezentację oceny studenta.
	 *
	 * @return tekst zawierający imię i nazwisko studenta, nazwę przedmiotu, nazwę
	 *         kryterium i liczbę punktów.
	 */
	@Override
	public String toString() {
		return student.getName() + " " + student.getSurname() + " | " + subject.getSubjectName() + " | " + criteriaName
				+ " | " + points + " pkt";
	}
}
