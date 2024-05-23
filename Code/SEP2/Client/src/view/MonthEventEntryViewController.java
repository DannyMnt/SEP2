package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class MonthEventEntryViewController {

    @FXML private Label eventTitleLabel;
    @FXML private Label eventTimeLabel;


    public void setEventTitleLabel(String title){
        eventTitleLabel.setText(title);
    }


    public void setEventTimeLabel(String time){
        eventTimeLabel.setText(time);
    }

}

