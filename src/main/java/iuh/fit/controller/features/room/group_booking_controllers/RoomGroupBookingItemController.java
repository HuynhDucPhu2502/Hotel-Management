package iuh.fit.controller.features.room.group_booking_controllers;

import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RoomGroupBookingItemController {

    @FXML
    private Label roomNumberLabel, roomCategoryLabel;

    @FXML
    private Button roomSelectBtn;

    public void setupContext(Room room) {
        roomNumberLabel.setText(room.getRoomNumber());
        roomCategoryLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    public Button getRoomSelectBtn() {
        return roomSelectBtn;
    }
}
