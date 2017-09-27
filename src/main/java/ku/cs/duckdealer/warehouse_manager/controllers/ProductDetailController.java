package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.duckdealer.warehouse_manager.models.StockedProduct;

import java.io.IOException;

public class ProductDetailController {

    @FXML
    private TextField nameField, priceField;
    @FXML
    private Label remainAmountLabel, idLabel, incSpaceLabel, decSpaceLabel;
    @FXML
    private Button btnIncrease, btnDecrease, btnEdit, btnOk, btnCancel;
    @FXML
    private FlowPane amountArea;

    private StockedProduct stockedProduct;
    private MainController mainCtrl;
    private boolean isEditing = false;
    private BorderPane mainPane;
    private Button source;

    @FXML
    private void initialize() {

        this.nameField.setDisable(true);
        this.priceField.setDisable(true);

        this.amountArea.getChildren().remove(this.btnDecrease);
        this.amountArea.getChildren().remove(this.decSpaceLabel);
        this.amountArea.getChildren().remove(this.remainAmountLabel);
        this.amountArea.getChildren().remove(this.incSpaceLabel);
        this.amountArea.getChildren().remove(this.btnIncrease);
        this.btnOk.setVisible(false);
        this.btnCancel.setVisible(false);
        this.btnEdit.setVisible(false);

        this.amountArea.getChildren().add(this.remainAmountLabel);
    }

    public void setup(StockedProduct p) {
        this.stockedProduct = p;
        this.idLabel.setText(this.stockedProduct.getProduct().getID());
        this.nameField.setEditable(false);
        this.nameField.setText(this.stockedProduct.getProduct().getName());
        this.priceField.setEditable(false);
        this.priceField.setText(this.stockedProduct.getProduct().getPrice()+"");
        this.remainAmountLabel.setText(this.stockedProduct.getQuantity()+"");

        initialize();
        this.btnEdit.setVisible(true);
    }


    public void toggleCreateMode(){
        this.nameField.setDisable(false);
        this.priceField.setDisable(false);

        this.nameField.setEditable(true);
        this.priceField.setEditable(true);
        this.btnEdit.setVisible(false);
        this.btnOk.setVisible(true);
        this.btnCancel.setVisible(true);

    }
    public void createProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            mainCtrl.login();
        }
        if (!AuthenticationService.NOT_LOGGED_IN){
            stockedProduct = new StockedProduct(nameField.getText(), Integer.parseInt(priceField.getText()));
            mainCtrl.getStock().newProduct(stockedProduct);
            initialize();
            mainCtrl.showAllProducts();
        }
    }
    public void cancel(){}

    public void updateAmount(ActionEvent event){
        if(stockedProduct != null){
            String text = ((Button)event.getSource()).getText();
            if(text.equals("+")){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/increaseProductPopUp.fxml"));
                stage.initModality(Modality.APPLICATION_MODAL);
                try {
                    stage.setScene(new Scene((Parent) loader.load()));
                    AmountController amountController = loader.getController();
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stockedProduct.setQuantity(this.mainCtrl.getAmountController().getIncreaseAmount());

            }else if (text.equals("-")){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/decreaseProductPopUp.fxml"));
                stage.initModality(Modality.APPLICATION_MODAL);
                try {
                    stage.setScene(new Scene((Parent) loader.load()));
                    AmountController amountController = loader.getController();
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stockedProduct.setQuantity(this.mainCtrl.getAmountController().getDecreaseAmount());
            }
        }

    }

    public void toggleEditMode() {
        if (AuthenticationService.NOT_LOGGED_IN) {
            this.mainCtrl.login();
            if (AuthenticationService.NOT_LOGGED_IN) {
                return;
            }
        }

        if (!isEditing) {
            this.btnEdit.setText("done");
            this.amountArea.getChildren().remove(this.remainAmountLabel);
            this.amountArea.getChildren().add(this.btnDecrease);
            this.amountArea.getChildren().add(this.decSpaceLabel);
            this.amountArea.getChildren().add(this.remainAmountLabel);
            this.amountArea.getChildren().add(this.incSpaceLabel);
            this.amountArea.getChildren().add(this.btnIncrease);
            if (AuthenticationService.LOGGED_IN_AS_OWNER) {
                this.nameField.setEditable(true);
                this.priceField.setEditable(true);
            }
        } else {

            if (AuthenticationService.LOGGED_IN_AS_OWNER) {

            }
            else if (AuthenticationService.LOGGED_IN_AS_STOCK) {

            }
        }
    }

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
