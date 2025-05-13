package projekt;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * Klasa reprezentująca studenta wraz z jego danymi, przynależnością do grupy
 * oraz ocenami z przedmiotów według określonych kryteriów.
 * 
 * 
 * 
 * @author Wiśniewski Mateusz
 */
public class Student {

	private Map<String, int[]> subjectScores = new HashMap<>();

	/** Imię studenta */
	private String name;

	/** Nazwisko studenta */
	private String surname;

	/** Numer albumu studenta */
	private String albumNumber;

	/** Grupa, do której przypisany jest student */
	private Group group;

	/**
	 * Mapa ocen studenta. Kluczem jest przedmiot, a wartością mapa par (nazwa
	 * kryterium, punkty).
	 */
	private Map<Subject, Map<String, Integer>> grades;

	/**
	 * Konstruktor domyślny – inicjalizuje pustego studenta.
	 */
	Student() {
		this.name = null;
		this.surname = null;
		this.albumNumber = null;
		this.grades = new HashMap<>();
	}

	/**
	 * Konstruktor tworzący studenta z podanymi danymi.
	 *
	 * @param name        imię studenta
	 * @param surname     nazwisko studenta
	 * @param albumNumber numer albumu studenta
	 * @param group       grupa przypisana studentowi
	 */
	Student(String name, String surname, String albumNumber, Group group) {
		this.name = name;
		this.surname = surname;
		this.albumNumber = albumNumber;
		this.group = group;
		this.grades = new HashMap<>();
	}

	/**
	 * Tworzy nowego studenta na podstawie podanych danych.
	 *
	 * @param name        imię studenta
	 * @param surname     nazwisko studenta
	 * @param albumNumber numer albumu
	 * @param group       grupa
	 * @return nowy obiekt Student
	 */
	public Student createStudent(String name, String surname, String albumNumber, Group group) {
		Student newStudent = new Student(name, surname, albumNumber, group);
		return newStudent;
	}

	/**
	 * Zwraca informacje o studencie jako tekst. (Wersja robocza, do uzupełnienia.)
	 *
	 * @return reprezentacja tekstowa studenta
	 */
	public String studentToString() {
		return null;
	}

	/**
	 * Dodaje punkty studentowi z określonego przedmiotu i kryterium.
	 *
	 * @param subject      przedmiot
	 * @param criteriaName nazwa kryterium
	 * @param points       liczba punktów
	 * @return true, jeśli dodano punkty
	 * @throws RegisterException jeśli punkty są spoza dozwolonego zakresu
	 */
	public boolean addPoints(Subject subject, String criteriaName, int points) throws RegisterException {
		Integer maxPoints = subject.getGradingCriteria().get(criteriaName);
		if (points < 0 || maxPoints == null || points > maxPoints) {
			throw new RegisterException("Niepoprawna liczba punktów lub brak kryterium w przedmiocie");
		}

		Map<String, Integer> subjectGrades = grades.get(subject);
		if (subjectGrades == null) {
			subjectGrades = new HashMap<>();
			grades.put(subject, subjectGrades);
		}

		subjectGrades.put(criteriaName, points);
		return true;
	}

	/////////////////////////
	/// Getters and Setters
	/////////////////////////
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAlbumNumber() {
		return this.albumNumber;
	}

	public void setAlbumNumber(String albumNumber) {
		this.albumNumber = albumNumber;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Map<Subject, Map<String, Integer>> getGrades() {
		return grades;
	}

	public void setGrades(Map<Subject, Map<String, Integer>> grades) {
		this.grades = grades;
	}

	public Map<String, int[]> getSubjectScores() {
		return subjectScores;
	}
}
