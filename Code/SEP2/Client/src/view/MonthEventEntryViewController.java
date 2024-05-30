package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for the MonthEventEntryView.
 * This controller is responsible for managing the UI elements of a single event entry in the month view.
 */
public class MonthEventEntryViewController {

    @FXML private Label eventTitleLabel;
    @FXML private Label eventTimeLabel;

    /**
     * Sets the text of the event title label.
     * @param title the title of the event
     */
    public void setEventTitleLabel(String title){
        eventTitleLabel.setText(title);
    }


    /**
     * Sets the text of the event time label.
     * @param time the time of the event
     */
    public void setEventTimeLabel(String time){
        eventTimeLabel.setText(time);
    }

}

