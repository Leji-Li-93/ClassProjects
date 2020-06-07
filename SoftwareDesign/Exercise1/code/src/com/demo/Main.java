package com.demo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shapes.MyCircle;
import shapes.MyColor;
import shapes.MyLine;
import shapes.MyPolygon;

public class Main extends Application implements EventHandler<ActionEvent> {

    private static double MIN_WIDTH = 200d;
    private static double MIN_HEIGHT = 140d;
    private static int DEFAULT_SIDE_NUM = 6;
    private TextField wTextField, hTextField, nTextField;
    private double currentWidth, currentHeight;
    private Scene popScene;
    private Pane popHolder;
    private Canvas drawCanvas;

    public static void main(String[] args) {
        // write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Use grid pane to layout the main stage
        // Use a pop up window to show the shape graphics
        // the main stage should be able to accept the width, height and the number of sides of the polygon from user
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(25,25,25,25));

        // use Text describe the view
        Text title = new Text("Draw Shapes");
        title.setFont(Font.font(20));
        root.add(title, 0, 0, 2, 1);
        // use Label for the description for better warping
        Label description = new Label("Please input the width, height of the pop up window, and the # of sides of the polygon.");
        description.setWrapText(true);
        root.add(description, 0, 1, 2,1);

        // use TextField to accept the user inputs
        // use Label to give hints to user
        Label wLabel = new Label("Width");
        wTextField = new TextField();
        root.add(wLabel, 0, 2);
        root.add(wTextField, 1, 2);

        Label hLabel = new Label("Height");
        hTextField = new TextField();
        root.add(hLabel, 0, 3);
        root.add(hTextField,1,3);

        Label nLabel = new Label("# of sides");
        nTextField = new TextField();
        root.add(nLabel, 0, 4);
        root.add(nTextField, 1, 4);

        // A button
        Button btn = new Button("Draw");
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().add(btn);
        root.add(btnBox, 0, 5, 2, 1);

        btn.setOnAction(this);

        Scene scene = new Scene(root, 320, 275);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Draw Shapes");
        primaryStage.show();
    }

    /**
     * This function will return a canvas that have the graph drawn
     * @param w the width of the canvas
     * @param h the height of the canvas
     * @param polygonNum the # of the sides of the polygon
     * @return
     */
    public Canvas getShowCanvas(double w, double h, int polygonNum){
        // keep the height and width properties
        // use for the resize
        currentHeight = h;
        currentWidth = w;

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        MyLine line1 = new MyLine(0,0, w, 0, MyColor.Black);
        MyLine line2 = new MyLine(0, 0, 0, h, MyColor.Black);
        MyLine line3 = new MyLine(0, h, w, h,  MyColor.Black);
        MyLine line4 = new MyLine(w, 0, w, h, MyColor.Black);
        MyLine diagonal1 = new MyLine(0, 0, w, h, MyColor.Black);
        MyLine diagonal2 = new MyLine(w, 0, 0, h, MyColor.Black);

        double midX = w / 2.0;
        double midY = h / 2.0;
        double outerInradius = MyPolygon.getInradiusByCircumradius(0.95*Math.min(w, h) / 2d, polygonNum);
        MyPolygon outerStrokeHexagon = new MyPolygon(midX, midY, polygonNum, outerInradius, MyColor.Black);
        MyPolygon outerFillHexagon = new MyPolygon(midX, midY, polygonNum, outerInradius, MyColor.Gray);
        MyCircle outerCircle = new MyCircle(midX, midY, outerInradius, MyColor.Yellow);

        double midInradius = MyPolygon.getInradiusByCircumradius(outerInradius, polygonNum);
        MyPolygon midHexagon = new MyPolygon(midX, midY, polygonNum, midInradius, MyColor.LightGreen);
        MyCircle midCircle = new MyCircle(midX, midY, midInradius, MyColor.LightPink);

        double innerInradius = MyPolygon.getInradiusByCircumradius(midInradius, polygonNum);
        MyPolygon innerHexagon = new MyPolygon(midX, midY, polygonNum, innerInradius, MyColor.SkyBlue);

        outerFillHexagon.draw(gc, true);
        outerStrokeHexagon.draw(gc);
        outerCircle.draw(gc, true);

        midHexagon.draw(gc, true);
        midCircle.draw(gc, true);

        innerHexagon.draw(gc, true);

        line1.draw(gc);
        line2.draw(gc);
        line3.draw(gc);
        line4.draw(gc);

        diagonal1.draw(gc);
        diagonal2.draw(gc);

        return canvas;
    }

    @Override
    public void handle(ActionEvent event) {
        double w, h;
        int n;

        w = getValueFromInput(wTextField, MIN_WIDTH, MIN_WIDTH);
        h = getValueFromInput(hTextField, MIN_HEIGHT, MIN_HEIGHT);
        n = (int)getValueFromInput(nTextField, 3, DEFAULT_SIDE_NUM);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        popHolder = new Pane();
        drawCanvas = getShowCanvas(w, h, n);
        popHolder.getChildren().add(drawCanvas);
        popScene = new Scene(popHolder, w, h);

        // when the window size changes
        // resize the draw shapes
        popScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                popHolder.getChildren().clear();
                popHolder.getChildren().add(getShowCanvas(newValue.doubleValue(), currentHeight, n));
                wTextField.setText(String.valueOf(newValue.doubleValue()));
            }
        });

        popScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                popHolder.getChildren().clear();
                popHolder.getChildren().add(getShowCanvas(currentWidth, newValue.doubleValue(), n));
                hTextField.setText(String.valueOf(newValue.doubleValue()));
            }
        });

        dialog.setScene(popScene);
        dialog.setTitle("Finish!");
        dialog.show();
    }

    /**
     * this function
     * @param input the input TextField object
     * @param condition the smallest value of a correct input should be
     * @param defaultValue the default value.
     * @return the value of text in the TextField object if user has the correct input
     * default value if the input cannot be recognized the input
     * or the input value is less than the condition value
     */
    private double getValueFromInput(TextField input, double condition, double defaultValue){
        double result;
        try {
            result = Double.parseDouble(input.getText());
            if( result < condition){
                result = defaultValue;
            }
            input.setText(String.valueOf(result));
        } catch (Exception e){
            result = defaultValue;
            input.setText(String.valueOf(result));
        }
        return result;
    }
}