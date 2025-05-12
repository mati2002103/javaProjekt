package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class SubjectPanel extends JPanel {

    private MyWindow parentWindow;
    private SubjectDB subjectDB;
    private DefaultTableModel subjectTableModel;
    private JTable subjectTable;

    public SubjectPanel(MyWindow parentWindow, SubjectDB subjectDB) {
        this.parentWindow = parentWindow;
        this.subjectDB = subjectDB;

        setLayout(new BorderLayout());

        subjectTableModel = new DefaultTableModel(new String[]{"Nazwa Przedmiotu", "Kryteria"}, 0);
        subjectTable = new JTable(subjectTableModel);
        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Przedmiot");
        JButton editCriteriaButton = new JButton("Edytuj Kryteria");
        JButton deleteButton = new JButton("Usuń Przedmiot");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editCriteriaButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addSubject());
        editCriteriaButton.addActionListener(e -> editCriteria());
        deleteButton.addActionListener(e -> deleteSubject());
        backButton.addActionListener(e -> parentWindow.showMenu());

        refreshTable();
    }

    private void addSubject() {
        String subjectName = JOptionPane.showInputDialog(this, "Podaj nazwę przedmiotu:");
        if (subjectName != null && !subjectName.trim().isEmpty()) {
            Subject subject = new Subject(subjectName.trim());
            subjectDB.addSubject(subject);
            refreshTable();
        }
    }

    private void editCriteria() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz przedmiot do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String subjectName = subjectTableModel.getValueAt(selectedRow, 0).toString();
        Subject subject = subjectDB.getSubjectByName(subjectName);
        if (subject == null) return;

        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> model = new DefaultListModel<>();
        subject.getGradingCriteria().forEach((k, v) -> model.addElement(k + " (" + v + ")"));

        JList<String> criteriaList = new JList<>(model);
        panel.add(new JScrollPane(criteriaList), BorderLayout.CENTER);

        JButton addCriterion = new JButton("Dodaj");
        JButton removeCriterion = new JButton("Usuń");
        JPanel btns = new JPanel();
        btns.add(addCriterion);
        btns.add(removeCriterion);
        panel.add(btns, BorderLayout.SOUTH);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Edytuj Kryteria", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);

        addCriterion.addActionListener(ae -> {
            JTextField nameField = new JTextField();
            JTextField maxPointsField = new JTextField();
            Object[] inputs = {
                    "Nazwa kryterium:", nameField,
                    "Maks. punkty:", maxPointsField
            };

            int result = JOptionPane.showConfirmDialog(dialog, inputs, "Nowe kryterium", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String name = nameField.getText().trim();
                    int max = Integer.parseInt(maxPointsField.getText().trim());
                    if (!name.isEmpty() && max >= 0) {
                        subject.updateCriterion(name, max);
                        model.addElement(name + " (" + max + ")");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Nieprawidłowa liczba punktów!", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeCriterion.addActionListener(ae -> {
            int index = criteriaList.getSelectedIndex();
            if (index != -1) {
                String entry = model.getElementAt(index);
                String key = entry.substring(0, entry.indexOf(" ("));
                subject.removeCriterion(key);
                model.remove(index);
            }
        });

        dialog.setVisible(true);
        refreshTable();
    }

    private void deleteSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) return;

        String name = subjectTableModel.getValueAt(selectedRow, 0).toString();
        if (subjectDB.deleteSubject(name)) {
            refreshTable();
        }
    }

    public void refreshTable() {
        subjectTableModel.setRowCount(0);
        for (Subject subject : subjectDB.getAllSubjects()) {
            StringBuilder criteriaText = new StringBuilder();
            for (Map.Entry<String, Integer> entry : subject.getGradingCriteria().entrySet()) {
                criteriaText.append(entry.getKey()).append(" (").append(entry.getValue()).append("), ");
            }
            if (criteriaText.length() > 0)
                criteriaText.setLength(criteriaText.length() - 2); // usuń ostatni przecinek

            subjectTableModel.addRow(new Object[]{subject.getSubjectName(), criteriaText.toString()});
        }
    }
}
