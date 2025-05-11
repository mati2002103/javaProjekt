package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca okienko dialogowe związane ze Sekcją Studenta,
 * w której można dodawać, edytować, usuwać studentów oraz importować i eksportować dane.
 */
public class StudentPanel extends JPanel {

    private MyWindow parentWindow;
    private DefaultTableModel studentTableModel;
    private List<Student> studentList;
    private StudentDB studentDB;

    public StudentPanel(MyWindow parentWindow, StudentDB studentDB) {
        this.parentWindow = parentWindow;
        this.studentList = new ArrayList<>();
        this.studentDB = studentDB; // Inicjalizacja bazy danych studentów

        setLayout(new BorderLayout());

        // Model tabeli dla studentów
        studentTableModel = new DefaultTableModel(new String[]{"Imię", "Nazwisko", "Numer Albumu", "Grupa"}, 0);
        JTable studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel z przyciskami
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Studenta");
        JButton editButton = new JButton("Edytuj Studenta");
        JButton deleteButton = new JButton("Usuń Studenta");
        JButton scoreButton = new JButton("Pokaż Wyniki");
        JButton assignButton = new JButton("Przypisz wynik");
        JButton importButton = new JButton("Importuj");
        JButton exportButton = new JButton("Eksportuj");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(scoreButton);
        buttonPanel.add(assignButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Obsługa przycisków
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent(studentTable));
        deleteButton.addActionListener(e -> deleteStudent(studentTable));
        scoreButton.addActionListener(e -> showStudentScores(studentTable));
        assignButton.addActionListener(e -> openAssignSubjectPanel(studentTable)); // Obsługa przycisku "Przypisz wynik"
        importButton.addActionListener(e -> importStudents());
        exportButton.addActionListener(e -> exportStudents());
        backButton.addActionListener(e -> parentWindow.showMenu());
    }

    // Tworzymy nowego studenta
    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField albumNumberField = new JTextField();
        JTextField groupField = new JTextField();

        Object[] message = {
                "Imię:", nameField,
                "Nazwisko:", surnameField,
                "Numer Albumu:", albumNumberField,
                "Grupa:", groupField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Dodaj Studenta", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String albumNumber = albumNumberField.getText().trim();
            String groupCode = groupField.getText().trim();

            // Sprawdzamy, czy wymagane pola nie są puste
            if (name.isEmpty() || surname.isEmpty() || albumNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Imię, nazwisko i numer albumu muszą być wypełnione!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = groupCode.isEmpty() ? null : new Group(groupCode, "", "");
            Student student = new Student(name, surname, albumNumber, group);
            studentList.add(student);
            studentDB.addStudentToStudentList(student);
            studentTableModel.addRow(new Object[]{name, surname, albumNumber, groupCode.isEmpty() ? "Brak grupy" : groupCode});
            JOptionPane.showMessageDialog(this, "Student został pomyślnie dodany!");
        }
    }

    // Edytujemy dane studenta tylko po jego wybraniu w tabeli
    private void editStudent(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Wybierz studenta do edycji!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = studentList.get(selectedRow);

        JTextField nameField = new JTextField(student.getName());
        JTextField surnameField = new JTextField(student.getSurname());
        JTextField albumNumberField = new JTextField(student.getAlbumNumber());
        JTextField groupField = new JTextField(student.getGroup() != null ? student.getGroup().getGroupCode() : "");

        Object[] message = {
                "Imię:", nameField,
                "Nazwisko:", surnameField,
                "Numer Albumu:", albumNumberField,
                "Grupa:", groupField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edytuj Studenta", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String albumNumber = albumNumberField.getText().trim();
            String groupCode = groupField.getText().trim();

            // Sprawdzamy, czy wymagane pola nie są puste
            if (name.isEmpty() || surname.isEmpty() || albumNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Imię, nazwisko i numer albumu muszą być wypełnione!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            student.setName(name);
            student.setSurname(surname);
            student.setAlbumNumber(albumNumber);
            student.setGroup(groupCode.isEmpty() ? null : new Group(groupCode, "", ""));

            studentTableModel.setValueAt(name, selectedRow, 0);
            studentTableModel.setValueAt(surname, selectedRow, 1);
            studentTableModel.setValueAt(albumNumber, selectedRow, 2);
            studentTableModel.setValueAt(groupCode.isEmpty() ? "Brak grupy" : groupCode, selectedRow, 3);

            JOptionPane.showMessageDialog(this, "Student został pomyślnie zaktualizowany!");
        }
    }

    // Usuwanie studenta
    private void deleteStudent(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Wybierz studenta do usunięcia!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz usunąć wybranego studenta?",
                "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            Student student = studentList.remove(selectedRow);
            studentDB.deleteStudentFromStudentList(student);
            studentTableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Student został pomyślnie usunięty!");
        }
    }

    // Import studentów
    private void importStudents() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                studentDB.loadFromFile(in, new GroupDB(), new SubjectDB());
                refreshStudentTable();
                JOptionPane.showMessageDialog(this, "Studenci zostali pomyślnie zaimportowani!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Błąd podczas importowania studentów: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Eksport studentów
    private void exportStudents() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                studentDB.saveToFile(out);
                JOptionPane.showMessageDialog(this, "Studenci zostali pomyślnie wyeksportowani!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Błąd podczas eksportowania studentów: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Wyświetlanie wyników studenta
    private void showStudentScores(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Wybierz studenta, aby zobaczyć jego wyniki!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student selectedStudent = studentList.get(selectedRow);

        // Tworzymy nowe okno dialogowe
        JDialog scoreDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Wyniki Studenta", true);
        scoreDialog.setSize(400, 300);
        scoreDialog.setLocationRelativeTo(this);

        // Dodajemy panel z wynikami
        // StudentScorePanel scorePanel = new StudentScorePanel(selectedStudent);
        // scoreDialog.add(scorePanel);

        // Wyświetlamy okno dialogowe
        scoreDialog.setVisible(true);
    }

    // Otwieranie panelu AssignSubjectPanel
    private void openAssignSubjectPanel(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Wybierz studenta, aby przypisać wynik!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student selectedStudent = studentList.get(selectedRow);

        // Tworzymy nowe okno dialogowe dla AssignSubjectPanel
        JDialog assignSubjectDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Przypisz Wynik", true);
        assignSubjectDialog.setSize(600, 400);
        assignSubjectDialog.setLocationRelativeTo(this);

//        // Tworzymy i dodajemy panel AssignSubjectPanel
//        AssignSubjectPanel assignSubjectPanel = new AssignSubjectPanel(selectedStudent, subjectList);
//        assignSubjectDialog.add(assignSubjectPanel);

        // Wyświetlamy okno dialogowe
        assignSubjectDialog.setVisible(true);
    }



    // Odświeżanie tabeli studentów
    private void refreshStudentTable() {
        studentTableModel.setRowCount(0); // Czyści tabelę
        for (Student student : studentDB.getStudents()) {
            studentTableModel.addRow(new Object[]{
                    student.getName(),
                    student.getSurname(),
                    student.getAlbumNumber(),
                    student.getGroup() != null ? student.getGroup().getGroupCode() : "Brak grupy"
            });
        }
    }
}