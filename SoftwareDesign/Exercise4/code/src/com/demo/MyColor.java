package com.demo;

import javafx.scene.paint.Color;

public enum MyColor {
    FireBrick(178,34,34),
    LightPink(255,182,193),
    OliveDrab(85,107,47),
    MediumAquamarine(0,250,154),
    Turquoise(64,224,208),
    RoyalBlue(65,105,225),
    White(255,255,255),
    Black(0,0,0),
    Gray(128,128,128),
    LightGray(211,211,211),
    Yellow(	255,255,0);

    private int r, g, b;

    private MyColor(){
        this(0, 0,0);
    }

    private MyColor(int r, int g, int b){
        setColor(r, g, b);
    }

    public Color toFXPaintColor(){
        return Color.rgb(r, g, b);
    }

    public void setColor(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public MyColor getColor(){
        return this;
    }

    public static Color randomColor(){
        int r = (int)(Math.random() * 256);
        int g = (int)(Math.random() * 256);
        int b = (int)(Math.random() * 256);

        return Color.rgb(r, g, b);
    }
}
