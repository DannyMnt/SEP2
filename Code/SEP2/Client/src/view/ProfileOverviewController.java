package view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import model.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.ProfileOverviewViewModel;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileOverviewController {
    private Region root;
    private ViewHandler viewHandler;
    private ProfileOverviewViewModel profileOverviewViewModel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private ComboBox<Country> comboBox;

    public ProfileOverviewController(){

    }

    public void init(ViewHandler viewHandler, ProfileOverviewViewModel profileOverviewViewModel, Region root) throws IOException, ParseException {
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
    }

    public void reset(){

    }
    public Region getRoot() {
        return root;
    }

    public void editEmail() {
        profileOverviewViewModel.editEmail();
        if(emailTextField.isDisable())
            emailTextField.setDisable(false);
        else if(emailTextField.getText().contains("@"))
            emailTextField.setDisable(true);
    }

    public void editPhoneNumber() {
        profileOverviewViewModel.editPhoneNumber();
        phoneNumberTextField.setDisable(!phoneNumberTextField.isDisable());
    }

    public static List<Country> loadCountries() throws IOException, ParseException {
        List<Country> countries = new ArrayList<>();

        // Parse JSON file
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("SEP2/CountryCodes.json"));

        // Iterate over JSON array and create Country objects
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
