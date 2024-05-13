package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Country;
import model.Event;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.ProfileOverviewViewModel;

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
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label ageLabel;
    @FXML private Label sexLabel;
    @FXML private ComboBox<Country> comboBox;
    @FXML private Label errorLabel;
    @FXML private Button editBtn;
    @FXML private TableView<Event> eventTable;
    @FXML TableColumn<Event, String> eventTitle;
    @FXML TableColumn<Event, String> startDate;
    public ProfileOverviewController(){

    }

    public void init(ViewHandler viewHandler, ProfileOverviewViewModel profileOverviewViewModel, Region root) throws IOException, ParseException {
        eventTitle.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        startDate.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getStartTime().toString()));
        eventTable.setItems(profileOverviewViewModel.getEvents());

        this.viewHandler = viewHandler;
        this.profileOverviewViewModel = profileOverviewViewModel;
        this.root = root;
        emailTextField.setDisable(true);
        phoneNumberTextField.setDisable(true);
        List<Country> countries = loadCountries();
        comboBox.getItems().addAll(countries);
        comboBox.setButtonCell(new ListCell<Country>(){
            @Override
            protected void updateItem(Country item, boolean empty){
                super.updateItem(item, empty);
                if(item != null && !empty)
                    setText(item.getName() + "(" + item.getDialCode() + ")");
                else
                    setText(null);
            }
        });

        comboBox.setCellFactory(param -> new ListCell<Country>(){
            @Override
            protected void updateItem(Country item, boolean empty){
                super.updateItem(item, empty);
                if(item != null && !empty)
                    setText(item.getName() + "(" + item.getDialCode() + ")");
                else
                    setText(null);
            }
        });

        emailTextField.textProperty().bindBidirectional(profileOverviewViewModel.getEmailTextFieldProperty());
        phoneNumberTextField.textProperty().bindBidirectional(profileOverviewViewModel.getPhoneNumberProperty());
        firstNameLabel.textProperty().bind(profileOverviewViewModel.getFirstNameProperty());
        lastNameLabel.textProperty().bind(profileOverviewViewModel.getLastNameProperty());
        ageLabel.textProperty().bind(profileOverviewViewModel.getAgeProperty());
        sexLabel.textProperty().bind(profileOverviewViewModel.getSexProperty());
    }

    public void reset(){

    }
    public Region getRoot() {
        return root;
    }

    public void editUser() throws RemoteException {
        if(emailTextField.isDisable() || phoneNumberTextField.isDisable() || comboBox.isDisable()){
            emailTextField.setDisable(false);
        phoneNumberTextField.setDisable(false);
        comboBox.setDisable(false);
        editBtn.setText("Save");
    }
        else if(profileOverviewViewModel.editEmail() && profileOverviewViewModel.editPhoneNumber() && profileOverviewViewModel.editPhoneCode()){
            emailTextField.setDisable(true);
            phoneNumberTextField.setDisable(true);
            comboBox.setDisable(true);
            editBtn.setText("Edit");
            profileOverviewViewModel.saveUser();
        }


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
}
