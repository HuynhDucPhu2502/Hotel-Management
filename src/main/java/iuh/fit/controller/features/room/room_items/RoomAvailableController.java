package iuh.fit.controller.features.room.room_items;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.ReservationFormController;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class RoomAvailableController {
    @FXML
    private Text roomNumberText;
    @FXML
    private Label roomCategoryNameLabel;

    // Context
    private MainController mainController;
    private Employee employee;
    private Room room;

<<<<<<< HEAD
    public void setRoom(Room room) {
        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
    }
=======
>>>>>>> 2574fbf6d0f106eca36c18cec6bf9666b30bfe81

    public void setupContext(MainController mainController, Employee employee, Room room) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
    }

    @FXML
    private void handleRoomClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/ReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationFormController reservationFormController = loader.getController();
            reservationFormController.setupContext(mainController, employee, room);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
