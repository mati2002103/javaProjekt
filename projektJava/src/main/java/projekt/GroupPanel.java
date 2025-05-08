package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GroupPanel extends JPanel {

    private MyWindow parentWindow;
    private DefaultTableModel groupTableModel;
    private List<Group> groupList;

    public GroupPanel(MyWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.groupList = new ArrayList<>();

        setLayout(new BorderLayout());

        // Model tabeli dla grup
        groupTableModel = new DefaultTableModel(new String[]{"Kod grupy", "Specjalizacja", "Opis"}, 0);
        JTable groupTable = new JTable(groupTableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel z przyciskami
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Grupę");
        JButton editButton = new JButton("Edytuj Grupę");
        JButton deleteButton = new JButton("Usuń Grupę");
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
        addButton.addActionListener(e -> addGroup());
        editButton.addActionListener(e -> editGroup(groupTable));
        deleteButton.addActionListener(e -> deleteGroup(groupTable));
        importButton.addActionListener(e -> importGroups());
        exportButton.addActionListener(e -> exportGroups());
        backButton.addActionListener(e -> parentWindow.showMenu());
    }

    private void addGroup() {
        JTextField codeField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] message = {
                "Kod grupy:", codeField,
                "Specjalizacja:", specializationField,
                "Opis:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Dodaj Grupę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String specialization = specializationField.getText().trim();
            String description = descriptionField.getText().trim();

            if (code.isEmpty() || specialization.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = new Group(code, specialization, description);
            groupList.add(group);
            groupTableModel.addRow(new Object[]{code, specialization, description});
            JOptionPane.showMessageDialog(this, "Grupa została pomyślnie dodana!");
        }
    }

    private void editGroup(JTable groupTable) {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz grupę do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentCode = (String) groupTableModel.getValueAt(selectedRow, 0);
        String currentSpecialization = (String) groupTableModel.getValueAt(selectedRow, 1);
        String currentDescription = (String) groupTableModel.getValueAt(selectedRow, 2);

        JTextField codeField = new JTextField(currentCode);
        JTextField specializationField = new JTextField(currentSpecialization);
        JTextField descriptionField = new JTextField(currentDescription);

        Object[] message = {
                "Kod grupy:", codeField,
                "Specjalizacja:", specializationField,
                "Opis:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edytuj Grupę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String specialization = specializationField.getText().trim();
            String description = descriptionField.getText().trim();

            if (code.isEmpty() || specialization.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = groupList.get(selectedRow);
            group.setGroupCode(code);
            group.setSpecialization(specialization);
            group.setDescription(description);

            groupTableModel.setValueAt(code, selectedRow, 0);
            groupTableModel.setValueAt(specialization, selectedRow, 1);
            groupTableModel.setValueAt(description, selectedRow, 2);

            JOptionPane.showMessageDialog(this, "Grupa została pomyślnie zaktualizowana!");
        }
    }

    private void deleteGroup(JTable groupTable) {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz grupę do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć wybraną grupę?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            groupList.remove(selectedRow);
            groupTableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Grupa została pomyślnie usunięta!");
        }
    }

    private void importGroups() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
                loadFromFile(in);
                JOptionPane.showMessageDialog(this, "Grupy zostały pomyślnie zaimportowane!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas importowania grup: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportGroups() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
                saveToFile(out);
                JOptionPane.showMessageDialog(this, "Grupy zostały pomyślnie wyeksportowane!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas eksportowania grup: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveToFile(DataOutputStream out) throws IOException {
        out.writeInt(groupList.size());
        for (Group g : groupList) {
            out.writeUTF(g.getGroupCode());
            out.writeUTF(g.getSpecialization());
            out.writeUTF(g.getDescription());

            out.writeInt(g.getStudents().size());
            for (Student s : g.getStudents()) {
                out.writeUTF(s.getName());
                out.writeUTF(s.getSurname());
                out.writeUTF(s.getAlbumNumber());
            }
        }
    }

    public void loadFromFile(DataInputStream in) throws IOException {
        groupList.clear();
        int groupCount = in.readInt();
        for (int i = 0; i < groupCount; i++) {
            String code = in.readUTF();
            String specialization = in.readUTF();
            String description = in.readUTF();

            Group g = new Group(code, specialization, description);

            int studentCount = in.readInt();
            for (int j = 0; j < studentCount; j++) {
                String name = in.readUTF();
                String surname = in.readUTF();
                String albumNumber = in.readUTF();
                Student s = new Student(name, surname, albumNumber, g);
                g.addStudent(s);
            }

            groupList.add(g);
            groupTableModel.addRow(new Object[]{code, specialization, description});
        }
    }
}