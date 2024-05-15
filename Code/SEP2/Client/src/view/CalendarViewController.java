package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.CalendarViewModel;
import viewmodel.RegisterUserViewModel;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CalendarViewController
{
  private Region root;
  private ViewHandler viewHandler;

  private CalendarViewModel calendarViewModel;

  @FXML GridPane gridPane;



  public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root){
    this.viewHandler = viewHandler;
    this.calendarViewModel = calendarViewModel;
    this.root = root;

//    for (int row = 0; row < gridPane.getRowCount(); row++) {
//      for (int col = 0; col < gridPane.getColumnCount(); col++) {
//        try {
//          // Load the sub-FXML file
//          FXMLLoader loader = new FXMLLoader(getClass().getResource("dayEntryView.fxml"));
//          Pane cellContent = loader.load();
//
//          // Add the loaded content to the corresponding cell in the GridPane
//          gridPane.add(cellContent, col, row);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }

  }

  public Region getRoot() {
    return root;
  }

  public void reset(){

  }


}
