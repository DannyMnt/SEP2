package view;

import com.dlsc.phonenumberfx.PhoneNumberField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import model.Country;
import org.json.simple.parser.ParseException;
import viewmodel.RegisterUserViewModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller class for the Register User view.
 */
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
    private PasswordField passwordTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private PasswordField confirmTextField;
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

    @FXML private HBox phoneHBox;

    @FXML
    private TextField phoneNumberSufix;

    @FXML
    private DatePicker birthdaySelect;

    @FXML
    private Circle imageField;

    @FXML
    private Pane imagePane;

    @FXML
    private ImageView imageUploadField;
    @FXML
    private ImageView imageUploadIcon;

    private PhoneNumberField phoneNumberField;

    private byte[] imageData;

    /**
     * Initializes the controller with the ViewHandler, RegisterUserViewModel, and root region.
     *
     * @param viewHandler            The ViewHandler instance.
     * @param viewModel              The RegisterUserViewModel instance.
     * @param root                   The root region of the view.
     * @throws IOException           If an I/O error occurs.
     * @throws ParseException        If a parse error occurs.
     */
    public void init(ViewHandler viewHandler, RegisterUserViewModel viewModel, Region root) throws IOException, ParseException {

        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        this.root = root;
        phase = 0;

        initializeImageView();
        phoneNumberField = new PhoneNumberField();

        phoneHBox.getChildren().add(phoneNumberField);


        emailTextField.textProperty().bindBidirectional(viewModel.getEmailStringProperty());
        passwordTextField.textProperty().bindBidirectional(viewModel.getPasswordStringProperty());
        confirmTextField.textProperty().bindBidirectional(viewModel.getConfirmTextStringProperty());
        firstNameTextField.textProperty().bindBidirectional(viewModel.getFirstNameStringProperty());
        lastNameTextField.textProperty().bindBidirectional(viewModel.getLastNameStringProperty());
        genderComboBox.valueProperty().bindBidirectional(viewModel.getGenderStringProperty());
        birthdaySelect.valueProperty().bindBidirectional(viewModel.getBirthProperty());
        imageUploadField.imageProperty().bindBidirectional(viewModel.getImagePropertyProperty());
        phoneNumberField.valueProperty().bindBidirectional(viewModel.getPhoneNumberStringProperty());

        emailTextField.setText("test");
        passwordTextField.setText("test");
        confirmTextField.setText("test");
        firstNameTextField.setText("test");
        lastNameTextField.setText("test");
        genderComboBox.valueProperty().set("Male");
        birthdaySelect.setValue(LocalDate.now());


        phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 21) {
                phoneNumberField.setText(oldValue);
            }
        });
        errorLabel.textProperty().bind(viewModel.getErrorStringProperty());

        genderComboBox.getItems().addAll("Male", "Female", "Other");


    }

    /**
     * Retrieves the root region of the view.
     *
     * @return The root region.
     */
    public Region getRoot() {
        return root;
    }


    public void reset() {

    }

    /**
     * Handles the login button click event.
     */
    @FXML
    private void loginButtonClicked() {
        viewHandler.openView("login");
    }

    /**
     * Handles the register button click event.
     */
    public void onRegister() {
        if(viewModel.register()) viewHandler.openView("calendar");
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str The string to check.
     * @return True if the string is null or empty, false otherwise.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }


    /**
     * Initializes the image view and sets up mouse event listeners.
     */
    public void initializeImageView() {
        Circle clip = new Circle();
        clip.setCenterX(imageUploadField.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(imageUploadField.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(imageUploadField.getFitWidth(), imageUploadField.getFitHeight()) / 2); // Radius of the circle

        imageUploadField.setClip(clip);

        imagePane.setOnMouseEntered(event -> {
            if (imageUploadField.getImage() != null) {
                imageUploadField.setOpacity(0.8);
                imageUploadIcon.setVisible(true);
            }
        });

        imagePane.setOnMouseExited(event -> {
            if (imageUploadField.getImage() != null) {
                imageUploadField.setOpacity(1);
                imageUploadIcon.setVisible(false);
            }
        });
    }

    /**
     * Updates the image view with the selected file.
     *
     * @param image The selected image file.
     */
    public void updateImageView(File image) {

        imageUploadField.setImage(new Image(image.toURI().toString()));
        imageUploadIcon.setVisible(false);
        imageUploadField.setOpacity(1);
    }


    /**
     * Opens a file chooser dialog to select an image file.
     */
    public void addFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image File");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            File tempDir = new File(System.getProperty("java.io.tmpdir"), "temp_images");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                int size = Math.min(image.getWidth(), image.getHeight());
                int x = (image.getWidth() - size) / 2;
                int y = (image.getHeight() - size) / 2;
                BufferedImage croppedImage = image.getSubimage(x, y, size, size);
                File tempFile = new File(tempDir, "temp_image.jpg");
                ImageIO.write(croppedImage, "jpg", tempFile);
                updateImageView(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
