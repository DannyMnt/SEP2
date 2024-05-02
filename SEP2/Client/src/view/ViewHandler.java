package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import viewmodel.ViewModelFactory;

public class ViewHandler {
    private ViewModelFactory viewModelFactory;
    private Stage primaryStage;
    private Scene currentScene;
    private AddEventViewController addEventViewController;
    private ProfileOverviewController profileOverviewController;

    public ViewHandler(ViewModelFactory viewModelFactory){
        this.viewModelFactory = viewModelFactory;
    }

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.currentScene = new Scene(new Region());
        openView("profile");
    }

    public void openView(String id){
        Region root = null;
        switch (id){
            case "addEvent":
                root = loadAddEventView("addEventView.fxml");
                break;
            case "profile":
                root = loadProfileOverviewView("profileOverviewView.fxml");
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
        if (addEventViewController == null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                addEventViewController = loader.getController();
                addEventViewController
                        .init(this, viewModelFactory.getAddEventViewModel(), root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            addEventViewController.reset();
        }
        return addEventViewController.getRoot();
    }

    private Region loadProfileOverviewView(String fxmlFile) {
        if (profileOverviewController == null)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(fxmlFile));
                Region root = loader.load();
                profileOverviewController = loader.getController();
                profileOverviewController
                        .init(this, viewModelFactory.getProfileOverviewViewModel(), root);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            profileOverviewController.reset();
        }
        return profileOverviewController.getRoot();
    }
}
