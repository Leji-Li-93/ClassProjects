package shapes;

import javafx.scene.canvas.GraphicsContext;

public class MyLine extends MyShape {
    private double x1, y1;
    private double x2, y2;

    public MyLine(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public MyLine(double x1, double y1, double x2, double y2, MyColor color){
        this(x1, y1, x2, y2);
        setColor(color);
    }

    public double getLength(){
        return Math.sqrt(Math.pow(this.x2 - this.x1, 2) + Math.pow(this.y2 - this.y1,2));
    }

    public double get_xAngle(){
        return Math.toDegrees(Math.atan2((this.y2 - this.y1), (this.x2 - this.x1)));
    }

    @Override
    public String toString(){
        return "MyLine{length=" + getLength()+ ", _xAngle=" + get_xAngle() + "}";
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor().toFXPaintColor());
        gc.strokeLine(this.x1, this.y1, this.x2, this.y2);
    }

    /**
     * the 0 and 1 is the first point, 2 and 3 is the second one
     * @return a double array holds two points
     */
    @Override
    public double[] getPoint() {
        return new double[]{this.x1, this.y1, this.x2, this.y2};
    }

    @Override
    public MyRectangle getMyBoundingBox() {
        return new MyRectangle(Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2), (this.x1 + this.x2)/2, (this.y1 + this.y2)/2, this.getColor());
    }

    @Override
    public void setPoint(double[] point) {
        this.x1 = point[0];
        this.y1 = point[1];
        this.x2 = point[2];
        this.y2 = point[3];
    }

    @Override
    public void moveTo(double deltaX, double deltaY) {
        this.x1 += deltaX;
        this.x2 += deltaX;
        this.y1 += deltaY;
        this.y2 += deltaY;
    }

}
