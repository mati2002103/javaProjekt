package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel Swing do zarządzania grupami. Umożliwia dodawanie, edytowanie, usuwanie
 * grup oraz powrót do menu głównego aplikacji.
 * <p>
 * Dane grup są pobierane z {@link GroupDB} i prezentowane w tabeli.
 * </p>
 * 
 * @author
 *     Wiśniewski Mateusz
 */
public class GroupPanel extends JPanel {

    /** Główne okno aplikacji, umożliwia nawigację do menu */
    private MyWindow parentWindow;

    /** Baza danych grup */
    private GroupDB groupDB;

    /** Model tabeli grup */
    private DefaultTableModel groupTableModel;

    /** Tabela wyświetlająca listę grup */
    private JTable groupTable;

    /**
     * Tworzy panel do zarządzania grupami.
     *
     * @param parentWindow główne okno aplikacji
     * @param groupDB      baza danych grup
     */
    public GroupPanel(MyWindow parentWindow, GroupDB groupDB) {
        this.parentWindow = parentWindow;
        this.groupDB = groupDB;

        setLayout(new BorderLayout());

        groupTableModel = new DefaultTableModel(new String[] { "Kod grupy", "Specjalizacja", "Opis" }, 0);
        groupTable = new JTable(groupTableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Grupę");
        JButton editButton = new JButton("Edytuj Grupę");
        JButton deleteButton = new JButton("Usuń Grupę");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addGroup());
        editButton.addActionListener(e -> editGroup());
        deleteButton.addActionListener(e -> deleteGroup());
        backButton.addActionListener(e -> parentWindow.showMenu());

        refreshTable();
    }

    /**
     * Wyświetla formularz do dodania nowej grupy i dodaje ją do bazy danych.
     * Pokazuje komunikat błędu, jeśli pola są puste.
     */
    private void addGroup() {
        JTextField codeField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] inputs = {
                "Kod grupy:", codeField,
                "Specjalizacja:", specializationField,
                "Opis:", descriptionField
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Dodaj Grupę", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String spec = specializationField.getText().trim();
            String desc = descriptionField.getText().trim();

            if (code.isEmpty() || spec.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = new Group(code, spec, desc);
            groupDB.addGroup(group);
            refreshTable();
        }
    }

    /**
     * Edytuje dane aktualnie zaznaczonej grupy w tabeli.
     * Pokazuje formularz z możliwością aktualizacji danych.
     */
    private void editGroup() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz grupę do edycji!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String currentCode = groupTableModel.getValueAt(selectedRow, 0).toString();
        String currentSpec = groupTableModel.getValueAt(selectedRow, 1).toString();
        String currentDesc = groupTableModel.getValueAt(selectedRow, 2).toString();

        JTextField codeField = new JTextField(currentCode);
        JTextField specField = new JTextField(currentSpec);
        JTextField descField = new JTextField(currentDesc);

        Object[] inputs = {
                "Kod grupy:", codeField,
                "Specjalizacja:", specField,
                "Opis:", descField
        };

        int result = JOptionPane.showConfirmDialog(this, inputs, "Edytuj Grupę", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            String spec = specField.getText().trim();
            String desc = descField.getText().trim();

            if (code.isEmpty() || spec.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Group group = groupDB.getGroupByCode(currentCode);
            if (group != null) {
                group.setGroupCode(code);
                group.setSpecialization(spec);
                group.setDescription(desc);
                refreshTable();
            }
        }
    }

    /**
     * Usuwa zaznaczoną grupę z bazy danych po potwierdzeniu.
     * Pokazuje komunikat błędu, jeśli żadna grupa nie jest zaznaczona.
     */
    private void deleteGroup() {
        int selectedRow = groupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz grupę do usunięcia!", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String code = groupTableModel.getValueAt(selectedRow, 0).toString();
        if (groupDB.deleteGroup(code)) {
            refreshTable();
        }
    }

    /**
     * Odświeża tabelę grup, ładując aktualne dane z {@link GroupDB}.
     */
    public void refreshTable() {
        groupTableModel.setRowCount(0);
        for (Group g : groupDB.getAllGroups()) {
            groupTableModel.addRow(new Object[] {
                    g.getGroupCode(),
                    g.getSpecialization(),
                    g.getDescription()
            });
        }
    }
}
