package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
	public Student getStudentByAlbumNumber(String albumNumber) {
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
		return new Student(name, surname, albumNumber, group);
	}

	/**
	 * Zapisuje listę studentów do strumienia w formacie binarnym.
	 * 
	 * @param out obiekt     strumień wyjściowy DataOutputStream z zapisanymi danymi studentów 
	 * @throws IOException   błąd zapisu
	 */
	public void saveToFile(DataOutputStream out) throws IOException {
		out.writeInt(students.size());
		for (Student s : students) {
			out.writeUTF(s.getName());
			out.writeUTF(s.getSurname());
			out.writeUTF(s.getAlbumNumber());

			if (s.getGroup() != null) {
				out.writeBoolean(true);
				out.writeUTF(s.getGroup().getGroupCode());
			} else {
				out.writeBoolean(false);
			}

			// zapisz oceny
			Map<Subject, Map<String, Integer>> grades = s.getGrades();
			out.writeInt(grades.size());
			for (Map.Entry<Subject, Map<String, Integer>> entry : grades.entrySet()) {
				Subject subject = entry.getKey();
				out.writeUTF(subject.getSubjectName());

				Map<String, Integer> criteriaMap = entry.getValue();
				out.writeInt(criteriaMap.size());
				for (Map.Entry<String, Integer> crit : criteriaMap.entrySet()) {
					out.writeUTF(crit.getKey());
					out.writeInt(crit.getValue());
				}
			}
		}
	}

	/**
	 * Wczytuje dane studentów z podanego strumienia wejściowego w formacie binarnym.
	 * 
	 * @param in            DataInputStream z którego zostaną odczytane dane studentów
	 * @param groupDB       baza grup
	 * @param subjectDB		baza przedmiotów
	 * @throws IOException  błąd odczytu
	 */
	public void loadFromFile(DataInputStream in, GroupDB groupDB, SubjectDB subjectDB) throws IOException {
		int studentCount = in.readInt();
		for (int i = 0; i < studentCount; i++) {
			String name = in.readUTF();
			String surname = in.readUTF();
			String albumNumber = in.readUTF();

			boolean hasGroup = in.readBoolean();
			Group g = null;
			if (hasGroup) {
				String groupCode = in.readUTF();
				g = groupDB.getGroupByCode(groupCode);
			}

			Student s = new Student(name, surname, albumNumber, g);

			int gradesCount = in.readInt();
			for (int j = 0; j < gradesCount; j++) {
				String subjectName = in.readUTF();
				Subject subj = subjectDB.getSubjectByName(subjectName);

				int criteriaCount = in.readInt();
				for (int k = 0; k < criteriaCount; k++) {
					String criteriaName = in.readUTF();
					int points = in.readInt();
					try {
						s.addPoints(subj, criteriaName, points);
					} catch (RegisterException e) {
						System.out.println("Błąd przy odczycie punktów: " + e.getMessage());
					}
				}
			}

			students.add(s);
		}
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
