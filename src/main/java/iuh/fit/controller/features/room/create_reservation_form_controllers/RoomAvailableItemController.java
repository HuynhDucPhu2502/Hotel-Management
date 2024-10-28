package iuh.fit.controller.features.room.create_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class RoomAvailableItemController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text roomNumberText;
    @FXML
    private Label roomCategoryNameLabel;

    // Context
    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(MainController mainController, Employee employee, RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        Room room = roomWithReservation.getRoom();
        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/iuh/fit/view/features/room/create_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
                    mainController, employee, roomWithReservation,
                    null, null, null
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
