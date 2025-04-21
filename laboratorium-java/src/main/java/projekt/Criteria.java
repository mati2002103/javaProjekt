package projekt;

public enum Criteria {
	activity(5),
	homeworks(10),
	project(10),
	test(25),
	exam(50);
	private int maxPoints;
	private Criteria(int maxPoints) {
	this.maxPoints = maxPoints;
	}
	public int getmaxPoints() {
		return maxPoints;
	}
}
