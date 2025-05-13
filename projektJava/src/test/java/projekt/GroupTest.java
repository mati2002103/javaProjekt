package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Klasa testowa do testowania funkcjonalności klasy {@link Group}. Sprawdza
 * poprawność konstruktora, dodawanie studentów oraz obsługę duplikatów.
 *
 *
 * @author Damian Jokisz
 * 
 * @see Group
 * @see Student
 * 
 * 
 * 
 * 
 * 
 */
class GroupTest {

	private Group group;
	private Student student;

	/**
	 * Inicjalizacja wspólnych danych testowych przed każdym testem. Tworzy
	 * przykładową grupę oraz przypisanego do niej studenta.
	 *
	 * @see Group#Group(String, String, String)
	 * @see Student#Student(String, String, String, Group)
	 */
	@BeforeEach
	void setUp() {
		group = new Group("ID05TC01", "Technologia chmury", "Grupa dla studentów zainteresowanych chmurą");
		student = new Student("Jan", "Kowalski", "123456", group);
	}

	/**
	 * Testuje konstruktor klasy {@link Group}. Sprawdza poprawność przypisania
	 * wartości do pól oraz czy zbiór studentów jest pusty.
	 *
	 * @see Group#Group(String, String, String)
	 * @see Group#getGroupCode()
	 * @see Group#getSpecialization()
	 * @see Group#getDescription()
	 * @see Group#getStudents()
	 */
	@Test
	void testGroupConstructorWithArguments() {
		assertNotNull(group);
		assertEquals("ID05TC01", group.getGroupCode());
		assertEquals("Technologia chmury", group.getSpecialization());
		assertEquals("Grupa dla studentów zainteresowanych chmurą", group.getDescription());
		assertTrue(group.getStudents().isEmpty());
	}

	/**
	 * Testuje metodę {@link Group#addStudent(Student)}. Sprawdza, czy student
	 * został poprawnie dodany do zbioru studentów w grupie.
	 *
	 * @see Group#addStudent(Student)
	 * @see Group#getStudents()
	 * @see Student
	 */
	@Test
	void testAddStudent() {
		group.addStudent(student);
		Set<Student> students = group.getStudents();

		assertEquals(1, students.size());
		assertTrue(students.contains(student));
	}

	/**
	 * Testuje dodanie tego samego studenta więcej niż jeden raz. Sprawdza, czy
	 * zbiór studentów nie zawiera duplikatów (zbiór Set).
	 *
	 * @see Group#addStudent(Student)
	 * @see Group#getStudents()
	 */
	@Test
	void testAddDuplicateStudent() {
		group.addStudent(student);
		group.addStudent(student);

		Set<Student> students = group.getStudents();
		assertEquals(1, students.size());
	}
}
