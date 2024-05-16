package view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;

public class DayEntryViewController
{
  @FXML
  private Label dateLabel;

  @FXML
  VBox eventContainer;

  public DayEntryViewController(){

  }

  public void setDate(LocalDate date) {
    dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    if (date.equals(LocalDate.now())) {
      dateLabel.getStyleClass().add("current-day");
    }

    loadEventEntries();
  }

  public void loadEventEntries() {
    String[] styles = {"classic", "left", "right", "full"};

    for (String style : styles) {
      Pane eventPane = createEventPane(style);
      eventContainer.getChildren().add(eventPane);
    }
  }

  private Pane createEventPane(String style) {
    Pane pane = new Pane();
    pane.setPrefSize(100, 50);
    pane.getStyleClass().add("event-pane");
    pane.getStyleClass().add(style);
//    pane.setStyle("-fx-background-radius: 5 5 5 5; -fx-padding: 3 5 3 5;");
    Label label = new Label(style);
    label.setFont(new Font("Arial", 14));
    label.setTextFill(Color.WHITE);
    label.setLayoutX(10);
    label.setLayoutY(15);

    pane.getChildren().add(label);

    return pane;
  }
}
