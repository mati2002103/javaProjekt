package projekt;
/*
@autor Natalia Rzeszótko

Program okienkowy który posiada parametry z klas Student,StudentDb,
Gropu,GroupDB, Subject, SubjectDb oraz Enuma Criteria
Działa na zasadzie kart (CardLayout), gdzie każda zakładka odpowiada innej funkcji (np. "Studenci", "Grupy").
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class MyWindow extends JFrame {
                                            //pobieram dane z innych dołączonych klas
    private CardLayout cardLayout;                      // "CardLayout" to taki układ, który pozwala przełączać się między różnymi ekranami (zakładkami).
    private JPanel mainPanel;
    private StudentDB studentDB = new StudentDB();      // przechowuje liste studentów
    private GroupDB groupDB = new GroupDB();            // przechowuje liste grup
    private SubjectDB subjectDB = new SubjectDB();      // przechowuje liste przedmiotów

    public MyWindow() {

                                                // wygląd i tytuł okna
        super("Aplikacja do zarządzania studentami, grupami i przedmiotami");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

                                                // CardLayout dla przełączania między panelami

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

                                                // Główne menu

        JPanel menuPanel = new JPanel();
        JButton studentButton = new JButton("Sekcja Studentów");// przyciski do odpowiednich sekcji
        JButton groupButton = new JButton("Sekcja Grup");
        JButton subjectButton = new JButton("Sekcja Przedmiotów");
        JButton pointsButton = new JButton("Zarządzanie Punktami");
        JButton backButton = new JButton("Wyjdź");
//dodanie przycisków w okienku
        menuPanel.add(studentButton);
        menuPanel.add(groupButton);
        menuPanel.add(subjectButton);
        menuPanel.add(pointsButton);
        menuPanel.add(backButton);

                                                // Dodanie paneli do CardLayout
        mainPanel.add(menuPanel, "Menu");                       //odpowiednie zakładki dla sekcji
        mainPanel.add(createStudentPanel(), "Student");
        mainPanel.add(createGroupPanel(), "Group");
        mainPanel.add(createSubjectPanel(), "Subject");
        mainPanel.add(createPointsPanel(), "Points");

                                                // Obsługa przycisków nawigacyjnych
        studentButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Student"));
        groupButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Group"));
        subjectButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Subject"));
        pointsButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Points"));
        backButton.addActionListener((ActionEvent e) -> System.exit(0));

        add(mainPanel);

        setVisible(true);
    }

                                            // Tworze Panel dla sekcji Student

    private JPanel createStudentPanel() {
        JPanel studentPanel = new JPanel(new BorderLayout());

        // Tabela do wyświetlania studentów
        DefaultTableModel studentTableModel = new DefaultTableModel(new String[]{"Imię", "Nazwisko", "Nr Albumu", "Grupa"}, 0);
        JTable studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable); //Dodajemy tabelę do przewijanego okna.
        studentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel przycisków do obsługi dodaj, usun ,edytuj
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Studenta");
        JButton editButton = new JButton("Edytuj Studenta");
        JButton deleteButton = new JButton("Usuń Studenta");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        studentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Obsługa przycisków
        addButton.addActionListener((ActionEvent e) -> {
                                //okienko dialogowe gdzie możemy wprowadzić dane danego studenta
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField albumNumberField = new JTextField();
            String[] groupCodes = groupDB.getAllGroups().stream().map(Group::getGroupCode).toArray(String[]::new);
            JComboBox<String> groupComboBox = new JComboBox<>(groupCodes);

            Object[] message = {
                    "Imię:", firstNameField,
                    "Nazwisko:", lastNameField,
                    "Nr Albumu:", albumNumberField,
                    "Grupa:", groupComboBox
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Dodaj Studenta", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String albumNumber = albumNumberField.getText();
                String groupCode = (String) groupComboBox.getSelectedItem();

                Group group = groupDB.getGroupByCode(groupCode);
                Student student = new Student(firstName, lastName, albumNumber, group);
                studentDB.addStudentToStudentList(student);
                group.addStudent(student);
                                            // dodajemy dane studenta do tabeli
                studentTableModel.addRow(new Object[]{firstName, lastName, albumNumber, groupCode});
            }
        });

        editButton.addActionListener((ActionEvent e) -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                String albumNumber = (String) studentTableModel.getValueAt(selectedRow, 2);
                Student student = studentDB.getStudentByAlbumNumber(Integer.parseInt(albumNumber));

                JTextField firstNameField = new JTextField(student.getName());
                JTextField lastNameField = new JTextField(student.getSurname());
                JTextField albumNumberField = new JTextField(student.getAlbumNumber());
                String[] groupCodes = groupDB.getAllGroups().stream().map(Group::getGroupCode).toArray(String[]::new);
                JComboBox<String> groupComboBox = new JComboBox<>(groupCodes);
                groupComboBox.setSelectedItem(student.getGroup().getGroupCode());

                Object[] message = {
                        "Imię:", firstNameField,
                        "Nazwisko:", lastNameField,
                        "Nr Albumu:", albumNumberField,
                        "Grupa:", groupComboBox
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edytuj Studenta", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    student.setName(firstNameField.getText());
                    student.setSurname(lastNameField.getText());
                    student.setAlbumNumber(albumNumberField.getText());
                    Group newGroup = groupDB.getGroupByCode((String) groupComboBox.getSelectedItem());
                    student.setGroup(newGroup);
//aktualizujemy dane studenta
                    studentTableModel.setValueAt(student.getName(), selectedRow, 0);
                    studentTableModel.setValueAt(student.getSurname(), selectedRow, 1);
                    studentTableModel.setValueAt(student.getAlbumNumber(), selectedRow, 2);
                    studentTableModel.setValueAt(newGroup.getGroupCode(), selectedRow, 3);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wybierz studenta do edycji.");
            }
        });
//obsługa przycisku usuń, usuwanie studenta
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                String albumNumber = (String) studentTableModel.getValueAt(selectedRow, 2);
                Student student = studentDB.getStudentByAlbumNumber(Integer.parseInt(albumNumber));
                studentDB.deleteStudentFromStudentList(student);
                student.getGroup().getStudents().remove(student);
                studentTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Wybierz studenta do usunięcia.");
            }
        });

        backButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Menu"));

        return studentPanel;
    }

                                            //Tworze Panle dla Sekcji Grupy
    private JPanel createGroupPanel() {
        JPanel groupPanel = new JPanel(new BorderLayout());

        DefaultTableModel groupTableModel = new DefaultTableModel(new String[]{"Kod grupy", "Specjalizacja", "Opis"}, 0);
        JTable groupTable = new JTable(groupTableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        groupPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Grupę");
        JButton editButton = new JButton("Edytuj Grupę");
        JButton deleteButton = new JButton("Usuń Grupę");
        JButton backButton = new JButton("Wróć");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        groupPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener((ActionEvent e) -> {
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
                String code = codeField.getText();
                String specialization = specializationField.getText();
                String description = descriptionField.getText();
                Group group = new Group(code, specialization, description);
                groupDB.addGroup(group);
                groupTableModel.addRow(new Object[]{code, specialization, description});
            }
        });

        editButton.addActionListener((ActionEvent e) -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow >= 0) {
                String code = (String) groupTableModel.getValueAt(selectedRow, 0);
                Group group = groupDB.getGroupByCode(code);

                JTextField codeField = new JTextField(group.getGroupCode());
                JTextField specializationField = new JTextField(group.getSpecialization());
                JTextField descriptionField = new JTextField(group.getDescription());

                Object[] message = {
                        "Kod grupy:", codeField,
                        "Specjalizacja:", specializationField,
                        "Opis:", descriptionField
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Edytuj Grupę", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    group.setGroupCode(codeField.getText());
                    group.setSpecialization(specializationField.getText());
                    group.setDescription(descriptionField.getText());

                    groupTableModel.setValueAt(group.getGroupCode(), selectedRow, 0);
                    groupTableModel.setValueAt(group.getSpecialization(), selectedRow, 1);
                    groupTableModel.setValueAt(group.getDescription(), selectedRow, 2);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wybierz grupę do edycji.");
            }
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedRow = groupTable.getSelectedRow();
            if (selectedRow >= 0) {
                String code = (String) groupTableModel.getValueAt(selectedRow, 0);
                groupDB.deleteGroup(code);
                groupTableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Wybierz grupę do usunięcia.");
            }
        });

        backButton.addActionListener((ActionEvent e) -> cardLayout.show(mainPanel, "Menu"));

        return groupPanel;
    }

                                            //Tworze panel dla sekcji przemioty
    private JPanel createSubjectPanel() {
        // Implementacja sekcji przedmiotów podobnie jak w sekcji grup i studentów.
        JPanel subjectPanel = new JPanel();
        // Kod do zarządzania przedmiotami
        return subjectPanel;
    }


                                            //Tworze Panel dla punktów
    private JPanel createPointsPanel() {
        // Implementacja sekcji zarządzania punktami
        JPanel pointsPanel = new JPanel();
        // Kod do zarządzania punktami
        return pointsPanel;
    }

    public static void main(String[] args) {
        new MyWindow();
    }
}