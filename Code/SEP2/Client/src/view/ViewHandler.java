package view;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Event;
import viewmodel.ViewModelFactory;

import java.io.IOException;

/**
 * The ViewHandler class manages the loading and switching of views within the application.
 * It serves as a mediator between the views and their corresponding controllers.
 */
public class ViewHandler {
    private ViewModelFactory viewModelFactory;
    private Stage primaryStage;
    private Scene currentScene;
    private AddEventViewController addEventViewController;
    private ProfileOverviewController profileOverviewController;
    private RegisterUserViewController registerUserViewController;

    private CalendarViewController calendarViewController;

    private LoginUserViewController loginUserViewController;

    /**
     * Constructs a ViewHandler with the provided ViewModelFactory.
     * @param viewModelFactory The ViewModelFactory to use for creating view models.
     */
    public ViewHandler(ViewModelFactory viewModelFactory){
        this.viewModelFactory = viewModelFactory;
    }

    /**
     * Initializes the primary stage and sets the initial scene to the login view.
     * @param primaryStage The primary stage of the application.
     */
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.currentScene = new Scene(new Region());
        openView("login");
    }

    /**
     * Opens the specified view identified by the given ID.
     * @param id The ID of the view to open.
     */
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

    /**
     * Loads the Add Event view from the specified FXML file.
     * If the view has not been loaded before, it initializes the view controller
     * and sets up the necessary dependencies. Otherwise, it resets the view controller.
     *
     * @param fxmlFile The path to the FXML file of the Add Event view.
     * @return The root node of the loaded Add Event view.
     */
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
            return addEventViewController.getRoot();
        } catch (IOException e) {
            e.printStackTrace();

            return new Region();
        }
    }

    /**
     * Loads the Calendar view from the specified FXML file.
     * If the view has not been loaded before, it initializes the view controller
     * and sets up the necessary dependencies. Otherwise, it resets the view controller.
     *
     * @param fxmlFile The path to the FXML file of the Calendar view.
     * @return The root node of the loaded Calendar view.
     */
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


    /**
     * Loads the Login User view from the specified FXML file.
     * If the view has not been loaded before, it initializes the view controller
     * and sets up the necessary dependencies. Otherwise, it resets the view controller.
     *
     * @param fxmlFile The path to the FXML file of the Login User view.
     * @return The root node of the loaded Login User view.
     */
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

    /**
     * Loads the Profile Overview view from the specified FXML file.
     * If the view has not been loaded before, it initializes the view controller
     * and sets up the necessary dependencies. Otherwise, it resets the view controller.
     *
     * @param fxmlFile The path to the FXML file of the Profile Overview view.
     * @return The root node of the loaded Profile Overview view.
     */
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


    /**
     * Loads the Register User view from the specified FXML file.
     * If the view has not been loaded before, it initializes the view controller
     * and sets up the necessary dependencies. Otherwise, it resets the view controller.
     *
     * @param fxmlFile The path to the FXML file of the Register User view.
     * @return The root node of the loaded Register User view.
     */
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


    /**
     * Loads the event view for the specified event.
     * @param event The event for which to load the view.
     * @throws IOException If an I/O error occurs during loading.
     */
    public void loadEventView(Event event) throws IOException {
        Stage eventStage = calendarViewController.showOverlay(primaryStage, event);

        EventHandler<MouseEvent> clickListener = mouseEvent -> eventStage.close();
        currentScene.addEventFilter(MouseEvent.MOUSE_CLICKED, clickListener);

    }


}
