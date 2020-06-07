package com.demo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

public class Main extends Application {

    private Map<Character, Double> frequency = null;
    private BorderPane borderPane;
    private Pane center;
    private TextField input;
    private double pieWidth = 500;
    private double pieHeight = 400;
    private int pieN = 3;

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Character Frequency");

        String filePath = "Alice in Wonderland.txt";
        // instantiate a FileChooser object to pick up other file
        FileChooser fileChooser = new FileChooser();
        // read txt file only
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        this.borderPane = new BorderPane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        this.center = new Pane();

        center.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pieWidth = newValue.doubleValue();
                updateCenter(pieWidth, pieHeight, pieN);
            }
        });

        center.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pieHeight = newValue.doubleValue();
                updateCenter(pieWidth, pieHeight, pieN);
            }
        });

        Label pathLabel = new Label(filePath);
        Button newFile = new Button("New File");
        top.getChildren().addAll(pathLabel, newFile);
        top.setAlignment(Pos.CENTER);
        top.setPrefWidth(500);
        top.setSpacing(10);
        top.setPadding(new Insets(10));

        newFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = fileChooser.showOpenDialog(primaryStage);
                if(null == selectedFile)
                    return;
                pathLabel.setText(selectedFile.getAbsolutePath());
                frequency = HistogramAlphaBet.sortMap(
                        HistogramAlphaBet.getFrequency(HistogramAlphaBet.readText(selectedFile))
                );
                updateCenter(pieWidth, pieHeight, pieN);
            }
        });

        Button submit = new Button("Draw");
        Label display = new Label("Show n most frequent letter: ");
        this.input = new TextField();
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pieN = getInputValue();
                updateCenter(pieWidth, pieHeight, pieN);
            }
        });
        bottom.getChildren().addAll(display, input, submit);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPrefWidth(500);
        bottom.setSpacing(10);
        bottom.setPadding(new Insets(10));

        frequency = HistogramAlphaBet.sortMap(
                HistogramAlphaBet.getFrequency(HistogramAlphaBet.readText(new File(filePath)))
        );

        updateCenter(pieWidth, pieHeight, pieN);

        borderPane.setTop(top);
        borderPane.setCenter(center);
        borderPane.setBottom(bottom);

        primaryStage.setScene(new Scene(borderPane));
        primaryStage.show();
    }

    /**
     * get the input value from the textfield
     * @return return the exact value if the input is valid,
     * if input less that 0 return 3, if input greater 26 return 26
     */
    private int getInputValue(){
        String s = this.input.getText();
        int n = 0;
        try{
            n = Integer.parseInt(s);
        }catch (Exception e){
            n = 3;
        }
        if(n < 0)
            n = 3;
        else if(n > 26)
            n = 26;
        input.setText(String.valueOf(n));
        return n;
    }

    /**
     * update the central view of the borderpane
     * @param width new width of the center pie chart
     * @param height new height of the center pie chart
     * @param n new n of the center pie chart
     */
    private void updateCenter(double width, double height, int n){
        if(null == center){
            center = new Pane();
        }
        center.getChildren().clear();
        center.getChildren().add(HistogramAlphaBet.getPieChartCanvas(frequency, width, height, n));
    }
}
