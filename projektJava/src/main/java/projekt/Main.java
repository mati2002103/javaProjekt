package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


//KLASA MAIN SWTORZONA DO CELÓW TESTOW

public class Main {
    public static void main(String[] args) {
        GroupDB groupDB = new GroupDB();
        SubjectDB subjectDB = new SubjectDB();
        StudentDB studentDB = new StudentDB();

        // === PRZYKŁADOWE DANE ===
        Group g = new Group("ID05TC01", "Technologia chmury", "Opis grupy");
        groupDB.addGroup(g);

        Subject subj = new Subject("Język Java");
        subjectDB.addSubject(subj);

        Student s = new Student("Jan", "Kowalski", "12345", g);
        try {
            s.addPoints(subj, Criteria.test, 25);
            s.addPoints(subj, Criteria.exam, 45);
        } catch (RegisterException e) {
            e.printStackTrace();
        }
        studentDB.addStudentToStudentList(s);

        // === ZAPIS DO PLIKU ===
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("dane.bin"))) {
            groupDB.saveToFile(out);
            subjectDB.saveToFile(out);
            studentDB.saveToFile(out);
            System.out.println("Dane zapisane do pliku.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // === WCZYTANIE Z PLIKU ===
        GroupDB loadedGroupDB = new GroupDB();
        SubjectDB loadedSubjectDB = new SubjectDB();
        StudentDB loadedStudentDB = new StudentDB();

        try (DataInputStream in = new DataInputStream(new FileInputStream("dane.bin"))) {
            loadedGroupDB.loadFromFile(in);
            loadedSubjectDB.loadFromFile(in);
            loadedStudentDB.loadFromFile(in, loadedGroupDB, loadedSubjectDB);
            System.out.println("Dane wczytane z pliku.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // === TEST WYSZUKIWANIA ===
        System.out.println("\n=== WYNIKI WYSZUKIWANIA ===");

        // Przykładowe wyszukiwanie: imię "Jan", grupa zaczyna się na "ID05", przedmiot "Język Java", kryterium TEST, minimum 20 pkt
        Set<Student> znalezieni = loadedStudentDB.searchStudents(
                null,   // imię
                null,    // nazwisko
                null,    // album
                null,  // kod grupy
                null,    // specjalizacja
                null, // przedmiot
                null, // kryterium
                null,      // min. punktów
                loadedSubjectDB);

        for (Student student : znalezieni) {
            System.out.println("Student: " + student.getName() + " " + student.getSurname());
            System.out.println("Nr albumu: " + student.getAlbumNumber());
            if (student.getGroup() != null) {
                System.out.println("Grupa: " + student.getGroup().getGroupCode() + " (" + student.getGroup().getSpecialization() + ")");
            }
            System.out.println("Oceny:");
            for (Map.Entry<Subject, Map<String, Integer>> entry : student.getGrades().entrySet()) {
                System.out.println("  Przedmiot: " + entry.getKey().getSubjectName());
                for (Map.Entry<String, Integer> grade : entry.getValue().entrySet()) {
                    System.out.println("    " + grade.getKey() + ": " + grade.getValue());
                }
            }
            System.out.println("-------------------------");
        }
    }
}
