package viewmodel;

import model.ClientModel;

public class ViewModelFactory {
    private AddEventViewModel addEventViewModel;

    public ViewModelFactory(ClientModel model){
        addEventViewModel = new AddEventViewModel(model);
    }

    public AddEventViewModel getAddEventViewModel() {
        return addEventViewModel;
    }
}
