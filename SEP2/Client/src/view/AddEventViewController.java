package view;

import javafx.scene.layout.Region;
import viewmodel.AddEventViewModel;

public class AddEventViewController {
    private Region root;
    private ViewHandler viewHandler;
    private AddEventViewModel addEventViewModel;

    public AddEventViewController(){

    }
    public void init(ViewHandler viewHandler, AddEventViewModel addEventViewModel, Region root){
        this.viewHandler = viewHandler;
        this.addEventViewModel = addEventViewModel;
        this.root = root;
    }

    public void reset(){
        System.out.println("wdawdada");

    }

    public Region getRoot() {
        return root;
    }
}
