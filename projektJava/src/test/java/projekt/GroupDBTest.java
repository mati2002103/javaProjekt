package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

/**
 * Klasa testowa dla klasy GroupDB, sprawdzająca poprawność operacji dodawania,
 * usuwania, aktualizacji, wyszukiwania i serializacji grup studenckich.
 */
class GroupDBTest {

    private GroupDB groupDB;
    private Group group;
    private Student student;

    /**
     * Inicjalizuje obiekty testowe przed każdym testem:
     * tworzy bazę grup, grupę oraz przypisanego do niej studenta.
     */
    @BeforeEach
    void setUp() {
        groupDB = new GroupDB();
        group = new Group("ID05TC01", "Technologia chmury", "Grupa dla studentów zainteresowanych chmurą");
        student = new Student("Jan", "Kowalski", "123456", group);
        group.addStudent(student);
    }

    /**
     * Testuje dodawanie grupy do bazy danych.
     *
     * @see GroupDB#addGroup(Group)
     */
    @Test
    void testAddGroup() {
        assertTrue(groupDB.getAllGroups().isEmpty());
        groupDB.addGroup(group);
        List<Group> groups = groupDB.getAllGroups();
        assertEquals(1, groups.size());
        assertTrue(groups.contains(group));
    }

    /**
     * Testuje poprawność wyszukiwania grupy po jej kodzie.
     *
     * @see GroupDB#getGroupByCode(String)
     */
    @Test
    void testGetGroupByCode() {
        groupDB.addGroup(group);
        Group foundGroup = groupDB.getGroupByCode("ID05TC01");
        assertNotNull(foundGroup);
        assertEquals(group, foundGroup);
    }

    /**
     * Testuje aktualizowanie specjalizacji i opisu grupy.
     *
     * @see GroupDB#updateGroup(String, String, String)
     */
    @Test
    void testUpdateGroup() {
        groupDB.addGroup(group);
        assertEquals("Technologia chmury", group.getSpecialization());
        assertEquals("Grupa dla studentów zainteresowanych chmurą", group.getDescription());

        boolean updated = groupDB.updateGroup("ID05TC01", "Technologie webowe", "Grupa dla studentów zajmujących się technologiami webowymi");

        assertTrue(updated);
        assertEquals("Technologie webowe", group.getSpecialization());
        assertEquals("Grupa dla studentów zajmujących się technologiami webowymi", group.getDescription());
    }

    /**
     * Testuje usuwanie grupy z bazy danych.
     *
     * @see GroupDB#deleteGroup(String)
     */
    @Test
    void testDeleteGroup() {
        groupDB.addGroup(group);
        assertEquals(1, groupDB.getAllGroups().size());

        boolean deleted = groupDB.deleteGroup("ID05TC01");

        assertTrue(deleted);
        assertTrue(groupDB.getAllGroups().isEmpty());
    }

    /**
     * Testuje zapis bazy danych do pliku i jej ponowne wczytanie.
     *
     * @throws IOException jeśli wystąpi błąd operacji plikowych
     * @see GroupDB#saveToFile(DataOutputStream)
     * @see GroupDB#loadFromFile(DataInputStream)
     */
    @Test
    void testSaveAndLoadFromFile() throws IOException {
        groupDB.addGroup(group);

        File tempFile = File.createTempFile("testGroups", ".dat");
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile))) {
            groupDB.saveToFile(out);
        }

        GroupDB loadedGroupDB = new GroupDB();
        try (DataInputStream in = new DataInputStream(new FileInputStream(tempFile))) {
            loadedGroupDB.loadFromFile(in);
        }

        List<Group> loadedGroups = loadedGroupDB.getAllGroups();
        assertEquals(1, loadedGroups.size());
        Group loadedGroup = loadedGroups.get(0);
        assertEquals("ID05TC01", loadedGroup.getGroupCode());
        assertEquals("Technologia chmury", loadedGroup.getSpecialization());
    }
}
