package projekt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

class GroupDBTest {

    private GroupDB groupDB;
    private Group group;
    private Student student;

    // Przygotowanie danych testowych przed każdym testem
    @BeforeEach
    void setUp() {
        // Tworzymy nową bazę grup
        groupDB = new GroupDB();
        
        // Tworzymy grupę
        group = new Group("ID05TC01", "Technologia chmury", "Grupa dla studentów zainteresowanych chmurą");
        
        // Tworzymy studenta i przypisujemy go do grupy
        student = new Student("Jan", "Kowalski", "123456", group);
        group.addStudent(student); // Dodajemy studenta do grupy
    }

    // Testujemy dodawanie grupy do bazy
    @Test
    void testAddGroup() {
        // Sprawdzamy, czy początkowo baza grup jest pusta
        assertTrue(groupDB.getAllGroups().isEmpty());
        
        // Dodajemy grupę do bazy
        groupDB.addGroup(group);
        
        // Sprawdzamy, czy grupa została dodana
        List<Group> groups = groupDB.getAllGroups();
        assertEquals(1, groups.size()); // Powinna zawierać 1 grupę
        assertTrue(groups.contains(group)); // Grupa powinna być w bazie
    }

    // Testujemy metodę getGroupByCode
    @Test
    void testGetGroupByCode() {
        // Dodajemy grupę do bazy
        groupDB.addGroup(group);
        
        // Sprawdzamy, czy grupa została poprawnie odnaleziona po kodzie
        Group foundGroup = groupDB.getGroupByCode("ID05TC01");
        assertNotNull(foundGroup); // Grupa powinna zostać znaleziona
        assertEquals(group, foundGroup); // Sprawdzamy, czy odnaleziona grupa to ta sama
    }

    // Testujemy metodę updateGroup
    @Test
    void testUpdateGroup() {
        // Dodajemy grupę do bazy
        groupDB.addGroup(group);
        
        // Sprawdzamy, czy początkowe dane grupy są takie, jak oczekiwano
        assertEquals("Technologia chmury", group.getSpecialization());
        assertEquals("Grupa dla studentów zainteresowanych chmurą", group.getDescription());

        // Aktualizujemy dane grupy
        boolean updated = groupDB.updateGroup("ID05TC01", "Technologie webowe", "Grupa dla studentów zajmujących się technologiami webowymi");
        
        // Sprawdzamy, czy aktualizacja się powiodła
        assertTrue(updated); 
        
        // Sprawdzamy, czy dane grupy zostały zaktualizowane
        assertEquals("Technologie webowe", group.getSpecialization());
        assertEquals("Grupa dla studentów zajmujących się technologiami webowymi", group.getDescription());
    }

    // Testujemy metodę deleteGroup
    @Test
    void testDeleteGroup() {
        // Dodajemy grupę do bazy
        groupDB.addGroup(group);
        
        // Sprawdzamy, czy grupa została dodana
        assertEquals(1, groupDB.getAllGroups().size());
        
        // Usuwamy grupę z bazy
        boolean deleted = groupDB.deleteGroup("ID05TC01");
        
        // Sprawdzamy, czy usunięcie się powiodło
        assertTrue(deleted);
        
        // Sprawdzamy, czy grupa została usunięta
        assertTrue(groupDB.getAllGroups().isEmpty()); // Baza powinna być pusta
    }

    // Testujemy metodę saveToFile i loadFromFile
    @Test
    void testSaveAndLoadFromFile() throws IOException {
        // Dodajemy grupę do bazy
        groupDB.addGroup(group);
        
        // Zapisujemy dane do pliku tymczasowego
        File tempFile = File.createTempFile("testGroups", ".dat");
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(tempFile))) {
            groupDB.saveToFile(out);
        }
        
        // Wczytujemy dane z pliku do nowej instancji GroupDB
        GroupDB loadedGroupDB = new GroupDB();
        try (DataInputStream in = new DataInputStream(new FileInputStream(tempFile))) {
            loadedGroupDB.loadFromFile(in);
        }
        
        // Sprawdzamy, czy dane zostały poprawnie załadowane
        List<Group> loadedGroups = loadedGroupDB.getAllGroups();
        assertEquals(1, loadedGroups.size()); // Powinna zawierać 1 grupę
        Group loadedGroup = loadedGroups.get(0);
        assertEquals("ID05TC01", loadedGroup.getGroupCode()); // Sprawdzamy kod grupy
        assertEquals("Technologia chmury", loadedGroup.getSpecialization()); // Sprawdzamy specjalizację
    }
}
