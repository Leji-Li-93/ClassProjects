package shapes;

public interface MyShapePosition extends MyPoint {

    public MyRectangle getMyBoundingBox();
    public boolean doOverlap(MyShapePosition other);
}
