package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import viewmodel.RegisterUserViewModel;

import java.rmi.RemoteException;

public class RegisterUserViewController {

    private ViewHandler viewHandler;
    private RegisterUserViewModel viewModel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
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
    }

    public Region getRoot() {
        return root;
    }

    public void reset(){

    }

    public void continueBtn() throws RemoteException {
        if(phase == 0){
            if(emailTextField.getText().isEmpty())
                errorLabel.setText("Email field cannot be empty");
//            else if(!viewModel.isEmailValid(emailTextField.getText()))
//                errorLabel.setText("Email is already in use");
            else if(!emailTextField.getText().contains("@"))
                errorLabel.setText("Email format is invalid");
            else {
                phase++;
                errorLabel.setText("");
                this.passwordTextField = new TextField();
                this.passwordTextField.setId("passwordTextField");
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

    public void phase3Generation(){
        TextField firstNameTextField = new TextField();
        firstNameTextField.setId("firstNameTextField");
        TextField lastNameTextField = new TextField();
        lastNameTextField.setId("lastNameTextField");
        DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setId("dateOfBirth");
        ComboBox<String> gender = new ComboBox<>();
        gender.setId("genderComboBox");
        ObservableList<String> elements = FXCollections.observableArrayList();
        elements.add("Male");
        elements.add("Female");
        elements.add("Other");
        gender.setItems(elements);
        TextField phoneNumberTextField = new TextField();
        phoneNumberTextField.setId("phoneNumberTextField");
        vbox2.getChildren().add(firstNameTextField);
        vbox2.getChildren().add(lastNameTextField);
        vbox2.getChildren().add(dateOfBirth);
        vbox2.getChildren().add(gender);
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
}
