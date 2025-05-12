package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {

    private Student student;
    private Group group;
    private Subject subject;
    
    @BeforeEach
    void setUp() {
        // Przygotowanie danych testowych
        group = new Group("G01", "Informatyka", "Grupa Informatyki");
        student = new Student("Jan", "Kowalski", "12345", group);
        subject = new Subject("Matematyka");  // Zakładając, że klasa Subject ma konstruktor przyjmujący nazwę przedmiotu
        
        subject.getGradingCriteria().put("Zaliczenie", 100);
    }

    // Test sprawdzający poprawne tworzenie obiektu Student
    @Test
    void testStudentCreation() {
        assertNotNull(student);
        assertEquals("Jan", student.getName());
        assertEquals("Kowalski", student.getSurname());
        assertEquals("12345", student.getAlbumNumber());
        assertEquals(group, student.getGroup());
    }

    // Test sprawdzający metodę addPoints()
    @Test
    void testAddPoints() throws RegisterException {
        assertTrue(student.addPoints(subject, "Zaliczenie", 85));  // Zakładając, że w przedmiocie istnieje kryterium "Zaliczenie"
        assertEquals(85, student.getGrades().get(subject).get("Zaliczenie"));
    }

    // Test sprawdzający, czy metoda addPoints() rzuca wyjątek dla niepoprawnych punktów
    @Test
    void testAddPointsThrowsException() {
        try {
            student.addPoints(subject, "Zaliczenie", -5);  // Niepoprawne punkty
            fail("Oczekiwano wyjątku RegisterException");
        } catch (RegisterException e) {
            assertEquals("Niepoprawna liczba punktów lub brak kryterium w przedmiocie", e.getMessage());
        }
    }

    // Test sprawdzający, czy metoda addPoints() rzuca wyjątek, jeśli brak kryterium
    @Test
    void testAddPointsThrowsExceptionForMissingCriteria() {
        try {
            student.addPoints(subject, "NieistniejąceKryterium", 50);  // Nieistniejące kryterium
            fail("Oczekiwano wyjątku RegisterException");
        } catch (RegisterException e) {
            assertEquals("Niepoprawna liczba punktów lub brak kryterium w przedmiocie", e.getMessage());
        }
    }

    // Test sprawdzający metodę createStudent() w klasie Student
    @Test
    void testCreateStudent() {
        Student newStudent = student.createStudent("Anna", "Nowak", "67890", group);
        assertNotNull(newStudent);
        assertEquals("Anna", newStudent.getName());
        assertEquals("Nowak", newStudent.getSurname());
        assertEquals("67890", newStudent.getAlbumNumber());
        assertEquals(group, newStudent.getGroup());
    }

    // Test sprawdzający metodę getGrades()
    @Test
    void testGetGrades() throws RegisterException {
        student.addPoints(subject, "Zaliczenie", 75);  // Dodajemy punkty do przedmiotu
        assertNotNull(student.getGrades().get(subject));
        assertEquals(75, student.getGrades().get(subject).get("Zaliczenie"));
    }
}
