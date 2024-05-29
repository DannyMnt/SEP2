package view;

import com.dlsc.phonenumberfx.PhoneNumberField;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import model.Country;
import model.Event;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.ProfileOverviewViewModel;
import viewmodel.ViewState;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ProfileOverviewController {
    private Region root;
    private ViewHandler viewHandler;
    private ProfileOverviewViewModel profileOverviewViewModel;
    @FXML private TextField emailTextField;
    @FXML private Label nameLabel;
    @FXML private TextField dateOfBirthTextField;
    @FXML private TextField genderLabel;
    @FXML private Label errorUserEdit;
    @FXML private Button editBtn;
    @FXML private PasswordField oldPasswordTextField;
    @FXML private PasswordField newPasswordTextField;
    @FXML private PasswordField checkPasswordTextField;
    @FXML private Label errorPassword;


    @FXML private ImageView profilePictureView;
    @FXML private ImageView smallProfilePictureView;

    private PhoneNumberField phoneNumberField;

    @FXML private HBox phoneHBox;

    @FXML private VBox upcomingEventVBox;
    @FXML private Label upcomingEventLabel;


    public ProfileOverviewController() {

    }

    public void init(ViewHandler viewHandler, ProfileOverviewViewModel profileOverviewViewModel, Region root) throws IOException, ParseException {
        profileOverviewViewModel.updateProfile();

        this.viewHandler = viewHandler;
        this.profileOverviewViewModel = profileOverviewViewModel;
        this.root = root;
        emailTextField.setDisable(true);
        phoneNumberField = new PhoneNumberField();
        phoneHBox.getChildren().add(phoneNumberField);
        phoneNumberField.setDisable(true);


        emailTextField.textProperty().bindBidirectional(profileOverviewViewModel.getEmailTextFieldProperty());
        phoneNumberField.valueProperty().bindBidirectional(profileOverviewViewModel.getPhoneNumberProperty());
        nameLabel.textProperty().bind(profileOverviewViewModel.getFullNameProperty());
        dateOfBirthTextField.textProperty().bind(profileOverviewViewModel.getDateOfBirthProperty());
        genderLabel.textProperty().bind(profileOverviewViewModel.getGenderProperty());
        oldPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getOldPasswordProperty());
        newPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getNewPasswordProperty());
        checkPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getCheckPasswordProperty());

        profilePictureView.imageProperty().bindBidirectional(profileOverviewViewModel.getImageProperty());
        smallProfilePictureView.imageProperty().bindBidirectional(profileOverviewViewModel.getImageProperty());

        Circle clip = new Circle();
        clip.setCenterX(profilePictureView.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(profilePictureView.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(profilePictureView.getFitWidth(), profilePictureView.getFitHeight()) / 2); // Radius of the circle
        profilePictureView.setClip(clip);

        Circle clip2 = new Circle();
        clip2.setCenterX(smallProfilePictureView.getFitWidth() / 2); // Center X of the circle
        clip2.setCenterY(smallProfilePictureView.getFitHeight() / 2); // Center Y of the circle
        clip2.setRadius(Math.min(smallProfilePictureView.getFitWidth(), smallProfilePictureView.getFitHeight()) / 2); // Radius of the circle
        smallProfilePictureView.setClip(clip2);

        errorPassword.textProperty().bindBidirectional(profileOverviewViewModel.errorPasswordProperty());
        errorUserEdit.textProperty().bindBidirectional(profileOverviewViewModel.errorUserEditProperty());

        oldPasswordTextField.focusedProperty().addListener((observable,olvValue,newValue) ->{
            if(!newValue){
                profileOverviewViewModel.onTextFieldLostFocus();
            }
        });

        newPasswordTextField.focusedProperty().addListener((observable,oldValue,newValue) ->{
            if(!newValue){
                profileOverviewViewModel.onNewPasswordFieldLostFocus();
            }
        });
        checkPasswordTextField.focusedProperty().addListener((observable,oldValue,newValue) ->{
            if(!newValue){
                profileOverviewViewModel.onNewPasswordFieldLostFocus();
            }
        });

reset();
    }

    public void loadUpcomingEvent(){
        Event event = profileOverviewViewModel.getUpcomingEvent();
        upcomingEventVBox.getChildren().clear();

        if(event == null) {
            upcomingEventLabel.setText("No upcoming event");
            return;
        }
        upcomingEventLabel.setText("Upcoming event");
        try{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("upcomingEventView.fxml"));
        VBox upcomingEvent = loader.load();
        UpcomingEventViewController controller = loader.getController();
        controller.init(profileOverviewViewModel.getUpcomingEvent(), profileOverviewViewModel);
        upcomingEventVBox.getChildren().add(upcomingEvent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reset() {
        profileOverviewViewModel.reset();
        loadUpcomingEvent();
    }

    public Region getRoot() {
        return root;
    }

    public void editUser() throws RemoteException {


        if (emailTextField.isDisable() || phoneNumberField.isDisable()) {
            emailTextField.setDisable(false);
            phoneNumberField.setDisable(false);
            editBtn.setText("Save");
        } else if (profileOverviewViewModel.editUser()) {
            emailTextField.setDisable(true);
            phoneNumberField.setDisable(true);
            editBtn.setText("Edit");
            profileOverviewViewModel.saveUser();
//            errorLabel.setText("");
        }
    }




    public void resetPassword() throws RemoteException {
        oldPasswordTextField.setDisable(false);
        newPasswordTextField.setDisable(false);
        checkPasswordTextField.setDisable(false);
        if(profileOverviewViewModel.resetPassword()){
            oldPasswordTextField.setDisable(true);
            newPasswordTextField.setDisable(true);
            checkPasswordTextField.setDisable(true);
            errorPassword.setText("");
            oldPasswordTextField.setText("");
            newPasswordTextField.setText("");
            checkPasswordTextField.setText("");
        }
    }




    public void openCalendarView() {
        viewHandler.openView("calendar");
    }
}
