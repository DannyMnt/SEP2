package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import viewmodel.AddEventViewModel;

import java.rmi.RemoteException;
import java.time.LocalDate;

public class AddEventViewController {
    private Region root;
    private ViewHandler viewHandler;
    private AddEventViewModel addEventViewModel;
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventDescription;

    @FXML
    private TextField locationTextField;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Label errorLabel;
    @FXML private TextField participantsTextField;
    @FXML private VBox listView;
    @FXML private AnchorPane anchorPane;
    public AddEventViewController(){

    }
    public void init(ViewHandler viewHandler, AddEventViewModel addEventViewModel, Region root){
        this.viewHandler = viewHandler;
        this.addEventViewModel = addEventViewModel;
        this.root = root;

        eventTitle.textProperty().bindBidirectional(addEventViewModel.getEventTitleProperty());
        eventDescription.textProperty().bindBidirectional(addEventViewModel.getEventDescriptionProperty());
        locationTextField.textProperty().bindBidirectional(addEventViewModel.getLocationProperty());
        startDate.valueProperty().bindBidirectional(addEventViewModel.getStartDate().valueProperty());
        endDate.valueProperty().bindBidirectional(addEventViewModel.getEndDate().valueProperty());
        errorLabel.textProperty().bindBidirectional(addEventViewModel.getErrorLabelProperty());
        eventTitle.setPromptText("Title");
        eventDescription.setPromptText("Description");
        locationTextField.setPromptText("Location");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(1));
        participantsTextField.textProperty().bindBidirectional(addEventViewModel.getParticipantsTextFieldProperty());
        addEventViewModel.addListener();
        addEventViewModel.setListView(listView, anchorPane);
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
