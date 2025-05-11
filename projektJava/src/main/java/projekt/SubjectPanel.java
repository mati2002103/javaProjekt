package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca okienko dialogowe związane ze Sekcją Przedmioty.
 * Umożliwia dodawanie, edytowanie i usuwanie przedmiotów oraz ustalanie maksymalnej liczby punktów.
 * Dodatkowo umożliwia importowanie i eksportowanie danych.
 */
public class SubjectPanel extends JPanel {

    private MyWindow parentWindow;
    private DefaultTableModel subjectTableModel;
    private List<Subject> subjectList;

    public SubjectPanel(MyWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.subjectList = new ArrayList<>();

        setLayout(new BorderLayout());

        // Model tabeli z kolumnami dla maksymalnych punktów
        subjectTableModel = new DefaultTableModel(
                new String[]{
                        "Nazwa Przedmiotu",
                        "Max pkt aktywność",
                        "Max pkt projekt",
                        "Max pkt prace domowe",
                        "Max pkt egzamin"
                }, 0);

        JTable subjectTable = new JTable(subjectTableModel);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel z przyciskami
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Przedmiot");
        JButton editButton = new JButton("Edytuj Max Punkty");
        JButton deleteButton = new JButton("Usuń Przedmiot");
        JButton importButton = new JButton("Importuj");
        JButton exportButton = new JButton("Eksportuj");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Obsługa przycisków
        addButton.addActionListener(e -> addSubject());
        editButton.addActionListener(e -> editMaxPoints(subjectTable));
        deleteButton.addActionListener(e -> deleteSubject(subjectTable));
        importButton.addActionListener(e -> importSubjects());
        exportButton.addActionListener(e -> exportSubjects());
        backButton.addActionListener(e -> parentWindow.showMenu());
    }

    private void addSubject() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField subjectNameField = new JTextField();
        JTextField activityMaxField = new JTextField("0");
        JTextField projectMaxField = new JTextField("0");
        JTextField homeworkMaxField = new JTextField("0");
        JTextField examMaxField = new JTextField("0");

        panel.add(new JLabel("Nazwa Przedmiotu:"));
        panel.add(subjectNameField);
        panel.add(new JLabel("Maksymalne punkty za aktywność:"));
        panel.add(activityMaxField);
        panel.add(new JLabel("Maksymalne punkty za projekt:"));
        panel.add(projectMaxField);
        panel.add(new JLabel("Maksymalne punkty za prace domowe:"));
        panel.add(homeworkMaxField);
        panel.add(new JLabel("Maksymalne punkty za egzamin:"));
        panel.add(examMaxField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Dodaj nowy przedmiot",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String subjectName = subjectNameField.getText().trim();

            if (subjectName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nazwa przedmiotu musi być wypełniona!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int activityMax = Integer.parseInt(activityMaxField.getText().trim());
                int projectMax = Integer.parseInt(projectMaxField.getText().trim());
                int homeworkMax = Integer.parseInt(homeworkMaxField.getText().trim());
                int examMax = Integer.parseInt(examMaxField.getText().trim());

                if (activityMax < 0 || projectMax < 0 || homeworkMax < 0 || examMax < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Wartości punktów nie mogą być ujemne!",
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Dodanie wiersza do tabeli
                subjectTableModel.addRow(new Object[]{
                        subjectName,
                        activityMax,
                        projectMax,
                        homeworkMax,
                        examMax
                });

                // Dodanie do listy
                subjectList.add(new Subject(subjectName));
                JOptionPane.showMessageDialog(this, "Przedmiot został pomyślnie dodany!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Wprowadź poprawne wartości liczbowe!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editMaxPoints(JTable subjectTable) {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz przedmiot do edycji!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField activityField = new JTextField(subjectTableModel.getValueAt(selectedRow, 1).toString());
        JTextField projectField = new JTextField(subjectTableModel.getValueAt(selectedRow, 2).toString());
        JTextField homeworkField = new JTextField(subjectTableModel.getValueAt(selectedRow, 3).toString());
        JTextField examField = new JTextField(subjectTableModel.getValueAt(selectedRow, 4).toString());

        panel.add(new JLabel("Maksymalne punkty za aktywność:"));
        panel.add(activityField);
        panel.add(new JLabel("Maksymalne punkty za projekt:"));
        panel.add(projectField);
        panel.add(new JLabel("Maksymalne punkty za prace domowe:"));
        panel.add(homeworkField);
        panel.add(new JLabel("Maksymalne punkty za egzamin:"));
        panel.add(examField);

        int option = JOptionPane.showConfirmDialog(this, panel,
                "Edytuj maksymalne punkty", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int activity = Integer.parseInt(activityField.getText().trim());
                int project = Integer.parseInt(projectField.getText().trim());
                int homework = Integer.parseInt(homeworkField.getText().trim());
                int exam = Integer.parseInt(examField.getText().trim());

                if (activity < 0 || project < 0 || homework < 0 || exam < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Wartości punktów nie mogą być ujemne!",
                            "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Aktualizacja tabeli
                subjectTableModel.setValueAt(activity, selectedRow, 1);
                subjectTableModel.setValueAt(project, selectedRow, 2);
                subjectTableModel.setValueAt(homework, selectedRow, 3);
                subjectTableModel.setValueAt(exam, selectedRow, 4);

                JOptionPane.showMessageDialog(this, "Maksymalne punkty zostały zaktualizowane!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Wprowadź poprawne wartości liczbowe!",
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSubject(JTable subjectTable) {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz przedmiot do usunięcia!",
                    "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz usunąć wybrany przedmiot?",
                "Potwierdzenie", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            subjectTableModel.removeRow(selectedRow);
            subjectList.remove(selectedRow);
            JOptionPane.showMessageDialog(this, "Przedmiot został pomyślnie usunięty!");
        }
    }

    private void importSubjects() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        String subjectName = parts[0];
                        int activityMax = Integer.parseInt(parts[1]);
                        int projectMax = Integer.parseInt(parts[2]);
                        int homeworkMax = Integer.parseInt(parts[3]);
                        int examMax = Integer.parseInt(parts[4]);

                        subjectTableModel.addRow(new Object[]{subjectName, activityMax, projectMax, homeworkMax, examMax});
                        subjectList.add(new Subject(subjectName));
                    }
                }
                JOptionPane.showMessageDialog(this, "Przedmioty zostały pomyślnie zaimportowane!");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas importowania przedmiotów: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportSubjects() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < subjectTableModel.getRowCount(); i++) {
                    String subjectName = subjectTableModel.getValueAt(i, 0).toString();
                    String activityMax = subjectTableModel.getValueAt(i, 1).toString();
                    String projectMax = subjectTableModel.getValueAt(i, 2).toString();
                    String homeworkMax = subjectTableModel.getValueAt(i, 3).toString();
                    String examMax = subjectTableModel.getValueAt(i, 4).toString();

                    writer.write(String.join(",", subjectName, activityMax, projectMax, homeworkMax, examMax));
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Przedmioty zostały pomyślnie wyeksportowane!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas eksportowania przedmiotów: " + e.getMessage(),
                        "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}