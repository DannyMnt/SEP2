package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import viewmodel.ViewModelFactory;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.rmi.RemoteException;

public class ViewHandler {
    private ViewModelFactory viewModelFactory;
    private Stage primaryStage;
    private Scene currentScene;
    private AddEventViewController addEventViewController;
    private ProfileOverviewController profileOverviewController;
    private RegisterUserViewController registerUserViewController;

    private CalendarViewController calendarViewController;

    private LoginUserViewController loginUserViewController;

    public ViewHandler(ViewModelFactory viewModelFactory){
        this.viewModelFactory = viewModelFactory;
    }

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.currentScene = new Scene(new Region());
        openView("login");
    }



    public void openView(String id){
        Region root = null;
        switch (id){
            case "addEvent":
                root = loadAddEventView("addEventView.fxml");
                break;
            case "profile":
                root = loadProfileOverviewView("profileOverviewView.fxml");
                break;
            case "register":
                root = loadRegisterUserView("registerUserView.fxml");
                break;
            case "login":
                root = loadLoginUserView("loginUserView.fxml");
                break;
            case "calendar":
                root = loadCalendarView("calendarView.fxml");
                break;
//            case "addEvent2":
//                root = openAddEventViewNewWindow();
//                break;
        }
        currentScene.setRoot(root);

        String title = "";
        if(root.getUserData() != null)
            title += root.getUserData();
        primaryStage.setTitle(title);
        primaryStage.setScene(currentScene);
        primaryStage.setWidth(root.getPrefWidth());
        primaryStage.setHeight(root.getPrefHeight());
        primaryStage.show();
    }

    private Region loadAddEventView(String fxmlFile) {
        try {
            if (addEventViewController == null) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                addEventViewController = loader.getController();
                addEventViewController.init(this, viewModelFactory.getAddEventViewModel(), root);
            } else {
                addEventViewController.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addEventViewController.getRoot();
    }

    private Region loadCalendarView(String fxmlFile) {
        if (calendarViewController == null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                calendarViewController = loader.getController();
                calendarViewController
                        .init(this, viewModelFactory.getCalendarViewModel(), root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            calendarViewController.reset();
        }
        return calendarViewController.getRoot();
    }


    private Region loadLoginUserView(String fxmlFile) {
        if (loginUserViewController == null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                loginUserViewController = loader.getController();
                loginUserViewController
                        .init(this, viewModelFactory.getLoginUserViewModel(), root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            loginUserViewController.reset();
        }
        return loginUserViewController.getRoot();
    }

    private Region loadProfileOverviewView(String fxmlFile) {
        if (profileOverviewController == null)
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                profileOverviewController = loader.getController();
                profileOverviewController
                        .init(this, viewModelFactory.getProfileOverviewViewModel(), root);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else{
            profileOverviewController.reset();
        }
        return profileOverviewController.getRoot();
    }

    private Region loadRegisterUserView(String fxmlFile) {
        if (registerUserViewController == null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                registerUserViewController = loader.getController();
                registerUserViewController
                        .init(this, viewModelFactory.getRegisterUserViewModel(), root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            registerUserViewController.reset();
        }
        return registerUserViewController.getRoot();
    }

//    private Region openAddEventViewNewWindow(){
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("addEventView.fxml"));
//            Region root = loader.load();
//
//            Stage stage = new Stage();
//            stage.setTitle("Add event");
//            stage.setScene(new Scene(root));
//            stage.show();
//
//            addEventViewController = loader.getController();
//            addEventViewController.init(this, viewModelFactory.getAddEventViewModel(), root);
//        }
//        catch(IOException e){
//            e.printStackTrace();
//        }
//        return addEventViewController.getRoot();
//    }
    public void showEvent() throws IOException {
        Stage eventStage = calendarViewController.showOverlay(primaryStage);
        currentScene.getRoot().setOnMouseClicked(e -> {
            eventStage.close(); // Close the event stage when mouse is clicked
        });
//        primaryStage.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
//            if (eventStage != null && eventStage.isShowing()) {
//                eventStage.close();
//            }
//        });
    }
}
