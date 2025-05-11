package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Klasa pozwalająca na przypisanie przedmiotu do wybranego studenta
 * oraz wpisanie liczby punktów uzyskanych przez studenta.
 */
public class AssignSubjectPanel extends JPanel {

    public AssignSubjectPanel(Student student, SubjectDB subjectDB) {
        if (student == null || subjectDB == null) {
            throw new IllegalArgumentException("Student lub baza danych przedmiotów nie może być null!");
        }

        setLayout(new BorderLayout());

        // Wyświetlanie informacji o wybranym studencie
        JLabel studentLabel = new JLabel("Student: " + student.getName() + " " + student.getSurname());
        add(studentLabel, BorderLayout.NORTH);

        // Pobranie listy przedmiotów z bazy danych
        List<Subject> subjectList = subjectDB.getAllSubjects();
        if (subjectList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brak dostępnych przedmiotów do przypisania!", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Tabela z przedmiotami
        DefaultTableModel subjectTableModel = new DefaultTableModel(new String[]{"Przedmiot", "Punkty"}, 0);
        JTable subjectTable = new JTable(subjectTableModel);
        JScrollPane subjectScrollPane = new JScrollPane(subjectTable);
        add(subjectScrollPane, BorderLayout.CENTER);

        // Dodanie przedmiotów do tabeli
        for (Subject subject : subjectList) {
            subjectTableModel.addRow(new Object[]{subject.getSubjectName(), 0});
        }

        // Przycisk do zapisania punktów
        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> {
            int rowCount = subjectTableModel.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String subjectName = (String) subjectTableModel.getValueAt(i, 0);
                int points;
                try {
                    points = Integer.parseInt(subjectTableModel.getValueAt(i, 1).toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Nieprawidłowa liczba punktów w wierszu " + (i + 1),
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                System.out.println("Przedmiot: " + subjectName + ", Punkty: " + points);
            }
            JOptionPane.showMessageDialog(this, "Punkty zostały zapisane!");
        });
        add(saveButton, BorderLayout.SOUTH);
    }
}