package ba.una.booking.controller;

import ba.una.booking.dao.FilmDaoImpl;
import ba.una.booking.model.Film;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddFilmController {

    @FXML private TextField nazivField;
    @FXML private TextField godinaField;
    @FXML private TextField trajanjeField;

    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> zanrCombo;

    @FXML private TextArea opisArea;

    @FXML
    public void initialize() {

        // STATUS (UI)
        statusCombo.getItems().setAll("Aktivan", "Neaktivan");
        statusCombo.setPromptText("Odaberi status");
        statusCombo.setButtonCell(makePromptButtonCell("Odaberi status"));
        statusCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item);
            }
        });

        // ŽANR
        zanrCombo.getItems().setAll(
                "Akcija", "Drama", "Komedija", "Triler", "Horor",
                "Sci-Fi", "Romantika", "Avantura",
                "Animirani", "Dokumentarni", "Fantazija", "Krimi"
        );
        zanrCombo.setPromptText("Odaberi žanr");
    }

    private ListCell<String> makePromptButtonCell(String prompt) {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? prompt : item);
            }
        };
    }

    @FXML
    private void onSaveFilm() {

        try {
            Film film = new Film();

            film.setNaziv(nazivField.getText());
            film.setOriginalniNaziv(nazivField.getText());
            film.setZanr(zanrCombo.getValue());
            film.setTrajanjeMin(Integer.parseInt(trajanjeField.getText()));

            if (!godinaField.getText().isBlank()) {
                film.setGodinaDistribucije(Integer.parseInt(godinaField.getText()));
            }

            // 🔥 KLJUČNI DIO – MAPIRANJE STATUSA
            String uiStatus = statusCombo.getValue();
            Film.Status dbStatus =
                    "Aktivan".equals(uiStatus)
                            ? Film.Status.AKTIVAN
                            : Film.Status.ZAVRSEN;

            film.setStatus(dbStatus);
            film.setKratakOpis(opisArea.getText());

            new FilmDaoImpl().save(film);

            System.out.println("✅ Film uspješno spremljen u bazu");
            MainController.showView("films.fxml");

        } catch (Exception e) {
            System.err.println("❌ Greška pri spremanju filma");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        MainController.showView("films.fxml");
    }

    @FXML
    private void goToAddPartner() {
        MainController.showView("AddPartner.fxml");
    }

    @FXML
    private void goToAddBooking() {
        MainController.showView("AddBooking.fxml");
    }
}
