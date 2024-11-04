package iuh.fit.controller.features.room.group_booking_controllers;

import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
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
    private EventHandler<ActionEvent> roomSelectHandler;

    public void initialize() {
        setupCustomerIDCardValidation();

        // Set delete action for deleteReservationFormBtn
        deleteReservationFormBtn.setOnAction(e -> {
            if (deleteReservationHandler != null) {
                deleteReservationHandler.handle(new ActionEvent(this, null));
            }
        });

        // Set room select action for roomSelectBtn
        roomSelectBtn.setOnAction(e -> {
            if (roomSelectHandler != null) {
                roomSelectHandler.handle(new ActionEvent(this, null));
            }
        });
    }

    /**
     * Set up the context for the reservation item, including handlers for deletion and room selection.
     *
     * @param reservationForm      the reservation form associated with this item
     * @param deleteHandler        the handler to be called when deleting this reservation item
     * @param roomSelectHandler    the handler to be called when selecting a room
     */
    public void setupContext(ReservationForm reservationForm, EventHandler<ActionEvent> deleteHandler, EventHandler<ActionEvent> roomSelectHandler) {
        this.reservationForm = reservationForm;
        this.deleteReservationHandler = deleteHandler;
        this.roomSelectHandler = roomSelectHandler;
    }

    private void setupCustomerIDCardValidation() {
        customerIDCardNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                clearCustomerInfo();
            } else if (newValue.length() > 12) {
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

    public void updateRoomInfo(Room room) {
        reservationForm.setRoom(room);
        roomNumberText.setText(room.getRoomNumber());
        roomCategoryName.setText(room.getRoomCategory().getRoomCategoryName());
    }
}
