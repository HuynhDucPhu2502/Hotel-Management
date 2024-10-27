package iuh.fit.controller.features.room;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.create_reservation_form_controllers.RoomAvailableItemController;
import iuh.fit.controller.features.room.create_reservation_form_controllers.RoomOnUseItemController;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RoomBookingController {
    @FXML
    private GridPane roomGridPane;

    @FXML
    private ComboBox<String> roomCategoryCBox;
    @FXML
    private ComboBox<String> roomFloorNumberCBox;

    @FXML
    private Button allBtn;
    @FXML
    private Button availableBtn;
    @FXML
    private Button onUseBtn;
    @FXML
    private Button overDueBtn;

    private List<Room> rooms;
    private MainController mainController;
    private Employee employee;

    private Button activeButton; // Lưu lại nút đang được nhấn
    private RoomStatus selectedStatus = null; // Trạng thái được chọn (mặc định null cho allBtn)

    public void initialize() {
        activeButton = allBtn; // Đặt mặc định là allBtn
        setActiveButtonStyle(allBtn);
    }

    public void setupContext(MainController mainController, Employee employeee) {
        this.mainController = mainController;
        this.employee = employeee;
        loadData();
        setupEventHandlers();
    }

    private void loadData() {
        rooms = RoomDAO.getRoom();
        rooms = RoomDAO.getRoom().stream()
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .toList();

        roomCategoryCBox.getItems().setAll(getRoomCategories());
        roomCategoryCBox.getSelectionModel().selectFirst();

        roomFloorNumberCBox.getItems().setAll(getFloorNumbers());
        roomFloorNumberCBox.getSelectionModel().selectFirst();

        displayFilteredRooms(rooms);
    }

    private List<String> getRoomCategories() {
        List<String> roomCategoryList = RoomCategoryDAO.getRoomCategory().stream()
                .map(rc -> rc.getRoomCategoryID() + " " + rc.getRoomCategoryName())
                .collect(Collectors.toList());
        roomCategoryList.addFirst("TẤT CẢ");
        return roomCategoryList;
    }

    private List<String> getFloorNumbers() {
        return List.of("TẤT CẢ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    }

    private void displayFilteredRooms(List<Room> rooms) {
        roomGridPane.getChildren().clear();

        int row = 0, col = 0;

        try {
            for (Room room : rooms) {
                FXMLLoader loader;
                Pane roomItem;

                switch (room.getRoomStatus()) {
                    case AVAILABLE -> {
                        loader = new FXMLLoader(getClass().getResource(
                                "/iuh/fit/view/features/room/create_reservation_form_panels/RoomAvailableItem.fxml"));
                        roomItem = loader.load();

                        RoomAvailableItemController controller = loader.getController();
                        controller.setupContext(mainController, employee, room);
                    }
                    case ON_USE -> {
                        loader = new FXMLLoader(getClass().getResource(
                                "/iuh/fit/view/features/room/create_reservation_form_panels/RoomOnUseItem.fxml"));
                        roomItem = loader.load();

                        RoomOnUseItemController controller = loader.getController();
                        controller.setRoom(room);
                    }
                    case OVERDUE -> {
                        loader = new FXMLLoader(getClass().getResource(
                                "/iuh/fit/view/features/room/create_reservation_form_panels/RoomOverDueItem.fxml"));
                        roomItem = loader.load();
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + room.getRoomStatus());
                }

                roomGridPane.add(roomItem, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSearch() {
        List<Room> filteredRooms = rooms;

        String selectedCategory = roomCategoryCBox.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && !selectedCategory.equals("TẤT CẢ")) {
            String categoryID = selectedCategory.split(" ")[0];
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getRoomCategory().getRoomCategoryID().equals(categoryID))
                    .collect(Collectors.toList());
        }

        String selectedFloor = roomFloorNumberCBox.getSelectionModel().getSelectedItem();
        if (selectedFloor != null && !selectedFloor.equals("TẤT CẢ")) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getRoomID().charAt(2) == selectedFloor.charAt(0))
                    .collect(Collectors.toList());
        }

        if (selectedStatus != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(room -> room.getRoomStatus() == selectedStatus)
                    .collect(Collectors.toList());
        }

        filteredRooms = filteredRooms.stream()
                .sorted(Comparator.comparing(Room::getRoomNumber))
                .collect(Collectors.toList());

        displayFilteredRooms(filteredRooms);
    }

    private void setupEventHandlers() {
        setupButtonAction(allBtn, null); // Hiển thị tất cả
        setupButtonAction(availableBtn, RoomStatus.AVAILABLE);
        setupButtonAction(onUseBtn, RoomStatus.ON_USE);
        setupButtonAction(overDueBtn, RoomStatus.OVERDUE);

        roomCategoryCBox.setOnAction(e -> handleSearch());
        roomFloorNumberCBox.setOnAction(e -> handleSearch());
    }

    private void setupButtonAction(Button button, RoomStatus status) {
        button.setOnAction(e -> {
            if (activeButton == button) {
                resetButtonStyle(button);
                activeButton = allBtn;
                setActiveButtonStyle(allBtn);
                selectedStatus = null;
            } else {
                if (activeButton != null) resetButtonStyle(activeButton);
                setActiveButtonStyle(button);
                activeButton = button;
                selectedStatus = status;
            }
            handleSearch();
        });
    }

    private void setActiveButtonStyle(Button button) {
        switch (button.getId()) {
            case "allBtn" -> button.getStyleClass().add("button-All-selected");
            case "availableBtn" -> button.getStyleClass().add("button-Available-selected");
            case "onUseBtn" -> button.getStyleClass().add("button-OnUse-selected");
            case "overDueBtn" -> button.getStyleClass().add("button-OverDue-selected");
            default -> throw new IllegalArgumentException("Không tìm thấy button ID");
        }
    }

    private void resetButtonStyle(Button button) {
        switch (button.getId()) {
            case "allBtn" -> button.getStyleClass().remove("button-All-selected");
            case "availableBtn" -> button.getStyleClass().remove("button-Available-selected");
            case "onUseBtn" -> button.getStyleClass().remove("button-OnUse-selected");
            case "overDueBtn" -> button.getStyleClass().remove("button-OverDue-selected");
            default -> throw new IllegalArgumentException("Không tìm thấy button ID");
        }
    }

}
