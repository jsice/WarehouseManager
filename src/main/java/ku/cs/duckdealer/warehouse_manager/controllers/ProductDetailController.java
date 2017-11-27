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
import ku.cs.duckdealer.models.ProductMovement;
import ku.cs.duckdealer.models.StockedProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

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
    private BorderPane mainPane;
    private List<ProductMovement> productMovementList = new ArrayList<>();

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
        this.nameField.setText(this.stockedProduct.getProduct().getName());
        this.priceField.setText(this.stockedProduct.getProduct().getPrice()+"");
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
                    stage.setScene(new Scene(loader.load()));
                    AmountController amountController = loader.getController();
                    stage.showAndWait();
                    if(!amountController.cancel){
                        int amount = amountController.getAmount();
                        if (amount <= 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Increase error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        remainAmountLabel.setText((stockedProduct.getQuantity()+amount)+"");
                        ProductMovement productMovement = new ProductMovement(stockedProduct.getProduct(), new GregorianCalendar(), false, amount, "Add");
                        this.productMovementList.add(productMovement);
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
                        int amount = amountController.getAmount();
                        String by = amountController.getBy();
                        int currentAmount = Integer.parseInt(remainAmountLabel.getText());
                        if (amount <= 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        if (currentAmount - amount < 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("quantity of product is below 0");
                            alert.showAndWait();
                            return;
                        }
                        if (by == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Decrease error");
                            alert.setContentText("Please choose 'Sold', 'Damaged', 'Expired' or 'User error'");
                            alert.showAndWait();
                            return;
                        }
                        remainAmountLabel.setText((currentAmount - amount)+"");
                        ProductMovement productMovement = new ProductMovement(stockedProduct.getProduct(), new GregorianCalendar(), true, amount, by);
                        this.productMovementList.add(productMovement);
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
        for (ProductMovement productMovement: this.productMovementList) {
            this.mainCtrl.getProductMovementService().add(productMovement);
        }
        this.productMovementList.clear();
        if (AuthenticationService.LOGGED_IN_AS_OWNER) {
            stockedProduct.getProduct().setName(nameField.getText());
            stockedProduct.getProduct().setPrice(Double.parseDouble(priceField.getText()));
        }
        this.stockedProduct.setQuantity(Integer.parseInt(this.remainAmountLabel.getText()));
        mainCtrl.showFilteredProducts();
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