package shapes;

import javafx.scene.canvas.GraphicsContext;

public class MyOval extends MyShape {
    private MyRectangle outerBox;

    public MyOval(double width, double height, double x, double y, MyColor color){
        super(x, y, color);
        outerBox = new MyRectangle(width, height, x, y, color);
    }

    public MyOval(MyRectangle box, MyColor color){
        this(box.getWidth(), box.getHeight(), box.getX(), box.getY(), color);
    }

    public double getPerimeter(){
        double a = outerBox.getWidth() / 2;
        double b = outerBox.getHeight() / 2;
        return Math.PI * (3*(a+b) - Math.sqrt((3*a+b)*(a+3*b)));
    }

    public MyRectangle getInnerBox(){
        return new MyRectangle(outerBox.getWidth()/Math.sqrt(2), outerBox.getHeight()/Math.sqrt(2), outerBox.getX(), outerBox.getY(), outerBox.getColor());
    }

    public double getArea(){
        return Math.PI * outerBox.getWidth() * outerBox.getHeight() / 4;
    }

    public String toString(){
        return "MyOval{"+
                "width="+ outerBox.getWidth()+
                ", height=" + outerBox.getHeight() +
                ", perimeter=" + getPerimeter() +
                ", area=" + getArea() +
                "}";
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor().toFXPaintColor());
        gc.fillOval(getX() - outerBox.getWidth()/2, getY() - outerBox.getHeight()/2, outerBox.getWidth(), outerBox.getHeight());
    }

    @Override
    public MyRectangle getMyBoundingBox() {
        return this.outerBox;
    }

}
