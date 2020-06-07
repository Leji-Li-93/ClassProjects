package com.demo;

public enum Semester{
    Spring("Spring"),
    Summer("Summer"),
    Fall("Fall"),
    Winter("Winter");

    private String val;
    private Semester(String s){
        val = s;
    }
    public String getVal(){
        return val;
    }
}
