package ba.una.booking;

import ba.una.booking.database.DBInit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // INIT DATABASE
        DBInit.init();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(loader.load(), 1200, 750);

        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        stage.setTitle("UNA Booking - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
