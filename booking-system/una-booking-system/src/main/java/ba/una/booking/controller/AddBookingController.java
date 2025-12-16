package ba.una.booking.controller;

import ba.una.booking.dao.BookingDao;
import ba.una.booking.dao.FilmDaoImpl;
import ba.una.booking.dao.PartnerDao;
import ba.una.booking.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddBookingController {

    @FXML private TextField filmField;
    @FXML private TextField partnerField;
    @FXML private TextField datumPocetkaField;
    @FXML private TextField datumZavrsetkaField;
    @FXML private TextField tipMaterijalaField;
    @FXML private TextArea detaljiArea;

    @FXML private RadioButton potvrdiRadio;
    @FXML private RadioButton odbijRadio;

    @FXML private Button btnAddPartner;
    @FXML private Button btnAddFilm;

    private ToggleGroup statusGroup;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    public void initialize() {
        statusGroup = new ToggleGroup();
        potvrdiRadio.setToggleGroup(statusGroup);
        odbijRadio.setToggleGroup(statusGroup);
        potvrdiRadio.setSelected(true);

        if (UserSession.isReferent()) {
            btnAddPartner.setVisible(false);
            btnAddPartner.setManaged(false);

            btnAddFilm.setVisible(false);
            btnAddFilm.setManaged(false);
        }
    }

    @FXML
    private void onSaveBooking() {
        try {
            // 1️⃣ Validacija
            if (filmField.getText().isBlank()
                    || partnerField.getText().isBlank()
                    || datumPocetkaField.getText().isBlank()
                    || datumZavrsetkaField.getText().isBlank()) {

                showAlert("Greška", "Sva obavezna polja moraju biti popunjena.");
                return;
            }

            // 2️⃣ Parsiranje datuma
            LocalDate datumOd = LocalDate.parse(
                    datumPocetkaField.getText().trim(), formatter);
            LocalDate datumDo = LocalDate.parse(
                    datumZavrsetkaField.getText().trim(), formatter);

            if (datumDo.isBefore(datumOd)) {
                showAlert("Greška", "Datum završetka ne može biti prije početka.");
                return;
            }

            // 3️⃣ Dohvati ID-eve
            String filmName = filmField.getText().trim();
            String partnerName = partnerField.getText().trim();

            int filmId = FilmDaoImpl.getIdByName(filmName);
            int partnerId = PartnerDao.getIdByName(partnerName);

            if (filmId == -1) {
                showAlert("Greška", "Film ne postoji u bazi.");
                return;
            }

            if (partnerId == -1) {
                showAlert("Greška", "Partner ne postoji u bazi.");
                return;
            }

            // 4️⃣ Status
            String status = potvrdiRadio.isSelected()
                    ? "POTVRDJENO"
                    : "ODBIJENO";

            // 5️⃣ INSERT U BAZU
            BookingDao.insertBooking(
                    filmId,
                    partnerId,
                    datumOd,
                    datumDo,
                    tipMaterijalaField.getText().trim(),
                    status,
                    UserSession.getUser().getId(),
                    detaljiArea.getText()
            );

            // 6️⃣ Uspjeh
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Uspjeh");
            ok.setHeaderText(null);
            ok.setContentText("Booking je uspješno dodan.");
            ok.showAndWait();

            MainController.showView("dashboard.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Greška", "Došlo je do greške pri spremanju bookinga.");
        }
    }

    @FXML
    private void onCancel() {
        MainController.showView("dashboard.fxml");
    }

    @FXML
    private void onDeleteBooking() {
        // nije implementirano
    }

    @FXML
    private void goToAddFilm() {
        MainController.showView("AddFilm.fxml");
    }

    @FXML
    private void goToAddPartner() {
        MainController.showView("AddPartner.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
