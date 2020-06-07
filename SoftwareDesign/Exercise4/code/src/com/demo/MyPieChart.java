package com.demo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyPieChart extends MyShape {
    private Map<Character, Integer> chartData;
    private double r;
    private static List<Color> colorList = new ArrayList<Color>();

    public MyPieChart(double x, double y, double r, int n, Map<Character, Integer> chartData){
        super(x, y);
        this.r = r;
        setData(chartData);
        for (int i = 0; i < 6; i++) {
            colorList.add(MyColor.randomColor());
        }
    }

    public MyPieChart(double x, double y, double r){
        this(x, y, r, 3, null);
    }

    public void setData(Map<Character, Integer> data){
        this.chartData = data;
    }

    /**
     * this function is to find how wide a string should occupies in a certain font
     * @param font some kind of font
     * @param text the target string
     * @return the width and height of the text will takes under the given font
     * [0] is the width
     * [1] is the height
     */
    private double[] getTextWidth(Font font, String text){
        Text helper = new Text(text);
        helper.setFont(font);
        helper.setWrappingWidth(0);
        helper.setLineSpacing(0);
        // prefWidth pass-in -1 because node has null content-bias
        double w = helper.prefWidth(-1);
        helper.setWrappingWidth((int)Math.ceil(w));
        return new double[]{
                Math.ceil(helper.getLayoutBounds().getWidth()),
                Math.ceil(helper.getLayoutBounds().getHeight())
        };
    }

    @Override
    public void draw(GraphicsContext gc) {
        if(chartData == null || chartData.size() == 0){
            String hint = "No data to display";
            Font font = new Font(16);
            double[] size = getTextWidth(font, hint);
            gc.setFont(font);
            gc.setFill(MyColor.Black.toFXPaintColor());
            gc.fillText(hint, getX() - size[0]/2, getY() - size[1]/2);
            return;
        }
        Iterator iterator;
        int count = 0;
        int sum = 0;
        iterator = chartData.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Character, Integer> entry = (Map.Entry<Character, Integer>)iterator.next();
            sum += entry.getValue();
        }
        // the sector is a part of a circle
        // fillArc works like the fillOval
        // it takes the top left corner of the bounding box
        // arc width and arc height are the radius of the circle
        // startAngle is the angle between the x axis and the right side of the sector, counterclockwise
        // arcExtent is the angle of the sector
        double startingX = this.getX() - this.r; // x of the top left corner
        double startingY = this.getY() - this.r; // y of the top left corner
        double sectorAngle = 0d; // arcExtent
        double angleShift = 90d; // startAngle
        double midAngle = 0d; // the angle of middle of a sector, use to locate the label
        double labelX = 0d, labelY = 0d; // the starting point of a label
        Font font = new Font(12); // use to unify the font of the label and find the length
        iterator = chartData.entrySet().iterator(); // draw the sectors
        while(iterator.hasNext()){
            Map.Entry<Character, Integer> entry = (Map.Entry<Character, Integer>)iterator.next();
            // all angles passed in to the fillArc method should be in degree
            sectorAngle = 360d * (double)entry.getValue() / (double)sum;
            gc.setFill(colorList.get(count));
            gc.fillArc(startingX, startingY, this.r * 2, this.r * 2, angleShift, sectorAngle, ArcType.ROUND);

            // prepare for the label of a sector
            String labelText = entry.getKey() + (": " + entry.getValue());

            // find the location of the label of a sector
            // it should be placed to the middle of its sector
            // r*1.1 makes the label has the padding to the piechart
            midAngle = (angleShift + sectorAngle / 2);
            double xShift = this.r * 1.1 * Math.cos(Math.toRadians(midAngle));
            labelX = this.getX() + xShift;
            labelY = this.getY() - this.r *1.1 * Math.sin(Math.toRadians(midAngle));

            // handle the spacing
            // since the width of the label is not constant
            // we need to find out the label width
            // and to avoid the label overlapping with the piechart or going out of the window
            // we should take the minimum of them, and set this value as the maximum of the label
            double labelWidth = getTextWidth(font, labelText)[0];
            if(xShift < 0){
                // if the label is in the left side of the piechart
                // the starting point should be the middle point of the sector arc minus the label width
                // r * 0.006 is the margin of the window
                labelWidth = Math.min(labelWidth, labelX - this.r * 0.06);
                labelX -= labelWidth;
            } else {
                labelWidth = Math.min(labelWidth, (2*getX() - labelX) - this.r * 0.06);
            }

            gc.setFill(MyColor.Black.toFXPaintColor());
            gc.setFont(font);
            gc.fillText(labelText, labelX, labelY, labelWidth);

            // accumulate the starting angle for the next sector
            angleShift += sectorAngle;
            count++;
        }
    }
}
