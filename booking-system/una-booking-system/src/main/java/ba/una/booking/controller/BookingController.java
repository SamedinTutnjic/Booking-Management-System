package ba.una.booking.controller;

import ba.una.booking.dao.BookingDao;
import ba.una.booking.model.Booking;
import ba.una.booking.model.BookingRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class BookingController {

    @FXML private Label totalBookingsLabel;
    @FXML private Label confirmedLabel;
    @FXML private Label rejectedLabel;
    @FXML private Label pendingLabel;

    @FXML private TableView<BookingRow> bookingTable;
    @FXML private TableColumn<BookingRow, String> filmColumn;
    @FXML private TableColumn<BookingRow, String> partnerColumn;
    @FXML private TableColumn<BookingRow, String> startDateColumn;
    @FXML private TableColumn<BookingRow, String> endDateColumn;
    @FXML private TableColumn<BookingRow, String> materialColumn;
    @FXML private TableColumn<BookingRow, String> statusColumn;
    @FXML private TableColumn<BookingRow, String> createdByColumn;
    @FXML private TableColumn<BookingRow, String> createdAtColumn;
    @FXML private TableColumn<BookingRow, Void> detailsColumn;

    @FXML private ComboBox<String> filmFilter;
    @FXML private ComboBox<String> partnerFilter;

    private final ObservableList<BookingRow> tableData = FXCollections.observableArrayList();
    private List<Booking> allBookings;

    // ================= INIT =================
    @FXML
    public void initialize() {

        // 🔥 STABILAN resize (bez FLEX_LAST_COLUMN bugova)
        bookingTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        filmColumn.setCellValueFactory(c -> c.getValue().filmProperty());
        partnerColumn.setCellValueFactory(c -> c.getValue().partnerProperty());
        startDateColumn.setCellValueFactory(c -> c.getValue().startDateProperty());
        endDateColumn.setCellValueFactory(c -> c.getValue().endDateProperty());
        materialColumn.setCellValueFactory(c -> c.getValue().materialProperty());
        statusColumn.setCellValueFactory(c -> c.getValue().statusProperty());
        createdByColumn.setCellValueFactory(c -> c.getValue().createdByProperty());
        createdAtColumn.setCellValueFactory(c -> c.getValue().createdAtProperty());

        // 🔒 sidra širine (opciono ali preporučeno)
        filmColumn.setPrefWidth(160);
        partnerColumn.setPrefWidth(160);
        startDateColumn.setPrefWidth(120);
        endDateColumn.setPrefWidth(120);
        materialColumn.setPrefWidth(100);
        statusColumn.setPrefWidth(120);
        createdByColumn.setPrefWidth(100);
        createdAtColumn.setPrefWidth(140);
        detailsColumn.setPrefWidth(120);

        setupStatusColumn();
        setupDetailsColumn();

        reloadFromDatabase();
    }

    // ================= STATUS COLUMN =================
    private void setupStatusColumn() {
        statusColumn.setCellFactory(col -> new TableCell<>() {
            private final Label lbl = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                lbl.setText(item);
                lbl.getStyleClass().setAll("status-pill");

                switch (item) {
                    case "Potvrđeno" -> lbl.getStyleClass().add("status-pill-confirmed");
                    case "Odbijeno" -> lbl.getStyleClass().add("status-pill-rejected");
                    default -> lbl.getStyleClass().add("status-pill-pending");
                }

                setGraphic(lbl);
                setAlignment(Pos.CENTER);
            }
        });
    }

    // ================= DETAILS COLUMN (CENTRIRANO) =================
    private void setupDetailsColumn() {

        detailsColumn.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Detalji");
            private final HBox box = new HBox(btn);

            {
                btn.getStyleClass().add("details-button");

                box.setAlignment(Pos.CENTER);
                box.setPrefWidth(Double.MAX_VALUE);

                btn.setOnAction(e -> {
                    BookingRow row = getTableView().getItems().get(getIndex());

                    Booking booking = allBookings.stream()
                            .filter(b -> b.getId() == row.getBookingId())
                            .findFirst()
                            .orElse(null);

                    if (booking != null) {
                        openDetailsPopup(booking);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    // ================= LOAD =================
    private void reloadFromDatabase() {
        allBookings = BookingDao.getAll();
        fillTable(allBookings);
        setupFilters();
        updateStats();
    }

    private void fillTable(List<Booking> bookings) {
        tableData.clear();

        for (Booking b : bookings) {
            tableData.add(new BookingRow(
                    b.getId(),
                    b.getFilm().getNaziv(),
                    b.getPartner().getNazivKina(),
                    b.getDatumPocetka().toString(),
                    b.getDatumZavrsetka().toString(),
                    b.getTipMaterijala(),
                    formatStatus(b.getStatus()),
                    "ID: " + b.getCreatedByUserId(),
                    b.getCreatedAt() != null ? b.getCreatedAt().toString() : ""
            ));
        }

        bookingTable.setItems(tableData);
    }

    // ================= FILTERS =================
    private void setupFilters() {

        List<String> films = allBookings.stream()
                .map(b -> b.getFilm().getNaziv())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        films.add(0, "Svi");
        filmFilter.getItems().setAll(films);

        List<String> partners = allBookings.stream()
                .map(b -> b.getPartner().getNazivKina())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        partners.add(0, "Svi");
        partnerFilter.getItems().setAll(partners);

        filmFilter.setValue("Svi");
        partnerFilter.setValue("Svi");

        filmFilter.setOnAction(e -> applyFilters());
        partnerFilter.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {
        String film = filmFilter.getValue();
        String partner = partnerFilter.getValue();

        List<Booking> filtered = allBookings.stream()
                .filter(b -> "Svi".equals(film) || b.getFilm().getNaziv().equals(film))
                .filter(b -> "Svi".equals(partner) || b.getPartner().getNazivKina().equals(partner))
                .collect(Collectors.toList());

        fillTable(filtered);
    }

    // ================= STATS =================
    private void updateStats() {
        totalBookingsLabel.setText(String.valueOf(allBookings.size()));
        confirmedLabel.setText(String.valueOf(
                allBookings.stream().filter(b -> b.getStatus() == Booking.Status.POTVRDJENO).count()
        ));
        rejectedLabel.setText(String.valueOf(
                allBookings.stream().filter(b -> b.getStatus() == Booking.Status.ODBIJEN).count()
        ));
        pendingLabel.setText(String.valueOf(
                allBookings.stream().filter(b -> b.getStatus() == Booking.Status.NA_CEKANJU).count()
        ));
    }

    private String formatStatus(Booking.Status s) {
        return switch (s) {
            case POTVRDJENO -> "Potvrđeno";
            case ODBIJEN -> "Odbijeno";
            default -> "Na čekanju";
        };
    }

    // ================= DETAILS POPUP =================
    private void openDetailsPopup(Booking booking) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/BookingDetails.fxml"));
            Parent root = loader.load();

            BookingDetailsController controller = loader.getController();
            controller.setBooking(booking);
            controller.setOnDeleteCallback(this::reloadFromDatabase);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBooking() {
        MainController.showView("AddBooking.fxml");
    }
}
