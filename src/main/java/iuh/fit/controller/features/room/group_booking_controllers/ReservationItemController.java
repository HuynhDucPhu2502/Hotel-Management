package iuh.fit.controller.features.room.group_booking_controllers;

import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.ReservationForm;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ReservationItemController {
    @FXML
    private TextField customerIDCardNumberTextField, customerFullnameTextField,
            customerPhoneNumberTextField, roomBookingDepositTextField;

    @FXML
    private Button roomSelectBtn, deleteReservationFormBtn;

    @FXML
    private Text roomNumberText, roomCategoryName;

    private ReservationForm reservationForm;
    private EventHandler<ActionEvent> deleteReservationHandler;

    public void initialize() {
        setupCustomerIDCardValidation();

        deleteReservationFormBtn.setOnAction(e -> {
            if (deleteReservationHandler != null) {
                deleteReservationHandler.handle(new ActionEvent(this, null));
            }
        });
    }

    public void setupContext(ReservationForm reservationForm, EventHandler<ActionEvent> deleteHandler) {
        this.reservationForm = reservationForm;
        this.deleteReservationHandler = deleteHandler;
    }

    // ==================================================================================================================
    // 2. Cài đặt các thành phần giao diện liên quan đến Khách Hàng
    // ==================================================================================================================
    private void setupCustomerIDCardValidation() {
        customerIDCardNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) clearCustomerInfo();
            else if (newValue.length() > 12) {
                handleInputExceedsLimit(oldValue);
            } else if (newValue.length() == 12) {
                validateIDCardNumber(newValue);
            } else if (reservationForm.getCustomer() != null) {
                clearCustomerInfo();
            }
        });
    }

    private void handleInputExceedsLimit(String oldValue) {
        Platform.runLater(() -> customerIDCardNumberTextField.setText(oldValue));
    }

    private void validateIDCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidIDCardNumber(idCardNumber)) {
            clearCustomerInfo();
        } else {
            Customer customer = CustomerDAO.getDataByIDCardNumber(idCardNumber);
            if (customer == null) {
                clearCustomerInfo();
            } else {
                reservationForm.setCustomer(customer);
                setCustomerInfo();
            }
        }
    }

    private void clearCustomerInfo() {
        reservationForm.setCustomer(null);
        customerFullnameTextField.clear();
        customerPhoneNumberTextField.clear();
    }

    private void setCustomerInfo() {
        customerFullnameTextField.setText(reservationForm.getCustomer().getFullName());
        customerPhoneNumberTextField.setText(reservationForm.getCustomer().getPhoneNumber());
    }

    public Button getRoomSelectBtn() {
        return roomSelectBtn;
    }

    public Button getDeleteReservationFormBtn() {
        return deleteReservationFormBtn;
    }
}
