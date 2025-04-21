package projekt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wiśniewski Mateusz
 * 
 *         Klasa odpowiedzialna za zarządzanie bazą grup.
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
}
