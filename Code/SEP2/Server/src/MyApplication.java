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
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        ServerModel model = new ModelManager();
        RmiServer server = new RmiServer(model);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            DatabaseSingleton.getInstance().disconnect();
        }));
//        System.out.println(PasswordUtility.hashPasswordWithSalt("password1"));
//        System.out.println(PasswordUtility.hashPasswordWithSalt("password2"));
//        System.out.println(PasswordUtility.hashPasswordWithSalt("password3"));
//        System.out.println(PasswordUtility.hashPasswordWithSalt("password4"));
//        System.out.println(PasswordUtility.hashPasswordWithSalt("password5"));
//        ViewModelFactory viewModelFactory = new ViewModelFactory(model);
//        ViewHandler view = new ViewHandler(viewModelFactory);
//        Client client = new Client();
//
//        view.start(primaryStage);
    }
}
