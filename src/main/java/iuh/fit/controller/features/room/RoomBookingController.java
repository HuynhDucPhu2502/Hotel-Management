package iuh.fit.controller.features.room;

import iuh.fit.controller.features.room.room_items.RoomAvailableController;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Room;
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

    // Combo Boxes
    @FXML
    private ComboBox<String> roomCategoryCBox;
    @FXML
    private ComboBox<String> roomFloorNumberCBox;

    // Buttons
    @FXML
    private Button allBtn;
    @FXML
    private Button availableBtn;
    @FXML
    private Button onUseBtn;
    @FXML
    private Button overDueBtn;

    private List<Room> rooms;

    public void initialize() {
        loadData();
        setupEventHandlers();
    }

    private void loadData() {
        rooms = RoomDAO.getRoom();

        List<String> roomCategories = RoomCategoryDAO.getRoomCategory()
                .stream()
                .map(roomCategory -> roomCategory.getRoomCategoryID() +
                        " " + roomCategory.getRoomCategoryName())
                .collect(Collectors.toList());
        roomCategories.add(0, "TẤT CẢ");
        roomCategoryCBox.getItems().setAll(roomCategories);

        if (!roomCategoryCBox.getItems().isEmpty())
            roomCategoryCBox.getSelectionModel().selectFirst();

        List<String> floorNumbers = List.of(
                "TẤT CẢ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        );
        roomFloorNumberCBox.getItems().setAll(floorNumbers);
        roomFloorNumberCBox.getSelectionModel().selectFirst();

        displayFilteredRooms(rooms);
    }

    private void displayFilteredRooms(List<Room> rooms) {
        roomGridPane.getChildren().clear();

        int row = 0;
        int col = 0;

        try {
            for (Room room : rooms) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/iuh/fit/view/features/room/room_items/RoomAvailableItem.fxml"));

                Pane roomItem = loader.load();

                RoomAvailableController controller = loader.getController();
                controller.setRoom(room);

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

        filteredRooms = filteredRooms.stream()
                .sorted(Comparator.comparing(Room::getRoomNumer))
                .collect(Collectors.toList());

        displayFilteredRooms(filteredRooms);
    }

    public void setupEventHandlers() {
        roomCategoryCBox.setOnAction(e -> handleSearch());
        roomFloorNumberCBox.setOnAction(e -> handleSearch());
    }


}
