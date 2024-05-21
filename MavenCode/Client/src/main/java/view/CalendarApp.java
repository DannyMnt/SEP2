package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CalendarApp extends Application {

    private static final int NUM_DAYS = 7;
    private static final int NUM_WEEKS = 5;

    @Override
    public void start(Stage primaryStage) {
        GridPane calendarGrid = new GridPane();

        // Create panes for each day in the calendar
        for (int week = 0; week < NUM_WEEKS; week++) {
            for (int day = 0; day < NUM_DAYS; day++) {
                Pane dayPane = createDayPane();
                calendarGrid.add(dayPane, day, week);
            }
        }

        System.out.println("test");

        Scene scene = new Scene(calendarGrid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Calendar");
        primaryStage.show();
    }

    // Method to create a pane for each day
    private Pane createDayPane() {
        Pane pane = new Pane();
        pane.setPrefSize(100, 100); // Adjust size as needed

        Label dayLabel = new Label("1"); // Default day number
        dayLabel.setTextAlignment(TextAlignment.CENTER);
        dayLabel.setLayoutX(10); // Adjust position as needed
        dayLabel.setLayoutY(10);

        // Add more elements to the pane as needed, such as calendar entries

        pane.getChildren().add(dayLabel);
        return pane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}