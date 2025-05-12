package projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SubjectTest {

    private Subject subject;

    @BeforeEach
    void setUp() {
        subject = new Subject("Matematyka");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Matematyka", subject.getSubjectName());
        assertTrue(subject.getGradingCriteria().isEmpty());
    }

    @Test
    void testSetSubjectName() {
        subject.setSubjectName("Fizyka");
        assertEquals("Fizyka", subject.getSubjectName());
    }

    @Test
    void testUpdateCriterion_AddNew() {
        subject.updateCriterion("Egzamin", 100);
        Map<String, Integer> criteria = subject.getGradingCriteria();
        assertEquals(1, criteria.size());
        assertEquals(100, criteria.get("Egzamin"));
    }

    @Test
    void testUpdateCriterion_UpdateExisting() {
        subject.updateCriterion("Kolokwium", 40);
        subject.updateCriterion("Kolokwium", 50);
        assertEquals(1, subject.getGradingCriteria().size());
        assertEquals(50, subject.getGradingCriteria().get("Kolokwium"));
    }

    @Test
    void testRemoveCriterion() {
        subject.updateCriterion("Projekt", 60);
        subject.removeCriterion("Projekt");
        assertFalse(subject.getGradingCriteria().containsKey("Projekt"));
    }

    @Test
    void testGetTotalMaxPoints() {
        subject.updateCriterion("Egzamin", 70);
        subject.updateCriterion("Kolokwium", 20);
        subject.updateCriterion("Projekt", 10);
        assertEquals(100, subject.getTotalMaxPoints());
    }

    @Test
    void testSetGradingCriteria() {
        Map<String, Integer> newCriteria = new LinkedHashMap<>();
        newCriteria.put("Test", 30);
        newCriteria.put("Lab", 20);

        subject.setGradingCriteria(newCriteria);

        assertEquals(2, subject.getGradingCriteria().size());
        assertEquals(50, subject.getTotalMaxPoints());
    }
}
