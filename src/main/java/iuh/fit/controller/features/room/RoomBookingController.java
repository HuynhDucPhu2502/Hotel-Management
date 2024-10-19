package iuh.fit.controller.features.room;

import iuh.fit.controller.features.room.room_items.RoomAvailableController;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class RoomBookingController {
    @FXML
    private GridPane roomGridPane;

    public void initialize() {
        displayRooms();
    }

    private void displayRooms() {
        List<Room> rooms = RoomDAO.getRoom();
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


}
