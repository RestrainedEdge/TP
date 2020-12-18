package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    class ResizableCanvas extends Canvas {
        public ResizableCanvas() {
            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
        }
        private void draw() {
            double width = getWidth();
            double height = getHeight();

            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, width, height);

            gc.setStroke(Color.RED);
            gc.setFill(Color.rgb(60, 63, 65));
            gc.fillRect(0,0, width, height);

            gc.strokeLine(0, 0, width, height);

            gc.strokeLine(0, height, width, 0);
        }
        @Override
        public boolean isResizable() {
            return true;
        }
        @Override
        public double prefWidth(double height) {
            return getWidth();
        }
        @Override
        public double prefHeight(double width) {
            return getHeight();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Project");
        BorderPane root = new BorderPane();
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");
        HBox topSide = new HBox();
        topSide.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(49, 51, 53),
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
        topSide.getChildren().addAll(button1, button2);
        VBox rightSide = new VBox();
        Button button3 = new Button("Button 3");
        Button button4 = new Button("Button 4");
        Button button5 = new Button("Button 5");
        Button button6 = new Button("Button 6");
        Label label1 = new Label("Label 1");
        button1.setOnAction(e -> {
            label1.setText("Button 1 was pressed");
        });
        rightSide.setAlignment(Pos.TOP_CENTER);
        rightSide.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(49, 51, 53),
                                CornerRadii.EMPTY,
                                Insets.EMPTY)));
        rightSide.getChildren().addAll(button3, button4, button5, button6, label1);
        ResizableCanvas canvas = new ResizableCanvas();
        Pane stackPane = new Pane();
        stackPane.getChildren().add(canvas);
        canvas.widthProperty().bind(
                stackPane.widthProperty());
        canvas.heightProperty().bind(
                stackPane.heightProperty());
        root.setTop(topSide);
        root.setRight(rightSide);
        root.setCenter(stackPane);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


}
