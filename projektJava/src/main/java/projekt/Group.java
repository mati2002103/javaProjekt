package projekt;

import java.util.HashSet;
import java.util.Set;

/**
 *   Klasa reprezentująca grupę studencką, zawierającą informacje o kodzie
 *   grupy, specjalizacji, opisie oraz przypisanych studentach.
 * 
 * @author Wiśniewski Mateusz
 * 
 *       
 */
public class Group {

	/** Kod grupy (np. ID05TC01) */
	private String groupCode;

	/** Specjalizacja przypisana grupie (np. Technologia chmury) */
	private String specialization;

	/** Opis grupy */
	private String description;

	/** Zbiór studentów przypisanych do grupy */
	private Set<Student> students;

	/**
	 * Konstruktor domyślny – tworzy pustą grupę bez przypisanych danych.
	 */
	Group() {
		this.groupCode = null;
		this.specialization = null;
		this.description = null;
		this.students = new HashSet<>();
	}

	/**
	 * Konstruktor inicjalizujący grupę na podstawie kodu, specjalizacji i opisu.
	 *
	 * @param groupCode      kod grupy
	 * @param specialization specjalizacja
	 * @param description    opis grupy
	 */
	public Group(String groupCode, String specialization, String description) {
		this.groupCode = groupCode;
		this.specialization = specialization;
		this.description = description;
		this.students = new HashSet<>();
	}

	/**
	 * Dodaje studenta do grupy, jeśli nie znajduje się jeszcze w zbiorze.
	 *
	 * @param s obiekt studenta do dodania
	 */
	public void addStudent(Student s) {
		if (!students.contains(s)) {
			students.add(s);
		}
	}

	////////////////////////////
	////// Getters and setters
	////////////////////////////
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}
