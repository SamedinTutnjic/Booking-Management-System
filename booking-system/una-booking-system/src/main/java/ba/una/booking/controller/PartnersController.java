package ba.una.booking.controller;

import ba.una.booking.dao.PartnerDao;
import ba.una.booking.model.Partner;
import ba.una.booking.model.PartnerRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.List;

public class PartnersController {

    @FXML private Button addPartnerButton;

    @FXML private TableView<PartnerRow> partnerTable;
    @FXML private TableColumn<PartnerRow, String> colNaziv;
    @FXML private TableColumn<PartnerRow, String> colGrad;
    @FXML private TableColumn<PartnerRow, String> colKontaktOsoba;
    @FXML private TableColumn<PartnerRow, String> colEmail;
    @FXML private TableColumn<PartnerRow, String> colTelefon;
    @FXML private TableColumn<PartnerRow, Void> colDetalji;

    @FXML private TextField searchField;
    @FXML private Button filterButton;
    @FXML private Button statusFilterButton;

    private final ObservableList<PartnerRow> masterData = FXCollections.observableArrayList();
    private FilteredList<PartnerRow> filteredList;
    private SortedList<PartnerRow> sortedList;

    // ================= INIT =================
    @FXML
    public void initialize() {

        // 🔒 Stabilan resize
        partnerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // 🚫 UGASI DEFAULT PLACEHOLDER
        partnerTable.setPlaceholder(new Label(""));

        // ⛔ Sakrij tabelu dok se puni
        partnerTable.setVisible(false);

        colNaziv.setCellValueFactory(c -> c.getValue().nazivProperty());
        colGrad.setCellValueFactory(c -> c.getValue().gradProperty());
        colKontaktOsoba.setCellValueFactory(c -> c.getValue().kontaktOsobaProperty());
        colEmail.setCellValueFactory(c -> c.getValue().emailProperty());
        colTelefon.setCellValueFactory(c -> c.getValue().telefonProperty());

        // 🔒 Sidra širine
        colNaziv.setPrefWidth(200);
        colGrad.setPrefWidth(120);
        colKontaktOsoba.setPrefWidth(180);
        colEmail.setPrefWidth(220);
        colTelefon.setPrefWidth(120);
        colDetalji.setPrefWidth(120);

        alignColumns();

        setupFiltering();
        setupDetailButton();
        setupFilters();

        loadPartnersAsync();
    }

    // ================= ASYNC LOAD =================
    private void loadPartnersAsync() {

        Task<List<Partner>> task = new Task<>() {
            @Override
            protected List<Partner> call() {
                return PartnerDao.getAll();
            }
        };

        task.setOnSucceeded(e -> {
            masterData.clear();

            for (Partner p : task.getValue()) {
                masterData.add(new PartnerRow(
                        p.getId(),
                        p.getNazivKina(),
                        p.getGrad(),
                        p.getKontaktOsoba(),
                        p.getEmail(),
                        p.getTelefon(),
                        p.getStatus().name()
                ));
            }

            // ✅ PODACI STIGLI → PRIKAŽI TABELU
            partnerTable.setVisible(true);
        });

        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task, "partners-loader").start();
    }

    // ================= FILTERING =================
    private void setupFiltering() {

        filteredList = new FilteredList<>(masterData, p -> true);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(partnerTable.comparatorProperty());

        partnerTable.setItems(sortedList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String s = newVal == null ? "" : newVal.toLowerCase();

            filteredList.setPredicate(p ->
                    p.getNaziv().toLowerCase().contains(s) ||
                            p.getGrad().toLowerCase().contains(s) ||
                            p.getKontaktOsoba().toLowerCase().contains(s) ||
                            p.getEmail().toLowerCase().contains(s)
            );
        });
    }

    // ================= DETAIL BUTTON =================
    private void setupDetailButton() {

        colDetalji.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("Detalji");
            private final HBox box = new HBox(btn);

            {
                btn.getStyleClass().add("detail-button");

                box.setAlignment(Pos.CENTER);
                box.setPrefWidth(Double.MAX_VALUE);

                btn.setOnAction(e -> {
                    PartnerRow partner =
                            getTableView().getItems().get(getIndex());
                    openDetailsPopup(partner);
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

    // ================= DETAILS POPUP =================
    private void openDetailsPopup(PartnerRow partner) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/PartnerDetails.fxml"));
            Parent root = loader.load();

            PartnerDetailsController controller = loader.getController();
            controller.setData(partner);
            controller.setOnDeleteCallback(this::loadPartnersAsync);

            Stage stage = new Stage();
            stage.setTitle("Detalji - " + partner.getNaziv());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= FILTER MENUS =================
    private void setupFilters() {

        filterButton.setOnAction(e -> {
            ContextMenu menu = new ContextMenu();

            MenuItem svi = new MenuItem("Svi");
            svi.setOnAction(ev -> filteredList.setPredicate(p -> true));
            menu.getItems().add(svi);

            masterData.stream()
                    .map(PartnerRow::getGrad)
                    .distinct()
                    .sorted()
                    .forEach(grad -> {
                        MenuItem item = new MenuItem(grad);
                        item.setOnAction(ev ->
                                filteredList.setPredicate(p ->
                                        p.getGrad().equalsIgnoreCase(grad))
                        );
                        menu.getItems().add(item);
                    });

            menu.show(filterButton, Side.BOTTOM, 0, 0);
        });

        statusFilterButton.setOnAction(e -> {
            ContextMenu menu = new ContextMenu();

            MenuItem svi = new MenuItem("Svi");
            svi.setOnAction(ev -> filteredList.setPredicate(p -> true));

            MenuItem aktivni = new MenuItem("Aktivni");
            aktivni.setOnAction(ev ->
                    filteredList.setPredicate(p ->
                            p.getStatus().equals("AKTIVAN")));

            MenuItem neaktivni = new MenuItem("Neaktivni");
            neaktivni.setOnAction(ev ->
                    filteredList.setPredicate(p ->
                            p.getStatus().equals("NEAKTIVAN")));

            menu.getItems().addAll(svi, aktivni, neaktivni);
            menu.show(statusFilterButton, Side.BOTTOM, 0, 0);
        });
    }

    private void alignColumns() {
        colNaziv.setStyle("-fx-alignment: CENTER;");
        colGrad.setStyle("-fx-alignment: CENTER;");
        colKontaktOsoba.setStyle("-fx-alignment: CENTER;");
        colEmail.setStyle("-fx-alignment: CENTER;");
        colTelefon.setStyle("-fx-alignment: CENTER;");
        colDetalji.setStyle("-fx-alignment: CENTER;");
    }

    @FXML
    private void openAddPartner() {
        MainController.showView("AddPartner.fxml");
    }
}
