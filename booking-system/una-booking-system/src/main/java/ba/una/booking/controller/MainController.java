package ba.una.booking.controller;

import ba.una.booking.model.User;
import ba.una.booking.session.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainController {

    @FXML private BorderPane rootPane;
    @FXML private VBox contentWrapper;
    @FXML private Label welcomeLabel;

    @FXML private Button btnDashboard;
    @FXML private Button btnAddBooking;
    @FXML private Button btnPartners;
    @FXML private Button btnFilms;
    @FXML private Button btnCalendar;
    @FXML private Button btnBooking;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;

    private User currentUser;

    private static MainController INSTANCE;

    // ===== DARK MODE STATE =====
    private boolean darkEnabled = false;

    public MainController() {}

    @FXML
    public void initialize() {
        INSTANCE = this;

        applyRolePermissions();
        highlightButton(btnDashboard);
        loadView("dashboard.fxml");

        // 🔒 zaključaj visinu prozora (da se sidebar ne pomjera)
        Platform.runLater(() -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (stage != null) {
                stage.setMinHeight(stage.getHeight());
                stage.setMaxHeight(stage.getHeight());
            }
        });
    }

    public static MainController getInstance() {
        return INSTANCE;
    }

    public void initUser(User user) {
        this.currentUser = user;
        if (welcomeLabel != null) {
            welcomeLabel.setText(
                    "Prijavljen: " + user.getUsername() + " (" + user.getRole() + ")"
            );
        }
    }

    private void applyRolePermissions() {
        if (UserSession.isReferent()) {
            btnCalendar.setVisible(false);
            btnCalendar.setManaged(false);

            btnPartners.setVisible(false);
            btnPartners.setManaged(false);

            btnSettings.setVisible(true);
        }
    }

    // ================= NAVIGACIJA =================

    @FXML private void showDashboard() {
        highlightButton(btnDashboard);
        loadView("dashboard.fxml");
    }

    @FXML private void showFilms() {
        highlightButton(btnFilms);
        loadView("films.fxml");
    }

    @FXML private void showPartners() {
        if (UserSession.isReferent()) return;
        highlightButton(btnPartners);
        loadView("partners.fxml");
    }

    @FXML private void showAddBooking() {
        highlightButton(btnAddBooking);
        loadView("AddBooking.fxml");
    }

    @FXML private void openCalendar() {
        if (UserSession.isReferent()) return;
        highlightButton(btnCalendar);
        loadView("calendar.fxml");
    }

    @FXML private void showBooking() {
        highlightButton(btnBooking);
        loadView("booking.fxml");
    }

    @FXML
    private void showSettings() {
        highlightButton(btnSettings);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/settings.fxml")
            );
            Parent view = loader.load();

            SettingsController controller = loader.getController();
            controller.init(currentUser, rootPane.getScene());

            controller.setThemeListener(this::setDarkEnabled);

            contentWrapper.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOGOUT =================

    @FXML
    private void onLogout() {
        try {
            Stage currentStage = (Stage) rootPane.getScene().getWindow();

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1200, 750);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("UNA Film - Login");
            stage.show();

            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VIEW LOAD =================

    public void loadView(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/" + fxmlName)
            );
            Node view = loader.load();
            contentWrapper.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BUTTON HIGHLIGHT =================

    private void highlightButton(Button activeBtn) {
        btnDashboard.getStyleClass().remove("active");
        btnFilms.getStyleClass().remove("active");
        btnPartners.getStyleClass().remove("active");
        btnAddBooking.getStyleClass().remove("active");
        btnCalendar.getStyleClass().remove("active");
        btnBooking.getStyleClass().remove("active");
        btnSettings.getStyleClass().remove("active");

        activeBtn.getStyleClass().add("active");
    }

    public static void showView(String fxml) {
        if (INSTANCE != null) {
            INSTANCE.loadView(fxml);
        }
    }

    // ================= DARK MODE (FINAL & CLEAN) =================

    public void setDarkEnabled(boolean enabled) {
        darkEnabled = enabled;

        if (enabled) {
            if (!rootPane.getStyleClass().contains("dark")) {
                rootPane.getStyleClass().add("dark");
            }
        } else {
            rootPane.getStyleClass().remove("dark");
        }

        // 🔁 forsiraj refresh odmah
        rootPane.applyCss();
        rootPane.layout();
    }

    public boolean isDarkEnabled() {
        return darkEnabled;
    }
}
