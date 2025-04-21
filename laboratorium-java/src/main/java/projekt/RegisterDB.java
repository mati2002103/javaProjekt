package projekt;

public class RegisterDB {
	public void przypiszStudentaDoGrupy(Student student, Group group) {
		student.setGroup(group); 
		group.addStudent(student); 
	}
	
}
