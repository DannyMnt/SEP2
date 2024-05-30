import javafx.application.Application;
import javafx.stage.Stage;
import mediator.ClientCallback;
import mediator.RmiClient;
import model.ClientModel;
import model.ModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;
import viewmodel.ViewState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * The MyApplication class extends the JavaFX Application class and represents the main application entry point.
 * It initializes the application components, sets up the view, and starts the application.
 */
public class MyApplication extends Application {
    /**
     * Initializes and starts the JavaFX application.
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     * @throws IOException if an I/O error occurs during initialization
     * @throws NotBoundException if the RMI registry is not bound
     */
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ViewState.getInstance().setUserID(UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"));
        ClientModel model = new ModelManager();

        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
        ViewHandler view = new ViewHandler(viewModelFactory);
        RmiClient client = new RmiClient();


        view.start(primaryStage);
    }
}
