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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Le Tran Gia Huy
 * @created 19/10/2024 - 3:24 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features.room
 */
public class RoomManagerController {
    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField roomIDTextField;

    @FXML
    private ComboBox roomCategoryComboBox;

    @FXML
    private TextField floorNumbTextField;

    @FXML
    private ComboBox roomStateComboBox;

    @FXML
    private ComboBox roomIDSearchField;

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

    private ObservableList<Room> items;

    public void initialize() {
        dialogPane.toFront();
        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetButton());
        addBtn.setOnAction(e -> handleAddAction());
    }

    private void loadData() {
        List<Room> rooms = RoomDAO.getRoom();
        items = FXCollections.observableArrayList(rooms);
        roomTableView.setItems(items);
        roomTableView.refresh();



        List<RoomStatus> roomStatusList = List.of(RoomStatus.AVAILABLE, RoomStatus.UNAVAILABLE);
        roomStateComboBox.getItems().setAll(roomStatusList);
        roomStateComboBox.getSelectionModel().selectFirst();

        List<RoomCategory> roomCategoryList = RoomCategoryDAO.getRoomCategory();
        // gán vào comboBox
        roomCategoryComboBox.getItems().setAll(roomCategoryList);
        // Mặc định chọn phần tử đầu tiên nếu comboBox không rỗng
        roomCategoryComboBox.getSelectionModel().selectFirst();

    }

    // Thiết lập dữ liệu cho bảng
    private void setupTable() {
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomStateColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        numberOfBedColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfBed"));

        setupActionColumn();
        roomTableView.setItems(items);
    }

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

    private void handleResetButton(){

    }

    private String handleRoomIDWhenAdd(){
        RoomCategory roomCategorySelected = (RoomCategory) roomCategoryComboBox.getSelectionModel().getSelectedItem();
        String newRoomID = RoomDAO.createRoomID(Integer.valueOf(floorNumbTextField.getText()), roomCategorySelected);
        return newRoomID;
    }

    private void handleAddAction() {
        String newRoomID = handleRoomIDWhenAdd();
        try {
            Room room = new Room(
                    newRoomID,
                    (RoomStatus) roomStateComboBox.getSelectionModel().getSelectedItem(),
                    LocalDateTime.now(),
                    (RoomCategory) roomCategoryComboBox.getSelectionModel().getSelectedItem()
            );

            RoomDAO.createData(room);
            dialogPane.showInformation("Thông báo", "Phòng mới được tạo với:\nMã phòng là "+newRoomID);
//            handleResetAction();
            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

//    private void handleUpdateBtn(RoomCategory roomCategory) {
//        String[] parts = roomCategory.getRoomCategoryName().split(" ", 3);
//        String rankName = parts[0] + " " + parts[1];
//        String roomCategoryName = parts[2];
//
//        roomIDTextField.setText(roomCategory.getRoomCategoryID());
//        roomCategoryRankCBox.setValue(rankName);
//        roomCategoryRankCBox.setDisable(true);
//        roomCategoryNameTextField.setText(roomCategoryName);
//        numberOfBedTextField.setText(String.valueOf(roomCategory.getNumberOfBed()));
//
//        addBtn.setManaged(false);
//        addBtn.setVisible(false);
//        updateBtn.setManaged(true);
//        updateBtn.setVisible(true);
//    }

}
