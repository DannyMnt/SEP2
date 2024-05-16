package view;

import javafx.fxml.FXML;
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

  @FXML GridPane eventContainer;

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

  public Pane createEventPane(String style,String  title) {
    Pane pane = new Pane();
    pane.setPrefSize(100, 50);
    pane.getStyleClass().add("event-pane");
    pane.getStyleClass().add(style);
//    pane.setStyle("-fx-background-radius: 5 5 5 5; -fx-padding: 3 5 3 5;");

    Label label = new Label(style);
    label.setText(title);
    label.setFont(new Font("Arial", 14));
    label.setTextFill(Color.WHITE);
    label.setLayoutX(10);
    label.setLayoutY(5);

    pane.getChildren().add(label);

    return pane;
  }

  public void addEventToContainer(Pane eventPane) {
    eventContainer.getChildren().add(eventPane);
  }

}
