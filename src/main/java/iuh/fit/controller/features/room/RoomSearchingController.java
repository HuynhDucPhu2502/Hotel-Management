package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomWithReservationDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Room;
import iuh.fit.models.enums.Position;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoomSearchingController {

    // Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // Search Fields
    @FXML
    private TextField roomIDSearchField;
    @FXML
    private ComboBox<String> roomStatusSearchField, roomCategorySearchField;
    @FXML
    private DatePicker dateOfCreationLowerBoundSearchField,
            dateOfCreationUpperBoundSearchField;

    // Table
    @FXML
    private TableView<Room> roomTableView;
    @FXML
    private TableColumn<Room, String> roomIDColumn;
    @FXML
    private TableColumn<Room, String> roomStatusColumn;
    @FXML
    private TableColumn<Room, LocalDateTime> dateOfCreationColumn;
    @FXML
    private TableColumn<Room, String> roomCategoryColumn;

    // Buttons
    @FXML
    private Button searchBtn, resetBtn;
    private ObservableList<Room> items;

    private MainController mainController;
    private Account account;

    public void setupContext(MainController mainController, Account account) {
        this.mainController = mainController;
        this.account = account;

        loadData();
        setupTable();
        roomTableView.setFixedCellSize(25);
    }

    public void initialize() {
        searchBtn.setOnAction(e -> handleSearchAction());
        resetBtn.setOnAction(e -> handleResetAction());
        dialogPane.toFront();
    }

    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                List<Room> roomList = RoomDAO.getRoom();
                items = FXCollections.observableArrayList(roomList);

                List<String> comboBoxItems = RoomCategoryDAO.getRoomCategory()
                        .stream()
                        .map(roomCategory -> roomCategory.getRoomCategoryID() +
                                " " + roomCategory.getRoomCategoryName())
                        .collect(Collectors.toList());
                comboBoxItems.addFirst("KHÔNG CÓ");
                comboBoxItems.addFirst("TẤT CẢ");

                Platform.runLater(() -> {
                    roomTableView.setItems(items);
                    roomTableView.refresh();

                    roomCategorySearchField.getItems().setAll(comboBoxItems);
                    roomStatusSearchField.getItems().setAll(
                            Stream.of(RoomStatus.values()).map(Enum::name).toList()
                    );
                    roomStatusSearchField.getSelectionModel().selectFirst();
                    roomCategorySearchField.getSelectionModel().selectFirst();
                });
                return null;
            }
        };

        new Thread(loadDataTask).start();
    }

    private void setupTable() {
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomStatusColumn.setCellValueFactory(new PropertyValueFactory<>("roomStatus"));
        dateOfCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfCreation"));
        roomCategoryColumn.setCellValueFactory(data -> {
            String categoryName = data.getValue().getRoomCategory().getRoomCategoryName();
            return new SimpleStringProperty(categoryName != null ? categoryName : "KHÔNG CÓ");
        });

        roomTableView.setItems(items);
        setupTableContextMenu();
    }

    private void setupTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem roomManageMenuItem = new MenuItem("Chỉnh sửa");
        MenuItem roomReservationManageMenuItem = new MenuItem("Quản lý đặt phòng");

        roomReservationManageMenuItem.setOnAction(event -> {
            Room room = roomTableView.getSelectionModel().getSelectedItem();
            if (room != null) {
                try {
                    handleCreateReservationForm(room);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        contextMenu.getItems().add(roomReservationManageMenuItem);

        if (account.getEmployee().getPosition() == Position.MANAGER) {
            roomManageMenuItem.setOnAction(event -> {
                Room room = roomTableView.getSelectionModel().getSelectedItem();
                if (room != null) {
                    try {
                        handleEditRoom(room);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            contextMenu.getItems().add(roomManageMenuItem);
        }

        roomTableView.setContextMenu(contextMenu);
    }

    private void handleEditRoom(Room room) throws IOException {
        if (room.getRoomStatus() != RoomStatus.AVAILABLE) {
            dialogPane.showInformation("LỖI", "Phòng này đang được sử dụng");
            return;
        }
        mainController.loadPanelRoomManagerController("/iuh/fit/view/features/room/RoomManagerPanel.fxml", room);
    }

    private void handleCreateReservationForm(Room room) throws IOException {
        RoomWithReservation roomWithReservations = RoomWithReservationDAO.getRoomWithReservationByRoomId(room.getRoomID());
        mainController.loadPanelCreateReservationFormController("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml", mainController, account, roomWithReservations);
    }

    private void handleResetAction() {
        roomIDSearchField.setText("");
        roomStatusSearchField.getSelectionModel().clearSelection();
        dateOfCreationLowerBoundSearchField.setValue(null);
        dateOfCreationUpperBoundSearchField.setValue(null);
        roomCategorySearchField.getSelectionModel().clearSelection();

        loadData();
    }

    private void handleSearchAction() {
        Task<ObservableList<Room>> searchTask = new Task<>() {
            @Override
            protected ObservableList<Room> call() {
                String roomID = roomIDSearchField.getText().isBlank() ? null : roomIDSearchField.getText().trim();
                String roomStatus = roomStatusSearchField.getSelectionModel().getSelectedItem();
                LocalDateTime lowerDate = handleDateInput(dateOfCreationLowerBoundSearchField.getValue());
                LocalDateTime upperDate = handleDateInput(dateOfCreationUpperBoundSearchField.getValue());
                String selectedCategory = roomCategorySearchField.getSelectionModel().getSelectedItem();
                String categoryID = handleCategoryIDInput(selectedCategory);

                List<Room> searchResults = RoomDAO.searchRooms(roomID, roomStatus, lowerDate, upperDate, categoryID);
                return FXCollections.observableArrayList(searchResults);
            }
        };

        searchTask.setOnSucceeded(e -> {
            items = searchTask.getValue();
            roomTableView.setItems(items);
        });

        new Thread(searchTask).start();
    }

    private LocalDateTime handleDateInput(LocalDate date) {
        return (date != null) ? date.atStartOfDay() : null;
    }

    private String handleCategoryIDInput(String selectedCategory) {
        if (selectedCategory == null) return null;
        if (selectedCategory.equalsIgnoreCase("TẤT CẢ")) return "ALL";
        if (selectedCategory.equalsIgnoreCase("KHÔNG CÓ")) return "NULL";
        return selectedCategory.split(" ")[0];
    }
}
