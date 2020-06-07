package shapes;

import javafx.scene.canvas.GraphicsContext;

public class MyPolygon extends MyShape {
    private int N;
    private double r;

    public MyPolygon(double x, double y, int N, double r){
        super(x, y);
        this.N = N;
        this.r = r;
    }

    public MyPolygon(double x, double y, int N, double r, MyColor color){
        this(x, y, N, r);
        setColor(color);
    }

    public double getArea(){
        return this.N * Math.pow(this.r, 2) * Math.tan(Math.toRadians(Math.PI / this.N));
    }

    public double getPerimeter(){
        return this.getSide() * this.N;
    }

    public double getAngle(){
        return (180d * this.N - 360d) / this.N;
    }

    public double getSide(){
        return 2d * this.r * Math.tan(Math.toRadians(Math.PI / this.N));
    }

    public static double getInradiusByCircumradius(double R, int N){
        return R * Math.cos(Math.toRadians(180d / N));
    }

    public static double getCircumradiusByInradius(double r, int N){
        return r / Math.cos(Math.toRadians(180d / N));
    }

    @Override
    public String toString() {
        return "MyPolygon{" +
                "side=" + getSide() +
                ", angle=" + getAngle() +
                ", perimeter=" + getPerimeter() +
                ", area=" + getArea() +
                '}';
    }

    @Override
    public void draw(GraphicsContext gc){
        draw(gc, false);
    }

    public void draw(GraphicsContext gc, boolean isFill) {
        double R = getCircumradiusByInradius(this.r, this.N);
        double innerAngle = 360d / this.N;
        double Xs[] = new double[this.N];
        double Ys[] = new double[this.N];

        for (int i = 0; i < this.N; i++) {
            Ys[i] = getY() + R * Math.sin(Math.toRadians(innerAngle * i - 90));
            Xs[i] = getX() + R * Math.cos(Math.toRadians(innerAngle * i - 90));
        }

        if(isFill){
            gc.setFill(getColor().toFXPaintColor());
            gc.fillPolygon(Xs, Ys, this.N);
        } else {
            gc.setStroke(getColor().toFXPaintColor());
            gc.strokePolygon(Xs, Ys, this.N);
        }
    }

    @Override
    public MyRectangle getMyBoundingBox() {
        double R = getCircumradiusByInradius(this.r, this.N);
        return new MyRectangle(R, R, this.getX(), this.getY(), this.getColor());
    }
}
