package ba.una.booking.controller;

import ba.una.booking.dao.PartnerDao;
import ba.una.booking.model.Partner;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddPartnerController {

    @FXML private TextField nazivField;
    @FXML private TextField gradField;
    @FXML private TextField adresaField;
    @FXML private TextField emailField;
    @FXML private TextField telefonField;
    @FXML private TextField kontaktOsobaField;

    @FXML private ComboBox<String> statusCombo;
    @FXML private TextArea napomenaArea;

    @FXML
    public void initialize() {
        statusCombo.getItems().setAll("Aktivan", "Neaktivan");
        statusCombo.setPromptText("Odaberi status");
    }

    @FXML
    private void onSavePartner() {

        try {
            Partner p = new Partner();

            p.setNazivKina(nazivField.getText());
            p.setGrad(gradField.getText());
            p.setAdresa(adresaField.getText());
            p.setEmail(emailField.getText());
            p.setTelefon(telefonField.getText());
            p.setKontaktOsoba(kontaktOsobaField.getText());
            p.setNapomena(napomenaArea.getText());

            // 🔑 MAPIRANJE STATUSA (UI → BAZA)
            boolean active = "Aktivan".equals(statusCombo.getValue());
            p.setStatus(active ? Partner.Status.AKTIVAN : Partner.Status.NEAKTIVAN);

            PartnerDao.insert(p);

            System.out.println("✅ Partner uspješno spremljen");
            MainController.showView("partners.fxml");

        } catch (Exception e) {
            System.err.println("❌ Greška pri spremanju partnera");
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancel() {
        MainController.showView("partners.fxml");
    }

    @FXML
    private void goToAddBooking() {
        MainController.showView("AddBooking.fxml");
    }

    @FXML
    private void goToAddFilm() {
        MainController.showView("AddFilm.fxml");
    }
}
