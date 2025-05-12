package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoreTest {

    private Student student;
    private Subject subject;
    private Score score;

    @BeforeEach
    void setUp() {
        // Przygotowujemy dane testowe przed każdym testem
        student = new Student("Jan", "Kowalski", "12345", null);  // Student bez przypisanej grupy
        subject = new Subject("Matematyka");  // Zakładając, że klasa Subject ma konstruktor przyjmujący nazwę przedmiotu
        score = new Score(student, subject, "Zaliczenie", 80);
    }

    // Test sprawdzający metodę toString() klasy Score
    @Test
    void testToString() {
        String expected = "Jan Kowalski | Matematyka | Zaliczenie | 80 pkt";
        assertEquals(expected, score.toString());
    }
    
    // Test sprawdzający, czy metoda toString() działa poprawnie dla innego zestawu danych
    @Test
    void testToStringWithDifferentData() {
        Student anotherStudent = new Student("Anna", "Nowak", "67890", null);
        Subject anotherSubject = new Subject("Informatyka");
        Score anotherScore = new Score(anotherStudent, anotherSubject, "Projekt", 95);
        
        String expected = "Anna Nowak | Informatyka | Projekt | 95 pkt";
        assertEquals(expected, anotherScore.toString());
    }
}

