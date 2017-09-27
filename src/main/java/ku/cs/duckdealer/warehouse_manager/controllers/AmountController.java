package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class AmountController {
    private int amount;
    private String by;
    @FXML
    private TextField amountField;
    @FXML
    private void initialize() {
      this.amountField.setText("1");
    }

    public void btnOk() {
        String amount = amountField.getText();
        this.amount = Integer.parseInt(amount);
        btnCancel();
    }

    public void btnCancel() {
        amountField.getScene().getWindow().hide();
    }

    public void btnBy(ActionEvent event){
        String text = ((RadioButton)event.getSource()).getText();
        if(text.equals("Sold")){
            by = "Sold";
        }else if (text.equals("Damaged")){
            by = "Damaged";
        }else if (text.equals("Expired")){
            by = "Expired";
        }else if (text.equals("Tester")){
            by = "Tester";
        }

    }

    public int getAmount() {
        return amount;
    }

    public String getBy() {
        return by;
    }
}
