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
import shapes.*;

import java.util.ArrayList;

public class Main extends Application implements EventHandler<ActionEvent> {

    private static double MIN_WIDTH = 200d;
    private static double MIN_HEIGHT = 140d;
    private TextField wTextField, hTextField, ratioTextField;
    private double currentWidth, currentHeight, ratio;
    private Scene popScene;
    private Pane popHolder;
    private Canvas drawCanvas;

    public static void main(String[] args) {
        // write your code here
        launch(args);

        MyOval e1 = new MyOval(20, 10, 50, 50, MyColor.LightPink);
        MyOval e2 = new MyOval(20, 10, 20, 10, MyColor.LightPink);
        MyRectangle r1 = new MyRectangle(20, 10, 40, 50, MyColor.LightPink);
        System.out.println("e1 is " + e1.toString());
        System.out.println("e2 is " + e2.toString());
        System.out.println("r1 is " + r1.toString());
        System.out.println("Does e1 overlap e2? : " + e1.doOverlap(e2));
        System.out.println("Dose e2 overlap r1? : " + e2.doOverlap(r1));
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
        Label description = new Label("Please input the width, height of the pop up window.");
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

        Label ratioLabel = new Label("a/b");
        ratioTextField = new TextField();
        root.add(ratioLabel, 0, 4);
        root.add(ratioTextField,1,4);

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
     * @return
     */
    public Canvas getShowCanvas(double w, double h){
        // keep the height and width properties
        // use for the resize
        currentHeight = h;
        currentWidth = w;

        double graphW = w, graphH = h;
        double boxRatio = w / h;
        if(ratio < boxRatio){
            graphW = ratio * h;
        } else if(ratio > boxRatio){
            graphH = w / ratio;
        }

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        (new MyRectangle(w, h, w/2, h/2, MyColor.Black)).draw(gc, false);
        ArrayList<MyShape> shapeList = new ArrayList<MyShape>(7);
        MyColor[] colors = {
                MyColor.Wheat,
                MyColor.OliveDrab,
                MyColor.FireBrick,
                MyColor.SkyBlue,
                MyColor.Yellow,
                MyColor.Plum
        };
        for (int i = 0; i < 3; i++) {
            MyRectangle rectangle = null;
            int length = shapeList.size();
            if(length == 0){
                rectangle = new MyRectangle(0.9*graphW, 0.9*graphH, w/2, h/2, MyColor.LightPink);
            }
            else{
                rectangle = ((MyOval)(shapeList.get(length - 1))).getInnerBox();
            }
            rectangle.setColor(colors[i*2]);
            shapeList.add(rectangle);
            shapeList.add(new MyOval(rectangle, colors[i*2+1]));
        }

        shapeList.add(new MyLine(0, 0, w, h, MyColor.Black));

        for(MyShape shape : shapeList){
            shape.draw(gc);
        }

        return canvas;
    }

    @Override
    public void handle(ActionEvent event) {
        double w, h;

        w = getValueFromInput(wTextField, MIN_WIDTH, MIN_WIDTH);
        h = getValueFromInput(hTextField, MIN_HEIGHT, MIN_HEIGHT);
        ratio = getValueFromInput(ratioTextField, 0, 2);

        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        popHolder = new Pane();
        drawCanvas = getShowCanvas(w, h);
        popHolder.getChildren().add(drawCanvas);
        popScene = new Scene(popHolder, w, h);

        // when the window size changes
        // resize the draw shapes
        popScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                popHolder.getChildren().clear();
                popHolder.getChildren().add(getShowCanvas(newValue.doubleValue(), currentHeight));
                wTextField.setText(String.valueOf(newValue.doubleValue()));
            }
        });

        popScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                popHolder.getChildren().clear();
                popHolder.getChildren().add(getShowCanvas(currentWidth, newValue.doubleValue()));
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