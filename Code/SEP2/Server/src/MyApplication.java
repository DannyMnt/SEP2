import javafx.application.Application;
import javafx.stage.Stage;
import mediator.RmiServer;

import model.DatabaseSingleton;
import model.ModelManager;
import model.PasswordUtility;
import model.ServerModel;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

import java.io.IOException;
import java.rmi.NotBoundException;

public class MyApplication extends Application
{
    /**
     * Initializes and starts the JavaFX application.
     *
     * <p>This method sets up the application model, creates the RMI server, and adds a shutdown hook
     * to ensure the database disconnects properly when the application is terminated.</p>
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set
     * @throws IOException if an I/O error occurs during the setup of the RMI server
     */
    public void start(Stage primaryStage) throws IOException {
        ServerModel model = new ModelManager();
        RmiServer server = new RmiServer(model);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            DatabaseSingleton.getInstance().disconnect();
        }));
    }
}
