package projekt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Główne okno aplikacji GUI służącej do zarządzania studentami, grupami i
 * przedmiotami.
 * <p>
 * Klasa odpowiada za nawigację między panelami oraz import i eksport danych
 * aplikacji.
 * </p>
 * 
 * @author Natala Rzeszótko
 */
public class MyWindow extends JFrame {

    /** Panel zarządzania studentami */
    private StudentPanel studentPanel;

    /** Panel zarządzania grupami */
    private GroupPanel groupPanel;

    /** Panel zarządzania przedmiotami */
    private SubjectPanel subjectPanel;

    /** Panel wyszukiwania studentów */
    private SearchStudentPanel searchStudentPanel;

    /** Layout do przełączania paneli */
    private final CardLayout cardLayout;

    /** Główny panel kontenera dla wszystkich podpaneli */
    private final JPanel mainPanel;

    /** Baza danych studentów */
    private final StudentDB studentDB;

    /** Baza danych przedmiotów */
    private final SubjectDB subjectDB;

    /** Baza danych grup */
    private final GroupDB groupDB;

    /** Wspólna pula wątków dla aplikacji */
    private final ExecutorService threadPool;

    /**
     * Konstruktor tworzący okno główne aplikacji.
     * Inicjalizuje wszystkie panele, bazy danych i konfiguracje GUI.
     */
    public MyWindow() {
        super("Aplikacja do zarządzania studentami, grupami i przedmiotami");
        initializeWindow();

        studentDB = new StudentDB();
        subjectDB = new SubjectDB();
        groupDB = new GroupDB();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupMenuPanel();
        setupFunctionalPanels();

        add(mainPanel);
        setVisible(true);

        threadPool = Executors.newFixedThreadPool(AppConfig.THREAD_POOL_SIZE);
    }

    /**
     * Inicjalizuje podstawowe właściwości okna:
     * rozmiar, lokalizację oraz obsługę zdarzenia zamknięcia.
     */
    private void initializeWindow() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    /**
     * Tworzy panel menu głównego z przyciskami nawigacyjnymi
     * oraz przyciskami importu, eksportu i wyjścia.
     */
    private void setupMenuPanel() {
        JPanel menuPanel = new JPanel(new BorderLayout());

        JPanel navPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttonLabels = { "Sekcja Studentów", "Sekcja Grup", "Sekcja Przedmiotów", "Wyszukiwanie Studenta" };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            button.addActionListener(e -> handleMenuAction(label));
            navPanel.add(button);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton importButton = new JButton("Importuj wszystkie dane");
        JButton exportButton = new JButton("Eksportuj wszystkie dane");
        JButton exitButton = new JButton("Wyjdź");

        importButton.addActionListener(e -> importAllData());
        exportButton.addActionListener(e -> exportAllData());
        exitButton.addActionListener(e -> confirmExit());

        bottomPanel.add(importButton);
        bottomPanel.add(exportButton);
        bottomPanel.add(exitButton);

        menuPanel.add(navPanel, BorderLayout.CENTER);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(menuPanel, "Menu");
    }

    /**
     * Obsługuje akcję przełączania widoku w zależności od wybranego przycisku menu.
     *
     * @param action etykieta przycisku, która determinuje wybór panelu
     */
    private void handleMenuAction(String action) {
        switch (action) {
            case "Sekcja Studentów":
                cardLayout.show(mainPanel, "Student");
                break;
            case "Sekcja Grup":
                cardLayout.show(mainPanel, "Group");
                break;
            case "Sekcja Przedmiotów":
                cardLayout.show(mainPanel, "Subject");
                break;
            case "Wyszukiwanie Studenta":
                cardLayout.show(mainPanel, "Search");
                break;
        }
    }

    /**
     * Inicjalizuje wszystkie funkcjonalne panele i dodaje je do głównego kontenera.
     */
    private void setupFunctionalPanels() {
        studentPanel = new StudentPanel(this, studentDB, subjectDB, groupDB);
        groupPanel = new GroupPanel(this, groupDB);
        subjectPanel = new SubjectPanel(this, subjectDB);
        searchStudentPanel = new SearchStudentPanel(this, studentDB);

        mainPanel.add(studentPanel, "Student");
        mainPanel.add(groupPanel, "Group");
        mainPanel.add(subjectPanel, "Subject");
        mainPanel.add(searchStudentPanel, "Search");
    }

    /**
     * Importuje dane studentów, grup i przedmiotów z pliku binarnego.
     * Wyświetla komunikaty o sukcesie lub błędzie.
     */
    private void importAllData() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            threadPool.submit(() -> {
                try (DataInputStream in = new DataInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                    groupDB.loadFromFile(in);
                    subjectDB.loadFromFile(in);
                    studentDB.loadFromFile(in, groupDB, subjectDB);

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Dane zaimportowane pomyślnie!");
                        refreshAllPanels();
                    });

                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Błąd importu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE));
                }
            });
        }
    }

    /**
     * Eksportuje dane studentów, grup i przedmiotów do pliku binarnego.
     * Wyświetla komunikaty o sukcesie lub błędzie.
     */
    private void exportAllData() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            threadPool.submit(() -> {
                try (DataOutputStream out = new DataOutputStream(new FileOutputStream(chooser.getSelectedFile()))) {
                    groupDB.saveToFile(out);
                    subjectDB.saveToFile(out);
                    studentDB.saveToFile(out);

                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "Dane wyeksportowane pomyślnie!"));

                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Błąd eksportu: " + e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE));
                }
            });
        }
    }

    /**
     * Odświeża wszystkie panele funkcjonalne, aby wyświetlały aktualne dane.
     */
    private void refreshAllPanels() {
        studentPanel.refreshStudentTable();
        groupPanel.refreshTable();
        subjectPanel.refreshTable();
    }

    /**
     * Wyświetla okno potwierdzenia przed zamknięciem aplikacji.
     * Jeśli użytkownik zatwierdzi, program zostaje zakończony.
     */
    private void confirmExit() {
        int option = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz zamknąć aplikację?",
                "Potwierdzenie wyjścia", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            threadPool.shutdownNow();
            dispose();
            System.exit(0);
        }
    }

    /**
     * Pokazuje panel menu głównego aplikacji.
     */
    public void showMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    /**
     * Główna metoda uruchamiająca aplikację.
     *
     * @param args argumenty wiersza poleceń (nieużywane)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MyWindow();
        });
    }
}
