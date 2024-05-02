import javafx.application.Application;
import javafx.stage.Stage;
import mediator.Client;
import mediator.RmiServer;
import model.Model;
import model.ModelManager;
import model.ServerModel;
import view.ViewHandler;
import viewmodel.ViewModelFactory;

import java.io.IOException;
import java.rmi.NotBoundException;

public class MyApplication extends Application
{
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ServerModel model = new ModelManager();
        RmiServer server = new RmiServer(model);
//        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
//        ViewHandler view = new ViewHandler(viewModelFactory);
//        Client client = new Client();
//
//        view.start(primaryStage);
    }
}
