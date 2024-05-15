package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;

public class CalendarViewController
{
  private Region root;
  private ViewHandler viewHandler;

  @FXML GridPane gridPane;

  public void init(){
    for (int row = 0; row < gridPane.getRowCount(); row++) {
      for (int col = 0; col < gridPane.getColumnCount(); col++) {
        try {
          // Load the sub-FXML file
          FXMLLoader loader = new FXMLLoader(getClass().getResource("dayEntryView.fxml"));
          Pane cellContent = loader.load();

          // Add the loaded content to the corresponding cell in the GridPane
          gridPane.add(cellContent, col, row);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
