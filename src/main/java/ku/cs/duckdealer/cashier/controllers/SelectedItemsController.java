package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ku.cs.duckdealer.models.Product;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;

public class SelectedItemsController {

    @FXML
    private GridPane itemsList;

    private Pane mainPane;
    private ArrayList<Label> labels = new ArrayList<>();
    private MainController mainCtrl;

    public void addItem(Product p, int selectedAmount){
        mainCtrl.getRegister().enterItem(p.getID(), selectedAmount);
        Label productName = new Label(p.getName());
        Label productPrice = new Label(p.getPrice() * selectedAmount + "");
        Label productQuantity = new Label(selectedAmount + "");
        labels.add(productName);
        labels.add(productPrice);
        labels.add(productQuantity);
        int row = (labels.size() + 1) / 3;
        itemsList.add(productName, 0, row);
        itemsList.add(productPrice, 2, row);
        itemsList.add(productQuantity, 1, row);
    }

    public void showItems(){

    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) { this.mainCtrl = mainCtrl; }
}