import javafx.application.Application;
import javafx.stage.Stage;
import mediator.RmiClient;
import model.ClientModel;
import model.ModelManager;
import view.ViewHandler;
import viewmodel.ViewModelFactory;
import viewmodel.ViewState;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.UUID;

public class MyApplication extends Application {
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ViewState.getInstance().setUserID(UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"));
        ClientModel model = new ModelManager();
        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
        ViewHandler view = new ViewHandler(viewModelFactory);
        RmiClient client = new RmiClient();


        view.start(primaryStage);
    }
}
