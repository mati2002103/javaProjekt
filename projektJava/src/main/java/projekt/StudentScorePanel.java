package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa reprezentująca okienko dialogowe związane z pokazywaniem wyników
 * danego przedmiotu dla wybranego studenta.
 */
public class StudentScorePanel extends JPanel {

    private DefaultTableModel scoreTableModel;
    private Map<String, Map<String, int[]>> studentScores; // Mapa: studentId -> (przedmiot -> wyniki)

    public StudentScorePanel(Student student, List<String> subjects, Map<String, Map<String, int[]>> globalScores) {
        // Ustawienie layoutu panelu
        setLayout(new BorderLayout());

        // Przechowywanie globalnych wyników
        this.studentScores = globalScores;

        // Pobranie wyników tego studenta lub zainicjalizowanie nowych
        Map<String, int[]> scores = studentScores.computeIfAbsent(student.getAlbumNumber(), k -> new HashMap<>());

        // Nagłówek z informacjami o studencie
        JLabel headerLabel = new JLabel("Wyniki studenta: " + student.getName() + " " + student.getSurname(), SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Kolumny tabeli wyników
        String[] columnNames = {"Przedmiot", "Aktywność", "Prace domowe", "Projekt", "Egzamin", "Suma"};

        // Konfiguracja modelu tabeli
        scoreTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Wszystkie kolumny oprócz "Przedmiot" są edytowalne
                return column != 0;
            }
        };

        // Wypełnienie tabeli przykładowymi danymi
        for (String subject : subjects) {
            int[] defaultScores = scores.computeIfAbsent(subject, k -> new int[]{0, 0, 0, 0, 0}); // Domyślne wartości
            scoreTableModel.addRow(new Object[]{
                    subject, defaultScores[0], defaultScores[1], defaultScores[2], defaultScores[3], defaultScores[4]
            });
        }

        // Tworzenie tabeli
        JTable scoreTable = new JTable(scoreTableModel);
        scoreTable.setRowHeight(25);
        scoreTable.setFillsViewportHeight(true);

        scoreTable.getTableHeader().setReorderingAllowed(false); // Blokowanie zmiany kolejności kolumn

        // Dodanie tabeli do JScrollPane
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setPreferredSize(new Dimension(800, 400)); // Ustawienie preferowanego rozmiaru
        add(scrollPane, BorderLayout.CENTER);

        // Tworzenie przycisków "Zapisz" i "Zamknij"
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Zapisz");
        JButton closeButton = new JButton("Zamknij");

        // Obsługa przycisku "Zapisz"
        saveButton.addActionListener(e -> saveScores(student));

        // Obsługa przycisku "Zamknij"
        closeButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
        });

        // Dodanie przycisków do panelu
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Metoda do zapisywania wyników studenta
    private void saveScores(Student student) {
        String studentId = student.getAlbumNumber();
        Map<String, int[]> scores = studentScores.get(studentId);

        for (int row = 0; row < scoreTableModel.getRowCount(); row++) {
            String subject = scoreTableModel.getValueAt(row, 0).toString();
            int activity = parseCellValue(row, 1);
            int homework = parseCellValue(row, 2);
            int project = parseCellValue(row, 3);
            int exam = parseCellValue(row, 4);
            int sum = activity + homework + project + exam;

            // Zapisanie wyników w mapie wyników
            scores.put(subject, new int[]{activity, homework, project, exam, sum});

            // Zaktualizowanie wartości "Suma" w tabeli
            scoreTableModel.setValueAt(sum, row, 5);
        }

        JOptionPane.showMessageDialog(this, "Wyniki zostały zapisane pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
    }

    // Pomocnicza metoda do parsowania wartości komórki
    private int parseCellValue(int row, int column) {
        Object value = scoreTableModel.getValueAt(row, column);
        if (value == null || value.toString().trim().isEmpty()) {
            return 0; // Jeśli komórka jest pusta, zwróć 0
        }
        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0; // Jeśli wartość nie jest liczbą, zwróć 0
        }
    }
}