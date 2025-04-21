package projekt;

/**
 * @author Wiśniewski Mateusz
 * 
 *         Enum definiujący kryteria oceniania dla przedmiotu, wraz z maksymalną
 *         liczbą punktów możliwych do uzyskania za każde kryterium.
 */
public enum Criteria {

	/** Aktywność na zajęciach (maks. 5 pkt) */
	activity(5),

	/** Prace domowe (maks. 10 pkt) */
	homeworks(10),

	/** Projekt (maks. 10 pkt) */
	project(10),

	/** Test (maks. 25 pkt) */
	test(25),

	/** Egzamin końcowy (maks. 50 pkt) */
	exam(50);

	/** Maksymalna liczba punktów dla danego kryterium */
	private final int maxPoints;

	/**
	 * Konstruktor przypisujący maksymalną liczbę punktów do kryterium.
	 *
	 * @param maxPoints maksymalna liczba punktów
	 */
	private Criteria(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	/**
	 * Zwraca maksymalną liczbę punktów dla danego kryterium.
	 *
	 * @return maksymalna liczba punktów
	 */
	public int getmaxPoints() {
		return maxPoints;
	}
}
