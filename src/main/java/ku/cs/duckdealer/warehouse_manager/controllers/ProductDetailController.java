package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ku.cs.duckdealer.warehouse_manager.models.Product;

public class ProductDetailController {

    @FXML
    private TextField nameField, priceField;
    @FXML
    private Label remainAmountLabel, idLabel, incSpaceLabel, decSpaceLabel;
    @FXML
    private Button btnIncrease, btnDecrease, btnEdit, btnOk, btnCancel;
    @FXML
    private FlowPane amountArea;

    private Product product;
    private MainController mainCtrl;
    private boolean isEditing = false;
    private BorderPane mainPane;


    @FXML
    private void initialize() {
        this.amountArea.getChildren().remove(this.btnDecrease);
        this.amountArea.getChildren().remove(this.decSpaceLabel);
        this.amountArea.getChildren().remove(this.remainAmountLabel);
        this.amountArea.getChildren().remove(this.incSpaceLabel);
        this.amountArea.getChildren().remove(this.btnIncrease);
        this.btnOk.setVisible(false);
        this.btnCancel.setVisible(false);

        this.amountArea.getChildren().add(this.remainAmountLabel);
    }

    public void setup(Product p) {
        this.product = p;
        this.idLabel.setText(this.product.getID());
        this.nameField.setEditable(false);
        this.nameField.setText(this.product.getName());
        this.priceField.setEditable(false);
        this.priceField.setText(this.product.getPrice()+"");
        this.remainAmountLabel.setText(this.product.getQuantity()+"");
        
        this.amountArea.getChildren().remove(this.btnDecrease);
        this.amountArea.getChildren().remove(this.decSpaceLabel);
        this.amountArea.getChildren().remove(this.remainAmountLabel);
        this.amountArea.getChildren().remove(this.incSpaceLabel);
        this.amountArea.getChildren().remove(this.btnIncrease);

        this.amountArea.getChildren().add(this.remainAmountLabel);
    }

    public void updateAmount(){

    }

    public void toggleCreateMode(){
        this.nameField.setEditable(true);
        this.priceField.setEditable(true);
        this.amountArea.getChildren().remove(btnEdit);
        this.btnOk.setVisible(true);
        this.btnCancel.setVisible(true);

    }
    public void createProduct(){
        if (AuthenticationService.NOT_LOGGED_IN){
            mainCtrl.login();
            if (!AuthenticationService.NOT_LOGGED_IN){
                product = new Product(nameField.getText(), Integer.parseInt(priceField.getText()));
                initialize();
            }
        }

    }

    public void cancel(){
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
