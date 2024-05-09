package view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.AddEventViewModel;

import java.rmi.RemoteException;

public class AddEventViewController {
    private Region root;
    private ViewHandler viewHandler;
    private AddEventViewModel addEventViewModel;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventDescription;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Label errorLabel;
    public AddEventViewController(){

    }
    public void init(ViewHandler viewHandler, AddEventViewModel addEventViewModel, Region root){
        this.viewHandler = viewHandler;
        this.addEventViewModel = addEventViewModel;
        this.root = root;

        eventTitle.textProperty().bindBidirectional(addEventViewModel.getEventTitleProperty());
        eventDescription.textProperty().bindBidirectional(addEventViewModel.getEventDescriptionProperty());
        startDate.valueProperty().bindBidirectional(addEventViewModel.getStartDate().valueProperty());
        endDate.valueProperty().bindBidirectional(addEventViewModel.getEndDate().valueProperty());
        errorLabel.textProperty().bindBidirectional(addEventViewModel.getErrorLabelProperty());
        eventTitle.setText("Title");
        eventDescription.setText("Description");
    }

    public void reset(){
    }

    public Region getRoot() {
        return root;
    }

    public void addEventBtn() throws RemoteException {
        addEventViewModel.addEvent();
    }
}