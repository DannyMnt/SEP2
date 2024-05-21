package view;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;

public class MonthDayEntryViewController
{
  @FXML
  private Label dateLabel;

  @FXML VBox eventContainer;

  public MonthDayEntryViewController(){

  }

  public void setDate(LocalDate date) {
    dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    if (date.equals(LocalDate.now())) {
      dateLabel.getStyleClass().add("current-day");
    }


  }

  public void loadEventEntries() {
    String[] styles = {"classic", "left", "right", "full"};

    for (String style : styles) {
      Pane eventPane = createEventPane(style,"bozo");
      eventContainer.getChildren().add(eventPane);
    }
  }
  public Pane createEmptyPane () {
    Pane emptyPane = new Pane();
        emptyPane.setPrefHeight(25);
        return emptyPane;
  }

  public Pane createEventPane(String style,String  title) {
    Pane pane = new Pane();
    pane.setPrefSize(100, 25);
    pane.getStyleClass().add("event-pane");
    pane.getStyleClass().add(style);

    pane.setOnMouseEntered(event -> pane.setCursor(Cursor.HAND));

    // Reset the cursor when the mouse exits the Pane
    pane.setOnMouseExited(event -> pane.setCursor(Cursor.DEFAULT));
//    pane.setStyle("-fx-background-radius: 5 5 5 5; -fx-padding: 3 5 3 5;");

    Label label = new Label(style);
    label.setText(title);
    label.setFont(new Font("Arial", 10));
    label.setTextFill(Color.BLACK);
    label.setLayoutX(10);
    label.setLayoutY(5);

    pane.getChildren().add(label);

    return pane;
  }

  public void addEventToContainer(Pane eventPane) {
    eventContainer.getChildren().add(eventPane);
  }

}
