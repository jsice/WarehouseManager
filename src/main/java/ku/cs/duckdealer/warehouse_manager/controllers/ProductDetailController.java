package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.duckdealer.models.StockedProduct;

import java.io.IOException;

public class ProductDetailController {

    @FXML
    private TextField nameField, priceField;
    @FXML
    private Label remainAmountLabel, idLabel, incSpaceLabel, decSpaceLabel, nameLabel, priceLabel;
    @FXML
    private Button btnIncrease, btnDecrease, btnEdit, btnCancel, btnSave;
    @FXML
    private FlowPane amountArea;

    private StockedProduct stockedProduct;
    private MainController mainCtrl;
    private boolean isEditing = false;
    private BorderPane mainPane;
    private Button source;

    @FXML
    private void initialize() {
        this.nameField.setVisible(false);
        this.priceField.setVisible(false);
        this.nameLabel.setVisible(true);
        this.priceLabel.setVisible(true);

        this.amountArea.getChildren().remove(this.btnDecrease);
        this.amountArea.getChildren().remove(this.decSpaceLabel);
        this.amountArea.getChildren().remove(this.remainAmountLabel);
        this.amountArea.getChildren().remove(this.incSpaceLabel);
        this.amountArea.getChildren().remove(this.btnIncrease);

        this.btnSave.setVisible(false);
        this.btnCancel.setVisible(false);
        if (this.stockedProduct != null) {
            this.btnEdit.setVisible(true);
        } else {
            this.btnEdit.setVisible(false);
        }

        this.amountArea.getChildren().add(this.remainAmountLabel);
    }

    public void setup(StockedProduct p) {
        this.stockedProduct = p;
        this.idLabel.setText(this.stockedProduct.getProduct().getID());
        this.nameLabel.setText(this.stockedProduct.getProduct().getName());
        this.priceLabel.setText(this.stockedProduct.getProduct().getPrice()+"");
        this.remainAmountLabel.setText(this.stockedProduct.getQuantity()+"");
        initialize();
    }

    public void cancel(){
        initialize();
    }

    public void updateAmount(ActionEvent event){
        if(stockedProduct != null){
            String text = ((Button)event.getSource()).getText();
            if(text.equals("+")){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/warehouse/increaseProductPopUp.fxml"));
                stage.initModality(Modality.APPLICATION_MODAL);
                try {
                    stage.setScene(new Scene((Parent) loader.load()));
                    AmountController amountController = loader.getController();
                    stage.showAndWait();
                    if(!amountController.cancel){
                        if (amountController.getAmount() <= 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Increase error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        remainAmountLabel.setText((stockedProduct.getQuantity()+amountController.getAmount())+"");

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }



            }else if (text.equals("-")){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/warehouse/decreaseProductPopUp.fxml"));
                stage.initModality(Modality.APPLICATION_MODAL);
                try {
                    stage.setScene(new Scene((Parent) loader.load()));
                    AmountController amountController = loader.getController();
                    stage.showAndWait();
                    if(!amountController.cancel) {
                        if (amountController.getAmount() <= 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        if (stockedProduct.getQuantity() - amountController.getAmount() < 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        if (amountController.getBy() == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("Please choose 'Sold', 'Damaged', 'Expired' or 'User error'");
                            alert.showAndWait();
                            return;
                        }
                        remainAmountLabel.setText((stockedProduct.getQuantity() - amountController.getAmount())+"");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

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

        this.amountArea.getChildren().remove(this.remainAmountLabel);
        this.amountArea.getChildren().add(this.btnDecrease);
        this.amountArea.getChildren().add(this.decSpaceLabel);
        this.amountArea.getChildren().add(this.remainAmountLabel);
        this.amountArea.getChildren().add(this.incSpaceLabel);
        this.amountArea.getChildren().add(this.btnIncrease);
        this.btnEdit.setVisible(false);
        this.btnSave.setVisible(true);
        this.btnCancel.setVisible(true);
        if (AuthenticationService.LOGGED_IN_AS_OWNER) {
            this.nameLabel.setVisible(false);
            this.priceLabel.setVisible(false);
            this.nameField.setVisible(true);
            this.priceField.setVisible(true);
        }
    }
    @FXML
    private void save() {
        if (AuthenticationService.NOT_LOGGED_IN) {
            this.mainCtrl.login();
            if (AuthenticationService.NOT_LOGGED_IN) {
                return;
            }
        }
        if (AuthenticationService.LOGGED_IN_AS_OWNER) {
            stockedProduct.getProduct().setName(nameField.getText());
            stockedProduct.getProduct().setPrice(Double.parseDouble(priceField.getText()));
        }
        this.stockedProduct.setQuantity(Integer.parseInt(this.remainAmountLabel.getText()));
        mainCtrl.showFilteredProducts();
//        mainCtrl.showProductDetail(stockedProduct);
        mainCtrl.getProductService().update(stockedProduct);
        initialize();
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