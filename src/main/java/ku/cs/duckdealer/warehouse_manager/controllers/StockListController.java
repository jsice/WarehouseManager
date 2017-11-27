package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.*;

public class StockListController {

    private Pane mainPane;
    private ArrayList<StockedProduct> stockedProducts;
    private MainController mainCtrl;
    private String searchText;
    private Label selectedID, selectedName, selectedPrice, selectedAmount;

    private StockedProduct selectedProduct;
    private Comparator<StockedProduct> selectedComparator;
    private Map<Label, Comparator<StockedProduct>[]> comparatorMap;
    private BackgroundFill selectedBackgroundFill, unselectedBackgroundFill;
    @FXML
    private Label idLabel, nameLabel, priceLabel, qtyLabel;
    @FXML
    private GridPane innerTableGrid;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchTextfield;
    @FXML
    private Button refreshButton;


    @FXML
    private void initialize() {

        this.stockedProducts = new ArrayList<>();
        searchText = "";
        selectedBackgroundFill = new BackgroundFill(Color.CORAL, CornerRadii.EMPTY, Insets.EMPTY);
        unselectedBackgroundFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
        filterComboBox.getItems().addAll("ID", "Name");
        filterComboBox.getSelectionModel().select(0);

        comparatorMap = new HashMap<>();

        Comparator<StockedProduct> idIncComparator = (o1, o2) -> o1.getProduct().getID().compareToIgnoreCase(o2.getProduct().getID());

        Comparator<StockedProduct> idDecComparator = (o1, o2) -> -o1.getProduct().getID().compareToIgnoreCase(o2.getProduct().getID());

        Comparator<StockedProduct> nameIncComparator = (o1, o2) -> o1.getProduct().getName().compareToIgnoreCase(o2.getProduct().getName());

        Comparator<StockedProduct> nameDecComparator = (o1, o2) -> -o1.getProduct().getName().compareToIgnoreCase(o2.getProduct().getName());

        Comparator<StockedProduct> priceIncComparator = (o1, o2) -> {
            if (o1.getProduct().getPrice() > o2.getProduct().getPrice()) return 1;
            else if (o1.getProduct().getPrice() < o2.getProduct().getPrice()) return -1;
            return 0;
        };

        Comparator<StockedProduct> priceDecComparator = (o1, o2) -> {
            if (o1.getProduct().getPrice() > o2.getProduct().getPrice()) return -1;
            else if (o1.getProduct().getPrice() < o2.getProduct().getPrice()) return 1;
            return 0;
        };

        Comparator<StockedProduct> qtyIncComparator = (o1, o2) -> {
            if (o1.getQuantity() > o2.getQuantity()) return 1;
            else if (o1.getQuantity() < o2.getQuantity()) return -1;
            return 0;
        };

        Comparator<StockedProduct> qtyDecComparator = (o1, o2) -> {
            if (o1.getQuantity() > o2.getQuantity()) return -1;
            else if (o1.getQuantity() < o2.getQuantity()) return 1;
            return 0;
        };

        comparatorMap.put(nameLabel,new Comparator[] {nameIncComparator, nameDecComparator});
        comparatorMap.put(idLabel,new Comparator[] {idIncComparator, idDecComparator});
        comparatorMap.put(priceLabel,new Comparator[] {priceIncComparator, priceDecComparator});
        comparatorMap.put(qtyLabel,new Comparator[] {qtyIncComparator, qtyDecComparator});

    }
    @FXML
    private void selectComparatorForSorting(MouseEvent e) {
        Label l = (Label) e.getSource();
        String oldText = l.getText();
        char lastchar = oldText.charAt(oldText.length()-1);
        idLabel.setText("ID");
        nameLabel.setText("Name");
        priceLabel.setText("Price");
        qtyLabel.setText("Quantity");
        if (lastchar == 'v') {
            l.setText(l.getText() + " ^");
        } else {
            l.setText(l.getText() + " v");
        }
        Comparator[] comp_arr = comparatorMap.get(l);
        selectedComparator = comp_arr[0];
        comp_arr[0] = comp_arr[1];
        comp_arr[1] = selectedComparator;
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

    public void refresh(){
        this.stockedProducts.removeAll(this.stockedProducts);
        stockedProducts.addAll(this.mainCtrl.getStock().getAllProducts());
        displayFilteredProducts();
    }

    private void setSelectedLabel(Label id, Label name, Label price, Label amount) {
        if (selectedID != null) {
            selectedID.setBackground(new Background(unselectedBackgroundFill));
            selectedName.setBackground(new Background(unselectedBackgroundFill));
            selectedPrice.setBackground(new Background(unselectedBackgroundFill));
            selectedAmount.setBackground(new Background(unselectedBackgroundFill));
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

            id.setBackground(new Background(unselectedBackgroundFill));
            name.setBackground(new Background(unselectedBackgroundFill));
            price.setBackground(new Background(unselectedBackgroundFill));
            amount.setBackground(new Background(unselectedBackgroundFill));

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
