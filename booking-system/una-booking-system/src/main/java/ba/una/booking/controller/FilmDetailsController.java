package ba.una.booking.controller;

import ba.una.booking.dao.FilmDao;
import ba.una.booking.dao.FilmDaoImpl;
import ba.una.booking.model.Film;
import ba.una.booking.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class FilmDetailsController {

    @FXML private TextField tfNaziv;
    @FXML private TextField tfGodina;
    @FXML private TextField tfTrajanje;
    @FXML private TextField tfStatus;
    @FXML private TextField tfZanr;

    @FXML private Button btnDelete;

    private Film selectedFilm;

    // 🔥 CALLBACK ZA REFRESH TABELE
    private Runnable onDeleteCallback;

    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }

    public void setFilm(Film film) {
        this.selectedFilm = film;

        tfNaziv.setText(film.getNaziv());
        tfGodina.setText(String.valueOf(film.getGodinaDistribucije()));
        tfTrajanje.setText(film.getTrajanjeMin() + " min");
        tfStatus.setText(film.getStatus().toString());
        tfZanr.setText(film.getZanr());

        tfNaziv.getParent().requestFocus();

        // 🔒 REFERENT NE MOŽE BRISATI
        if (UserSession.isReferent()) {
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) tfNaziv.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDeleteFilm() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText("Da li ste sigurni?");
        confirm.setContentText("Film će biti trajno obrisan.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            FilmDao filmDao = new FilmDaoImpl();

            // 🔴 PROVJERA BOOKINGA
            if (filmDao.hasBookings(selectedFilm.getId())) {
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Brisanje nije moguće");
                warning.setHeaderText("Film ima aktivne bookinge");
                warning.setContentText(
                        "Ovaj film se ne može obrisati jer postoji jedan ili više booking-a vezanih za njega."
                );
                warning.showAndWait();
                return;
            }

            // 🟢 BRISANJE IZ BAZE
            filmDao.delete(selectedFilm.getId());

            // 🔥 ODMAH REFRESH TABELE U PARENT CONTROLLERU
            if (onDeleteCallback != null) {
                onDeleteCallback.run();
            }

            closeWindow();

        } catch (Exception e) {
            e.printStackTrace();

            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Greška");
            error.setHeaderText("Došlo je do greške");
            error.setContentText("Film nije moguće obrisati.");
            error.showAndWait();
        }
    }
}
