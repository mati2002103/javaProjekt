package projekt;

import java.util.Set;

public class Group {

	private String groupCode;
	private String specialization;
	private String description;
	private Set<Student> students;

	Group() {
		this.groupCode = null;
		this.specialization = null;
		this.description = null;

	}

	public Group(String groupCode, String specialization, String description) {
		this.groupCode = groupCode;
		this.specialization = specialization;
		this.description = description;
	}
	public void addStudent(Student s) {
	    if (!students.contains(s)) {
	    	students.add(s);
	    }
	}

	////////////////////////////
	////// Getters and setters
	////////////////////////////
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

}
