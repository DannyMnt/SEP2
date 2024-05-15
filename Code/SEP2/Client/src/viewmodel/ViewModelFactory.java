package viewmodel;

import model.ClientModel;

import java.io.IOException;
import java.rmi.RemoteException;

public class ViewModelFactory {
    private AddEventViewModel addEventViewModel;
    private ProfileOverviewViewModel profileOverviewViewModel;
    private RegisterUserViewModel registerUserViewModel;

    private CalendarViewModel calendarViewModel;

    private LoginUserViewModel loginUserViewModel;



    public ViewModelFactory(ClientModel model) throws IOException {
        addEventViewModel = new AddEventViewModel(model);
        profileOverviewViewModel = new ProfileOverviewViewModel(model);
        registerUserViewModel = new RegisterUserViewModel(model);
        loginUserViewModel = new LoginUserViewModel(model);
        calendarViewModel = new CalendarViewModel(model);
    }

    public CalendarViewModel getCalendarViewModel() {
        return calendarViewModel;
    }

    public LoginUserViewModel getLoginUserViewModel() {return  loginUserViewModel;}

    public AddEventViewModel getAddEventViewModel() {
        return addEventViewModel;
    }

    public ProfileOverviewViewModel getProfileOverviewViewModel() {
        return profileOverviewViewModel;
    }

    public RegisterUserViewModel getRegisterUserViewModel() {
        return registerUserViewModel;
    }
}
