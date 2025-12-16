package ba.una.booking.controller;

import ba.una.booking.session.UserSession;
import ba.una.booking.dao.FilmDao;
import ba.una.booking.dao.FilmDaoImpl;
import ba.una.booking.model.Film;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class FilmsController {

    @FXML private Button btnAddFilm;
    @FXML private TableView<Film> filmTable;
    @FXML private TableColumn<Film, String>  colNaziv;
    @FXML private TableColumn<Film, Integer> colGodina;
    @FXML private TableColumn<Film, String>  colTrajanje;
    @FXML private TableColumn<Film, String>  colStatus;
    @FXML private TableColumn<Film, Void>    colDetalji;

    @FXML private TextField  searchField;
    @FXML private MenuButton genreFilterButton;
    @FXML private MenuButton statusFilterButton;

    private String selectedGenre  = "ALL";
    private String selectedStatus = "ALL";

    private final FilmDao filmDao = new FilmDaoImpl();
    private final ObservableList<Film> filmList = FXCollections.observableArrayList();

    // ---------------- INIT ----------------
    @FXML
    public void initialize() {

        if (UserSession.isReferent()) {
            btnAddFilm.setVisible(false);
            btnAddFilm.setManaged(false);
        }

        // 🔒 STABILAN RESIZE
        filmTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // 🚫 UGASI DEFAULT PLACEHOLDER
        filmTable.setPlaceholder(new Label(""));

        // ⛔ SAKRIJ TABELU DOK SE PUNI
        filmTable.setVisible(false);

        filmTable.setItems(filmList); // samo jednom

        genreFilterButton.setText("Žanr");
        statusFilterButton.setText("Status");

        setupColumns();

        reloadAsync();
    }

    // ---------------- KOLONE ----------------
    private void setupColumns() {

        colNaziv.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(c.getValue().getNaziv())
        );

        colGodina.setCellValueFactory(c ->
                new ReadOnlyObjectWrapper<>(c.getValue().getGodinaDistribucije())
        );

        colTrajanje.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(c.getValue().getTrajanjeMin() + " min")
        );

        colStatus.setCellValueFactory(c ->
                new ReadOnlyStringWrapper(c.getValue().getStatus().toString())
        );

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setGraphic(null);
                    return;
                }

                Label lbl = new Label(status);
                lbl.getStyleClass().add(
                        "AKTIVAN".equals(status) ? "status-active" : "status-inactive"
                );

                setGraphic(lbl);
                setAlignment(Pos.CENTER);
            }
        });

        setupDetailButtonColumn();

        colDetalji.setPrefWidth(120);
        colDetalji.setStyle("-fx-alignment: CENTER;");
    }

    // ---------------- DETALJI BUTTON ----------------
    private void setupDetailButtonColumn() {

        colDetalji.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Detalji");
            private final HBox box = new HBox(btn);

            {
                btn.getStyleClass().add("detail-button");

                box.setAlignment(Pos.CENTER);
                box.setPrefWidth(Double.MAX_VALUE);

                btn.setOnAction(e -> {
                    Film film = getTableView().getItems().get(getIndex());
                    openFilmDetails(film);
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

    // ---------------- DETALJI ----------------
    private void openFilmDetails(Film film) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/film_details.fxml"));
            Parent root = loader.load();

            FilmDetailsController controller = loader.getController();
            controller.setFilm(film);
            controller.setOnDeleteCallback(this::reloadAsync);

            Stage stage = new Stage();
            stage.setTitle("Detalji - " + film.getNaziv());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- ASYNC LOADING ----------------
    private void reloadAsync() {

        final String query  = searchField.getText();
        final String genre  = selectedGenre;
        final String status = selectedStatus;

        new Thread(() -> {
            try {
                var result = filmDao.findFiltered(query, genre, status);

                Platform.runLater(() -> {
                    filmList.setAll(result);

                    // ✅ PODACI STIGLI → PRIKAŽI TABELU
                    filmTable.setVisible(true);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "films-loader-thread").start();
    }

    // ---------------- SEARCH ----------------
    @FXML
    private void onSearch() {
        reloadAsync();
    }

    // ---------------- GENRE FILTER ----------------
    @FXML private void filterGenreAll()      { selectedGenre = "ALL";       genreFilterButton.setText("Žanr");       reloadAsync(); }
    @FXML private void filterGenreAction()   { selectedGenre = "Akcija";    genreFilterButton.setText("Akcija");    reloadAsync(); }
    @FXML private void filterGenreDrama()    { selectedGenre = "Drama";     genreFilterButton.setText("Drama");     reloadAsync(); }
    @FXML private void filterGenreScifi()    { selectedGenre = "Sci-Fi";    genreFilterButton.setText("Sci-Fi");    reloadAsync(); }
    @FXML private void filterGenreAnimated() { selectedGenre = "Animirani"; genreFilterButton.setText("Animirani"); reloadAsync(); }
    @FXML private void filterGenreCrime()    { selectedGenre = "Crime";     genreFilterButton.setText("Crime");     reloadAsync(); }

    // ---------------- STATUS FILTER ----------------
    @FXML private void filterStatusAll()      { selectedStatus = "ALL";       statusFilterButton.setText("Status");    reloadAsync(); }
    @FXML private void filterStatusActive()   { selectedStatus = "AKTIVAN";   statusFilterButton.setText("Aktivan");   reloadAsync(); }
    @FXML private void filterStatusInactive() { selectedStatus = "NEAKTIVAN"; statusFilterButton.setText("Neaktivan"); reloadAsync(); }

    // ---------------- + DODAJ FILM ----------------
    @FXML
    private void handleAddFilm() {
        MainController.showView("AddFilm.fxml");
    }
}
