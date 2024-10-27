package iuh.fit.controller.features.room.create_reservation_form_controllers;

import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class RoomOnUseItemController {
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
