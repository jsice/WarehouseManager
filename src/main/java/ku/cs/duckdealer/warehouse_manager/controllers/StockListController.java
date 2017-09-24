package ku.cs.duckdealer.warehouse_manager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StockListController {

    private Pane mainPane;

    @FXML
    private GridPane innerTableGrid;
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private TextField searchTextfield;

    public void createNewProduct(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authorizationPopUp.fxml"));

        try {

            System.out.println("WORKING");
            stage.setScene(new Scene((Parent) loader.load()));

            AuthorizationController authController = loader.getController();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (!authController.isLoggedIn()){
                return;
            }

        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }


    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }
}
