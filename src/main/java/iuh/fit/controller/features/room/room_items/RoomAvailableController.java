package iuh.fit.controller.features.room.room_items;

import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class RoomAvailableController {
    @FXML
    private Text roomNumberTextField;
    @FXML
    private Label roomCategoryNameLabel;

    private Room room;

    public void setRoom(Room room) {
        String roomNumber = room.getRoomID().substring(2);
        String roomCategoryName = room.getRoomCategory().getRoomCategoryName();

        roomNumberTextField.setText(roomNumber);
        roomCategoryNameLabel.setText(roomCategoryName);
    }
}
