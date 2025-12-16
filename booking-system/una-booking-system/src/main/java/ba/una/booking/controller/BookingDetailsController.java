package ba.una.booking.controller;

import ba.una.booking.dao.BookingDao;
import ba.una.booking.model.Booking;
import ba.una.booking.session.UserSession;
import ba.una.booking.util.BookingFilmExcelExporter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class BookingDetailsController {

    @FXML private AnchorPane root;

    @FXML private TextField filmField;
    @FXML private TextField partnerField;
    @FXML private TextField startDateField;
    @FXML private TextField endDateField;
    @FXML private TextField materialField;
    @FXML private TextField statusField;
    @FXML private TextField createdByField;
    @FXML private TextField createdAtField;

    @FXML private Button btnExport;
    @FXML private Button btnDelete;

    private Booking booking;
    private Runnable onDeleteCallback;

    @FXML
    public void initialize() {

        if (UserSession.isReferent()) {
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
            btnExport.setVisible(false);
            btnExport.setManaged(false);
        }

        // ⬇⬇⬇ OVO JE KLJUČ
        Platform.runLater(this::applyTheme);

        disableHighlight();
    }

    private void applyTheme() {
        root.getStyleClass().remove("dark");

        if (UserSession.isDarkMode()) {
            root.getStyleClass().add("dark");
        }
    }

    private void disableHighlight() {
        clear(filmField);
        clear(partnerField);
        clear(startDateField);
        clear(endDateField);
        clear(materialField);
        clear(statusField);
        clear(createdByField);
        clear(createdAtField);
    }

    private void clear(TextField tf) {
        tf.setFocusTraversable(false);
        tf.deselect();
    }

    public void setBooking(Booking booking) {
        this.booking = booking;

        filmField.setText(booking.getFilm().getNaziv());
        partnerField.setText(booking.getPartner().getNazivKina());
        startDateField.setText(booking.getDatumPocetka().toString());
        endDateField.setText(booking.getDatumZavrsetka().toString());
        materialField.setText(booking.getTipMaterijala());
        statusField.setText(booking.getStatus().name());
        createdByField.setText(("REFERENT " + booking.getCreatedByUserId()).toUpperCase());
        createdAtField.setText(
                booking.getCreatedAt() != null ? booking.getCreatedAt().toString() : ""
        );
    }

    @FXML
    private void handleExport() {
        String filmName = booking.getFilm().getNaziv();

        FileChooser fc = new FileChooser();
        fc.setTitle("Export booking-a za film");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx")
        );
        fc.setInitialFileName("bookings_" + filmName + ".xlsx");

        File file = fc.showSaveDialog(root.getScene().getWindow());
        if (file == null) return;

        new Thread(() -> {
            try {
                List<Booking> bookings =
                        BookingDao.getByFilmId(booking.getFilm().getId());
                BookingFilmExcelExporter.export(filmName, bookings, file);

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleClose() {
        ((Stage) root.getScene().getWindow()).close();
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Da li ste sigurni?");
        alert.setContentText("Booking će biti trajno obrisan.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            BookingDao.deleteById(booking.getId());
            if (onDeleteCallback != null) onDeleteCallback.run();
            handleClose();
        }
    }

    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }
}
