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
    @FXML private TextField eventTitle;
    @FXML private TextArea eventDescription;
    @FXML private TextField locationTextField;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextField startTime;
    @FXML private TextField endTime;
    @FXML private Label errorLabel;
    @FXML private TextField participantsTextField;
    @FXML private VBox listView;
    @FXML private AnchorPane anchorPane;
    @FXML private AnchorPane attendeesAnchorPane;
    @FXML private VBox attendeesVBox;
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
        startTime.textProperty().bindBidirectional(addEventViewModel.getStartTimeProperty());
        endTime.textProperty().bindBidirectional(addEventViewModel.getEndTimeProperty());
        addEventViewModel.addListener();
//        addEventViewModel.setListView(listView, anchorPane);

        eventTitle.setText("testing");
        eventDescription.setText("testing");
        locationTextField.setText("testing");
        startTime.setText("10:00");
        endTime.setText("11:00");
        addEventViewModel.setListView(listView, anchorPane, attendeesAnchorPane, attendeesVBox);
    }

    public void reset(){
        eventTitle.setText(null);
        eventDescription.setText(null);
        startTime.setText(null);
        endTime.setText(null);
        locationTextField.setText(null);
        addEventViewModel.reset();
    }

    public Region getRoot() {
        return root;
    }

    public void addEventBtn() throws RemoteException {
        addEventViewModel.addEvent();
        viewHandler.openView("calendar");
        reset();
    }

    public void cancelBtn(){
        viewHandler.openView("calendar");
        reset();
    }
}
