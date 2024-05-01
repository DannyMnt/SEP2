package view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.AddEventViewModel;

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
    }

    public void reset(){

    }

    public Region getRoot() {
        return root;
    }

    public void addEventBtn() {
        addEventViewModel.addEvent();
    }
}
