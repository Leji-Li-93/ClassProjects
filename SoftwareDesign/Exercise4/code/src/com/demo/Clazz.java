package com.demo;

public class Clazz {
    private String courseID;
    private int studentID;
    private int section;
    private int year;
    private Semester semester;
    private GPA GPA;

    public Clazz(String courseID, int studentID, int section, int year, Semester semester, GPA GPA) {
        this.courseID = courseID;
        this.studentID = studentID;
        this.section = section;
        this.year = year;
        this.semester = semester;
        this.GPA = GPA;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public GPA getGPA() {
        return GPA;
    }

    public void setGPA(GPA GPA) {
        this.GPA = GPA;
    }
}
