package projekt;

import javax.swing.*;
import java.awt.*;

/**
 * Główna klasa okna aplikacji z panelem nawigacyjnym (Menu)
 * i wszystkimi sekcjami: Student, Grupy, Przedmioty, Wyszukiwanie.
 */
public class MyWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MyWindow() {
        super("Aplikacja do zarządzania studentami, grupami i przedmiotami");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Wspólne bazy danych
        StudentDB studentDB = new StudentDB();
        SubjectDB subjectDB = new SubjectDB();
        GroupDB groupDB = new GroupDB();

        // Panel menu głównego
        JPanel menuPanel = new JPanel();
        JButton studentButton = new JButton("Sekcja Studentów");
        JButton groupButton = new JButton("Sekcja Grup");
        JButton subjectButton = new JButton("Sekcja Przedmiotów");
        JButton searchButton = new JButton("Wyszukiwanie Studenta");
        JButton backButton = new JButton("Wyjdź");

        menuPanel.add(studentButton);
        menuPanel.add(groupButton);
        menuPanel.add(subjectButton);
        menuPanel.add(searchButton);
        menuPanel.add(backButton);

        mainPanel.add(menuPanel, "Menu");

        // Panele funkcjonalne
        StudentPanel studentPanel = new StudentPanel(this, studentDB, subjectDB, groupDB);
        GroupPanel groupPanel = new GroupPanel(this, groupDB);
        SubjectPanel subjectPanel = new SubjectPanel(this, subjectDB);
        SearchStudentPanel searchStudentPanel = new SearchStudentPanel(this, studentDB);

        mainPanel.add(studentPanel, "Student");
        mainPanel.add(groupPanel, "Group");
        mainPanel.add(subjectPanel, "Subject");
        mainPanel.add(searchStudentPanel, "Search");

        // Obsługa przycisków
        studentButton.addActionListener(e -> cardLayout.show(mainPanel, "Student"));
        groupButton.addActionListener(e -> cardLayout.show(mainPanel, "Group"));
        subjectButton.addActionListener(e -> cardLayout.show(mainPanel, "Subject"));
        searchButton.addActionListener(e -> cardLayout.show(mainPanel, "Search"));
        backButton.addActionListener(e -> System.exit(0));

        add(mainPanel);
        setVisible(true);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    public static void main(String[] args) {
        new MyWindow();
    }
}
