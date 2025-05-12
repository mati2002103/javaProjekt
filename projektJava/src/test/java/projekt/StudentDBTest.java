package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class StudentDBTest {

    private StudentDB studentDB;
    private Group group;
    private Subject subject;

    @BeforeEach
    void setUp() {
        studentDB = new StudentDB();
        group = new Group("G1", "Informatyka", "Grupa testowa");
        subject = new Subject("Programowanie");
        subject.getGradingCriteria().put("Zaliczenie", 100);
    }

    @Test
    void testAddStudentToGroup() {
        Student student = new Student("Jan", "Kowalski", "123", null);
        studentDB.addStudentToGroup(student, group);

        assertEquals(group, student.getGroup());
        assertTrue(group.getStudents().contains(student));
    }

    @Test
    void testAddAndDeleteStudentFromList() {
        Student student = new Student("Anna", "Nowak", "456", group);

        studentDB.addStudentToStudentList(student);
        assertTrue(studentDB.getStudents().contains(student));

        studentDB.deleteStudentFromStudentList(student);
        assertFalse(studentDB.getStudents().contains(student));
    }

    @Test
    void testGetStudentByAlbumNumber() {
        Student student = new Student("Tomasz", "Lewandowski", "789", group);
        studentDB.addStudentToStudentList(student);

        Student result = studentDB.getStudentByAlbumNumber("789");
        assertNotNull(result);
        assertEquals("Tomasz", result.getName());
    }

    @Test
    void testCreateStudent() {
        Student s = studentDB.createStudent("Anna", "Zielińska", "321", group);
        assertEquals("Anna", s.getName());
        assertEquals("321", s.getAlbumNumber());
        assertEquals(group, s.getGroup());
    }

    @Test
    void testSaveAndLoadFromFile() throws Exception {
        // Przygotuj dane
        Student student = new Student("Paweł", "Kowal", "001", group);
        student.addPoints(subject, "Zaliczenie", 90);
        studentDB.addStudentToStudentList(student);

        // Serializacja do strumienia w pamięci
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        studentDB.saveToFile(out);

        // Deserializacja z tego samego strumienia
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DataInputStream in = new DataInputStream(bais);

        StudentDB loadedDB = new StudentDB();
        GroupDB groupDB = new GroupDB();
        groupDB.addGroup(group);

        SubjectDB subjectDB = new SubjectDB();
        subjectDB.addSubject(subject);

        loadedDB.loadFromFile(in, groupDB, subjectDB);

        assertEquals(1, loadedDB.getStudents().size());
        Student loadedStudent = loadedDB.getStudentByAlbumNumber("001");
        assertNotNull(loadedStudent);
        assertEquals("Paweł", loadedStudent.getName());
        assertEquals(90, loadedStudent.getGrades().get(subject).get("Zaliczenie"));
    }
}
