package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import viewmodel.AddEventViewModel;
import viewmodel.ProfileOverviewViewModel;

public class ProfileOverviewController {
    private Region root;
    private ViewHandler viewHandler;
    private ProfileOverviewViewModel profileOverviewViewModel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;

    public ProfileOverviewController(){

    }

    public void init(ViewHandler viewHandler, ProfileOverviewViewModel profileOverviewViewModel, Region root){
        this.viewHandler = viewHandler;
        this.profileOverviewViewModel = profileOverviewViewModel;
        this.root = root;
        emailTextField.setDisable(true);
        phoneNumberTextField.setDisable(true);

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
        if(phoneNumberTextField.isDisable())
            phoneNumberTextField.setDisable(false);
        else
            phoneNumberTextField.setDisable(true);
    }
}
