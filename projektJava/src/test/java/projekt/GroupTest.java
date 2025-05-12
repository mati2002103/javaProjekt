package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class GroupTest {

    private Group group;
    private Student student;

    // Ustawiamy wspólne dane przed każdym testem
    @BeforeEach
    void setUp() {
        // Tworzymy nową grupę z przykładowymi danymi
        group = new Group("ID05TC01", "Technologia chmury", "Grupa dla studentów zainteresowanych chmurą");
        
        // Tworzymy studenta przypisanego do tej grupy
        student = new Student("Jan", "Kowalski", "123456", group);
    }

    // Testujemy konstruktor klasy Group, który inicjalizuje grupę z parametrami
    @Test
    void testGroupConstructorWithArguments() {
        // Sprawdzamy, czy grupy zostały poprawnie przypisane
        assertNotNull(group);
        assertEquals("ID05TC01", group.getGroupCode());
        assertEquals("Technologia chmury", group.getSpecialization());
        assertEquals("Grupa dla studentów zainteresowanych chmurą", group.getDescription());
        
        // Sprawdzamy, czy grupa nie ma jeszcze przypisanych studentów
        assertTrue(group.getStudents().isEmpty());
    }

    // Testujemy dodanie studenta do grupy
    @Test
    void testAddStudent() {
        group.addStudent(student);
        
        // Pobieramy listę studentów przypisanych do grupy
        Set<Student> students = group.getStudents();
        
        // Sprawdzamy, czy lista studentów zawiera właśnie dodanego studenta
        assertEquals(1, students.size()); // Lista powinna zawierać 1 studenta
        assertTrue(students.contains(student)); // Grupa powinna zawierać tego studenta
    }

    // Testujemy, co się stanie, gdy spróbujemy dodać tego samego studenta więcej niż raz
    @Test
    void testAddDuplicateStudent() {
        group.addStudent(student);
        group.addStudent(student); // próbujemy dodać tego samego studenta ponownie
        
        // Sprawdzamy, czy lista studentów nadal zawiera tylko 1 studenta
        Set<Student> students = group.getStudents();
        assertEquals(1, students.size()); // Powinna być tylko 1 kopia studenta
    }
}
