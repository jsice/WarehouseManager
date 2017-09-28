package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StockListController {

    private Pane mainPane;
    private ArrayList<StockedProduct> stockedProducts;
    private ArrayList<Label> labels;
    private MainController mainCtrl;
    private String searchText;
    private Label selectedID, selectedName, selectedPrice, selectedAmount;

    private StockedProduct selectedProduct;
    private Comparator<StockedProduct> selectedComparator,
            idIncComparator, idDecComparator,
            nameIncComparator, nameDecComparator,
            priceIncComparator, priceDecComparator,
            qtyIncComparator, qtyDecComparator;
    private BackgroundFill selectedBackgroundFill;
    @FXML
    private Label idLabel, nameLabel, priceLabel, qtyLabel;
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
        filterComboBox.getItems().addAll("ID", "Name");
        filterComboBox.getSelectionModel().select(0);

        idIncComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                return o1.getProduct().getID().compareToIgnoreCase(o2.getProduct().getID());
            }
        };

        idDecComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                return -o1.getProduct().getID().compareToIgnoreCase(o2.getProduct().getID());
            }
        };

        nameIncComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                return o1.getProduct().getName().compareToIgnoreCase(o2.getProduct().getName());
            }
        };

        nameDecComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                return -o1.getProduct().getName().compareToIgnoreCase(o2.getProduct().getName());
            }
        };

        priceIncComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                if (o1.getProduct().getPrice() > o2.getProduct().getPrice()) return 1;
                else if (o1.getProduct().getPrice() < o2.getProduct().getPrice()) return -1;
                return 0;
            }
        };

        priceDecComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                if (o1.getProduct().getPrice() > o2.getProduct().getPrice()) return -1;
                else if (o1.getProduct().getPrice() < o2.getProduct().getPrice()) return 1;
                return 0;
            }
        };

        qtyIncComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                if (o1.getQuantity() > o2.getQuantity()) return 1;
                else if (o1.getQuantity() < o2.getQuantity()) return -1;
                return 0;
            }
        };

        qtyDecComparator = new Comparator<StockedProduct>() {
            @Override
            public int compare(StockedProduct o1, StockedProduct o2) {
                if (o1.getQuantity() > o2.getQuantity()) return -1;
                else if (o1.getQuantity() < o2.getQuantity()) return 1;
                return 0;
            }
        };

    }
    @FXML
    private void selectComparatorForSorting(MouseEvent e) {
        Label l = (Label) e.getSource();
        idLabel.setText("ID");
        nameLabel.setText("Name");
        priceLabel.setText("Price");
        qtyLabel.setText("Quantity");
        if (l == idLabel) {
            if (selectedComparator == idIncComparator) {
                selectedComparator = idDecComparator;
                l.setText("ID ^");
            }
            else {
                selectedComparator = idIncComparator;
                l.setText("ID v");
            }
        } else if (l == nameLabel) {
            if (selectedComparator == nameIncComparator) {
                selectedComparator = nameDecComparator;
                l.setText("Name ^");
            }
            else {
                selectedComparator = nameIncComparator;
                l.setText("Name v");
            }
        } else if (l == priceLabel) {
            if (selectedComparator == priceIncComparator) {
                selectedComparator = priceDecComparator;
                l.setText("Price ^");
            }
            else {
                selectedComparator = priceIncComparator;
                l.setText("Price v");
            }
        } else if (l == qtyLabel) {
            if (selectedComparator == qtyIncComparator) {
                selectedComparator = qtyDecComparator;
                l.setText("Quantity ^");
            }
            else {
                selectedComparator = qtyIncComparator;
                l.setText("Quantity v");
            }
        }
        showFilteredProducts();
    }

    public void createNewProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            this.mainCtrl.login();
        }
        if (!AuthenticationService.NOT_LOGGED_IN && AuthenticationService.LOGGED_IN_AS_OWNER){
            mainCtrl.createProduct();
        }
    }

    private void sortProducts() {
        if (this.selectedComparator != null)
            Collections.sort(this.stockedProducts, this.selectedComparator);
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
                String productInfo = p.getProduct().getID();
                if (this.filterComboBox.getSelectionModel().getSelectedIndex() == 1) productInfo = p.getProduct().getName();
                if (productInfo.contains(searchText)) {
                    stockedProducts.add(p);
                }
            }
        }
    }

    private void setSelectedLabel(Label id, Label name, Label price, Label amount) {
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
            id.setAlignment(Pos.CENTER);
            name.setPrefHeight(38);
            name.setPrefWidth(123);
            name.setAlignment(Pos.CENTER);
            price.setPrefHeight(38);
            price.setPrefWidth(123);
            price.setAlignment(Pos.CENTER);
            amount.setPrefHeight(38);
            amount.setPrefWidth(122);
            amount.setAlignment(Pos.CENTER);

            if (p == selectedProduct) setSelectedLabel(id, name, price, amount);

            EventHandler<MouseEvent> productLabelEventHandler = new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    mainCtrl.showProductDetail(p);
                    setSelectedLabel(id, name, price, amount);
                    selectedProduct = p;
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
        showFilteredProducts();
    }

    public void showFilteredProducts() {
        this.filterStockedProducts();
        this.sortProducts();
        this.displayFilteredProducts();
    }

    public void showAllProducts() {
        searchText = null;
        this.searchTextfield.setText("");
        showFilteredProducts();
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

    public void setSelectedProduct(StockedProduct selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
}
