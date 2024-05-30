package viewmodel;

import model.ClientModel;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * A factory class for creating and providing ViewModel instances.
 */
public class ViewModelFactory {
    private AddEventViewModel addEventViewModel;
    private ProfileOverviewViewModel profileOverviewViewModel;
    private RegisterUserViewModel registerUserViewModel;

    private CalendarViewModel calendarViewModel;

    private LoginUserViewModel loginUserViewModel;
    private ClientModel model;

    /**
     * Constructor for ViewModelFactory.
     * Initializes the ViewModel instances.
     *
     * @param model The ClientModel instance to be used by the ViewModels.
     * @throws IOException If an I/O error occurs during initialization.
     */
    public ViewModelFactory(ClientModel model) throws IOException {
        this.model = model;
        addEventViewModel = new AddEventViewModel(model);
        profileOverviewViewModel = new ProfileOverviewViewModel(model);
        registerUserViewModel = new RegisterUserViewModel(model);
        loginUserViewModel = new LoginUserViewModel(model);
        calendarViewModel = new CalendarViewModel(model);
    }

    /**
     * Returns the CalendarViewModel instance.
     *
     * @return The CalendarViewModel instance.
     */
    public CalendarViewModel getCalendarViewModel() {
        return calendarViewModel;
    }

    /**
     * Returns the LoginUserViewModel instance.
     *
     * @return The LoginUserViewModel instance.
     */
    public LoginUserViewModel getLoginUserViewModel() {return  loginUserViewModel;}

    /**
     * Returns the AddEventViewModel instance.
     *
     * @return The AddEventViewModel instance.
     */
    public AddEventViewModel getAddEventViewModel() {
        return addEventViewModel;
    }

    /**
     * Returns the ProfileOverviewViewModel instance.
     *
     * @return The ProfileOverviewViewModel instance.
     */
    public ProfileOverviewViewModel getProfileOverviewViewModel() {
        return profileOverviewViewModel;
    }

    /**
     * Creates and returns a new ProfileOverviewViewModel instance.
     *
     * @return A new ProfileOverviewViewModel instance.
     * @throws RemoteException If a remote method invocation error occurs.
     */
    public ProfileOverviewViewModel create() throws RemoteException{
        return new ProfileOverviewViewModel(model);
    }

    /**
     * Returns the RegisterUserViewModel instance.
     *
     * @return The RegisterUserViewModel instance.
     */
    public RegisterUserViewModel getRegisterUserViewModel() {
        return registerUserViewModel;
    }
}
