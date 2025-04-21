package projekt;

import java.util.HashMap;
import java.util.Map;

public class Subject {
    public static final String[] SUBJECT_NAMES = {
        "Język Java",
        "Język C++",
        "Język C#",
        "Analiza Matematyczna",
        "Matematyka Dyskretna",
        "Statystyka"
    };

    private String subjectName;
    
    private Map<Criteria, Integer> gradingCriteria;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
        this.gradingCriteria = new HashMap<>();
        for (Criteria c : Criteria.values()) {
            gradingCriteria.put(c, c.getmaxPoints());
        }
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Map<Criteria, Integer> getGradingCriteria() {
        return gradingCriteria;
    }

    public void updateCriterion(Criteria criteria, int newMax) {
        gradingCriteria.put(criteria, newMax);
    }
}
