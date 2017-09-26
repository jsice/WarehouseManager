package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;


public class AmountController {
    private int increaseAmount, decreaseAmount;
    @FXML
    private TextField amountIncreaseField, amountDecreaseField;
    @FXML
    private void initialize() {
      this.amountIncreaseField.setText("1");
      this.amountDecreaseField.setText("1");
    }

    public void btnSubmitIncrease() {
        String amount = amountIncreaseField.getText();
        increaseAmount = Integer.parseInt(amount);

//        if (!AuthenticationService.login(username, password)) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setHeaderText("Login Error");
//            alert.setContentText("Username or Password wrong");
//            alert.showAndWait();
//            return;
//        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Increase Successful");
//        alert.setContentText("Welcome");
        alert.showAndWait();
        btnCancel();
    }

    public void btnCancel() {
        amountIncreaseField.getScene().getWindow().hide();
    }

    public int getIncreaseAmount() {
        return increaseAmount;
    }

    public void btnSubmitDecrease(){
        String amount = amountIncreaseField.getText();
        decreaseAmount = Integer.parseInt(amount);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Decrease Successful");
        alert.showAndWait();
        btnCancel();

    }
    public void btnBy(ActionEvent event){
        String text = ((RadioButton)event.getSource()).getText();

    }

    public int getDecreaseAmount() { return decreaseAmount; }
}
