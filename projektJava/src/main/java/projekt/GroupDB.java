package projekt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * Klasa odpowiedzialna za zarządzanie bazą grup.
 * 
 * @author Wiśniewski Mateusz, Rafał Okoniecki
 */
public class GroupDB {

	/** Lista przechowująca wszystkie grupy */
	private List<Group> groupList = new ArrayList<>();

	/**
	 * Dodaje nową grupę do bazy.
	 *
	 * @param group obiekt grupy do dodania
	 */
	public void addGroup(Group group) {
		groupList.add(group);
	}

	/**
	 * Zwraca grupę na podstawie jej kodu.
	 *
	 * @param groupCode kod grupy
	 * @return obiekt grupy, jeśli znaleziono; w przeciwnym razie null
	 */
	public Group getGroupByCode(String groupCode) {
		for (Group g : groupList) {
			if (g.getGroupCode().equals(groupCode)) {
				return g;
			}
		}
		return null;
	}

	/**
	 * Zwraca wszystkie grupy w bazie.
	 *
	 * @return lista wszystkich grup
	 */
	public List<Group> getAllGroups() {
		return groupList;
	}

	/**
	 * Aktualizuje dane grupy na podstawie jej kodu.
	 *
	 * @param groupCode         kod grupy do aktualizacji
	 * @param newSpecialization nowa specjalizacja
	 * @param newDescription    nowy opis
	 * @return true jeśli aktualizacja się powiodła, false jeśli nie znaleziono
	 *         grupy
	 */
	public boolean updateGroup(String groupCode, String newSpecialization, String newDescription) {
		Group group = getGroupByCode(groupCode);
		if (group != null) {
			group.setSpecialization(newSpecialization);
			group.setDescription(newDescription);
			return true;
		}
		return false;
	}

	/**
	 * Usuwa grupę z bazy na podstawie jej kodu.
	 *
	 * @param groupCode kod grupy do usunięcia
	 * @return true jeśli usunięto grupę, false jeśli nie znaleziono
	 */
	public boolean deleteGroup(String groupCode) {
		Group group = getGroupByCode(groupCode);
		if (group != null) {
			groupList.remove(group);
			return true;
		}
		return false;
	}

	/**
	 * Zapisuje listę group do strumienia w formacie binarnym.
	 * 
	 * @param out strumień wyjściowy DataOutputStream z zapisanymi danymi grup
	 * @throws IOException błąd zapisu
	 */

	public void saveToFile(DataOutputStream out) throws IOException {
		out.writeInt(groupList.size());
		for (Group g : groupList) {
			out.writeUTF(g.getGroupCode());
			out.writeUTF(g.getSpecialization());
			out.writeUTF(g.getDescription());

			out.writeInt(g.getStudents().size());
			for (Student s : g.getStudents()) {
				out.writeUTF(s.getName());
				out.writeUTF(s.getSurname());
				out.writeUTF(s.getAlbumNumber());
			}
		}
	}

	/**
	 * Wczytuje liste grup z podanego strumienia wejściowego w formacie binarnym.
	 * 
	 * @param in strumień wejściowy DataInputStream
	 * @throws IOException błąd odczytu
	 */

	public void loadFromFile(DataInputStream in) throws IOException {
		int groupCount = in.readInt();
		for (int i = 0; i < groupCount; i++) {
			String code = in.readUTF();
			String specialization = in.readUTF();
			String description = in.readUTF();

			Group g = new Group(code, specialization, description);

			int studentCount = in.readInt();
			for (int j = 0; j < studentCount; j++) {
				String name = in.readUTF();
				String surname = in.readUTF();
				String albumNumber = in.readUTF();
				Student s = new Student(name, surname, albumNumber, g);
				g.addStudent(s);
			}

			groupList.add(g);
		}
	}
}
