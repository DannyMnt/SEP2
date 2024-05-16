package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.RegisterUserViewModel;
import view.ViewHandler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RegisterUserViewController {

    private ViewHandler viewHandler;
    private RegisterUserViewModel viewModel;

    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<Country> prefixComboBox;

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField confirmTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    private int phase;
    private Region root;

    @FXML
    private VBox vbox1;
    @FXML
    private VBox vbox2;

    public void init(ViewHandler viewHandler, RegisterUserViewModel viewModel, Region root) throws IOException, ParseException {

        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
        phase = 0;
        emailTextField.textProperty().bindBidirectional(viewModel.getEmailStringProperty());
        passwordTextField.textProperty().bindBidirectional(viewModel.getPasswordStringProperty());
        firstNameTextField.textProperty().bindBidirectional(viewModel.getFirstNameStringProperty());
        lastNameTextField.textProperty().bindBidirectional(viewModel.getLastNameStringProperty());
        confirmTextField.textProperty().bindBidirectional(viewModel.getConfirmTextStringProperty());

        ObservableList<String> elements = FXCollections.observableArrayList();
        elements.add("Male");
        elements.add("Female");
        elements.add("Other");
        genderComboBox.setItems(elements);

        List<Country> countries = loadCountries();

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

        emailTextField.textProperty().addListener((observable,oldValue,newValue) -> {
            if (newValue.matches(emailRegex)){
                errorLabel.setText("");
            }else {
                errorLabel.setText("Invalid email format");
            }
        });

        prefixComboBox.getItems().addAll(countries);
        prefixComboBox.setButtonCell(new ListCell<Country>(){
            @Override
            protected void updateItem(Country item, boolean empty){
                super.updateItem(item, empty);
                if(item != null && !empty)
                    setText(item.getName() + "(" + item.getDialCode() + ")");
                else
                    setText(null);
            }
        });

        prefixComboBox.setCellFactory(param -> new ListCell<Country>(){
            @Override
            protected void updateItem(Country item, boolean empty){
                super.updateItem(item, empty);
                if(item != null && !empty)
                    setText(item.getName() + "(" + item.getDialCode() + ")");
                else
                    setText(null);
            }
        });
    }

    public Region getRoot() {
        return root;
    }

    public void reset(){

    }

    @FXML
    private void loginButtonClicked(){
        viewHandler.openView("login");
    }

    public void continueBtn() throws IOException, ParseException {
        if(phase == 0){
            if(emailTextField.getText().isEmpty())
                errorLabel.setText("Email field cannot be empty");
            else if(!viewModel.isEmailFree(emailTextField.getText()))
                errorLabel.setText("Email is already in use");
            else if(!emailTextField.getText().contains("@"))
                errorLabel.setText("Email format is invalid");
            else {
                phase++;
                errorLabel.setText("");
                passwordTextField.setDisable(false);
                confirmTextField.setDisable(false);
            }
        }
        else if(phase == 1){
            errorLabel.setText("");
            if(this.passwordTextField.getText().isEmpty()) {
                errorLabel.setText("Password filled cannot be empty");
            }
            else if(this.passwordTextField.getText().length() < 5) {
                errorLabel.setText("Password is too short");
            }
            else if (this.confirmTextField.getText().equals(this.passwordTextField.getText())) {
                phase++;
                firstNameTextField.setDisable(false);
                lastNameTextField.setDisable(false);

            }else {
                errorLabel.setText("Passwords do not match");
            }
        }
        else if(phase == 2){
        viewModel.createUser();

        }
    }

//    public void phase3Generation() throws IOException, ParseException {
//        TextField firstNameTextField = new TextField();
//        firstNameTextField.setId("firstNameTextField");
//        firstNameTextField.textProperty().bindBidirectional(viewModel.getFirstNameStringProperty());
//        TextField lastNameTextField = new TextField();
//        lastNameTextField.setId("lastNameTextField");
//        lastNameTextField.textProperty().bindBidirectional(viewModel.getLastNameStringProperty());
//        DatePicker dateOfBirth = new DatePicker();
//        dateOfBirth.setId("dateOfBirth");
//        dateOfBirth.valueProperty().bindBidirectional(viewModel.getBirthDate().valueProperty());
//        ComboBox<String> gender = new ComboBox<>();
//        gender.setId("genderComboBox");
//        ObservableList<String> elements = FXCollections.observableArrayList();
//        elements.add("Male");
//        elements.add("Female");
//        elements.add("Other");
//        gender.setItems(elements);
//        gender.valueProperty().bindBidirectional(viewModel.getGenderStringProperty());
//        ComboBox<Country> comboBox = new ComboBox<Country>();
//        List<Country> countries = loadCountries();
//        comboBox.getItems().addAll(countries);
//        comboBox.setButtonCell(new ListCell<Country>(){
//            @Override
//            protected void updateItem(Country item, boolean empty){
//                super.updateItem(item, empty);
//                if(item != null && !empty)
//                    setText(item.getName() + "(" + item.getDialCode() + ")");
//                else
//                    setText(null);
//            }
//        });
//
//        comboBox.setCellFactory(param -> new ListCell<Country>(){
//            @Override
//            protected void updateItem(Country item, boolean empty){
//                super.updateItem(item, empty);
//                if(item != null && !empty)
//                    setText(item.getName() + "(" + item.getDialCode() + ")");
//                else
//                    setText(null);
//            }
//        });
//        TextField phoneNumberTextField = new TextField();
//        phoneNumberTextField.setId("phoneNumberTextField");
//        phoneNumberTextField.textProperty().bindBidirectional(viewModel.getPhoneNumberStringProperty());
//        vbox2.getChildren().add(firstNameTextField);
//        vbox2.getChildren().add(lastNameTextField);
//        vbox2.getChildren().add(dateOfBirth);
//        vbox2.getChildren().add(gender);
//        vbox2.getChildren().add(comboBox);
//        vbox2.getChildren().add(phoneNumberTextField);
//
//        Label firstNameLabel = new Label("First name");
//        firstNameLabel.setPadding(new Insets(0, 0 , 10, 0));
//        Label lastNameLabel = new Label("Last name");
//        lastNameLabel.setPadding(new Insets(0, 0 , 10, 0));
//        Label dateOfBirthLabel = new Label("Date of birth");
//        dateOfBirthLabel.setPadding(new Insets(0, 0 , 10, 0));
//        Label genderLabel = new Label("Gender");
//        genderLabel.setPadding(new Insets(0, 0 , 10, 0));
//        Label phoneNumberLabel = new Label("Phone number");
//        phoneNumberLabel.setPadding(new Insets(0, 0 , 10, 0));
//        vbox1.getChildren().add(firstNameLabel);
//        vbox1.getChildren().add(lastNameLabel);
//        vbox1.getChildren().add(dateOfBirthLabel);
//        vbox1.getChildren().add(genderLabel);
//        vbox1.getChildren().add(phoneNumberLabel);
//    }

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

    public void addFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                System.out.println("Selected file: " + file.getAbsolutePath());
                if (selectedFiles != null) {
                    System.out.println("File received");
                    File uploadsDir = new File("../Code/SEP2/Client/uploads");
                    if (!uploadsDir.exists()) {
                        uploadsDir.mkdirs();
                    }
                    System.out.println("Final stage");
                        File destinationFile = new File(uploadsDir, file.getName());
                    try {
                        File destFile = new File(uploadsDir, file.getName());
                        Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("File copied to: " + destFile.getAbsolutePath());

                    } catch (IOException e) {
                        System.err.println("Error copying/moving file: " + e.getMessage());
                    }

                }
            }
        }
    }
}
