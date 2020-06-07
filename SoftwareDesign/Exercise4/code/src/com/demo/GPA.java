package com.demo;

public enum GPA {
    A('A'),
    B('B'),
    C('C'),
    D('D'),
    F('F'),
    W('W');

    private char val;
    private GPA(char gpa){
        val = gpa;
    }
    public char getVal(){
        return val;
    }
}
