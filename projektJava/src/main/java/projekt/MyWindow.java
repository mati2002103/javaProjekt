package projekt;

import javax.swing.*;
import java.awt.*;

public class MyWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MyWindow() {
        super("Aplikacja do zarządzania studentami, grupami i przedmiotami");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel menuPanel = new JPanel();
        JButton studentButton = new JButton("Sekcja Studentów");
        JButton groupButton = new JButton("Sekcja Grup");
        JButton subjectButton = new JButton("Sekcja Przedmiotów");
        JButton backButton = new JButton("Wyjdź");

        menuPanel.add(studentButton);
        menuPanel.add(groupButton);
        menuPanel.add(subjectButton);
        menuPanel.add(backButton);

        mainPanel.add(menuPanel, "Menu");

        // Dodanie paneli do głównego okna
        StudentPanel studentPanel = new StudentPanel(this);
        GroupPanel groupPanel = new GroupPanel(this);
        SubjectPanel subjectPanel = new SubjectPanel(this);

        mainPanel.add(studentPanel, "Student");
        mainPanel.add(groupPanel, "Group");
        mainPanel.add(subjectPanel, "Subject");

        studentButton.addActionListener(e -> cardLayout.show(mainPanel, "Student"));
        groupButton.addActionListener(e -> cardLayout.show(mainPanel, "Group"));
        subjectButton.addActionListener(e -> cardLayout.show(mainPanel, "Subject"));
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