package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Klasa reprezentująca okienko dialogowe związane z pokazywaniem wyników
 * danego przedmiotu dla wybranego studenta.
 */
public class StudentScorePanel extends JPanel {

    private DefaultTableModel scoreTableModel;

    public StudentScorePanel(Student student, List<Subject> subjects) {
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Wyniki studenta: " + student.getName() + " " + student.getSurname(), SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Zbieranie wszystkich możliwych kryteriów ze wszystkich przedmiotów
        Set<String> allCriteria = new TreeSet<>();
        for (Subject subject : subjects) {
            allCriteria.addAll(subject.getGradingCriteria().keySet());
        }

        // Tworzenie nagłówków kolumn: Przedmiot | Kryteria... | Suma
        String[] columnNames = new String[allCriteria.size() + 2];
        columnNames[0] = "Przedmiot";
        int colIndex = 1;
        for (String criteriaName : allCriteria) {
            columnNames[colIndex++] = criteriaName;
        }
        columnNames[colIndex] = "Suma";

        // Konfiguracja modelu tabeli
        scoreTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && column != getColumnCount() - 1;
            }
        };

        // Uzupełnienie danych tabeli z ocenami studenta
        for (Subject subject : subjects) {
            Map<String, Integer> grades = student.getGrades().getOrDefault(subject, new HashMap<>());
            Object[] row = new Object[columnNames.length];
            row[0] = subject.getSubjectName();
            int sum = 0;

            for (int i = 1; i < columnNames.length - 1; i++) {
                int points = grades.getOrDefault(columnNames[i], 0);
                row[i] = points;
                sum += points;
            }

            row[columnNames.length - 1] = sum;
            scoreTableModel.addRow(row);
        }

        JTable scoreTable = new JTable(scoreTableModel);
        scoreTable.setRowHeight(25);
        scoreTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Panel z przyciskami
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Zapisz");
        JButton closeButton = new JButton("Zamknij");

        // Obsługa przycisku Zapisz
        saveButton.addActionListener(e -> {
            for (int row = 0; row < scoreTableModel.getRowCount(); row++) {
                String subjectName = scoreTableModel.getValueAt(row, 0).toString();

                Subject subject = subjects.stream()
                        .filter(s -> s.getSubjectName().equals(subjectName))
                        .findFirst()
                        .orElse(null);

                if (subject == null) continue;

                Map<String, Integer> newGrades = new HashMap<>();
                int total = 0;

                for (int col = 1; col < scoreTableModel.getColumnCount() - 1; col++) {
                    String criteriaName = scoreTableModel.getColumnName(col);
                    int value = parseCellValue(row, col);
                    newGrades.put(criteriaName, value);
                    total += value;
                }

                student.getGrades().put(subject, newGrades);
                scoreTableModel.setValueAt(total, row, scoreTableModel.getColumnCount() - 1);
            }

            JOptionPane.showMessageDialog(this, "Wyniki zostały zapisane pomyślnie!", "Sukces", JOptionPane.INFORMATION_MESSAGE);
        });

        closeButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Pomocnicza metoda do parsowania wartości komórki
    private int parseCellValue(int row, int col) {
        Object value = scoreTableModel.getValueAt(row, col);
        if (value == null || value.toString().trim().isEmpty()) return 0;

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
