package ku.cs.duckdealer.cashier.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ku.cs.duckdealer.models.StockedProduct;

public class SelectItemPopUpController {


    private int amount ;
    private GridPane mainPane;
    private MainController mainCtrl ;

    @FXML
    private Label prodIdLabel, prodNameLabel , maxAmountLabel ;
    @FXML
    private TextField amountField ;

    @FXML
    private void handleBtnOk() {
        mainCtrl.getCashierListCtrl().setCancelStatus(true);
        int maxAmount = Integer.parseInt(maxAmountLabel.getText());


        if (maxAmount != 0 ) {
            if ("".equals(amountField.getText())) { amount = Integer.parseInt(amountField.getPromptText()); }
            else {
                int enteredAmount = Integer.parseInt(amountField.getText());
                if (enteredAmount > maxAmount) { amount = maxAmount; }
                else if (enteredAmount < 0){}//alert amount less than 0
                else { amount = enteredAmount ; }
            }
        }
        else {
            //alert show cannot add this item -> amount = 0
        }
        amountField.getScene().getWindow().hide();
    }

    @FXML
    private void handleBtnCancel(){
        mainCtrl.getCashierListCtrl().setCancelStatus(false);
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

    public void setMainCtrl(MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

}