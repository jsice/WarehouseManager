package ku.cs.duckdealer.cashier.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class selectedItemsController {

    private Pane mainPane;
//    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<Label> labels;
    private MainController mainCtrl;


    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) { this.mainCtrl = mainCtrl; }
}