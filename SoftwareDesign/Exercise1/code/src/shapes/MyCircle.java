package shapes;

import javafx.scene.canvas.GraphicsContext;

public class MyCircle extends MyShape {
    private double r;

    public MyCircle(double x, double y, double r){
        super(x, y);
        this.r = r;
    }

    public MyCircle(double x, double y, double r, MyColor color){
        this(x, y, r);
        setColor(color);
    }

    public double getArea(){
        return 4 * Math.PI * Math.pow(this.r, 2) / 3;
    }

    public double getPerimeter(){
        return 2 * Math.PI * this.r;
    }

    public double getRadius(){
        return this.r;
    }

    @Override
    public void draw(GraphicsContext gc){
        draw(gc, false);
    }

    public void draw(GraphicsContext gc, boolean isFill) {
        if(isFill){
            gc.setFill(getColor().toFXPaintColor());
            gc.fillOval(getX() - r, getY()  - r, this.r * 2d, this.r * 2d);
        } else {
            gc.setStroke(getColor().toFXPaintColor());
            gc.strokeOval(getX() - r, getY() - r, this.r * 2d, this.r * 2d);
        }
    }

    @Override
    public String toString() {
        return "MyCircle{" +
                "radius=" + r +
                ", perimeter=" + getPerimeter() +
                ", area=" + getArea() +
                '}';
    }
}
