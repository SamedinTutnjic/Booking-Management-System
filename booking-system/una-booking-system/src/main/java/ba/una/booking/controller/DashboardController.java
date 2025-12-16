package ba.una.booking.controller;

import ba.una.booking.dao.BookingDao;
import ba.una.booking.dao.FilmDaoImpl;
import ba.una.booking.dao.PartnerDao;
import ba.una.booking.util.AuthUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class DashboardController {

    @FXML private Label filmsCountLabel;
    @FXML private Label partnersCountLabel;
    @FXML private Label bookingsMonthLabel;
    @FXML private Label partnersActivityLabel;

    @FXML private LineChart<String, Number> bookingsChart;
    @FXML private LineChart<String, Number> partnersChart;

    @FXML private HBox quickAddBookingBox;
    @FXML private HBox quickAddFilmBox;
    @FXML private HBox quickAddPartnerBox;

    @FXML
    public void initialize() {
        loadStats();
        loadBookingsChart();
        loadPartnersChart();

        if (AuthUtil.isReferent()) {
            quickAddFilmBox.setVisible(false);
            quickAddFilmBox.setManaged(false);
            quickAddPartnerBox.setVisible(false);
            quickAddPartnerBox.setManaged(false);
        }
    }

    // ================= STATS =================
    private void loadStats() {
        try {
            filmsCountLabel.setText(String.valueOf(new FilmDaoImpl().countActive()));
            partnersCountLabel.setText(String.valueOf(PartnerDao.countAll()));
            bookingsMonthLabel.setText(String.valueOf(BookingDao.countThisMonth()));
            partnersActivityLabel.setText(String.valueOf(BookingDao.countActivePartners()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= BOOKINGS CHART =================
    private void loadBookingsChart() {
        bookingsChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        BookingDao.bookingsPerMonth().forEach((month, count) -> {
            series.getData().add(new XYChart.Data<>(month, count));
        });

        bookingsChart.getData().add(series);
    }

    // ================= PARTNERS CHART =================
    private void loadPartnersChart() {
        partnersChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        BookingDao.topPartners().forEach((partner, count) -> {
            series.getData().add(new XYChart.Data<>(partner, count));
        });

        partnersChart.getData().add(series);
    }

    // ================= QUICK ACTIONS =================
    @FXML
    private void onAddBookingQuick(MouseEvent e) {
        MainController.showView("AddBooking.fxml");
    }

    @FXML
    private void onAddFilmQuick(MouseEvent e) {
        AuthUtil.requireAdmin();
        MainController.showView("AddFilm.fxml");
    }

    @FXML
    private void onAddPartnerQuick(MouseEvent e) {
        AuthUtil.requireAdmin();
        MainController.showView("AddPartner.fxml");
    }
}
