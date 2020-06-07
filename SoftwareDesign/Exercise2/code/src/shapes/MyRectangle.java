package shapes;

import javafx.scene.canvas.GraphicsContext;

public class MyRectangle extends MyShape {
    double h, w;

    /**
     *
     * @param height
     * @param width
     * @param x
     * @param y
     * @param color
     */
    public MyRectangle(double width, double height, double x, double y, MyColor color){
        super(x, y, color);
        this.h = height;
        this.w = width;
    }

    public MyRectangle(double height, double width, MyColor color){
        this(height, width, 0, 0, color);
    }

    public double getHeight() {
        return h;
    }

    public void setHeight(double h) {
        this.h = h;
    }

    public double getWidth() {
        return w;
    }

    public void setWidth(double w) {
        this.w = w;
    }

    public double getPerimeter() {
        return 2 * (this.h + this.w);
    }

    public void setPerimeter(double perimeter) {
        double perimeterOrigin = getPerimeter();
        double ratio = perimeter / perimeterOrigin;
        this.h *= ratio;
        this.w *= ratio;
    }

    public double getArea() {
        return this.h * this.w;
    }

    public void setArea(double area) {
        double areaOrigin = getArea();
        double ratio = Math.sqrt(area/ areaOrigin);
        this.h *= ratio;
        this.w *= ratio;
    }

    public String toString(){
        return "MyRectangle{" +
                "width=" + this.w +
                ", height=" + this.h +
                ", perimeter=" + getPerimeter() +
                ", area=" + getArea() +
                "}";
    }

    @Override
    public void draw(GraphicsContext gc) {
        draw(gc, true);
    }

    public void draw(GraphicsContext gc, boolean isFill){
        if(isFill){
            gc.setFill(this.getColor().toFXPaintColor());
            gc.fillRect(getX() - this.w/2, getY() - this.h/2, this.w, this.h);
        } else {
            gc.setStroke(this.getColor().toFXPaintColor());
            gc.strokeRect(getX() - this.w/2, getY() - this.h/2, this.w, this.h);
        }
    }

    @Override
    public MyRectangle getMyBoundingBox() {
        return this;
    }
}
