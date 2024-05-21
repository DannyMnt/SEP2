package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
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
    @FXML private TextField phoneNumberTextField;
    @FXML private Label nameLabel;
    @FXML private TextField dateOfBirthTextField;
    @FXML private TextField genderLabel;
    @FXML private ComboBox<Country> comboBox;
    @FXML private Label errorLabel;
    @FXML private Button editBtn;
    @FXML private TextField oldPasswordTextField;
    @FXML private TextField newPasswordTextField;
    @FXML private TextField checkPasswordTextField;
    @FXML private Label errorLabel2;
    @FXML private Label eventTitle;
    @FXML private Label eventDate;
    @FXML private Label eventTime;
    @FXML private Label eventDescription;
    @FXML private Label eventLocation;

    @FXML private ImageView profilePictureView;
    @FXML private ImageView smallProfilePictureView;

    public ProfileOverviewController() {

    }

    public void init(ViewHandler viewHandler, ProfileOverviewViewModel profileOverviewViewModel, Region root) throws IOException, ParseException {
        profileOverviewViewModel.getUser(ViewState.getInstance());

        this.viewHandler = viewHandler;
        this.profileOverviewViewModel = profileOverviewViewModel;
        this.root = root;
        emailTextField.setDisable(true);
        phoneNumberTextField.setDisable(true);
        List<Country> countries = loadCountries();
        comboBox.getItems().addAll(countries);
        comboBox.setValue(getCountryByDialCode(countries, profileOverviewViewModel.getPhoneNumberProperty().get()));
        comboBox.setButtonCell(new ListCell<Country>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty)
                    setText(item.getDialCode());
                else{
                    setText(null);
                }
            }
        });

        comboBox.setCellFactory(param -> new ListCell<Country>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty)
                    setText(item.getName() + "(" + item.getDialCode() + ")");
                else
                    setText(null);
            }
        });

        comboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                profileOverviewViewModel.getPhoneNumberProperty().set(newVal.getDialCode());
            }
        });

        emailTextField.textProperty().bindBidirectional(profileOverviewViewModel.getEmailTextFieldProperty());
        phoneNumberTextField.textProperty().bindBidirectional(profileOverviewViewModel.getPhoneNumberProperty2());
        nameLabel.textProperty().bind(profileOverviewViewModel.getFullNameProperty());
        dateOfBirthTextField.textProperty().bind(profileOverviewViewModel.getDateOfBirthProperty());
        genderLabel.textProperty().bind(profileOverviewViewModel.getGenderProperty());
        oldPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getOldPasswordProperty());
        newPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getNewPasswordProperty());
        checkPasswordTextField.textProperty().bindBidirectional(profileOverviewViewModel.getCheckPasswordProperty());
        eventTitle.textProperty().bindBidirectional(profileOverviewViewModel.getEventTitleProperty());
        eventDate.textProperty().bindBidirectional(profileOverviewViewModel.getEventDateProperty());
        eventTime.textProperty().bindBidirectional(profileOverviewViewModel.getEventTimeProperty());
        eventDescription.textProperty().bindBidirectional(profileOverviewViewModel.getEventDescriptionProperty());
        eventLocation.textProperty().bindBidirectional(profileOverviewViewModel.getEventLocationProperty());
        profilePictureView.imageProperty().bindBidirectional(profileOverviewViewModel.getImageProperty());
        smallProfilePictureView.imageProperty().bindBidirectional(profileOverviewViewModel.getImageProperty());
    }

    public void reset() {

    }

    public Region getRoot() {
        return root;
    }

    public void editUser() throws RemoteException {
        if (emailTextField.isDisable() || phoneNumberTextField.isDisable() || comboBox.isDisable()) {
            emailTextField.setDisable(false);
            phoneNumberTextField.setDisable(false);
            comboBox.setDisable(false);
            editBtn.setText("Save");
        } else if (profileOverviewViewModel.editEmail() && profileOverviewViewModel.editPhoneNumber() && profileOverviewViewModel.editPhoneCode()) {
            emailTextField.setDisable(true);
            phoneNumberTextField.setDisable(true);
            comboBox.setDisable(true);
            editBtn.setText("Edit");
            profileOverviewViewModel.saveUser();
            errorLabel.setText("");
        }
        else if(!profileOverviewViewModel.editEmail())
            errorLabel.setText("Invalid Email");
        else if(!profileOverviewViewModel.editPhoneNumber())
            errorLabel.setText("Invalid telephone number");
    }


    public static List<Country> loadCountries() throws IOException, ParseException {
        List<Country> countries = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("SEP2/CountryCodes.json"));


        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            String name = (String) jsonObj.get("name");
            String dialCode = (String) jsonObj.get("dial_code");
            String code = (String) jsonObj.get("code");
            countries.add(new Country(name, dialCode, code));
        }

        return countries;
    }

    private Country getCountryByDialCode(List<Country> countries, String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            String[] parts = phoneNumber.split(" ");
            if (parts.length > 0) {
                String dialCode = parts[0];
                for (Country country : countries) {
                    if (country.getDialCode().equals(dialCode)) {
                        return country;
                    }
                }
            }
        }
        return null;
    }

    public void resetPassword() throws RemoteException {
        oldPasswordTextField.setDisable(false);
        newPasswordTextField.setDisable(false);
        checkPasswordTextField.setDisable(false);
        if(profileOverviewViewModel.resetPassword()){
            oldPasswordTextField.setDisable(true);
            newPasswordTextField.setDisable(true);
            checkPasswordTextField.setDisable(true);
            errorLabel2.setText("");
        }
        else
            errorLabel2.setText("Passwords are incorrect");
    }

    public void openCalendarView() {
        viewHandler.openView("calendar");
    }
}
