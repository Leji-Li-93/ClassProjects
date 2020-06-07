package com.demo;

public enum Sex{
    F('F'),
    M('M');

    private char val;
    private Sex(){
        this('F');
    }
    private Sex(char c){
        val = c;
    }

    public char getVal() {
        return val;
    }
}
