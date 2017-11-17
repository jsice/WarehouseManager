package ku.cs.duckdealer.warehouse_manager;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.duckdealer.warehouse_manager.controllers.MainController;

public class WarehouseMain extends Application {
    public static void main( String[] args ) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        MainController main = new MainController(primaryStage);
        main.start();
    }
}
