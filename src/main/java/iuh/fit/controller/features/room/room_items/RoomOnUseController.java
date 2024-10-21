package iuh.fit.controller.features.room.room_items;

import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class RoomOnUseController {
    @FXML
    private Text roomNumberText;
    @FXML
    private Label roomCategoryNameLabel;

    private Room room;

    public void setRoom(Room room) {
        roomNumberText.setText(room.getRoomNumber());
        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }
}
