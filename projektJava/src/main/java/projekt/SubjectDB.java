package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wiśniewski Mateusz
 * 
 * Klasa odpowiedzialna za zarządzanie bazą przedmiotów.
 */
public class SubjectDB {

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
	public boolean updateSubjectCriteria(String name, Map<Criteria, Integer> newCriteria) {
		Subject subject = getSubjectByName(name);
		if (subject != null) {
			for (Map.Entry<Criteria, Integer> entry : newCriteria.entrySet()) {
				subject.updateCriterion(entry.getKey(), entry.getValue());
			}
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

            Map<Criteria, Integer> criteriaMap = s.getGradingCriteria();
            out.writeInt(criteriaMap.size());
            for (Map.Entry<Criteria, Integer> entry : criteriaMap.entrySet()) {
                out.writeUTF(entry.getKey().name());
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
            for (int j = 0; j < criteriaCount; j++) {
                String criteriaName = in.readUTF();
                int maxPoints = in.readInt();
                subj.updateCriterion(Criteria.valueOf(criteriaName), maxPoints);
            }

            subjectList.add(subj);
        }
    }
	
}
