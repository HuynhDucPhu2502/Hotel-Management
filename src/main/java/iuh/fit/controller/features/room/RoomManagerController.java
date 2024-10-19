package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import javafx.fxml.FXML;

/**
 * @author Le Tran Gia Huy
 * @created 19/10/2024 - 3:24 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features.room
 */
public class RoomManagerController {
    @FXML
    private DialogPane dialogPane;

    public void initialize() {
        dialogPane.toFront();
    }

}
