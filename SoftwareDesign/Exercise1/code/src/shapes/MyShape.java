package shapes;


import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class MyShape extends Object {
    private double x;
    private double y;
    private MyColor color;

    public MyShape(double x, double y, MyColor color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public MyShape(double x, double y){
        this(x, y, MyColor.Black);
    }
    public MyShape(MyColor color){
        this(0, 0, color);
    }

    public MyShape(){
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public MyColor getColor() {
        return color;
    }

    public void setColor(MyColor color) {
        this.color = color;
    }

    public abstract void draw(GraphicsContext gc);

    @Override
    public String toString(){
        return "Class MyShapeis the hierarchy’s superclass and inherits the Java class Object. An\n" +
                "implementation of the class defines a point (x, y) and the color of the shape. ";
    }
}
