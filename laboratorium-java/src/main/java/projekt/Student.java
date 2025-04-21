package projekt;

import java.util.HashMap;
import java.util.Map;

public class Student  {
	// Imie studenta
	private String name;
	// Nazwisko studenta
	private String surname;
	// Numer Albumu Studenta
	private String albumNumber;
	// Grupa do której przynależy student
	private Group group;
	// Mapa przechowująca oceny studenta z danego przedmiotu oraz kryterium
	private Map<Subject, Map<String, Integer>> grades;

	// Pusty konstruktor inicjalizujący
	Student() {
		this.name = null;
		this.surname = null;
		this.albumNumber = null;
		this.grades = new HashMap<>();
	}

	// Konstruktor parametryczny tworzacy studenta
	Student(String name, String surname, String albumNumber, Group group, Map<Subject, Map<String, Integer>> grades) {
		this.name = name;
		this.surname = surname;
		this.albumNumber = albumNumber;
		this.grades = new HashMap<>();
	}

	public Student createStudent(String name, String surname, String albumNumber, Group group) {
		return null;
	}

	public String studentToString() {
		return null;
	}

	public void assignStudentGroup(Group group) {
		this.group = group;
	}

	public boolean addPoints(Subject subject, Criteria criteria, int points) throws RegisterException {
		if (points < 0 || points > criteria.getmaxPoints()) {
			throw new RegisterException("Niepoprawna liczba punktów");
		}

		Map<String, Integer> subjectGrades = grades.get(subject);
		if (subjectGrades == null) {
			subjectGrades = new HashMap<>();
			grades.put(subject, subjectGrades);
		}

		subjectGrades.put(criteria.name(), points);
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
		return albumNumber;
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

}
