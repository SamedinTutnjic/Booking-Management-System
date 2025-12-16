package ba.una.booking.model;

import java.time.LocalDate;

public class Booking {

    private int id;
    private Film film;
    private Partner partner;

    private LocalDate datumPocetka;
    private LocalDate datumZavrsetka;

    private String tipMaterijala;
    private String napomena;

    private Status status;
    private int createdByUserId;
    private LocalDate createdAt;

    public enum Status {
        NA_CEKANJU,
        POTVRDJENO,
        ODBIJEN
    }

    public Booking() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public LocalDate getDatumPocetka() {
        return datumPocetka;
    }

    public void setDatumPocetka(LocalDate datumPocetka) {
        this.datumPocetka = datumPocetka;
    }

    public LocalDate getDatumZavrsetka() {
        return datumZavrsetka;
    }

    public void setDatumZavrsetka(LocalDate datumZavrsetka) {
        this.datumZavrsetka = datumZavrsetka;
    }

    public String getTipMaterijala() {
        return tipMaterijala;
    }

    public void setTipMaterijala(String tipMaterijala) {
        this.tipMaterijala = tipMaterijala;
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

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
