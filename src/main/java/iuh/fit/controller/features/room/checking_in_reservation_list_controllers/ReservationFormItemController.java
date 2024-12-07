package iuh.fit.controller.features.room.checking_in_reservation_list_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
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
    @FXML
    private Label customerFullNameLabel, customerIDCardNumberLabel, employeeFullNameLabel,
            checkInDateLabel, checkoutDateLabel;
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private ReservationForm reservationForm;
    private Employee employee;
    private RoomWithReservation roomWithReservation;
    private NotificationButtonController notificationButtonController;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(
            MainController mainController,
            ReservationForm reservationForm,
            Employee employee,
            RoomWithReservation roomWithReservation,
            NotificationButtonController notificationButtonController
    ) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.notificationButtonController = notificationButtonController;

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
                    getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationFormDetailsPanel.fxml")
            );
            AnchorPane layout = loader.load();

            ReservationFormDetailsController reservationFormDetailsController = loader.getController();
            reservationFormDetailsController.setupContext(
                    mainController, reservationForm, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
