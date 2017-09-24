package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ku.cs.duckdealer.warehouse_manager.models.Product;

public class ProductDetailController {

    @FXML
    private TextField productNameField, productNumberField, productPriceField;
    @FXML
    private Label remainAmountLabel;

    private Product product;
    private String user;

    public void updateAmount(){

    }

    public void confirm(){

        back();
    }

    public void back(){
        productNameField.getScene().getWindow().hide();
    }

    public void setEditable(){
        if (user.equals("Stock")){
            productNumberField.setDisable(true);
            productNameField.setDisable(true);
            productPriceField.setDisable(true);
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;

        productNumberField.setText(product.getNumber() + "");
        productNameField.setText(product.getName());
        productPriceField.setText(product.getPrice() + "");
        remainAmountLabel.setText(product.getAmount() + "");
    }

    public void setUser(String user) {
        this.user = user;
        setEditable();
    }
}
