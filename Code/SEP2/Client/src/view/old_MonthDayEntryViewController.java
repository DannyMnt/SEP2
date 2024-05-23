package view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;

public class old_MonthDayEntryViewController
{
  @FXML
  private Label dateLabel;

  @FXML VBox eventContainer;

  public old_MonthDayEntryViewController(){

  }

  public void setDate(LocalDate date) {
    dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    if (date.equals(LocalDate.now())) {
      dateLabel.getStyleClass().add("current-day");
    }


  }

//  public void loadEventEntries() {
//    String[] styles = {"classic", "left", "right", "full"};
//
//    for (String style : styles) {
//      Pane eventPane = createEventPane(style,"bozo");
//      eventContainer.getChildren().add(eventPane);
//    }
//  }
  public Pane createEmptyPane () {
    Pane emptyPane = new Pane();
        emptyPane.setPrefHeight(25);
        return emptyPane;
  }

  public HBox createEventPane(String style, String title, String time) {
//    Pane pane = new Pane();
//    pane.setPrefSize(100, 25);
//    pane.getStyleClass().add("event-pane");
//    pane.getStyleClass().add(style);


    // Create an HBox to hold the labels
    HBox hbox = new HBox();
    hbox.setPrefSize(100, 25);
//    hbox.setSpacing(5);
    hbox.getStyleClass().add("event-pane");
    hbox.getStyleClass().add(style);

    hbox.setOnMouseEntered(event -> hbox.setCursor(Cursor.HAND));

    // Reset the cursor when the mouse exits the Pane
    hbox.setOnMouseExited(event -> hbox.setCursor(Cursor.DEFAULT));


    // Create the first label with the title
    Label titleLabel = new Label(title);
    titleLabel.setFont(new Font("Arial", 10));
    titleLabel.setTextFill(Color.BLACK);
    titleLabel.setMaxWidth(Double.MAX_VALUE);
    titleLabel.setMaxHeight(25);
    HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

    // Create the second label with the fixed value "00:00"
    Label timeLabel = new Label(time);
    timeLabel.setFont(new Font("Arial", 10));
    timeLabel.setTextFill(Color.BLACK);
    timeLabel.setMinWidth(25);
    timeLabel.setMaxHeight(25);

    // Set margins for the labels
    HBox.setMargin(titleLabel, new Insets(0, 6, 0, 6)); // top, right, bottom, left
    HBox.setMargin(timeLabel, new Insets(0, 6, 0, 6)); // top, right, bottom, left


    // Add the labels to the HBox
    hbox.getChildren().addAll(titleLabel, timeLabel);


    return hbox;
  }

  public void addEventToContainer(Pane eventPane) {
    eventContainer.getChildren().add(eventPane);
  }

}
