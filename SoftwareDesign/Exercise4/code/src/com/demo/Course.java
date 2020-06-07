package com.demo;

public class Course {
    private String ID;
    private String title;
    private String department;

    public Course(String ID, String title, String department) {
        this.ID = ID;
        this.title = title;
        this.department = department;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
