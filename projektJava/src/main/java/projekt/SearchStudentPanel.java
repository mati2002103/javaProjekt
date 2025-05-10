package projekt;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 *Klasa reprezentująca okienko dialogowe związane ze wyszukiwaniem studentów
 *  po określonych danych podanych przez użytkownika
 */
public class SearchStudentPanel extends JPanel {

    private MyWindow parentWindow;
    private StudentDB studentDB;
    private DefaultTableModel resultTableModel;

    public SearchStudentPanel(MyWindow parentWindow, StudentDB studentDB) {
        this.parentWindow = parentWindow;
        this.studentDB = studentDB;

        setLayout(new BorderLayout());

        // Panel do wprowadzania kryteriów wyszukiwania
        JPanel searchPanel = new JPanel(new GridLayout(5, 2));
        JTextField nameField = new JTextField();
        JTextField surnameField = new JTextField();
        JTextField albumNumberField = new JTextField();
        JTextField groupField = new JTextField();
        JButton searchButton = new JButton("Szukaj");

        searchPanel.add(new JLabel("Imię:"));
        searchPanel.add(nameField);
        searchPanel.add(new JLabel("Nazwisko:"));
        searchPanel.add(surnameField);
        searchPanel.add(new JLabel("Numer Albumu:"));
        searchPanel.add(albumNumberField);
        searchPanel.add(new JLabel("Grupa:"));
        searchPanel.add(groupField);
        searchPanel.add(new JLabel());
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Model tabeli wyników
        resultTableModel = new DefaultTableModel(new String[]{"Imię", "Nazwisko", "Numer Albumu", "Grupa"}, 0);
        JTable resultTable = new JTable(resultTableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Przycisk powrotu
        JButton backButton = new JButton("Wróć");
        backButton.addActionListener(e -> parentWindow.showMenu());
        add(backButton, BorderLayout.SOUTH);

        // Obsługa wyszukiwania
        searchButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String albumNumber = albumNumberField.getText().trim();
            String groupCode = groupField.getText().trim();

            searchStudents(name, surname, albumNumber, groupCode);
        });
    }

    private void searchStudents(String name, String surname, String albumNumber, String groupCode) {
        // Czyszczenie tabeli wyników
        resultTableModel.setRowCount(0);

        // Filtracja studentów na podstawie kryteriów
        List<Student> filteredStudents = studentDB.getStudents().stream()
                .filter(student -> (name.isEmpty() || student.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(student -> (surname.isEmpty() || student.getSurname().toLowerCase().contains(surname.toLowerCase())))
                .filter(student -> (albumNumber.isEmpty() || student.getAlbumNumber().contains(albumNumber)))
                .filter(student -> (groupCode.isEmpty() || (student.getGroup() != null && student.getGroup().getGroupCode().toLowerCase().contains(groupCode.toLowerCase()))))
                .collect(Collectors.toList());

        // Dodawanie wyników do tabeli
        for (Student student : filteredStudents) {
            resultTableModel.addRow(new Object[]{
                    student.getName(),
                    student.getSurname(),
                    student.getAlbumNumber(),
                    student.getGroup() != null ? student.getGroup().getGroupCode() : "Brak grupy"
            });
        }

        if (filteredStudents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nie znaleziono studentów spełniających podane kryteria.", "Brak wyników", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}