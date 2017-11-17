package ku.cs.duckdealer.cashier;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.duckdealer.cashier.controllers.MainController;

public class CashierMain extends  Application
{
    public static void main( String[] args ) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        MainController main = new MainController(primaryStage);
        main.start();
    }
}
