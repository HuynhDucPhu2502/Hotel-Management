package iuh.fit.controller.features.room.room_changing_controllers;

import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class RoomAvailableChangingItemController {
    @FXML
    private Text roomNumberText;
    @FXML
    private Label roomCategoryNameLabel;
    @FXML
    private Button changingBtn;

    public void setupContext(Room room) {

        roomNumberText.setText(room.getRoomNumber());
        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    public Button getChangingBtn() {
        return changingBtn;
    }
}
