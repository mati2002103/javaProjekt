package projekt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class MyWindow extends JFrame {
	private StudentPanel studentPanel;
	private GroupPanel groupPanel;
	private SubjectPanel subjectPanel;
	private SearchStudentPanel searchStudentPanel;

	private final CardLayout cardLayout;
	private final JPanel mainPanel;
	private final StudentDB studentDB;
	private final SubjectDB subjectDB;
	private final GroupDB groupDB;

	public MyWindow() {
		super("Aplikacja do zarządzania studentami, grupami i przedmiotami");
		initializeWindow();

		// Inicjalizacja wspólnych baz danych
		studentDB = new StudentDB();
		subjectDB = new SubjectDB();
		groupDB = new GroupDB();

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		setupMenuPanel();
		setupFunctionalPanels();

		add(mainPanel);
		setVisible(true);
	}

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

	private void setupMenuPanel() {
		JPanel menuPanel = new JPanel(new BorderLayout());

		// Główne przyciski nawigacyjne
		JPanel navPanel = new JPanel(new GridLayout(4, 1, 10, 10));
		navPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		String[] buttonLabels = { "Sekcja Studentów", "Sekcja Grup", "Sekcja Przedmiotów", "Wyszukiwanie Studenta" };

		for (String label : buttonLabels) {
			JButton button = new JButton(label);
			button.setFont(new Font("Arial", Font.PLAIN, 16));
			button.addActionListener(e -> handleMenuAction(label));
			navPanel.add(button);
		}

		// Panel z przyciskami import/export i wyjścia
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

	private void importAllData() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try (DataInputStream in = new DataInputStream(new FileInputStream(chooser.getSelectedFile()))) {
				// Kolejność odczytu musi być zgodna z zapisem
				groupDB.loadFromFile(in);
				subjectDB.loadFromFile(in);
				studentDB.loadFromFile(in, groupDB, subjectDB);

				JOptionPane.showMessageDialog(this, "Dane zaimportowane pomyślnie!");
				refreshAllPanels(); // <-- kluczowe dodanie

			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Błąd importu: " + e.getMessage(), "Błąd",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void exportAllData() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try (DataOutputStream out = new DataOutputStream(new FileOutputStream(chooser.getSelectedFile()))) {
				// Kolejność zapisu musi być zgodna z odczytem
				groupDB.saveToFile(out);
				subjectDB.saveToFile(out);
				studentDB.saveToFile(out);

				JOptionPane.showMessageDialog(this, "Dane wyeksportowane pomyślnie!");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Błąd eksportu: " + e.getMessage(), "Błąd",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void refreshAllPanels() {
		studentPanel.refreshStudentTable();
		groupPanel.refreshTable();
		subjectPanel.refreshTable();
	}

	private void confirmExit() {
		int option = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz zamknąć aplikację?",
				"Potwierdzenie wyjścia", JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			dispose();
			System.exit(0);
		}
	}

	public void showMenu() {
		cardLayout.show(mainPanel, "Menu");
	}

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