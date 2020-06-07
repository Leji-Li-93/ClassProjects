package com.demo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

public class RandomDataGenerator {
    private DBConnector connector = null;

    public RandomDataGenerator(DBConnector connector){
        this.connector = connector;
    }

    /**
     * This function will insert random-generated students to the database,
     * as well as adding that student to the given course
     * @param courseID the course of the class that need add more students
     * @param section the section of the class
     * @param year the year of the class
     * @param semester the semester of the class
     * @param repeat the amount of randomly generated student
     */
    public void classSampleData(String courseID, int section, int year, Semester semester, int repeat){
        int count = 0;
        while(count < repeat){
            Student ranS = randomStudent();
            if(connector.insertStudent(ranS)){
                connector.insertClass(new Clazz(courseID, ranS.getID(), section, year, semester, getGPA()));
                count++;
            }
        }
    }

    /**
     * generate a student randomly.
     * The student ID, name and sex are being picked up randomly
     * @return randomly generated student
     */
    private Student randomStudent(){
        Random random = new Random();
        String first = "";
        String last = "";
        for(int i = 0; i < 5; i++){
            first += (char)(random.nextInt(26) + 'a');
        }
        for(int i = 0; i < 3; i++){
            last += (char)(random.nextInt(26) + 'a');
        }
        int id = random.nextInt(89999999) + 10000000;
        Sex[] sexes = Sex.values();
        return new Student(id, first, last,sexes[random.nextInt(1)]);
    }

    /**
     * putting the initial data to the database
     */
    public void initializeData(){
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(new Student(12345678, "Leji", "Li", "leji@email.com", Sex.M));
        students.add(new Student(15978634, "Kara", "Chen", "kara@email.com", Sex.F));
        students.add(new Student(32641287, "Jiayi", "Li", "jiayi@email.com", Sex.F));
        students.add(new Student(98732164, "Ceci", "Ao", "cci@email.com", Sex.F));
        students.add(new Student(80204672, "Erik", "Hu", "erik@email.com", Sex.M));
        students.add(new Student(74123690, "Yubo", "Liang", "erik@email.com", Sex.M));


        ArrayList<Course> courses = new ArrayList<Course>(3);
        courses.add(new Course("CSC22100", "Software Design Laboratory", "Computer Science"));
        courses.add(new Course("CSC11300", "Programming Language", "Computer Science"));
        courses.add(new Course("CSC22000", "Algorithms", "Computer Science"));

        int[] sections = {42264, 42255, 25696};
        int[] years = {2020, 2020, 2019};
        Semester[] semesters = {Semester.Spring, Semester.Spring, Semester.Fall};
        ArrayList<Clazz> clazzes = new ArrayList<Clazz>();
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < courses.size(); j++) {
                clazzes.add(new Clazz(courses.get(j).getID(), students.get(i).getID(), sections[j], years[j], semesters[j], getGPA()));
            }
        }

        for(Student s: students){
            connector.insertStudent(s);
        }
        for(Course c: courses){
            connector.insertCourse(c);
        }
        for(Clazz c: clazzes){
            connector.insertClass(c);
        }
    }

    /**
     * randomly generate a GPA in letter form
     * @return a random GPA in letter
     */
    private GPA getGPA(){
        GPA[] GPAs = GPA.values();
        Random random = new Random();
        return GPAs[random.nextInt(GPAs.length)];
    }
}
