package shapes;

public interface MyPoint {

    /**
     * return the center of this shape
     * @return the 0 is x, 1 is y ...
     */
    public double[] getPoint();

    /**
     *
     * @param point a double array, whose 0 is x, 1 is y...
     */
    public void setPoint(double[] point);

    /**
     * move a point(x, y) to (x + deltaX, y + deltaY)
     * @param deltaX the distance of movement in x-axis
     * @param deltaY the distance of movement in y-axis
     */
    public void moveTo(double deltaX, double deltaY);

    /**
     *
     * @param otherPoint a double array, whose 0 is x, 1 is y ...
     * @return the distance between the hosting point and the given point
     */
    public double distanceTo(MyPoint otherPoint);
}
