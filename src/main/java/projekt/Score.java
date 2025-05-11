package projekt;

/**
 * Klasa reprezentująca przypisanie punktów studenta z danego kryterium danego przedmiotu.
 */
public class Score {
    private Student student;
    private Subject subject;
    private String criteriaName;
    private int points;

    public Score(Student student, Subject subject, String criteriaName, int points) {
        this.student = student;
        this.subject = subject;
        this.criteriaName = criteriaName;
        this.points = points;
    }

    @Override
    public String toString() {
        return student.getName() + " " + student.getSurname() +
               " | " + subject.getSubjectName() +
               " | " + criteriaName +
               " | " + points + " pkt";
    }
}
