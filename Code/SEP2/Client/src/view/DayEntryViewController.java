package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class DayEntryViewController
{
  @FXML
  private Label dateLabel;

  public DayEntryViewController(){

  }

  public void setDate(LocalDate date) {
    dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    if (date.equals(LocalDate.now())) {
      dateLabel.getStyleClass().add("current-day");
    }
  }
}
