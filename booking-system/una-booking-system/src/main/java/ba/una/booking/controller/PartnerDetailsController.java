package ba.una.booking.controller;

import ba.una.booking.dao.PartnerDao;
import ba.una.booking.model.PartnerRow;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PartnerDetailsController {

    @FXML private TextField txtNaziv;
    @FXML private TextField txtGrad;
    @FXML private TextField txtKontakt;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefon;
    @FXML private TextField txtStatus;

    private PartnerRow partner;
    private Runnable onDeleteCallback;

    public void setData(PartnerRow partner) {
        this.partner = partner;

        txtNaziv.setText(partner.getNaziv());
        txtGrad.setText(partner.getGrad());
        txtKontakt.setText(partner.getKontaktOsoba());
        txtEmail.setText(partner.getEmail());
        txtTelefon.setText(partner.getTelefon());
        txtStatus.setText(partner.getStatus());

        disableFocus();
    }

    private void disableFocus() {
        txtNaziv.setFocusTraversable(false);
        txtGrad.setFocusTraversable(false);
        txtKontakt.setFocusTraversable(false);
        txtEmail.setFocusTraversable(false);
        txtTelefon.setFocusTraversable(false);
        txtStatus.setFocusTraversable(false);

        txtNaziv.deselect();
        txtNaziv.getParent().requestFocus();
    }

    @FXML
    private void closeWindow() {
        ((Stage) txtNaziv.getScene().getWindow()).close();
    }

    @FXML

  private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Da li ste sigurni?");
        alert.setContentText("Partner će biti trajno obrisan.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            PartnerDao.deleteById(partner.getId());

            if (onDeleteCallback != null) {
                onDeleteCallback.run(); // 🔥 refresh tabele
            }

            closeWindow();
        }
    }


    public void setOnDeleteCallback(Runnable callback) {
        this.onDeleteCallback = callback;
    }
}
