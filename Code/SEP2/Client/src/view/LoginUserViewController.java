package view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.AddEventViewModel;
import viewmodel.LoginUserViewModel;

import java.rmi.RemoteException;
import java.time.LocalDate;

public class LoginUserViewController {
    private Region root;
    private ViewHandler viewHandler;

    private LoginUserViewModel loginUserViewModel;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private Label errorLabel;


    public LoginUserViewController(){

    }
    public void init(ViewHandler viewHandler, LoginUserViewModel loginUserViewModel, Region root){
        this.viewHandler = viewHandler;
        this.loginUserViewModel = loginUserViewModel;
        this.root = root;

        passwordField.textProperty().bindBidirectional(loginUserViewModel.getPasswordStringProperty());
        emailField.textProperty().bindBidirectional(loginUserViewModel.getEmailStringProperty());
        errorLabel.textProperty().bind(loginUserViewModel.getErrorStringProperty());

    }

    public void reset(){
    }

    public Region getRoot() {
        return root;
    }

    public void login() throws RemoteException {
        if (loginUserViewModel.loginUser()) {

            viewHandler.openView("calendar");
        }
    }

    public void loginButtonClicked() {
        viewHandler.openView("register");
    }
}
