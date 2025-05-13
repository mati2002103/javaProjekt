package projekt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Panel wyświetlający wyniki danego studenta z poszczególnych przedmiotów.
 * Umożliwia przeglądanie i edytowanie punktów z kryteriów oceniania oraz ich
 * zapisywanie.
 * <p>
 * Wyniki prezentowane są w zakładkach — po jednej dla każdego przedmiotu.
 * </p>
 * 
 * @author Natalia Rzeszótko, Wiśniewski Mateusz
 */
public class StudentScorePanel extends JPanel {

	/** Student, którego wyniki są wyświetlane */
	private Student student;

	/** Lista wszystkich przedmiotów */
	private List<Subject> subjects;

	/** Komponent zakładek (tabów) z osobnymi panelami dla każdego przedmiotu */
	private JTabbedPane tabbedPane;

	/**
	 * Konstruktor tworzący panel z wynikami danego studenta.
	 *
	 * @param student  student, którego wyniki mają być pokazane
	 * @param subjects lista wszystkich przedmiotów w systemie
	 */
	public StudentScorePanel(Student student, List<Subject> subjects) {
		this.student = student;
		this.subjects = subjects;
		initializeUI();
	}

	/**
	 * Inicjalizuje komponenty interfejsu użytkownika: nagłówek, zakładki i
	 * przyciski.
	 */
	private void initializeUI() {
		setLayout(new BorderLayout());

		JLabel headerLabel = new JLabel("Wyniki studenta: " + student.getName() + " " + student.getSurname(),
				SwingConstants.CENTER);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		add(headerLabel, BorderLayout.NORTH);

		tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);

		for (Subject subject : subjects) {
			JPanel subjectPanel = createSubjectPanel(subject);
			tabbedPane.addTab(subject.getSubjectName(), subjectPanel);
		}

		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Zapisz");
		JButton closeButton = new JButton("Zamknij");

		saveButton.addActionListener(e -> saveAllChanges());
		closeButton.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());

		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Tworzy panel dla konkretnego przedmiotu, zawierający tabelę z kryteriami i
	 * punktami.
	 *
	 * @param subject przedmiot, którego panel jest tworzony
	 * @return panel z tabelą wyników i podsumowaniem
	 */
	private JPanel createSubjectPanel(Subject subject) {
		JPanel panel = new JPanel(new BorderLayout());

		Map<String, Integer> criteria = subject.getGradingCriteria();
		String[] columnNames = { "Kryterium", "Punkty", "Max punktów" };

		DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};

		Map<String, Integer> studentGrades = student.getGrades().getOrDefault(subject, new HashMap<>());
		int totalPoints = 0;
		int totalMaxPoints = 0;

		for (Map.Entry<String, Integer> entry : criteria.entrySet()) {
			String criteriaName = entry.getKey();
			int maxPoints = entry.getValue();
			int points = studentGrades.getOrDefault(criteriaName, 0);

			model.addRow(new Object[] { criteriaName, points, maxPoints });

			totalPoints += points;
			totalMaxPoints += maxPoints;
		}

		JTable table = new JTable(model);
		table.setRowHeight(25);

		JPanel summaryPanel = new JPanel(new GridLayout(1, 2));
		summaryPanel.add(new JLabel("Suma punktów: " + totalPoints));
		summaryPanel.add(new JLabel("Suma max punktów: " + totalMaxPoints));

		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(summaryPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Zapisuje zmiany dokonane w punktach we wszystkich zakładkach. Waliduje
	 * wartości punktów oraz aktualizuje dane studenta. Wyświetla komunikat o
	 * błędach lub sukcesie.
	 */
	private void saveAllChanges() {
		List<String> errors = new ArrayList<>();
		boolean hasErrors = false;

		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			Component component = tabbedPane.getComponentAt(i);
			if (component instanceof JPanel) {
				JScrollPane scrollPane = (JScrollPane) ((JPanel) component).getComponent(0);
				JTable table = (JTable) scrollPane.getViewport().getView();
				DefaultTableModel model = (DefaultTableModel) table.getModel();

				String subjectName = tabbedPane.getTitleAt(i);
				Subject subject = findSubjectByName(subjectName);
				if (subject == null)
					continue;

				Map<String, Integer> newGrades = new HashMap<>();
				int totalPoints = 0;
				int totalMaxPoints = 0;

				for (int row = 0; row < model.getRowCount(); row++) {
					String criteriaName = (String) model.getValueAt(row, 0);
					int points;
					try {
						points = Integer.parseInt(model.getValueAt(row, 1).toString());
					} catch (NumberFormatException e) {
						points = 0;
					}
					int maxPoints = (Integer) model.getValueAt(row, 2);

					if (points < 0 || points > maxPoints) {
						errors.add(
								subjectName + ": " + criteriaName + " - nieprawidłowa wartość (0-" + maxPoints + ")");
						hasErrors = true;
					} else {
						newGrades.put(criteriaName, points);
						totalPoints += points;
						totalMaxPoints += maxPoints;
					}
				}

				if (!hasErrors) {
					student.getGrades().put(subject, newGrades);

					JPanel summaryPanel = (JPanel) ((JPanel) component).getComponent(1);
					((JLabel) summaryPanel.getComponent(0)).setText("Suma punktów: " + totalPoints);
					((JLabel) summaryPanel.getComponent(1)).setText("Suma max punktów: " + totalMaxPoints);
				}
			}
		}

		if (hasErrors) {
			JOptionPane.showMessageDialog(this, "Wystąpiły błędy:\n" + String.join("\n", errors), "Błędy w danych",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, "Dane zostały poprawnie zapisane!", "Sukces",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Wyszukuje przedmiot na podstawie jego nazwy.
	 *
	 * @param subjectName nazwa przedmiotu
	 * @return obiekt przedmiotu lub {@code null} jeśli nie znaleziono
	 */
	private Subject findSubjectByName(String subjectName) {
		return subjects.stream().filter(s -> s.getSubjectName().equals(subjectName)).findFirst().orElse(null);
	}
}
