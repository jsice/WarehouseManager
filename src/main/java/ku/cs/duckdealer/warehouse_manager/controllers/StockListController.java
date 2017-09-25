package ku.cs.duckdealer.warehouse_manager.controllers;

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
        stage.initModality(Modality.APPLICATION_MODAL);
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            AuthorizationController authController = loader.getController();
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Pane getMainPane() {
        return mainPane;
    }

    public void setMainPane(Pane mainPane) {
        this.mainPane = mainPane;
    }
}
