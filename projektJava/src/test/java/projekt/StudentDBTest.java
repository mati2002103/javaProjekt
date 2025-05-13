package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

/**
 * Klasa testowa jednostkowa dla {@link StudentDB}. Testuje operacje dodawania,
 * usuwania, wyszukiwania oraz serializacji danych studenta.
 * 
 * @author Damian Jokisz
 */
class StudentDBTest {

	private StudentDB studentDB;
	private Group group;
	private Subject subject;
	private SubjectDB subjectDB;
	private GroupDB groupDB;

	/**
	 * Inicjalizacja danych testowych przed każdym testem. Tworzy bazę studentów,
	 * grup i przedmiotów, oraz ustawia przykładowe kryteria oceniania.
	 */
	@BeforeEach
	void setUp() {
		studentDB = new StudentDB();
		group = new Group("G1", "Informatyka", "Grupa testowa");

		subject = new Subject("Programowanie");
		subject.getGradingCriteria().put("Zaliczenie", 100); // ustawienie kryterium

		subjectDB = new SubjectDB();
		subjectDB.addSubject(subject);

		groupDB = new GroupDB();
		groupDB.addGroup(group);
	}

	/**
	 * Testuje dodanie studenta do grupy i sprawdza, czy został on poprawnie
	 * przypisany.
	 */
	@Test
	void testAddStudentToGroup() {
		Student student = new Student("Jan", "Kowalski", "123", null);
		studentDB.addStudentToGroup(student, group);

		assertEquals(group, student.getGroup());
		assertTrue(group.getStudents().contains(student));
	}

	/**
	 * Testuje dodanie i usunięcie studenta z listy studentów w bazie.
	 */
	@Test
	void testAddAndDeleteStudentFromList() {
		Student student = new Student("Anna", "Nowak", "456", group);

		studentDB.addStudentToStudentList(student);
		assertTrue(studentDB.getStudents().contains(student));

		studentDB.deleteStudentFromStudentList(student);
		assertFalse(studentDB.getStudents().contains(student));
	}

	/**
	 * Testuje wyszukiwanie studenta po numerze albumu.
	 */
	@Test
	void testGetStudentByAlbumNumber() {
		Student student = new Student("Tomasz", "Lewandowski", "789", group);
		studentDB.addStudentToStudentList(student);

		Student result = studentDB.getStudentByAlbumNumber("789");
		assertNotNull(result);
		assertEquals("Tomasz", result.getName());
	}

	/**
	 * Testuje tworzenie nowego obiektu studenta i sprawdza jego poprawność.
	 */
	@Test
	void testCreateStudent() {
		Student s = studentDB.createStudent("Anna", "Zielińska", "321", group);
		assertEquals("Anna", s.getName());
		assertEquals("321", s.getAlbumNumber());
		assertEquals(group, s.getGroup());
	}

	/**
	 * Testuje poprawność zapisu i odczytu danych studentów do/z pliku (w tym ocen i
	 * grupy).
	 *
	 * @throws Exception jeśli wystąpi błąd wejścia/wyjścia lub deserializacji
	 */
	@Test
	void testSaveAndLoadFromFile() throws Exception {
		// Przygotowanie studenta i przypisanie punktów z użyciem poprawnej instancji
		// przedmiotu
		Student student = new Student("Paweł", "Kowal", "001", group);
		student.addPoints(subject, "Zaliczenie", 90);
		studentDB.addStudentToStudentList(student);

		// Serializacja do pamięci
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		studentDB.saveToFile(out);

		// Deserializacja z pamięci
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		DataInputStream in = new DataInputStream(bais);

		StudentDB loadedDB = new StudentDB();
		loadedDB.loadFromFile(in, groupDB, subjectDB);

		// Walidacja poprawności danych po deserializacji
		assertEquals(1, loadedDB.getStudents().size());
		Student loadedStudent = loadedDB.getStudentByAlbumNumber("001");
		assertNotNull(loadedStudent);
		assertEquals("Paweł", loadedStudent.getName());
		assertEquals(90, loadedStudent.getGrades().get(subject).get("Zaliczenie"));
	}
}
