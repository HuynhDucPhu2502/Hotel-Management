package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.RoomStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RoomManagerController {

    // Input Fields
    @FXML
    private ComboBox<RoomCategory> roomCategoryComboBox;
    @FXML
    private TextField floorNumbTextField;
    @FXML
    private ComboBox<RoomStatus> roomStateComboBox;

    // Search Fields
    @FXML
    private ComboBox<String> roomIDSearchField;
    @FXML
    private TextField roomStateSearchField;
    @FXML
    private TextField numberOfBedSearchField;

    // Table
    @FXML
    private TableView<Room> roomTableView;
    @FXML
    private TableColumn<Room, String> roomIDColumn;
    @FXML
    private TableColumn<Room, Double> roomStateColumn;
    @FXML
    private TableColumn<Room, String> numberOfBedColumn;
    @FXML
    private TableColumn<Room, Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // Dialog Pane
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Room> items;

    // Gọi mấy phương thức để gắn sự kiện và dữ liệu cho lúc đầu khởi tạo giao diện
    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        List<Room> rooms = RoomDAO.getRoom();
        items = FXCollections.observableArrayList(rooms);
        roomTableView.setItems(items);
        roomTableView.refresh();

        List<RoomStatus> roomStatusList = List.of(RoomStatus.AVAILABLE, RoomStatus.UNAVAILABLE);
        roomStateComboBox.getItems().setAll(roomStatusList);
        roomStateComboBox.getSelectionModel().selectFirst();

        List<RoomCategory> roomCategoryList = RoomCategoryDAO.getRoomCategory();
        roomCategoryComboBox.getItems().setAll(roomCategoryList);
        roomCategoryComboBox.getSelectionModel().selectFirst();

    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomStateColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        numberOfBedColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfBed"));

        setupActionColumn();
        roomTableView.setItems(items);
    }

    // setup cho cột thao tác
    // THAM KHẢO
    private void setupActionColumn() {
        Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                updateButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
//                    handleUpdateBtn(roomCategory);
                });

                deleteButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
//                    handleDeleteAction(roomCategory);
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
        floorNumbTextField.clear();
        roomCategoryComboBox.getSelectionModel().selectFirst();
        roomStateComboBox.getSelectionModel().selectFirst();

        roomTableView.getSelectionModel().clearSelection();

        addBtn.setVisible(true);
        updateBtn.setVisible(false);

        floorNumbTextField.requestFocus();
    }

    // Chức năng 2: Thêm
    private void handleAddAction() {
        try {
            String newRoomID = handleRoomIDGenerate();
            Room room = new Room(
                    newRoomID,
                    roomStateComboBox.getSelectionModel().getSelectedItem(),
                    LocalDateTime.now(),
                    roomCategoryComboBox.getSelectionModel().getSelectedItem()
            );

            RoomDAO.createData(room);

            dialogPane.showInformation("Thông báo", "Phòng mới được tạo thành công với mã phòng: " + newRoomID);

            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private String handleRoomIDGenerate() {
        RoomCategory roomCategorySelected = roomCategoryComboBox.getSelectionModel().getSelectedItem();
        int floorNumb = Integer.parseInt(floorNumbTextField.getText());

        return RoomDAO.roomIDGenerate(floorNumb, roomCategorySelected);
    }

    // Chức năng 3: Xóa
    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    // 4.2 Chức năng cập nhật
    // Chức năng 5: Tìm kiếm



}
