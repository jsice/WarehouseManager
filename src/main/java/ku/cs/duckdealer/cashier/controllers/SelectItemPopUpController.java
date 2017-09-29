package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ku.cs.duckdealer.models.StockedProduct;

public class SelectItemPopUpController {


    private int amount ;

    private GridPane mainPane;

    @FXML
    private Label prodIdLabel, prodNameLabel , maxAmountLabel ;
    @FXML
    private TextField amountField ;

    @FXML
    private void handleBtnOk() {
        amount = Integer.parseInt(amountField.getText()) ;
        if (amount > Integer.parseInt(maxAmountLabel.getText())){
            amount = Integer.parseInt(maxAmountLabel.getText()) ;
        }
        amountField.getScene().getWindow().hide();
    }

    @FXML
    private void handleBtnCancel(){
        amountField.getScene().getWindow().hide();
    }

    public int getAmount (){
        return amount ;
    }

    public void setAllLabelText(StockedProduct selectedProduct) {
        prodIdLabel.setText(selectedProduct.getProduct().getID());
        prodNameLabel.setText(selectedProduct.getProduct().getName());
        maxAmountLabel.setText(selectedProduct.getQuantity()+"" );
        amountField.setPromptText("1"); // 1 by default
    }


    public GridPane getMainPane() {
        return mainPane;
    }

    public void setMainPane(GridPane mainPane) {
        this.mainPane = mainPane;
    }
}