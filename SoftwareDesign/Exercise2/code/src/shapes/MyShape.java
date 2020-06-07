package shapes;


import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public abstract class MyShape extends Object implements MyShapePosition {
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

    @Override
    public MyRectangle getMyBoundingBox() {
        return new MyRectangle(0, 0, this.x, this.y, this.color);
    }

    @Override
    public boolean doOverlap(MyShapePosition other) {
        if(distanceTo(other) == 0){
            return true;
        }

        MyRectangle myBox = getMyBoundingBox();
        MyRectangle otherBox = other.getMyBoundingBox();
        // checking the center point and the edges
        // two rectangles are considered not touching each other should meet the two conditions:
        // the distance between two xs should be larger than half of the sum of their width.
        // the distance between two ys should be larger than half of hte sum of their height.
        if((Math.abs(myBox.getWidth() + otherBox.getWidth())/2 < Math.abs(myBox.getX() - otherBox.getX())) &&
                (Math.abs(myBox.getHeight() + otherBox.getHeight())/2 < Math.abs(myBox.getY() - otherBox.getY()))){
            return false;
        }

        return true;
    }

    @Override
    public double[] getPoint() {
        return new double[]{this.x, this.y};
    }

    @Override
    public void setPoint(double[] point) {
        this.x = point[0];
        this.y = point[1];
    }

    @Override
    public void moveTo(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    @Override
    public double distanceTo(MyPoint point) {
        double[] otherPoint = point.getPoint();
        return Math.sqrt(Math.pow(this.x - otherPoint[0], 2) + Math.pow(this.y - otherPoint[1], 2));
    }
}
