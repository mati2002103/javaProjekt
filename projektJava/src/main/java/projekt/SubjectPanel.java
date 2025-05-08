package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Natalia Rzeszótko
 *Klasa reprezentująca okienko dialogowe związane ze Sekcją Przedmioty
 *  możemy do niej własnorecznie wpisać przedmioty ,edytować i usuwać
 *   oraz importować i exportować dane
 */

public class SubjectPanel extends JPanel {

    private MyWindow parentWindow;
    private DefaultTableModel subjectTableModel;
    private List<Subject> subjectList;
    private SubjectDB subjectDB;

    public SubjectPanel(MyWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.subjectList = new ArrayList<>();
        this.subjectDB = new SubjectDB(); // Inicjalizacja bazy danych przedmiotów

        setLayout(new BorderLayout());

        // Model tabeli dla przedmiotów
        subjectTableModel = new DefaultTableModel(new String[]{"Nazwa Przedmiotu"}, 0);
        JTable subjectTable = new JTable(subjectTableModel);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel z przyciskami
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Przedmiot");
        JButton editButton = new JButton("Edytuj Przedmiot");
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
        editButton.addActionListener(e -> editSubject(subjectTable));
        deleteButton.addActionListener(e -> deleteSubject(subjectTable));
        importButton.addActionListener(e -> importSubjects());
        exportButton.addActionListener(e -> exportSubjects());
        backButton.addActionListener(e -> parentWindow.showMenu());
    }


    private void addSubject() {
        JTextField subjectNameField = new JTextField();

        Object[] message = {
                "Nazwa Przedmiotu:", subjectNameField };

        int option = JOptionPane.showConfirmDialog(this, message, "Dodaj Przedmiot", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subjectName = subjectNameField.getText().trim();

            if (subjectName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa przedmiotu musi być wypełniona!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Subject subject = new Subject(subjectName);
            subjectList.add(subject);
            subjectDB.addSubject(subject);
            subjectTableModel.addRow(new Object[]{subjectName});
            JOptionPane.showMessageDialog(this, "Przedmiot został pomyślnie dodany!");
        }
    }

    private void editSubject(JTable subjectTable) {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz przedmiot do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Subject subject = subjectList.get(selectedRow);

        JTextField subjectNameField = new JTextField(subject.getSubjectName());

        Object[] message = {
                "Nazwa Przedmiotu:", subjectNameField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edytuj Przedmiot", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subjectName = subjectNameField.getText().trim();

            if (subjectName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nazwa przedmiotu musi być wypełniona!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            subject.setSubjectName(subjectName);
            subjectTableModel.setValueAt(subjectName, selectedRow, 0);
            JOptionPane.showMessageDialog(this, "Przedmiot został pomyślnie zaktualizowany!");
        }
    }

    private void deleteSubject(JTable subjectTable) {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz przedmiot do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybrany przedmiot?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            Subject subject = subjectList.remove(selectedRow);
            subjectDB.deleteSubject(subject.getSubjectName());
            subjectTableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Przedmiot został pomyślnie usunięty!");
        }
    }

    private void importSubjects() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                subjectDB.loadFromFile(in);
                refreshSubjectTable();
                JOptionPane.showMessageDialog(this, "Przedmioty zostały pomyślnie zaimportowane!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas importowania przedmiotów: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportSubjects() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                subjectDB.saveToFile(out);
                JOptionPane.showMessageDialog(this, "Przedmioty zostały pomyślnie wyeksportowane!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas eksportowania przedmiotów: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshSubjectTable() {
        subjectTableModel.setRowCount(0); // Czyści tabelę
        for (Subject subject : subjectDB.getAllSubjects()) {
            subjectTableModel.addRow(new Object[]{subject.getSubjectName()});
        }
    }
}