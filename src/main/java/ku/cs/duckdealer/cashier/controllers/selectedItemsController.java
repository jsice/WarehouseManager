package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;

public class selectedItemsController {

    @FXML
    private GridPane itemsList;

    private Pane mainPane;
    private ArrayList<Label> labels;
    private MainController mainCtrl;
    private ArrayList<StockedProduct> items;

    public void initialize(){
        labels = new ArrayList<Label>();
        items = new ArrayList<StockedProduct>();
    }

    public void addItem(StockedProduct p){
        items.add(p);
        showItems();
    }

    public void showItems(){
        for (int i = 0; i < items.size(); i++){
            StockedProduct p = items.get(i);
            Label productName = new Label(p.getProduct().getName());
            Label productPrice = new Label(p.getProduct().getPrice() * p.getQuantity() + "");
            Label productQuantity = new Label(p.getQuantity() + "");
            itemsList.add(productName, 0, i);
            itemsList.add(productQuantity, 1, i);
            itemsList.add(productPrice, 2, i);
        }
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) { this.mainCtrl = mainCtrl; }
}