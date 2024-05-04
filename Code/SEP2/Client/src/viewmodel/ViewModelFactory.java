package viewmodel;

import model.ClientModel;

public class ViewModelFactory {
    private AddEventViewModel addEventViewModel;
    private ProfileOverviewViewModel profileOverviewViewModel;

    public ViewModelFactory(ClientModel model){
        addEventViewModel = new AddEventViewModel(model);
        profileOverviewViewModel = new ProfileOverviewViewModel(model);
    }

    public AddEventViewModel getAddEventViewModel() {
        return addEventViewModel;
    }

    public ProfileOverviewViewModel getProfileOverviewViewModel() {
        return profileOverviewViewModel;
    }
}
