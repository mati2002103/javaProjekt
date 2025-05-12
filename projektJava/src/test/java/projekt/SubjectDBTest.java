package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SubjectDBTest {

    private SubjectDB subjectDB;

    @BeforeEach
    void setUp() {
        subjectDB = new SubjectDB();
    }

    @Test
    void testAddAndGetSubjectByName() {
        Subject math = new Subject("Matematyka");
        subjectDB.addSubject(math);

        Subject retrieved = subjectDB.getSubjectByName("matematyka");
        assertNotNull(retrieved);
        assertEquals("Matematyka", retrieved.getSubjectName());
    }

    @Test
    void testGetSubjectByName_NotFound() {
        assertNull(subjectDB.getSubjectByName("Fizyka"));
    }

    @Test
    void testGetAllSubjects() {
        subjectDB.addSubject(new Subject("Matematyka"));
        subjectDB.addSubject(new Subject("Fizyka"));
        List<Subject> all = subjectDB.getAllSubjects();
        assertEquals(2, all.size());
    }

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

    @Test
    void testUpdateSubjectCriteria_Failure() {
        Map<String, Integer> criteria = new HashMap<>();
        criteria.put("Egzamin", 60);

        boolean result = subjectDB.updateSubjectCriteria("Nieistniejący", criteria);
        assertFalse(result);
    }

    @Test
    void testDeleteSubject_Success() {
        Subject subj = new Subject("Chemia");
        subjectDB.addSubject(subj);

        boolean result = subjectDB.deleteSubject("Chemia");
        assertTrue(result);
        assertNull(subjectDB.getSubjectByName("Chemia"));
    }

    @Test
    void testDeleteSubject_Failure() {
        boolean result = subjectDB.deleteSubject("Geografia");
        assertFalse(result);
    }

    @Test
    void testSaveAndLoadFromFile() throws IOException {
        Subject subj1 = new Subject("Programowanie");
        subj1.updateCriterion("Lab", 30);
        subj1.updateCriterion("Egzamin", 70);

        Subject subj2 = new Subject("Systemy Operacyjne");
        subj2.updateCriterion("Projekt", 100);

        subjectDB.addSubject(subj1);
        subjectDB.addSubject(subj2);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(byteOut);

        subjectDB.saveToFile(dataOut);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        DataInputStream dataIn = new DataInputStream(byteIn);

        SubjectDB loadedDB = new SubjectDB();
        loadedDB.loadFromFile(dataIn);

        List<Subject> loadedSubjects = loadedDB.getAllSubjects();
        assertEquals(2, loadedSubjects.size());

        Subject loaded1 = loadedDB.getSubjectByName("Programowanie");
        assertNotNull(loaded1);
        assertEquals(100, loaded1.getTotalMaxPoints());

        Subject loaded2 = loadedDB.getSubjectByName("Systemy Operacyjne");
        assertEquals(100, loaded2.getTotalMaxPoints());
    }
}
