package projekt;

public class StudentDB {
	public void addStudentToGroup(Student student, Group group) {
		student.setGroup(group); 
		group.addStudent(student); 
	}
	
}
