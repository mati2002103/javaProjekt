package projekt;

import java.util.HashSet;

/**
 * Klasa odpowiedzialna za zarządzanie bazą danych studentów.
 */
public class StudentDB {

	/** Zbiór studentów */
	private HashSet<Student> students;

	/**
	 * Konstruktor domyślny inicjalizujący pustą bazę studentów.
	 */
	StudentDB() {
		this.students = new HashSet<>();
	}

	/**
	 * Dodaje studenta do wskazanej grupy i ustawia jego przypisanie do grupy.
	 *
	 * @param student obiekt studenta
	 * @param group   grupa, do której ma zostać przypisany student
	 */
	public void addStudentToGroup(Student student, Group group) {
		student.setGroup(group);
		group.addStudent(student);
	}

	/**
	 * Dodaje studenta do wewnętrznej listy studentów.
	 *
	 * @param s student do dodania
	 */
	public void addStudentToStudentList(Student s) {
		this.students.add(s);
	}

	/**
	 * Usuwa studenta z wewnętrznej listy studentów.
	 *
	 * @param s student do usunięcia
	 */
	public void deleteStudentFromStudentList(Student s) {
		this.students.remove(s);
	}

	/**
	 * Wyszukuje studenta na podstawie numeru albumu.
	 *
	 * @param albumNumber numer albumu studenta
	 * @return obiekt studenta, jeśli znaleziono; w przeciwnym razie null
	 */
	public Student getStudentByAlbumNumber(int albumNumber) {
		for (Student s : students) {
			if (s.getAlbumNumber().equals(albumNumber)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Tworzy nowego studenta z podanymi danymi.
	 *
	 * @param name        imię studenta
	 * @param surname     nazwisko studenta
	 * @param albumNumber numer albumu
	 * @param group       grupa, do której należy student
	 * @return nowo utworzony student
	 */
	public Student createStudent(String name, String surname, String albumNumber, Group group) {
		Student newStudent = new Student(name, surname, albumNumber, group);
		return newStudent;
	}
	
	/////////////////////////
	/// Getters and Setters
	/////////////////////////
	
	public HashSet<Student> getStudents() {
		return students;
	}

	public void setStudents(HashSet<Student> students) {
		this.students = students;
	}
	
}
