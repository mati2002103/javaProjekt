package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testowa jednostkowa dla klasy {@link SubjectDB}.
 * Testuje dodawanie, usuwanie, modyfikację i serializację przedmiotów.
 */
class SubjectDBTest {

    private SubjectDB subjectDB;

    /**
     * Inicjalizacja obiektu {@link SubjectDB} przed każdym testem.
     */
    @BeforeEach
    void setUp() {
        subjectDB = new SubjectDB();
    }

    /**
     * Test dodania przedmiotu i pobrania go po nazwie (case-insensitive).
     */
    @Test
    void testAddAndGetSubjectByName() {
        Subject math = new Subject("Matematyka");
        subjectDB.addSubject(math);

        Subject retrieved = subjectDB.getSubjectByName("matematyka");
        assertNotNull(retrieved);
        assertEquals("Matematyka", retrieved.getSubjectName());
    }

    /**
     * Test próby pobrania nieistniejącego przedmiotu.
     */
    @Test
    void testGetSubjectByName_NotFound() {
        assertNull(subjectDB.getSubjectByName("Fizyka"));
    }

    /**
     * Test zwracający wszystkie przedmioty dodane do bazy.
     */
    @Test
    void testGetAllSubjects() {
        subjectDB.addSubject(new Subject("Matematyka"));
        subjectDB.addSubject(new Subject("Fizyka"));
        List<Subject> all = subjectDB.getAllSubjects();
        assertEquals(2, all.size());
    }

    /**
     * Test poprawnej aktualizacji kryteriów oceniania przedmiotu.
     */
    @Test
    void testUpdateSubjectCriteria_Success() {
        Subject subj = new Subject("Biologia");
        subjectDB.addSubject(subj);

        Map<String, Integer> criteria = new HashMap<>();
        criteria.put("Kolokwium", 50);
        criteria.put("Projekt", 30);

        boolean result = subjectDB.updateSubjectCriteria("Biologia", criteria);
        assertTrue(result);

        Subject updated = subjectDB.getSubjectByName("Biologia");
        assertEquals(2, updated.getGradingCriteria().size());
        assertEquals(80, updated.getTotalMaxPoints());
    }

    /**
     * Test nieudanej próby aktualizacji kryteriów dla nieistniejącego przedmiotu.
     */
    @Test
    void testUpdateSubjectCriteria_Failure() {
        Map<String, Integer> criteria = new HashMap<>();
        criteria.put("Egzamin", 60);

        boolean result = subjectDB.updateSubjectCriteria("Nieistniejący", criteria);
        assertFalse(result);
    }

    /**
     * Test poprawnego usunięcia przedmiotu z bazy.
     */
    @Test
    void testDeleteSubject_Success() {
        Subject subj = new Subject("Chemia");
        subjectDB.addSubject(subj);

        boolean result = subjectDB.deleteSubject("Chemia");
        assertTrue(result);
        assertNull(subjectDB.getSubjectByName("Chemia"));
    }

    /**
     * Test nieudanej próby usunięcia nieistniejącego przedmiotu.
     */
    @Test
    void testDeleteSubject_Failure() {
        boolean result = subjectDB.deleteSubject("Geografia");
        assertFalse(result);
    }

    /**
     * Test serializacji i deserializacji bazy danych przedmiotów.
     *
     * @throws IOException gdy wystąpi błąd przy zapisie lub odczycie z/do strumienia
     * @see SubjectDB#saveToFile(DataOutputStream)
     * @see SubjectDB#loadFromFile(DataInputStream)
     */
    @Test
    void testSaveAndLoadFromFile() throws IOException {
        Subject subj1 = new Subject("Programowanie");
        subj1.updateCriterion("Lab", 30);
        subj1.updateCriterion("Egzamin", 70);

        Subject subj2 = new Subject("Systemy Operacyjne");
        subj2.updateCriterion("Projekt", 100);

        subjectDB.addSubject(subj1);
        subjectDB.addSubject(subj2);

        // Serializacja do pamięci
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);
        subjectDB.saveToFile(dataOut);

        // Deserializacja z pamięci
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        DataInputStream dataIn = new DataInputStream(byteIn);
        SubjectDB loadedDB = new SubjectDB();
        loadedDB.loadFromFile(dataIn);

        // Weryfikacja danych po odczycie
        List<Subject> loadedSubjects = loadedDB.getAllSubjects();
        assertEquals(2, loadedSubjects.size());

        Subject loaded1 = loadedDB.getSubjectByName("Programowanie");
        assertNotNull(loaded1);
        assertEquals(100, loaded1.getTotalMaxPoints());

        Subject loaded2 = loadedDB.getSubjectByName("Systemy Operacyjne");
        assertEquals(100, loaded2.getTotalMaxPoints());
    }
}
