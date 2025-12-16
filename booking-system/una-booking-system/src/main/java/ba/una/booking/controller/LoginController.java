package ba.una.booking.controller;

import ba.una.booking.dao.UserDao;
import ba.una.booking.model.User;
import ba.una.booking.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserDao userDao = new UserDao();

    @FXML
    public void initialize() {
        passwordField.setOnAction(this::onLogin);
        errorLabel.setText("");
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // VALIDACIJA
        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            errorLabel.setText("Unesite korisničko ime i šifru.");
            return;
        }

        try {
            // PRONAĐI USERA
            User user = userDao.findByUsername(username);

            if (user == null) {
                errorLabel.setText("Korisnik ne postoji.");
                return;
            }

            if (!user.isActive()) {
                errorLabel.setText("Korisnik je deaktiviran.");
                return;
            }

            // ⚠️ PLAIN TEXT PROVJERA (jer H2 trenutno čuva "admin")
            if (!password.equals(user.getPasswordHash())) {
                errorLabel.setText("Pogrešna lozinka.");
                return;
            }

            // SPREMI U SESIJU
            UserSession.setUser(user);

            // OTVORI DASHBOARD
            openMainWindow(user);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Greška pri prijavi.");
        }
    }

    private void openMainWindow(User user) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.initUser(user);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(root, 1200, 750);
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.setTitle("UNA Film - Dashboard");
        stage.show();
    }
}
