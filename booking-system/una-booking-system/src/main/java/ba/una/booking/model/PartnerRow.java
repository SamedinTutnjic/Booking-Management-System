package ba.una.booking.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PartnerRow {

    private final IntegerProperty id;

    private final StringProperty naziv;
    private final StringProperty grad;
    private final StringProperty kontaktOsoba;
    private final StringProperty email;
    private final StringProperty telefon;
    private final StringProperty status;

    public PartnerRow(
            int id,
            String naziv,
            String grad,
            String kontaktOsoba,
            String email,
            String telefon,
            String status
    ) {
        this.id = new SimpleIntegerProperty(id);
        this.naziv = new SimpleStringProperty(naziv);
        this.grad = new SimpleStringProperty(grad);
        this.kontaktOsoba = new SimpleStringProperty(kontaktOsoba);
        this.email = new SimpleStringProperty(email);
        this.telefon = new SimpleStringProperty(telefon);
        this.status = new SimpleStringProperty(status);
    }

    // ===== ID (KLJUČNO ZA DELETE) =====
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // ===== OSTALA POLJA =====
    public String getNaziv() {
        return naziv.get();
    }

    public StringProperty nazivProperty() {
        return naziv;
    }

    public String getGrad() {
        return grad.get();
    }

    public StringProperty gradProperty() {
        return grad;
    }

    public String getKontaktOsoba() {
        return kontaktOsoba.get();
    }

    public StringProperty kontaktOsobaProperty() {
        return kontaktOsoba;
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getTelefon() {
        return telefon.get();
    }

    public StringProperty telefonProperty() {
        return telefon;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }
}
