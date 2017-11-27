package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import ku.cs.duckdealer.models.ProductMovement;
import ku.cs.duckdealer.models.StockedProduct;

import java.util.GregorianCalendar;


public class NewProductController {

    @FXML
    private TextField nameField, priceField;
    @FXML
    private Label remainAmountLabel, idLabel;

    private StockedProduct stockedProduct;
    private MainController mainCtrl;
    private BorderPane mainPane;

    @FXML
    private void initialize() {
        this.idLabel.setText("undefined");
        this.remainAmountLabel.setText("0");
        this.nameField.setText("");
        this.priceField.setText("");
    }

    public void createProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            mainCtrl.login();
        }
        if (!AuthenticationService.NOT_LOGGED_IN){
            if (!nameField.getText().equals("") && !priceField.getText().equals("")) {
                stockedProduct = new StockedProduct(nameField.getText(), Double.parseDouble(priceField.getText()));
                mainCtrl.getStock().newProduct(stockedProduct);
                mainCtrl.getProductService().add(stockedProduct);
                ProductMovement productMovement = new ProductMovement(stockedProduct.getProduct(), new GregorianCalendar(), false, 0, "Create");
                mainCtrl.getProductMovementService().add(productMovement);
                initialize();
                mainCtrl.showProductDetail(stockedProduct);
                mainCtrl.showAllProducts();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Failed to create product");
                alert.setContentText("Please fill all product's detail");
                alert.show();
            }

        }
    }

    public void cancel(){initialize();}

    public BorderPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
}