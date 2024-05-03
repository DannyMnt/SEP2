import javafx.application.Application;
import javafx.stage.Stage;
import mediator.Client;
import mediator.RmiClient;
import model.ClientModel;
import model.ModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

import java.io.IOException;
import java.rmi.NotBoundException;

public class MyApplication extends Application
{
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ClientModel model = new ModelManager();
        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
        ViewHandler view = new ViewHandler(viewModelFactory);
        RmiClient client = new RmiClient();

        view.start(primaryStage);
    }
}
