package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class AmountController {
    private int increaseAmount, decreaseAmount;
    private String by;
    @FXML
    private TextField amountIncreaseField, amountDecreaseField;
    @FXML
    private void initialize() {
      this.amountIncreaseField.setText("1");
      this.amountDecreaseField.setText("1");
    }

    public void btnOkIncrease() {
        String amount = amountIncreaseField.getText();
        increaseAmount = Integer.parseInt(amount);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Increase Successful");
        alert.showAndWait();
        btnCancel();
    }

    public void btnCancel() {
        amountIncreaseField.getScene().getWindow().hide();
    }

    public int getIncreaseAmount() {
        return increaseAmount;
    }

    public void btnOkDecrease(){
        String amount = amountIncreaseField.getText();
        decreaseAmount = Integer.parseInt(amount);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Decrease Successful");
        alert.showAndWait();
        btnCancel();

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

    public int getDecreaseAmount() { return decreaseAmount; }
}
