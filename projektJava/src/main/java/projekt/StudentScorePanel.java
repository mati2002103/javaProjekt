package projekt;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Klasa reprezentująca okienko dialogowe związane z pokazywaniem wyników
 * danego przedmiotu dla wybranego studenta.
 */
public class StudentScorePanel extends JPanel {

    private Student student;
    private List<Subject> subjects;
    private JTabbedPane tabbedPane;

    public StudentScorePanel(Student student, List<Subject> subjects) {
        this.student = student;
        this.subjects = subjects;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Nagłówek z nazwą studenta
        JLabel headerLabel = new JLabel("Wyniki studenta: " + student.getName() + " " + student.getSurname(), 
                                      SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(headerLabel, BorderLayout.NORTH);

        // Panel z kartami dla każdego przedmiotu
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Tworzenie panelu dla każdego przedmiotu
        for (Subject subject : subjects) {
            JPanel subjectPanel = createSubjectPanel(subject);
            tabbedPane.addTab(subject.getSubjectName(), subjectPanel);
        }

        // Panel przycisków
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Zapisz");
        JButton closeButton = new JButton("Zamknij");

        saveButton.addActionListener(e -> saveAllChanges());
        closeButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSubjectPanel(Subject subject) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Pobierz kryteria dla przedmiotu
        Map<String, Integer> criteria = subject.getGradingCriteria();
        
        // Nagłówki kolumn: Kryterium | Punkty | Max punktów
        String[] columnNames = {"Kryterium", "Punkty", "Max punktów"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Tylko kolumna z punktami jest edytowalna
            }
        };

        // Wypełnij tabelę danymi
        Map<String, Integer> studentGrades = student.getGrades().getOrDefault(subject, new HashMap<>());
        int totalPoints = 0;
        int totalMaxPoints = 0;

        for (Map.Entry<String, Integer> entry : criteria.entrySet()) {
            String criteriaName = entry.getKey();
            int maxPoints = entry.getValue();
            int points = studentGrades.getOrDefault(criteriaName, 0);
            
            model.addRow(new Object[]{criteriaName, points, maxPoints});
            
            totalPoints += points;
            totalMaxPoints += maxPoints;
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        
        // Dodaj podsumowanie pod tabelą
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2));
        summaryPanel.add(new JLabel("Suma punktów: " + totalPoints));
        summaryPanel.add(new JLabel("Suma max punktów: " + totalMaxPoints));
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void saveAllChanges() {
        List<String> errors = new ArrayList<>();
        boolean hasErrors = false;

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component component = tabbedPane.getComponentAt(i);
            if (component instanceof JPanel) {
                JScrollPane scrollPane = (JScrollPane) ((JPanel) component).getComponent(0);
                JTable table = (JTable) scrollPane.getViewport().getView();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                
                String subjectName = tabbedPane.getTitleAt(i);
                Subject subject = findSubjectByName(subjectName);
                if (subject == null) continue;
                
                Map<String, Integer> newGrades = new HashMap<>();
                int totalPoints = 0;
                int totalMaxPoints = 0;

                for (int row = 0; row < model.getRowCount(); row++) {
                    String criteriaName = (String) model.getValueAt(row, 0);
                    int points;
                    try {
                        points = Integer.parseInt(model.getValueAt(row, 1).toString());
                    } catch (NumberFormatException e) {
                        points = 0;
                    }
                    int maxPoints = (Integer) model.getValueAt(row, 2);
                    
                    if (points < 0 || points > maxPoints) {
                        errors.add(subjectName + ": " + criteriaName + " - nieprawidłowa wartość (0-" + maxPoints + ")");
                        hasErrors = true;
                    } else {
                        newGrades.put(criteriaName, points);
                        totalPoints += points;
                        totalMaxPoints += maxPoints;
                    }
                }
                
                if (!hasErrors) {
                    // Aktualizacja danych studenta
                    student.getGrades().put(subject, newGrades);
                    
                    // Aktualizacja podsumowania
                    JPanel summaryPanel = (JPanel) ((JPanel) component).getComponent(1);
                    ((JLabel) summaryPanel.getComponent(0)).setText("Suma punktów: " + totalPoints);
                    ((JLabel) summaryPanel.getComponent(1)).setText("Suma max punktów: " + totalMaxPoints);
                }
            }
        }
        
        if (hasErrors) {
            JOptionPane.showMessageDialog(this, 
                "Wystąpiły błędy:\n" + String.join("\n", errors),
                "Błędy w danych",
                JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Dane zostały poprawnie zapisane!",
                "Sukces",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private Subject findSubjectByName(String subjectName) {
        return subjects.stream()
            .filter(s -> s.getSubjectName().equals(subjectName))
            .findFirst()
            .orElse(null);
    }
}