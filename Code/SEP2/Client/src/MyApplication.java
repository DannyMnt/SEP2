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

public class MyApplication extends Application {
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ViewState.getInstance().setUserID(UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"));
        ClientModel model = new ModelManager();

        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
        ViewHandler view = new ViewHandler(viewModelFactory);
        RmiClient client = new RmiClient();



//        byte[] imageData = model.getImage();
//        System.out.println(imageData.toString());
//        Files.write(Paths.get(""UUID.randomUUID().toString()+".jpg"), imageData);

        view.start(primaryStage);
    }
}
