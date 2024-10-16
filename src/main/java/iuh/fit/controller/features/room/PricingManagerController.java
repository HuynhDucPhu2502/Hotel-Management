package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.PricingDAO;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.PriceUnit;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.ErrorMessages;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PricingManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> pricingIDSearchField;
    @FXML
    private TextField priceSearchField;
    @FXML
    private TextField unitSearchField;
    @FXML
    private TextField roomCategorySearchField;


    // Input Fields
    @FXML
    private TextField pricingIDTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ComboBox<String> unitCBox;
    @FXML
    private ComboBox<String> roomCategoryCBox;

    // Table
    @FXML
    private TableView<Pricing> pricingTableView;
    @FXML
    private TableColumn<Pricing, String> pricingIDColumn;
    @FXML
    private TableColumn<Pricing, Double> priceColumn;
    @FXML
    private TableColumn<Pricing, String> unitColumn;
    @FXML
    private TableColumn<Pricing, String> roomCategoryColumn;
    @FXML
    private TableColumn<Pricing , Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Pricing> items;

    // Gọi mấy phương thức để gắn sự kiện và dữ liệu cho lúc đầu khởi tạo giao diện
    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        pricingIDSearchField.setOnAction(e -> handleSearchAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        unitCBox.getItems().setAll(
                Stream.of(PriceUnit.values()).map(Enum::name).toList()
        );
        unitCBox.getSelectionModel().selectFirst();

        List<String> comboBoxItems = RoomCategoryDAO.getRoomCategory()
                .stream()
                .map(roomCategory -> roomCategory.getRoomCategoryid()
                        + " " + roomCategory.getRoomCategoryName())
                .collect(Collectors.toList());

        ObservableList<String> observableComboBoxItems = FXCollections.observableArrayList(comboBoxItems);
        roomCategoryCBox.getItems().setAll(observableComboBoxItems);
        roomCategoryCBox.getSelectionModel().selectFirst();

        pricingIDTextField.setText(PricingDAO.getNextPricingID());

        List<String> Ids = PricingDAO.getTopThreeID();
        pricingIDSearchField.getItems().setAll(Ids);

        List<Pricing> pricingList = PricingDAO.getPricing();
        items = FXCollections.observableArrayList(pricingList);
        pricingTableView.setItems(items);
        pricingTableView.refresh();
    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        pricingIDColumn.setCellValueFactory(new PropertyValueFactory<>("pricingID"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        unitColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPriceUnit().name()));
        roomCategoryColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRoomCategory().getRoomCategoryName()));

        setupActionColumn();
    }

    // setup cho cột thao tác
    // THAM KHẢO
    private void setupActionColumn() {
        Callback<TableColumn<Pricing, Void>, TableCell<Pricing, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final HBox hBox = new HBox(10);


            {
                // Thêm class CSS cho các button
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                // Set hành động cho các button
                updateButton.setOnAction(event -> {
                    Pricing pricing = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(pricing);
                });

                deleteButton.setOnAction(event -> {
                    Pricing pricing = getTableView().getItems().get(getIndex());
                    handleDeleteAction(pricing);
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    // Chức năng 1: Làm mới
    private void handleResetAction() {
        pricingIDTextField.setText(PricingDAO.getNextPricingID());
        priceTextField.setText("");
        if (!unitCBox.getItems().isEmpty()) unitCBox.getSelectionModel().selectFirst();
        if (!roomCategoryCBox.getItems().isEmpty()) roomCategoryCBox.getSelectionModel().selectFirst();;

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    // Chức năng 2: Thêm
    private void handleAddAction() {
        try {
            String selectedRoomCategory = roomCategoryCBox.getSelectionModel().getSelectedItem();
            String roomCategoryID = selectedRoomCategory.split(" ")[0];
            RoomCategory roomCategory = RoomCategoryDAO.getDataByID(roomCategoryID);


            Pricing pricing = new Pricing(
                    pricingIDTextField.getText(),
                    ConvertHelper.priceUnitConverter(unitCBox.getSelectionModel().getSelectedItem()),
                    ConvertHelper.doubleConverter(priceTextField.getText(), ErrorMessages.PRICING_INVALID_FORMAT),
                    roomCategory
            );

            PricingDAO.createData(pricing);
            handleResetAction();
            loadData();
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(Pricing pricing) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa giá loại phòng này?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                PricingDAO.deleteData(pricing.getPricingID());
                handleResetAction();
                loadData();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(Pricing pricing) {
        pricingIDTextField.setText(pricing.getPricingID());
        priceTextField.setText(String.valueOf(pricing.getPrice()));

        unitCBox.getSelectionModel().select(pricing.getPriceUnit().name());

        String roomCategoryDisplay = pricing.getRoomCategory().getRoomCategoryid() + " " +
                pricing.getRoomCategory().getRoomCategoryName();
        roomCategoryCBox.getSelectionModel().select(roomCategoryDisplay);

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    // 4.2 Chức năng cập nhật
    private void handleUpdateAction() {
        try {
            String selectedRoomCategory = roomCategoryCBox.getSelectionModel().getSelectedItem();
            String roomCategoryID = selectedRoomCategory.split(" ")[0];
            RoomCategory roomCategory = RoomCategoryDAO.getDataByID(roomCategoryID);

            Pricing updatedPricing = new Pricing(
                    pricingIDTextField.getText(),
                    ConvertHelper.priceUnitConverter(unitCBox.getSelectionModel().getSelectedItem()),
                    ConvertHelper.doubleConverter(priceTextField.getText(), ErrorMessages.PRICING_INVALID_FORMAT),
                    roomCategory
            );

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật giá phòng này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        PricingDAO.updateData(updatedPricing);
                        Platform.runLater(() -> {
                            handleResetAction();
                            loadData();
                        });
                    } catch (IllegalArgumentException e) {
                        dialogPane.showWarning("LỖI", e.getMessage());
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // 5. Chức năng Tìm kiếm
    private void handleSearchAction() {
        priceSearchField.setText("");
        unitSearchField.setText("");
        roomCategorySearchField.setText("");

        String searchText = pricingIDSearchField.getValue();
        List<Pricing> pricingList;

        if (searchText == null || searchText.isEmpty()) {
            pricingList = PricingDAO.getPricing();
        } else {
            pricingList = PricingDAO.findDataByContainsId(searchText);
            if (pricingList.size() == 1) {
                Pricing pricing = pricingList.getFirst();
                priceSearchField.setText(String.valueOf(pricing.getPrice()));
                unitSearchField.setText(pricing.getPriceUnit().name());
                roomCategorySearchField.setText(pricing.getRoomCategory().getRoomCategoryName());
            }
        }

        // Cập nhật lại bảng với dữ liệu tìm kiếm
        items.setAll(pricingList);
        pricingTableView.setItems(items);
    }




}
