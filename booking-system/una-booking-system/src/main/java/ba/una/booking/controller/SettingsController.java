package ba.una.booking.controller;

import ba.una.booking.model.User;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.function.Consumer;

public class SettingsController {

    @FXML private Label roleLabel;
    @FXML private Label settingsNameLabel;
    @FXML private ToggleButton themeToggle;
    @FXML private ImageView avatarImage;

    @FXML private javafx.scene.layout.StackPane avatarWrapper;

    private Scene mainScene;
    private User currentUser;

    // callback prema MainController da upali/ugasi global theme
    private Consumer<Boolean> themeListener;

    public void setThemeListener(Consumer<Boolean> listener) {
        this.themeListener = listener;
    }

    // POZIVA SE IZ MainController
    public void init(User user, Scene scene) {
        this.currentUser = user;
        this.mainScene = scene;

        applyUserData(user);
        setupAvatar(user);
    }

    @FXML
    public void initialize() {

        // 1️⃣ uzmi trenutno stanje iz MainController-a
        boolean dark = MainController.getInstance().isDarkEnabled();

        // 2️⃣ postavi stanje TOGGLE-a
        themeToggle.setSelected(dark);
        themeToggle.setText(dark ? "🌙" : "☀");

        // 3️⃣ TEK ONDA dodaj listener
        themeToggle.selectedProperty().addListener((obs, oldV, isDark) -> {
            themeToggle.setText(isDark ? "🌙" : "☀");

            if (themeListener != null) {
                themeListener.accept(isDark);
            }
        });
    }


    private void applyUserData(User user) {
        if (user == null) return;

        boolean isAdmin = user.getRole() != null && user.getRole().equalsIgnoreCase("admin");

        settingsNameLabel.setText(isAdmin ? "Admin" : "Referent");
        roleLabel.setText(isAdmin ? "Una Film Admin" : "Una Film Referent");

        // krug iza avatara: admin plav, referent tamno žut
        if (avatarWrapper != null) {
            avatarWrapper.getStyleClass().removeAll("avatar-admin", "avatar-referent");
            avatarWrapper.getStyleClass().add(isAdmin ? "avatar-admin" : "avatar-referent");
        }
    }

    private void setupAvatar(User user) {
        boolean isAdmin = user != null && user.getRole() != null && user.getRole().equalsIgnoreCase("admin");

        String imgPath = isAdmin ? "/img/admin.png" : "/img/referent.png";
        var url = getClass().getResource(imgPath);

        if (url != null) {
            Image img = new Image(url.toExternalForm(), 120, 120, true, true);
            avatarImage.setImage(img);
            avatarImage.setClip(new Circle(55, 55, 55));
        }
    }
}
