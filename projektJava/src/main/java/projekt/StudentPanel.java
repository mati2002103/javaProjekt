package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel GUI służący do zarządzania listą studentów – umożliwia dodawanie, edytowanie, usuwanie oraz podgląd wyników.
 * <p>
 * Studenci są pobierani i zapisywani za pomocą {@link StudentDB}, {@link GroupDB} oraz {@link SubjectDB}.
 * </p>
 * 
 * @author
 *     Wiśniewski Mateusz
 */
public class StudentPanel extends JPanel {

    /** Referencja do głównego okna aplikacji */
    private MyWindow parentWindow;

    /** Baza danych studentów */
    private StudentDB studentDB;

    /** Baza danych przedmiotów */
    private SubjectDB subjectDB;

    /** Baza danych grup */
    private GroupDB groupDB;

    /** Model tabeli studentów */
    private DefaultTableModel studentTableModel;

    /** Tabela wyświetlająca studentów */
    private JTable studentTable;

    /**
     * Konstruktor panelu studentów.
     *
     * @param parentWindow główne okno aplikacji
     * @param studentDB    baza danych studentów
     * @param subjectDB    baza danych przedmiotów
     * @param groupDB      baza danych grup
     */
    public StudentPanel(MyWindow parentWindow, StudentDB studentDB, SubjectDB subjectDB, GroupDB groupDB) {
        this.parentWindow = parentWindow;
        this.studentDB = studentDB;
        this.subjectDB = subjectDB;
        this.groupDB = groupDB;

        setLayout(new BorderLayout());

        studentTableModel = new DefaultTableModel(new String[]{"Imię", "Nazwisko", "Numer Albumu", "Grupa"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 2; // numer albumu jest nieedytowalny
            }
        };

        studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Studenta");
        JButton editButton = new JButton("Edytuj");
        JButton deleteButton = new JButton("Usuń");
        JButton showScoresButton = new JButton("Wyniki");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showScoresButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        showScoresButton.addActionListener(e -> showScoresForSelectedStudent());
        backButton.addActionListener(e -> parentWindow.showMenu());
    }

    /**
     * Wyświetla formularz i dodaje nowego studenta na podstawie danych wprowadzonych przez użytkownika.
     */
    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField albumField = new JTextField();

        JComboBox<String> groupComboBox = new JComboBox<>();
        groupComboBox.addItem("Brak");
        for (Group g : groupDB.getAllGroups()) {
            groupComboBox.addItem(g.getGroupCode());
        }

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Imię:"));
        panel.add(nameField);
        panel.add(new JLabel("Nazwisko:"));
        panel.add(surnameField);
        panel.add(new JLabel("Numer Albumu:"));
        panel.add(albumField);
        panel.add(new JLabel("Grupa:"));
        panel.add(groupComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Dodaj Studenta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String album = albumField.getText().trim();
            String groupCode = (String) groupComboBox.getSelectedItem();

            if (name.isEmpty() || surname.isEmpty() || album.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = "Brak".equals(groupCode) ? null : groupDB.getGroupByCode(groupCode);
            Student newStudent = new Student(name, surname, album, group);
            studentDB.addStudentToStudentList(newStudent);
            if (group != null) group.addStudent(newStudent);

            studentTableModel.addRow(new Object[]{
                name, surname, album, group != null ? group.getGroupCode() : "Brak"
            });
        }
    }

    /**
     * Edytuje dane zaznaczonego studenta.
     * Pokazuje formularz z aktualnymi danymi i zapisuje zmiany.
     */
    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz studenta do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentName = studentTableModel.getValueAt(selectedRow, 0).toString();
        String currentSurname = studentTableModel.getValueAt(selectedRow, 1).toString();
        String currentAlbum = studentTableModel.getValueAt(selectedRow, 2).toString();

        Student student = studentDB.getStudentByAlbumNumber(currentAlbum);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono studenta!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(currentName);
        JTextField surnameField = new JTextField(currentSurname);
        JTextField albumField = new JTextField(currentAlbum);
        albumField.setEditable(false);

        JComboBox<String> groupComboBox = new JComboBox<>();
        groupComboBox.addItem("Brak");
        for (Group g : groupDB.getAllGroups()) {
            groupComboBox.addItem(g.getGroupCode());
        }
        groupComboBox.setSelectedItem(student.getGroup() != null ? student.getGroup().getGroupCode() : "Brak");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Imię:"));
        panel.add(nameField);
        panel.add(new JLabel("Nazwisko:"));
        panel.add(surnameField);
        panel.add(new JLabel("Numer Albumu:"));
        panel.add(albumField);
        panel.add(new JLabel("Grupa:"));
        panel.add(groupComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edytuj Studenta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newSurname = surnameField.getText().trim();
            String selectedGroup = (String) groupComboBox.getSelectedItem();

            if (newName.isEmpty() || newSurname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Imię i nazwisko nie mogą być puste!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            student.setName(newName);
            student.setSurname(newSurname);

            if (!"Brak".equals(selectedGroup)) {
                Group group = groupDB.getGroupByCode(selectedGroup);
                if (group != null) {
                    student.setGroup(group);
                    group.addStudent(student);
                }
            } else {
                student.setGroup(null);
            }

            studentTableModel.setValueAt(newName, selectedRow, 0);
            studentTableModel.setValueAt(newSurname, selectedRow, 1);
            studentTableModel.setValueAt(student.getGroup() != null ? student.getGroup().getGroupCode() : "Brak", selectedRow, 3);
        }
    }

    /**
     * Usuwa zaznaczonego studenta z bazy danych i z tabeli.
     * Pokazuje komunikat o powodzeniu.
     */
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz studenta do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String albumNumber = studentTableModel.getValueAt(selectedRow, 2).toString();
        Student student = studentDB.getStudentByAlbumNumber(albumNumber);

        if (student != null) {
            studentDB.deleteStudentFromStudentList(student);
            studentTableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Student został usunięty.");
        }
    }

    /**
     * Wyświetla okno dialogowe z wynikami studenta zaznaczonego w tabeli.
     */
    private void showScoresForSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz studenta, dla którego chcesz wyświetlić wyniki!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String albumNumber = studentTableModel.getValueAt(selectedRow, 2).toString();
        Student student = studentDB.getStudentByAlbumNumber(albumNumber);

        if (student == null) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono studenta!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Subject> subjects = subjectDB.getAllSubjects();
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Wyniki studenta", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(new StudentScorePanel(student, subjects));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    /**
     * Odświeża tabelę studentów, wczytując dane z bazy {@link StudentDB}.
     */
    public void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : studentDB.getStudents()) {
            studentTableModel.addRow(new Object[]{
                s.getName(), s.getSurname(), s.getAlbumNumber(),
                s.getGroup() != null ? s.getGroup().getGroupCode() : "Brak"
            });
        }
    }
}
