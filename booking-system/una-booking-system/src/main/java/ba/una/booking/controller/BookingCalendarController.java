package ba.una.booking.controller;

import ba.una.booking.dao.BookingDao;
import ba.una.booking.model.Booking;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.Tooltip;

public class BookingCalendarController {

    @FXML private ComboBox<String> filmFilter;
    @FXML private ComboBox<String> partnerFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML private Button resetFiltersButton;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private Button todayButton;

    @FXML private Label monthLabel;
    @FXML private GridPane calendarGrid;

    private LocalDate currentMonth;
    private final LocalDate minMonth = LocalDate.now().withDayOfMonth(1);

    private List<Booking> allBookings = new ArrayList<>();
    private boolean suppressReload = false;

    private final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("LLLL yyyy", new Locale("bs", "BA"));

    @FXML
    public void initialize() {
        currentMonth = minMonth;

        initButtons();
        initFilters();
        reload();
        updatePrevButtonState();
    }

    // ---------------- BUTTONS ----------------

    private void initButtons() {

        prevMonthButton.setOnAction(e -> changeMonth(currentMonth.minusMonths(1)));
        nextMonthButton.setOnAction(e -> changeMonth(currentMonth.plusMonths(1)));
        todayButton.setOnAction(e -> changeMonth(minMonth));

        resetFiltersButton.setOnAction(e -> {
            suppressReload = true;

            filmFilter.getSelectionModel().selectFirst();
            partnerFilter.getSelectionModel().selectFirst();
            statusFilter.getSelectionModel().selectFirst();

            suppressReload = false;
            reload();
        });
    }

    private void changeMonth(LocalDate newMonth) {
        if (!newMonth.isBefore(minMonth)) {
            currentMonth = newMonth;
            reload();
            updatePrevButtonState();
        }
    }

    private void updatePrevButtonState() {
        prevMonthButton.setDisable(!currentMonth.isAfter(minMonth));
    }

    // ---------------- FILTERS ----------------

    private void initFilters() {

        filmFilter.getItems().add("Svi filmovi");
        filmFilter.getItems().addAll(BookingDao.getAllFilmNames());
        filmFilter.getSelectionModel().selectFirst();

        partnerFilter.getItems().add("Svi partneri");
        partnerFilter.getItems().addAll(BookingDao.getAllPartnerNames());
        partnerFilter.getSelectionModel().selectFirst();

        statusFilter.getItems().add("Svi statusi");
        statusFilter.getItems().addAll(BookingDao.getAllStatuses());
        statusFilter.getSelectionModel().selectFirst();

        filmFilter.valueProperty().addListener((a,b,c) -> reload());
        partnerFilter.valueProperty().addListener((a,b,c) -> reload());
        statusFilter.valueProperty().addListener((a,b,c) -> reload());
    }


    private void reload() {
        if (suppressReload) return;

        String film = normalize(filmFilter);
        String partner = normalize(partnerFilter);
        String status = normalize(statusFilter);

        allBookings = BookingDao.getForMonth(
                currentMonth.getYear(),
                currentMonth.getMonthValue(),
                film,
                partner,
                status
        );

        refreshCalendar();
    }

    // 🔑 KLJUČNA METODA
    private String normalize(ComboBox<String> box) {
        String val = box.getValue();
        if (val == null) return "";
        if (val.startsWith("Svi")) return "";
        return val;
    }
    // ---------------- CALENDAR ----------------

    private void refreshCalendar() {

        String formatted = monthFormatter.format(currentMonth);
        monthLabel.setText(
                formatted.substring(0,1).toUpperCase() + formatted.substring(1)
        );

        calendarGrid.getChildren().clear();
        calendarGrid.getRowConstraints().clear();

        int daysInMonth = currentMonth.lengthOfMonth();
        int offset = (currentMonth.getDayOfWeek().getValue() + 6) % 7;
        int rows = (offset + daysInMonth + 6) / 7;

        for (int i = 0; i < rows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(80);
            calendarGrid.getRowConstraints().add(rc);
        }

        LocalDate first = currentMonth.withDayOfMonth(1);

        for (int cell = 0; cell < rows * 7; cell++) {

            int day = cell - offset + 1;
            VBox box;

            if (day >= 1 && day <= daysInMonth) {
                LocalDate date = first.withDayOfMonth(day);
                box = createDayCell(date);
            } else {
                box = createEmptyCell();
            }

            calendarGrid.add(box, cell % 7, cell / 7);
        }
    }

    private VBox createEmptyCell() {
        VBox v = new VBox();
        v.getStyleClass().add("calendar-day");
        v.getChildren().addAll(new Label(""), new VBox());
        return v;
    }

    private VBox createDayCell(LocalDate date) {

        VBox root = new VBox();
        root.getStyleClass().add("calendar-day");

        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.getStyleClass().add("calendar-day-number");

        VBox bookingsBox = new VBox();
        bookingsBox.getStyleClass().add("calendar-day-bookings");
        VBox.setVgrow(bookingsBox, Priority.ALWAYS);
        bookingsBox.setMaxHeight(Double.MAX_VALUE);
        bookingsBox.setMinHeight(0);

        allBookings.stream()
                .filter(b ->
                        !date.isBefore(b.getDatumPocetka()) &&
                                !date.isAfter(b.getDatumZavrsetka())
                )
                .forEach(b -> bookingsBox.getChildren().add(createBookingChip(b)));

        root.getChildren().addAll(dayLabel, bookingsBox);

        String tooltipText = buildDayTooltip(date);
        if (tooltipText != null) {
            Tooltip tooltip = new Tooltip(tooltipText);
            tooltip.setShowDelay(Duration.millis(400));
            tooltip.getStyleClass().add("day-tooltip");
            Tooltip.install(root, tooltip);
        }

        return root;
    }


    private String buildTooltipText(Booking b) {
        return
                "Film: " + b.getFilm().getNaziv() + "\n" +
                        "Partner: " + b.getPartner().getNazivKina() + "\n" +
                        "Početak: " + b.getDatumPocetka() + "\n" +
                        "Završetak: " + b.getDatumZavrsetka() + "\n" +
                        "Status: " + b.getStatus().name();
    }

    private HBox createBookingChip(Booking b) {

        Label label = new Label(
                b.getFilm().getNaziv() + " - " + b.getPartner().getNazivKina()
        );

        label.getStyleClass().add("booking-chip");

        if (b.getStatus() == Booking.Status.POTVRDJENO) {
            label.getStyleClass().add("booking-chip-confirmed");
        } else {
            label.getStyleClass().add("booking-chip-option");
        }

        Tooltip tooltip = new Tooltip(buildTooltipText(b));
        tooltip.getStyleClass().add("booking-tooltip");

        tooltip.setShowDelay(Duration.millis(300));

        Tooltip.install(label, tooltip);

        return new HBox(label);
    }

    private String buildDayTooltip(LocalDate date) {

        List<Booking> bookingsForDay = allBookings.stream()
                .filter(b ->
                        !date.isBefore(b.getDatumPocetka()) &&
                                !date.isAfter(b.getDatumZavrsetka())
                )
                .toList();

        if (bookingsForDay.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Bookinzi:\n");

        for (Booking b : bookingsForDay) {
            sb.append("• ")
                    .append(b.getFilm().getNaziv())
                    .append(" – ")
                    .append(b.getPartner().getNazivKina())
                    .append("\n");
        }

        return sb.toString();
    }


}
