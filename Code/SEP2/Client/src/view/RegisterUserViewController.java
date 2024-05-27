package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
//import org.xbill.DNS.Lookup;
//import org.xbill.DNS.Record;
//import org.xbill.DNS.Type;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.dlsc.phonenumberfx.PhoneNumberField;

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


        errorLabel.textProperty().bind(viewModel.getErrorStringProperty());

        ObservableList<String> elements = FXCollections.observableArrayList();
        elements.add("Male");
        elements.add("Female");
        elements.add("Other");


    }

    public Region getRoot() {
        return root;
    }

    public void reset() {

    }

    @FXML
    private void loginButtonClicked() {
        viewHandler.openView("login");
    }

    public void onRegister() {
        if(viewModel.register()) viewHandler.openView("calendar");



    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }



    public void initializeImageView() {
        Circle clip = new Circle();
        clip.setCenterX(imageUploadField.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(imageUploadField.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(imageUploadField.getFitWidth(), imageUploadField.getFitHeight()) / 2); // Radius of the circle

        // Set the clip to the ImageView
        imageUploadField.setClip(clip);

        // Set up mouse enter event handler
        imagePane.setOnMouseEntered(event -> {
            if (imageUploadField.getImage() != null) {
                imageUploadField.setOpacity(0.8);
                imageUploadIcon.setVisible(true);
            }
        });

        // Set up mouse exit event handler
        imagePane.setOnMouseExited(event -> {
            if (imageUploadField.getImage() != null) {
                imageUploadField.setOpacity(1);
                imageUploadIcon.setVisible(false);
            }
        });
    }

    public void updateImageView(File image) {
        System.out.println(image.toPath());
        System.out.println(image);
        imageUploadField.setImage(new Image(image.toURI().toString()));
        imageUploadIcon.setVisible(false);
        imageUploadField.setOpacity(1);
//        imageUploadIcon.setVisible(false);
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
