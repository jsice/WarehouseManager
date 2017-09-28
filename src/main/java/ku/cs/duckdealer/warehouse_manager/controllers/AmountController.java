package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;


public class AmountController {
    private int amount = 1;
    public boolean cancel = true;
    private String by;
    @FXML
    private TextField amountField;
    @FXML
    private RadioButton sold, userError, damaged, expired;
    @FXML
    private void initialize() {
        this.amountField.setText("1");
        if (sold!=null ){
            ToggleGroup group=new ToggleGroup();
            sold.setToggleGroup(group);
            userError.setToggleGroup(group);
            damaged.setToggleGroup(group);
            expired.setToggleGroup(group);
        }
    }

    public void btnOk() {
        String amount = amountField.getText();
        this.amount = Integer.parseInt(amount);
        btnCancel();
        cancel = false;
    }

    public void btnCancel() {
        cancel = true;
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
        }else if (text.equals("User error")){
            by = "User error";
        }

    }

    public int getAmount() {
        return amount;
    }

    public String getBy() { return by; }

}
