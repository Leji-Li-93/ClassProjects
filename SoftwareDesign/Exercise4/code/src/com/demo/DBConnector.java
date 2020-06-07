package com.demo;

import javafx.util.Pair;
import org.omg.CORBA.INTERNAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DBConnector {

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3306/exercise_student";
    private Connection conn = null;

    public DBConnector(String userName, String psw){
        try{
            Class.forName(JDBC_DRIVER);
            conn =DriverManager.getConnection(DB_URL, userName, psw);
            System.out.println("Connected to DB");
        } catch (Exception e){
            e.toString();
        }
        boolean initialized = initialize();
        if(initialized){
            System.out.println("DB initialized!");
        } else{
            System.out.println("DB cannot be initialized");
        }
    }

    /**
     * create the tables if not exist
     * @return
     */
    private boolean initialize() {
        if(null == conn){
            System.out.println("Connection Error! Cannot initialize Database!");
            return false;
        }
        String studentTable = "CREATE TABLE IF NOT EXISTS Students(" +
                "studentID INT UNSIGNED NOT NULL," +
                "firstName VARCHAR(255) NOT NULL," +
                "lastName  VARCHAR(255) NOT NULL," +
                "email VARCHAR(255)," +
                "sex ENUM('F', 'M')," +
                "PRIMARY KEY(studentID)" +
                ")";
        String coursesTable = "CREATE TABLE IF NOT EXISTS Courses(" +
                "courseID VARCHAR(10) NOT NULL," +
                "courseTitle VARCHAR(255) NOT NULL," +
                "department VARCHAR(255) NOT NULL," +
                "PRIMARY KEY(courseID)" +
                ")";
        String classesTable = "CREATE TABLE IF NOT EXISTS Classes(" +
                "courseID VARCHAR(10) NOT NULL," +
                "studentID INT UNSIGNED NOT NULL," +
                "section INT UNSIGNED NOT NULL," +
                "year INT UNSIGNED NOT NULL," +
                "semester ENUM('Spring', 'Summer', 'Fall', 'Winter')," +
                "GPA ENUM('A', 'B', 'C', 'D', 'F', 'W')," +
                "PRIMARY KEY (courseID, studentID, section)," +
                "FOREIGN KEY (courseID) REFERENCES Courses (courseID)," +
                "FOREIGN KEY (studentID) REFERENCES Students (studentID)" +
                ")";
        try {
            Statement statement = conn.createStatement();
            statement.execute(studentTable);
            statement.execute(coursesTable);
            statement.execute(classesTable);
        } catch (SQLException throwables) {
            System.out.println("Initialize failed!");
            System.out.println(throwables.toString());
            return false;
        }
        return true;
    }

    /**
     * insert a student to database
     * @param student
     * @return
     */
    public boolean insertStudent(Student student){
        String s = "INSERT Students" +
                "(studentID, firstName, lastName, email, sex)" +
                " VALUES (" +
                "'" + student.getID() + "'," +
                "'" + student.getFirstName() + "'," +
                "'" + student.getLastName() + "'," +
                "'" + student.getEmail() + "'," +
                "'" + student.getSex().getVal()+ "'" +
                ")";
        return insert2DB(s,
                "Cannot add a student!",
                "Add student failed!");
    }

    /**
     * insert a course to database
     * @param course
     * @return
     */
    public boolean insertCourse(Course course){
        String s = "INSERT Courses (courseID, courseTitle, department) VALUES (" +
                "'" + course.getID() + "'," +
                "'" + course.getTitle() + "'," +
                "'" + course.getDepartment() + "'" +
                ")";
        return insert2DB(s,
                "Cannot add a course!",
                "Add course failed!");
    }

    /**
     * insert a class to database
     * @param clazz
     * @return
     */
    public boolean insertClass(Clazz clazz){
        String s = "INSERT Classes (courseID, studentID, section, year, semester, GPA) VALUES (" +
                "'" + clazz.getCourseID() + "'," +
                "'" + clazz.getStudentID() + "'," +
                "'" + clazz.getSection() + "'," +
                "'" + clazz.getYear() + "'," +
                "'" + clazz.getSemester().getVal() + "'," +
                "'" + clazz.getGPA().getVal() + "'" +
                ")";
        return insert2DB(s,
                "Cannot add a class!",
                "Add class failed!");
    }

    /**
     * execute the given sql
     * @param sql
     * @param NoConnectionWarning the warning message when the connection is empty
     * @param failAddWarning
     * @return
     */
    private boolean insert2DB(String sql, String NoConnectionWarning, String failAddWarning){
        if(null == conn){
            System.out.println("Connection Error! " + NoConnectionWarning);
            return false;
        }
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException throwables) {
            System.out.println(failAddWarning);
            System.out.println(throwables.toString());
            return false;
        }
        return true;
    }

    /**
     * get the count of records in the Classes table
     * @return
     */
    public int getClassCount(){
        if(null == conn){
            System.out.println("Connection Empty! Cannot get count");
            return -1;
        }
        int count = -1;
        String s = "SELECT COUNT(*) AS Val FROM Classes";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(s);
            while (resultSet.next()){
                count = resultSet.getInt("Val");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.toString());
            return -1;
        }
        return count;
    }

    /**
     * get the GPA count of a given class
     * @param courseID the course ID of the class
     * @param year the year of the class
     * @param semester the semester of the class
     * @return an map that contains the GPAs count
     */
    public Map<Character, Integer> getClassesByCourse(String courseID, int year, Semester semester){
        if(null == conn){
            System.out.println("Connection Error!");
            return null;
        }
        String s = "SELECT GPA AS Grade, COUNT(*) AS Val FROM classes " +
                "WHERE courseID = '"+ courseID +"' " +
                "AND year = "+ year + " " +
                "AND semester = '" + semester.getVal() +"' " +
                "GROUP BY GPA";
        Map<Character, Integer> map = new HashMap<Character, Integer>(6);
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(s);
            while (resultSet.next()){
                char grade = resultSet.getString("Grade").charAt(0);
                int val = resultSet.getInt("Val");
                map.put(grade, val);
                System.out.println(grade + " -> " + val);
            }
        } catch (SQLException throwables) {
            System.out.println("Query Error!");
            System.out.println(throwables.toString());
            return null;
        }
        return map;
    }

    /**
     * close the database connection
     */
    public void close(){
        if(null != conn){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.toString();
            }
        }
    }
}
