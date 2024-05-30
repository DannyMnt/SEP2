package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.LoginUserViewModel;

import java.rmi.RemoteException;

/**
 * Controller class for the LoginUserView.
 */
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

    /**
     * Initializes the controller with the specified view handler, view model, and root region.
     * @param viewHandler the view handler
     * @param loginUserViewModel the view model for user login
     * @param root the root region of the view
     */
    public void init(ViewHandler viewHandler, LoginUserViewModel loginUserViewModel, Region root) {
        this.viewHandler = viewHandler;
        this.loginUserViewModel = loginUserViewModel;
        this.root = root;

        passwordField.textProperty().bindBidirectional(loginUserViewModel.getPasswordStringProperty());
        emailField.textProperty().bindBidirectional(loginUserViewModel.getEmailStringProperty());
        errorLabel.textProperty().bind(loginUserViewModel.getErrorStringProperty());

    }

    /**
     * Resets the view.
     */
    public void reset() {
    }

    /**
     * Gets the root region of the view.
     * @return the root region
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Handles the login process.
     * If login is successful, opens the calendar view.
     * @throws RemoteException if a remote exception occurs
     */
    public void login() throws RemoteException {
        if (loginUserViewModel.loginUser()) {
            viewHandler.openView("calendar");
        }
    }

    /**
     * Handles the event when the register button is clicked.
     * Opens the registration view.
     */
    public void loginButtonClicked() {
        viewHandler.openView("register");
    }
}
