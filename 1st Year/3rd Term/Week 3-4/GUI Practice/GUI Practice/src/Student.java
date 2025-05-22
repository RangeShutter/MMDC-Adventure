public class Student {


public static final float Milestone1_MAX = 25.0f;
public static final float Milestone2_MAX = 40.0f;
public static final float TerminalAssessement3_MAX = 35.0f;
//attributes
private String studentNumber;
private String studentName;
private float milestone1;
private float milestone2;
private float terminalAssessement;
private float averageGrade;

//getter and setter methods
//getter methods
public String getStudentNumber() {
return studentNumber;
}

public String getStudentName() {
return studentName;
}

public String getMilestone1() {
return String.valueOf(milestone1);
}

public String getMilestone2() {
return String.valueOf(milestone2);
}

public String getTerminalAssessement3() {
return String.valueOf(terminalAssessement);
}

public String getAveGrade() {
return String.valueOf(averageGrade);
}

//setter methods
public void setStudentNumber(String studNumber) {
studentNumber = studNumber;
}

public void setStudentName(String studName) {
studentName = studName;
}

public void setQuiz1(float m1) {
milestone1 =m1;
}

public void setQuiz2(float m2) {
milestone2 = m2;
}

public void setQuiz3(float TA) {
terminalAssessement = TA;
}

//methods
public float computeAverage() {
averageGrade = (milestone1 + milestone2 + terminalAssessement) / 3;
return averageGrade;
}
}
