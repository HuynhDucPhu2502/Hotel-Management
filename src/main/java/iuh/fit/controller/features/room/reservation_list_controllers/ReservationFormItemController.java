package iuh.fit.controller.features.room.reservation_list_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReservationFormItemController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    // 1.1 Label
    @FXML
    private Label customerFullNameLabel;
    @FXML
    private Label customerIDCardNumberLabel;
    @FXML
    private Label employeeFullNameLabel;
    @FXML
    private Label checkInDateLabel;
    @FXML
    private Label checkoutDateLabel;

    // 1.2 Formatter
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.forLanguageTag("vi-VN"));

    // 1.3 Context
    private MainController mainController;
    private ReservationForm reservationForm;
    private Employee employee;
    private RoomWithReservation roomWithReservation;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(
            MainController mainController,
            ReservationForm reservationForm,
            Employee employee,
            RoomWithReservation roomWithReservation
    ) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        Customer customer = reservationForm.getCustomer();
        Employee reservationFormEmployee = reservationForm.getEmployee();

        customerFullNameLabel.setText(customer.getFullName());
        customerIDCardNumberLabel.setText(customer.getIdCardNumber());
        employeeFullNameLabel.setText(reservationFormEmployee.getFullName());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkoutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToReservationFormDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/iuh/fit/view/features/room/reservation_list_panels/ReservationFormDetailsPanel.fxml")
            );
            AnchorPane layout = loader.load();

            ReservationFormDetailsController reservationFormDetailsController = loader.getController();
            reservationFormDetailsController.setupContext(
                    mainController, reservationForm, employee, roomWithReservation
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
