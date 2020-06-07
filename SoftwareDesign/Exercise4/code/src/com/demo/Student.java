package com.demo;

public class Student {
    private int ID;
    private String firstName;
    private String lastName;
    private String email;
    private Sex sex;

    public Student(int ID, String firstName, String lastName, String email, Sex sex) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sex = sex;
    }

    public Student(int ID, String firstName, String lastName, Sex sex) {
        this(ID, firstName, lastName, "", sex);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
