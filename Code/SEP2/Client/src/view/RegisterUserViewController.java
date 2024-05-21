package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.Country;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import viewmodel.RegisterUserViewModel;
import view.ViewHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
    @FXML
    private TextField phoneNumberSufix;

    @FXML
    private DatePicker BirthdaySelect;

    @FXML private Circle imageField;

    @FXML private Pane imagePane;

    @FXML private ImageView imageUploadField;
    @FXML private ImageView imageUploadIcon;

    public void init(ViewHandler viewHandler, RegisterUserViewModel viewModel, Region root) throws IOException, ParseException {

        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
        phase = 0;

        initializeImageView();
//        imageUploadField.setImage(new Image("images/profilePicture1.png"));
//        imageUploadIcon.setVisible(false);



//        emailTextField.setOnKeyPressed(event -> {
//            try {
//                continueBtn();
//            } catch (IOException | ParseException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        confirmTextField.setOnKeyPressed(event -> {
//            try {
//                continueBtn();
//            } catch (IOException | ParseException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        emailTextField.textProperty().bindBidirectional(viewModel.getEmailStringProperty());
//        passwordTextField.textProperty().bindBidirectional(viewModel.getPasswordStringProperty());
//        firstNameTextField.textProperty().bindBidirectional(viewModel.getFirstNameStringProperty());
//        lastNameTextField.textProperty().bindBidirectional(viewModel.getLastNameStringProperty());
//        confirmTextField.textProperty().bindBidirectional(viewModel.getConfirmTextStringProperty());
//
//        ObservableList<String> elements = FXCollections.observableArrayList();
//        elements.add("Male");
//        elements.add("Female");
//        elements.add("Other");
//        genderComboBox.setItems(elements);
//
//        List<Country> countries = loadCountries();
//
//        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
//
//        emailTextField.textProperty().addListener((observable,oldValue,newValue) -> {
//            if (newValue.matches(emailRegex)){
//                errorLabel.setText("");
//            }else {
//                errorLabel.setText("Invalid email format");
//            }
//        });
//
//        prefixComboBox.getItems().addAll(countries);
//        prefixComboBox.setButtonCell(new ListCell<Country>(){
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
//        prefixComboBox.setCellFactory(param -> new ListCell<Country>(){
//            @Override
//            protected void updateItem(Country item, boolean empty){
//                super.updateItem(item, empty);
//                if(item != null && !empty)
//                    setText(item.getName() + "(" + item.getDialCode() + ")");
//                else
//                    setText(null);
//            }
//        });
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

//    public void continueBtn() throws IOException, ParseException {
//        if(phase == 0){
//            if(emailTextField.getText().isEmpty() || emailTextField.equals("a") )
//                errorLabel.setText("Email field cannot be empty");
//            else if(!viewModel.isEmailFree(emailTextField.getText()))
//                errorLabel.setText("Email is already in use");
//            else if(!emailTextField.getText().contains("@"))
//                errorLabel.setText("Email format is invalid");
//            else {
//                phase++;
//                errorLabel.setText("");
//                passwordTextField.setDisable(false);
//                confirmTextField.setDisable(false);
//            }
//        }
//        else if(phase == 1){
//            errorLabel.setText("");
//            if(passwordTextField.getText().isEmpty()) {
//                errorLabel.setText("Password filled cannot be empty");
//            }
//            else if(this.passwordTextField.getText().length() < 5) {
//                errorLabel.setText("Password is too short");
//            }
//            else if (this.confirmTextField.getText().equals(this.passwordTextField.getText())) {
//                phase++;
//                firstNameTextField.setDisable(false);
//                lastNameTextField.setDisable(false);
//                genderComboBox.setDisable(false);
//                prefixComboBox.setDisable(false);
//                phoneNumberSufix.setDisable(false);
//                BirthdaySelect.setDisable(false);
//
//
//            }else {
//                errorLabel.setText("Passwords do not match");
//            }
//        }
//        else if(phase == 2){
//        viewModel.createUser();
//
//        }
//    }


    public void initializeImageView() {
        Circle clip = new Circle();
        clip.setCenterX(imageUploadField.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(imageUploadField.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(imageUploadField.getFitWidth(), imageUploadField.getFitHeight()) / 2); // Radius of the circle

        // Set the clip to the ImageView
        imageUploadField.setClip(clip);

        // Set up mouse enter event handler
        imagePane.setOnMouseEntered(event -> {
            if(imageUploadField.getImage() != null){
                imageUploadField.setOpacity(0.8);
            imageUploadIcon.setVisible(true);
            }
        });

        // Set up mouse exit event handler
        imagePane.setOnMouseExited(event -> {
            if(imageUploadField.getImage() != null) {
                imageUploadField.setOpacity(1);
                imageUploadIcon.setVisible(false);
            }
        });
    }

    public void updateImageView(File image){
        System.out.println(image.toPath());
        System.out.println(image);
        imageUploadField.setImage(new Image(image.toURI().toString()));
        imageUploadIcon.setVisible(false);
        imageUploadField.setOpacity(1);
//        imageUploadIcon.setVisible(false);
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

    public void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image File");

        // Set initial directory to user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Add filter for image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        // Show file chooser dialog and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            // Create a temporary directory
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "temp_images");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // Convert image to JPEG format and save with a temporary name
            try {
                BufferedImage image = ImageIO.read(selectedFile);

                int size = Math.min(image.getWidth(), image.getHeight());
                int x = (image.getWidth() - size) / 2;
                int y = (image.getHeight() - size) / 2;

                // Crop the original image to a square
                BufferedImage croppedImage = image.getSubimage(x, y, size, size);


                File tempFile = new File(tempDir, "temp_image.jpg");
                ImageIO.write(croppedImage, "jpg", tempFile);
                updateImageView(tempFile);
                System.out.println("Image converted and saved to: " + tempFile.getAbsolutePath());

            } catch (IOException e) {
                System.err.println("Error converting image: " + e.getMessage());
            }
        }

    }
}
