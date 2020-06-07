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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.Map;

public class Main extends Application {
    private DBConnector connector = null;
    private Map<Character, Integer> chartData = null;
    private double pieWidth = 500;
    private double pieHeight = 400;
    private Pane center = null;
    private VBox right = null;
    private Label sideMessage = null;

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        connector = new DBConnector("root", "321478965");
        RandomDataGenerator generator = new RandomDataGenerator(connector);

        if(connector.getClassCount() == 0){
            generator.initializeData();
        }
        chartData = connector.getClassesByCourse("CSC22100", 2020, Semester.Spring);

        primaryStage.setTitle("GPA Chart");
        BorderPane borderPane = new BorderPane();
        center = new Pane();

        center.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pieWidth = newValue.doubleValue();
                updateCenter(pieWidth, pieHeight);
            }
        });

        center.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pieHeight = newValue.doubleValue();
                updateCenter(pieWidth, pieHeight);
            }
        });

        Label topHint = new Label("Click the button \n to add more random grade ");
        Button click = new Button("Click");
        // add more students into this class
        click.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generator.classSampleData("CSC22100", 42264, 2020, Semester.Spring, 20);
                chartData = connector.getClassesByCourse("CSC22100", 2020, Semester.Spring);
                updateCenter(pieWidth, pieHeight);
                updateSideMessage();
            }
        });

        right = new VBox();
        sideMessage = new Label();
        right.getChildren().addAll(topHint, click, sideMessage);
        right.setAlignment(Pos.CENTER_LEFT);
        right.setSpacing(10);
        right.setPadding(new Insets(10));

        updateSideMessage();
        updateCenter(pieWidth, pieHeight);
        borderPane.setCenter(center);
        borderPane.setRight(right);

        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
    }

    /**
     * when close the application, shutdown the database connection
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        if(null != connector){
            connector.close();
        }
    }

    /**
     * update the central view of the borderpane
     * @param width new width of the center pie chart
     * @param height new height of the center pie chart
     */
    private void updateCenter(double width, double height){
        Canvas canvas = new Canvas(width, height);
        double r = 0.6 * Math.min(width, height) / 2;
        MyPieChart chart = new MyPieChart(width/2, height/2, r);
        chart.setData(chartData);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        chart.draw(gc);
        if(null == center){
            center = new Pane();
        }
        center.getChildren().clear();
        center.getChildren().add(canvas);
    }

    /**
     * display the number of students enrolled in this class for each letter grade.
     */
    private void updateSideMessage(){
        int[] counts = new int[6];
        Iterator iterator = chartData.entrySet().iterator();
        int sum = 0;
        while (iterator.hasNext()){
            Map.Entry<Character,Integer> entry = (Map.Entry<Character, Integer>)iterator.next();
            if(entry.getKey() == 'F'){
                counts[4] = entry.getValue();
            } else if(entry.getKey() == 'W'){
                counts[5] = entry.getValue();
            } else{
                counts[entry.getKey() - 'A'] = entry.getValue();
            }
            sum += entry.getValue();
        }
        if(null == sideMessage){
            sideMessage = new Label();
        }
        String s = "Total Students: " + sum + "\n";
        for(int i = 0; i < 4; i++){
            s += (char)(i + 'A') + ": " + counts[i] + "\n";
        }
        s += "F: " + counts[4] + "\n";
        s += "W: " + counts[5];
        sideMessage.setText(s);
    }
}
