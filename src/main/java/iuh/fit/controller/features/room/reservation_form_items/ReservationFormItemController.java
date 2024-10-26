package iuh.fit.controller.features.room.reservation_form_items;

import iuh.fit.controller.MainController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ReservationFormItemController {
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

    // Context
    private MainController mainController;
    private ReservationForm reservationForm;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.forLanguageTag("vi-VN"));

    public void setupContext(MainController mainController, ReservationForm reservationForm) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;

        Customer customer = reservationForm.getCustomer();
        Employee employee = reservationForm.getEmployee();

        customerFullNameLabel.setText(customer.getFullName());
        customerIDCardNumberLabel.setText(customer.getIdCardNumber());
        employeeFullNameLabel.setText(employee.getFullName());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkoutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
    }
}
