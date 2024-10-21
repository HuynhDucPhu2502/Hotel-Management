package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.models.RoomCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RoomCategoryManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> roomCategoryIDSearchField;
    @FXML
    private TextField roomCategoryNameSearchField;
    @FXML
    private TextField numberOfBedSearchField;

    // Input Fields
    @FXML
    private TextField roomCategoryIDTextField;
    @FXML
    private ComboBox<String> roomCategoryRankCBox;
    @FXML
    private TextField roomCategoryNameTextField;
    @FXML
    private TextField numberOfBedTextField;

    // Table
    @FXML
    private TableView<RoomCategory> roomCategoryTableView;
    @FXML
    private TableColumn<RoomCategory, String> roomCategoryIDColumn;
    @FXML
    private TableColumn<RoomCategory, Double> roomCategoryNameColumn;
    @FXML
    private TableColumn<RoomCategory, String> numberOfBedColumn;
    @FXML
    private TableColumn<RoomCategory, Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    @FXML
    private DialogPane dialogPane;

    private ObservableList<RoomCategory> items;

    public void initialize() {
        dialogPane.toFront();
        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        roomCategoryIDSearchField.setOnAction(e -> handleSearchAction());
    }

    // Load dữ liệu lên giao diện
    private void loadData() {
        List<String> ids = RoomCategoryDAO.getTopThreeID();
        roomCategoryIDSearchField.getItems().setAll(ids);

        roomCategoryIDTextField.setText(RoomCategoryDAO.getNextRoomCategoryID());

        List<RoomCategory> roomCategories = RoomCategoryDAO.getRoomCategory();
        items = FXCollections.observableArrayList(roomCategories);
        roomCategoryTableView.setItems(items);
        roomCategoryTableView.refresh();

        List<String> roomRank = List.of("Phòng Thường", "Phòng VIP");
        roomCategoryRankCBox.getItems().setAll(roomRank);
        roomCategoryRankCBox.getSelectionModel().selectFirst();

    }

    // Thiết lập dữ liệu cho bảng
    private void setupTable() {
        roomCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomCategoryID"));
        roomCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("roomCategoryName"));
        numberOfBedColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfBed"));

        setupActionColumn();
        roomCategoryTableView.setItems(items);
    }

    // Thiết lập cột thao tác
    private void setupActionColumn() {
        Callback<TableColumn<RoomCategory, Void>, TableCell<RoomCategory, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                updateButton.setOnAction(event -> {
                    RoomCategory roomCategory = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(roomCategory);
                });

                deleteButton.setOnAction(event -> {
                    RoomCategory roomCategory = getTableView().getItems().get(getIndex());
                    handleDeleteAction(roomCategory);
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
        roomCategoryIDTextField.setText(RoomCategoryDAO.getNextRoomCategoryID());
        roomCategoryRankCBox.setDisable(false);
        roomCategoryRankCBox.getSelectionModel().selectFirst();

        roomCategoryNameTextField.setText("");
        roomCategoryRankCBox.getSelectionModel().clearSelection();
        numberOfBedTextField.setText("");

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    // Chức năng 2: Thêm
    private void handleAddAction() {
        try {
            RoomCategory roomCategory = new RoomCategory(
                    roomCategoryIDTextField.getText(),
                    roomCategoryRankCBox.getSelectionModel().getSelectedItem() + " " + roomCategoryNameTextField.getText(),
                    Integer.parseInt(numberOfBedTextField.getText())
            );

            RoomCategoryDAO.createData(roomCategory);
            handleResetAction();
            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(RoomCategory roomCategory) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa loại phòng này? Bạn sẽ mất thông tin bên GIÁ PHÒNG");
        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                RoomCategoryDAO.deleteData(roomCategory.getRoomCategoryID());
                loadData();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(RoomCategory roomCategory) {
        String[] parts = roomCategory.getRoomCategoryName().split(" ", 3);
        String rankName = parts[0] + " " + parts[1];
        String roomCategoryName = parts[2];

        roomCategoryIDTextField.setText(roomCategory.getRoomCategoryID());
        roomCategoryRankCBox.setValue(rankName);
        roomCategoryRankCBox.setDisable(true);
        roomCategoryNameTextField.setText(roomCategoryName);
        numberOfBedTextField.setText(String.valueOf(roomCategory.getNumberOfBed()));

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    // 4.2 Chức năng cập nhật
    private void handleUpdateAction() {
        try {
            RoomCategory roomCategory = new RoomCategory(
                    roomCategoryIDTextField.getText(),
                    roomCategoryRankCBox.getSelectionModel().getSelectedItem() + " " + roomCategoryNameTextField.getText(),
                    Integer.parseInt(numberOfBedTextField.getText())
            );

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật loại phòng này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    RoomCategoryDAO.updateData(roomCategory);

                    handleResetAction();
                    loadData();
                }
            });
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 5: Tìm kiếm
    private void handleSearchAction() {
        roomCategoryNameSearchField.setText("");
        numberOfBedSearchField.setText("");

        String searchText = roomCategoryIDSearchField.getValue();
        List<RoomCategory> roomCategories;

        if (searchText == null || searchText.isEmpty()) {
            roomCategories = RoomCategoryDAO.getRoomCategory();
        } else {
            roomCategories = RoomCategoryDAO.findDataByContainsId(searchText);
            if (!roomCategories.isEmpty()) {
                RoomCategory roomCategory = roomCategories.getFirst();
                roomCategoryNameSearchField.setText(roomCategory.getRoomCategoryName());
                numberOfBedSearchField.setText(String.valueOf(roomCategory.getNumberOfBed()));
            }
        }

        items.setAll(roomCategories);
        roomCategoryTableView.setItems(items);
    }



 }
