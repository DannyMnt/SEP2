package viewmodel;

import model.ClientModel;

import java.rmi.RemoteException;

public class ViewModelFactory {
    private AddEventViewModel addEventViewModel;
    private ProfileOverviewViewModel profileOverviewViewModel;
    private RegisterUserViewModel registerUserViewModel;

    private LoginUserViewModel loginUserViewModel;

    public ViewModelFactory(ClientModel model) throws RemoteException {
        addEventViewModel = new AddEventViewModel(model);
        profileOverviewViewModel = new ProfileOverviewViewModel(model);
        registerUserViewModel = new RegisterUserViewModel(model);
        loginUserViewModel = new LoginUserViewModel(model);
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
