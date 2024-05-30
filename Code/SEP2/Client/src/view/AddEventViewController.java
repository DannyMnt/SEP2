package view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utill.TimeFormatter;
import viewmodel.AddEventViewModel;

import javax.swing.text.DateFormatter;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller class for the Add Event view.
 */
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

    /**
     * Constructs an AddEventViewController.
     */
    public AddEventViewController(){

    }

    /**
     * Initializes the controller with the specified view handler, view model, and root region.
     * @param viewHandler the view handler
     * @param addEventViewModel the view model
     * @param root the root region
     */
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
        startTime.setText(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now()));
        endTime.setText(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now().plusHours(1)));
        participantsTextField.textProperty().bindBidirectional(addEventViewModel.getParticipantsTextFieldProperty());
        startTime.textProperty().bindBidirectional(addEventViewModel.getStartTimeProperty());
        endTime.textProperty().bindBidirectional(addEventViewModel.getEndTimeProperty());
        addEventViewModel.addListener();


        addEventViewModel.setListView(listView, anchorPane, attendeesAnchorPane, attendeesVBox);
    }

    /**
     * Resets the view model.
     */
    public void reset(){
        addEventViewModel.reset();
    }

    /**
     * Gets the root region of the view.
     * @return the root region
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Handles the add event button action.
     * @throws RemoteException if a remote exception occurs
     */
    public void addEventBtn() throws RemoteException {
        if(addEventViewModel.addEvent()){

        viewHandler.openView("calendar");
        reset();
        }
    }

    /**
     * Handles the cancel button action.
     */
    public void cancelBtn(){
        viewHandler.openView("calendar");
        reset();
    }
}
