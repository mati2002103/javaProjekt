package projekt;

import java.util.ArrayList;
import java.util.List;

public class GroupDB {
	  private List<Group> groupList = new ArrayList<>();

	    
	    public void addGroup(Group group) {
	        groupList.add(group);
	    }

	    
	    public Group getGroupByCode(String groupCode) {
	        for (Group g : groupList) {
	            if (g.getGroupCode().equals(groupCode)) {
	                return g;
	            }
	        }
	        return null; 
	    }

	    public List<Group> getAllGroups() {
	        return groupList;
	    }

	    
	    public boolean updateGroup(String groupCode, String newSpecialization, String newDescription) {
	        Group group = getGroupByCode(groupCode);
	        if (group != null) {
	            group.setSpecialization(newSpecialization);
	            group.setDescription(newDescription);
	            return true;
	        }
	        return false;
	    }

	    
	    public boolean deleteGroup(String groupCode) {
	        Group group = getGroupByCode(groupCode);
	        if (group != null) {
	            groupList.remove(group);
	            return true;
	        }
	        return false;
	    }
}
