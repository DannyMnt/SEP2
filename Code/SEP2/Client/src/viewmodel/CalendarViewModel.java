package viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mediator.LoginPackage;
import model.ClientModel;
import model.Event;
import view.MonthDayEntryViewController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CalendarViewModel {
    private ClientModel model;

    private ListProperty<Event> events;

    private SimpleObjectProperty<GridPane> gridPane = new SimpleObjectProperty<>();

    private SimpleStringProperty monthLabel = new SimpleStringProperty();


    public SimpleObjectProperty<GridPane> getGridPaneProperty() {
        return gridPane;
    }


    public SimpleStringProperty getMonthLabelProperty() {
        return monthLabel;
    }

    public CalendarViewModel(ClientModel model){

        this.model = model;


        try
        {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));


        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Event> getEvents(LocalDate startDate, LocalDate endDate)
    {
        return events.get();
    }



    public ListProperty<Event> eventsProperty()
    {
        return events;
    }
}
