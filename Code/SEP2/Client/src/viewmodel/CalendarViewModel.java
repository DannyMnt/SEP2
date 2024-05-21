package viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mediator.LoginPackage;
import model.ClientModel;
import model.Event;
import model.User;
import view.MonthDayEntryViewController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class CalendarViewModel {
    private ClientModel model;


    private ListProperty<Event> events;

    private SimpleObjectProperty<GridPane> gridPane = new SimpleObjectProperty<>();

    private SimpleStringProperty monthLabel = new SimpleStringProperty();
    private SimpleObjectProperty<Image> imageProperty;


    public SimpleObjectProperty<GridPane> getGridPaneProperty() {
        return gridPane;
    }


    public SimpleStringProperty getMonthLabelProperty() {
        return monthLabel;
    }

    public CalendarViewModel(ClientModel model) {

        this.model = model;
        this.imageProperty = new SimpleObjectProperty<>();

        try {

            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));


        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Event> getEvents(LocalDate startDate, LocalDate endDate) {
        return events.get();
    }


    public void reset() {
        try {
//            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));
            User user = model.getUserById(ViewState.getInstance().getUserID());
            imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public Event getEvent(UUID eventId) throws RemoteException {
        return model.getEvent(eventId);
    }

    public SimpleObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }


    public ListProperty<Event> eventsProperty() {
        return events;
    }
}
