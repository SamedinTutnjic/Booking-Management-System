package ba.una.booking.model;

import javafx.beans.property.*;

public class Film {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty naziv = new SimpleStringProperty();
    private StringProperty originalniNaziv = new SimpleStringProperty();
    private StringProperty zanr = new SimpleStringProperty();
    private IntegerProperty trajanjeMin = new SimpleIntegerProperty();
    private IntegerProperty godinaDistribucije = new SimpleIntegerProperty();
    private StringProperty kratakOpis = new SimpleStringProperty();
    private ObjectProperty<Status> status = new SimpleObjectProperty<>();

    public enum Status {
        AKTIVAN, ZAVRSEN
    }

    public Film() {}

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Naziv
    public String getNaziv() { return naziv.get(); }
    public void setNaziv(String value) { naziv.set(value); }
    public StringProperty nazivProperty() { return naziv; }

    // Originalni naziv
    public String getOriginalniNaziv() { return originalniNaziv.get(); }
    public void setOriginalniNaziv(String value) { originalniNaziv.set(value); }
    public StringProperty originalniNazivProperty() { return originalniNaziv; }

    // Zanr
    public String getZanr() { return zanr.get(); }
    public void setZanr(String value) { zanr.set(value); }
    public StringProperty zanrProperty() { return zanr; }

    // Trajanje
    public int getTrajanjeMin() { return trajanjeMin.get(); }
    public void setTrajanjeMin(int value) { trajanjeMin.set(value); }
    public IntegerProperty trajanjeMinProperty() { return trajanjeMin; }

    // Godina distribucije
    public int getGodinaDistribucije() { return godinaDistribucije.get(); }
    public void setGodinaDistribucije(int value) { godinaDistribucije.set(value); }
    public IntegerProperty godinaDistribucijeProperty() { return godinaDistribucije; }

    // Kratak opis
    public String getKratakOpis() { return kratakOpis.get(); }
    public void setKratakOpis(String value) { kratakOpis.set(value); }
    public StringProperty kratakOpisProperty() { return kratakOpis; }

    // Status
    public Status getStatus() { return status.get(); }
    public void setStatus(Status value) { status.set(value); }
    public ObjectProperty<Status> statusProperty() { return status; }

    @Override
    public String toString() {
        return naziv.get();
    }
}
