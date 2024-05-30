package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.LoginUserViewModel;

import java.rmi.RemoteException;

public class LoginUserViewController {
    private Region root;
    private ViewHandler viewHandler;

    private LoginUserViewModel loginUserViewModel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private Label errorLabel;


    public LoginUserViewController() {

    }

    public void init(ViewHandler viewHandler, LoginUserViewModel loginUserViewModel, Region root) {
        this.viewHandler = viewHandler;
        this.loginUserViewModel = loginUserViewModel;
        this.root = root;

        passwordField.textProperty().bindBidirectional(loginUserViewModel.getPasswordStringProperty());
        emailField.textProperty().bindBidirectional(loginUserViewModel.getEmailStringProperty());
        errorLabel.setText("");

    }

    public void reset() {
    }

    public Region getRoot() {
        return root;
    }

    public void login() throws RemoteException {
        if (loginUserViewModel.loginUser()) {
            viewHandler.openView("calendar");
        } else errorLabel.setText(loginUserViewModel.getErrorStringProperty().getValue());
    }

    public void loginButtonClicked() {
        viewHandler.openView("register");
    }
}
