import javafx.application.Application;

/**
 * The ServerMain class contains the main method which serves as the entry point of the server application.
 * It launches the JavaFX application by calling Application.launch with MyApplication.class.
 */
public class ServerMain {
    /**
     * The main method which serves as the entry point of the application.
     * It launches the JavaFX application by calling Application.launch with MyApplication.class.
     *
     * @param args the command line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(MyApplication.class);
    }
}