package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.RegisterUserViewModel;
import view.ViewHandler;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserViewController {

    private ViewHandler viewHandler;
    private RegisterUserViewModel viewModel;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox vbox1;
    @FXML
    private VBox vbox2;
    private int phase;
    private Region root;

    public void init(ViewHandler viewHandler, RegisterUserViewModel viewModel, Region root){
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
        phase = 0;
        emailTextField.textProperty().bindBidirectional(viewModel.getEmailStringProperty());
    }

    public Region getRoot() {
        return root;
    }

    public void reset(){

    }

    public void continueBtn() throws IOException, ParseException {
        if(phase == 0){
            if(emailTextField.getText().isEmpty())
                errorLabel.setText("Email field cannot be empty");
//            else if(!viewModel.isEmailFree(emailTextField.getText()))
//                errorLabel.setText("Email is already in use");
            else if(!emailTextField.getText().contains("@"))
                errorLabel.setText("Email format is invalid");
            else {
                phase++;
                errorLabel.setText("");
                this.passwordTextField = new PasswordField();
                this.passwordTextField.setId("passwordTextField");
                this.passwordTextField.textProperty().bindBidirectional(viewModel.getPasswordStringProperty());
                Label passwordLabel = new Label("Password");
                passwordLabel.setPadding(new Insets(10, 0, 10, 0));
                vbox1.getChildren().add(passwordLabel);
                vbox2.getChildren().add(passwordTextField);
            }
        }
        else if(phase == 1){
            errorLabel.setText("");
            if(this.passwordTextField.getText().isEmpty()) {
                errorLabel.setText("Password filed cannot be empty");
            }
            else if(this.passwordTextField.getText().length() < 5) {
                errorLabel.setText("Password is too short");
            }
            else {
                phase++;
                phase3Generation();
            }
        }
        else if(phase == 2){
            System.out.println("User created(not really)");
            viewModel.createUser();
        }
    }

    public void phase3Generation() throws IOException, ParseException {
        TextField firstNameTextField = new TextField();
        firstNameTextField.setId("firstNameTextField");
        firstNameTextField.textProperty().bindBidirectional(viewModel.getFirstNameStringProperty());
        TextField lastNameTextField = new TextField();
        lastNameTextField.setId("lastNameTextField");
        lastNameTextField.textProperty().bindBidirectional(viewModel.getLastNameStringProperty());
        DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setId("dateOfBirth");
        dateOfBirth.valueProperty().bindBidirectional(viewModel.getBirthDate().valueProperty());
        ComboBox<String> gender = new ComboBox<>();
        gender.setId("genderComboBox");
        ObservableList<String> elements = FXCollections.observableArrayList();
        elements.add("Male");
        elements.add("Female");
        elements.add("Other");
        gender.setItems(elements);
        gender.valueProperty().bindBidirectional(viewModel.getGenderStringProperty());
        ComboBox<Country> comboBox = new ComboBox<Country>();
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
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.setId("phoneNumberTextField");
        phoneNumberTextField.textProperty().bindBidirectional(viewModel.getPhoneNumberStringProperty());
        vbox2.getChildren().add(firstNameTextField);
        vbox2.getChildren().add(lastNameTextField);
        vbox2.getChildren().add(dateOfBirth);
        vbox2.getChildren().add(gender);
        vbox2.getChildren().add(comboBox);
        vbox2.getChildren().add(phoneNumberTextField);

        Label firstNameLabel = new Label("First name");
        firstNameLabel.setPadding(new Insets(0, 0 , 10, 0));
        Label lastNameLabel = new Label("Last name");
        lastNameLabel.setPadding(new Insets(0, 0 , 10, 0));
        Label dateOfBirthLabel = new Label("Date of birth");
        dateOfBirthLabel.setPadding(new Insets(0, 0 , 10, 0));
        Label genderLabel = new Label("Gender");
        genderLabel.setPadding(new Insets(0, 0 , 10, 0));
        Label phoneNumberLabel = new Label("Phone number");
        phoneNumberLabel.setPadding(new Insets(0, 0 , 10, 0));
        vbox1.getChildren().add(firstNameLabel);
        vbox1.getChildren().add(lastNameLabel);
        vbox1.getChildren().add(dateOfBirthLabel);
        vbox1.getChildren().add(genderLabel);
        vbox1.getChildren().add(phoneNumberLabel);
    }

    private List<Country> loadCountries() throws IOException, ParseException {
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
