package ba.una.booking.model;

public class Partner {

    private int id;
    private String nazivKina;
    private String grad;
    private String adresa;
    private String kontaktOsoba;
    private String email;
    private String telefon;
    private Integer kapacitet;
    private String napomena;
    private Status status;

    public enum Status {
        AKTIVAN, NEAKTIVAN
    }

    // ===== GET / SET =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivKina() {
        return nazivKina;
    }

    public void setNazivKina(String nazivKina) {
        this.nazivKina = nazivKina;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getKontaktOsoba() {
        return kontaktOsoba;
    }

    public void setKontaktOsoba(String kontaktOsoba) {
        this.kontaktOsoba = kontaktOsoba;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Integer getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(Integer kapacitet) {
        this.kapacitet = kapacitet;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return nazivKina;
    }
}
