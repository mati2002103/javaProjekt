package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa odpowiedzialna za zarządzanie bazą przedmiotów.
 * 
 * @author Okoniecki Rafał
 * 
 */
public class SubjectDB {
	private Map<String, Integer> gradingCriteria;

	/** Lista przechowująca wszystkie przedmioty */
	private List<Subject> subjectList = new ArrayList<>();

	/**
	 * Dodaje nowy przedmiot do bazy.
	 *
	 * @param subject przedmiot do dodania
	 */
	public void addSubject(Subject subject) {
		subjectList.add(subject);
	}

	/**
	 * Wyszukuje przedmiot po nazwie.
	 *
	 * @param name nazwa przedmiotu
	 * @return obiekt przedmiotu, jeśli znaleziono; w przeciwnym razie null
	 */
	public Subject getSubjectByName(String name) {
		for (Subject s : subjectList) {
			if (s.getSubjectName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Ustawia nowe kryteria oceniania dla przedmiotu.
	 *
	 * @param gradingCriteria mapa kryteriów i ich maksymalnych punktów
	 */
	public void setGradingCriteria(Map<String, Integer> gradingCriteria) {
	    this.gradingCriteria = gradingCriteria;
	}
	
	
	
	/**
	 * Zwraca listę wszystkich przedmiotów w bazie.
	 *
	 * @return lista wszystkich przedmiotów
	 */
	public List<Subject> getAllSubjects() {
		return subjectList;
	}

	/**
	 * Aktualizuje kryteria ocen dla przedmiotu.
	 *
	 * @param name        nazwa przedmiotu do aktualizacji
	 * @param newCriteria mapa nowych kryteriów ocen z maksymalnymi punktami
	 * @return true jeśli aktualizacja się powiodła, false jeśli nie znaleziono
	 *         przedmiotu
	 */
	public boolean updateSubjectCriteria(String name, Map<String, Integer> newCriteria) {
		Subject subject = getSubjectByName(name);
		if (subject != null) {
			subject.setGradingCriteria(newCriteria);
			return true;
		}
		return false;
	}

	/**
	 * Usuwa przedmiot z bazy na podstawie jego nazwy.
	 *
	 * @param name nazwa przedmiotu do usunięcia
	 * @return true jeśli usunięto przedmiot, false jeśli nie znaleziono
	 */
	public boolean deleteSubject(String name) {
		Subject subject = getSubjectByName(name);
		if (subject != null) {
			subjectList.remove(subject);
			return true;
		}
		return false;
	}

	/**
	 * Zapisuje listę przedmiotów do strumienia w formacie binarnym.
	 *
	 * @param out 			strumień wyjściowy DataOutputStream z zapisanymi danymi przedmiotów
	 * @throws IOException  błąd zapisu
	 */
	public void saveToFile(DataOutputStream out) throws IOException {
		out.writeInt(subjectList.size());
		for (Subject s : subjectList) {
			out.writeUTF(s.getSubjectName());

			Map<String, Integer> criteriaMap = s.getGradingCriteria();
			out.writeInt(criteriaMap.size());
			for (Map.Entry<String, Integer> entry : criteriaMap.entrySet()) {
				out.writeUTF(entry.getKey());
				out.writeInt(entry.getValue());
			}
		}
	}

	/**
	 * Wczytuje listę przedmiotów ze strumienia w formacie binarnym.
	 *
	 * @param in			strumień wejściowy DataInputStream
	 * @throws IOException  błąd odczytu
	 */
	public void loadFromFile(DataInputStream in) throws IOException {
		int subjectCount = in.readInt();
		for (int i = 0; i < subjectCount; i++) {
			String name = in.readUTF();
			Subject subj = new Subject(name);

			int criteriaCount = in.readInt();
			Map<String, Integer> criteriaMap = new HashMap<>();
			for (int j = 0; j < criteriaCount; j++) {
				String criteriaName = in.readUTF();
				int maxPoints = in.readInt();
				criteriaMap.put(criteriaName, maxPoints);
			}

			subj.setGradingCriteria(criteriaMap);
			subjectList.add(subj);
		}
	}
}
