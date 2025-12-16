package ba.una.booking.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class BookingRow {

    private final IntegerProperty bookingId;
    private final StringProperty film;
    private final StringProperty partner;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty material;
    private final StringProperty status;
    private final StringProperty createdBy;
    private final StringProperty createdAt;

    public BookingRow(
            int bookingId,
            String film,
            String partner,
            String startDate,
            String endDate,
            String material,
            String status,
            String createdBy,
            String createdAt
    ) {
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.film = new SimpleStringProperty(film);
        this.partner = new SimpleStringProperty(partner);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.material = new SimpleStringProperty(material);
        this.status = new SimpleStringProperty(status);
        this.createdBy = new SimpleStringProperty(createdBy);
        this.createdAt = new SimpleStringProperty(createdAt);
    }

    public int getBookingId() {
        return bookingId.get();
    }

    public IntegerProperty bookingIdProperty() {
        return bookingId;
    }

    public String getFilm() { return film.get(); }
    public StringProperty filmProperty() { return film; }

    public String getPartner() { return partner.get(); }
    public StringProperty partnerProperty() { return partner; }

    public String getStartDate() { return startDate.get(); }
    public StringProperty startDateProperty() { return startDate; }

    public String getEndDate() { return endDate.get(); }
    public StringProperty endDateProperty() { return endDate; }

    public String getMaterial() { return material.get(); }
    public StringProperty materialProperty() { return material; }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }

    public String getCreatedBy() { return createdBy.get(); }
    public StringProperty createdByProperty() { return createdBy; }

    public String getCreatedAt() { return createdAt.get(); }
    public StringProperty createdAtProperty() { return createdAt; }
}
