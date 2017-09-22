package ku.cs.duckdealer.warehouse_manger;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.duckdealer.warehouse_manger.controllers.MainController;

/**
 * Hello world!
 *
 */
public class App extends Application {
    public static void main( String[] args ) {
        launch();
    }

    public void start(Stage primaryStage) throws Exception {
        MainController main = new MainController(primaryStage);
        main.start();
    }
}
