package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RoomOnUseItemController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text roomNumberText;
    @FXML
    private Text checkOutDateText;
    @FXML
    private Label roomCategoryNameLabel;
    @FXML
    private Label customerFullNameLabel;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

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
        Customer customer = roomWithReservation.getReservationForm().getCustomer();
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
        customerFullNameLabel.setText(customer.getFullName());
        checkOutDateText.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml"));
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
