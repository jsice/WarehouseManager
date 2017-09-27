package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ku.cs.duckdealer.warehouse_manager.models.Product;
import ku.cs.duckdealer.warehouse_manager.models.StockedProduct;

import java.util.ArrayList;

public class StockListController {

    private Pane mainPane;
    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<Label> labels;
    private MainController mainCtrl;

    private String searchText;

    private Label selectedID, selectedName, selectedPrice, selectedAmount;

    private BackgroundFill selectedBackgroundFill;

    @FXML
    private GridPane innerTableGrid;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchTextfield;

    @FXML
    private void initialize() {
        this.stockedProducts = new ArrayList<StockedProduct>();
        labels = new ArrayList<Label>();
        searchText = "";
        selectedBackgroundFill = new BackgroundFill(Color.CORAL, CornerRadii.EMPTY, Insets.EMPTY);
    }

    public void createNewProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            this.mainCtrl.login();
        }
        if (!AuthenticationService.NOT_LOGGED_IN && AuthenticationService.LOGGED_IN_AS_OWNER){
            mainCtrl.createProduct();
        }

    }

    private void clearGrid() {
        selectedID = null;
        selectedName = null;
        selectedPrice = null;
        selectedAmount = null;
        innerTableGrid.getChildren().clear();
        innerTableGrid.setPrefHeight(400);
        this.innerTableGrid.setGridLinesVisible(false);
        this.innerTableGrid.setGridLinesVisible(true);

    }

    private void filterStockedProducts() {
        this.stockedProducts.removeAll(this.stockedProducts);
        if (searchText == null) {
            stockedProducts.addAll(this.mainCtrl.getStock().getAllProducts());
        } else {
            for (StockedProduct p: this.mainCtrl.getStock().getAllProducts()) {
                if (p.getProduct().getID().contains(searchText)) {
                    stockedProducts.add(p);
                }
            }
        }
    }

    private void displayFilteredProducts() {
        clearGrid();
        int row = 0;
        for (final StockedProduct p: this.stockedProducts) {
            if (row >= 10) {
                this.innerTableGrid.setPrefHeight(this.innerTableGrid.getPrefHeight() + 40);
                this.innerTableGrid.addRow(row);
            }
            final Label id = new Label(p.getProduct().getID()+"");
            final Label name = new Label(p.getProduct().getName());
            final Label price = new Label(p.getProduct().getPrice()+"");
            final Label amount = new Label(p.getQuantity()+"");
            id.setPrefHeight(38);
            id.setPrefWidth(122);
            name.setPrefHeight(38);
            name.setPrefWidth(123);
            price.setPrefHeight(38);
            price.setPrefWidth(123);
            amount.setPrefHeight(38);
            amount.setPrefWidth(122);

            EventHandler<MouseEvent> productLabelEventHandler = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    mainCtrl.showProductDetail(p);
                    if (selectedID != null) {
                        selectedID.setBackground(null);
                        selectedName.setBackground(null);
                        selectedPrice.setBackground(null);
                        selectedAmount.setBackground(null);
                    }
                    selectedID = id;
                    selectedName = name;
                    selectedPrice = price;
                    selectedAmount = amount;
                    selectedID.setBackground(new Background(selectedBackgroundFill));
                    selectedName.setBackground(new Background(selectedBackgroundFill));
                    selectedPrice.setBackground(new Background(selectedBackgroundFill));
                    selectedAmount.setBackground(new Background(selectedBackgroundFill));
                }
            };

            id.setOnMouseClicked(productLabelEventHandler);
            name.setOnMouseClicked(productLabelEventHandler);
            price.setOnMouseClicked(productLabelEventHandler);
            amount.setOnMouseClicked(productLabelEventHandler);

            this.labels.add(id);
            this.labels.add(name);
            this.labels.add(price);
            this.labels.add(amount);
            this.innerTableGrid.add(id, 0, row);
            this.innerTableGrid.add(name, 1, row);
            this.innerTableGrid.add(price, 2, row);
            this.innerTableGrid.add(amount, 3, row);
            row++;
        }
    }

    @FXML
    private void showProducts() {
        searchText = searchTextfield.getText();
        this.filterStockedProducts();
        this.displayFilteredProducts();
    }

    public void showAllProducts() {
        searchText = null;
        this.searchTextfield.setText("");
        this.filterStockedProducts();
        this.displayFilteredProducts();

        if (stockedProducts.size() > 0) {
            selectedID = labels.get(0);
            selectedName = labels.get(1);
            selectedPrice = labels.get(2);
            selectedAmount = labels.get(3);
            selectedID.setBackground(new Background(selectedBackgroundFill));
            selectedName.setBackground(new Background(selectedBackgroundFill));
            selectedPrice.setBackground(new Background(selectedBackgroundFill));
            selectedAmount.setBackground(new Background(selectedBackgroundFill));
        }
    }

    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}
