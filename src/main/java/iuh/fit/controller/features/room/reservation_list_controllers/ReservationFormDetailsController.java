package iuh.fit.controller.features.room.reservation_list_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.utils.Calculator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReservationFormDetailsController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    // 1.1 Buttons
    @FXML
    private Button backBtn;
    @FXML
    private Button reservationFormListNavigate;
    @FXML
    private Button bookingRoomNavigate;

    // 1.2 Labels
    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label roomCategoryLabel;
    @FXML
    private Label checkInDateLabel;
    @FXML
    private Label checkOutDateLabel;
    @FXML
    private Label stayLengthLabel;
    @FXML
    private Label bookingDepositLabel;
    @FXML
    private Label customerIDLabel;
    @FXML
    private Label customerFullnameLabel;
    @FXML
    private Label cusomerPhoneNumberLabel;
    @FXML
    private Label customerEmailLabel;
    @FXML
    private Label customerIDCardNumberLabel;
    @FXML
    private Label employeeFullNameLabel;
    @FXML
    private Label employeePositionLabel;
    @FXML
    private Label employeeIDLabel;
    @FXML
    private Label employeePhoneNumberLabel;

    // 1.3 Formatter
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    // 1.4 Context
    private MainController mainController;
    private ReservationForm reservationForm;
    private Employee employee;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {

    }

    public void setupContext(
            MainController mainController, ReservationForm reservationForm, Employee employee) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.employee = employee;

        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        reservationFormListNavigate.setOnAction(e -> navigateToReservationListPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        setupReservationForm();
    }

    // ==================================================================================================================
    // 2.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        Room reservationFormRoom = reservationForm.getRoom();
        Customer reservationFormCustomer = reservationForm.getCustomer();
        Employee reservationFormEmployee = reservationForm.getEmployee();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomNumber());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkOutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
        stayLengthLabel.setText(Calculator.calculateStayLength(
                reservationForm.getCheckInDate(),
                reservationForm.getCheckOutDate()
        ));
        bookingDepositLabel.setText(Calculator.calculateBookingDeposit(
                reservationFormRoom,
                reservationForm.getCheckInDate(),
                reservationForm.getCheckOutDate()
        ) + " VND");

        customerIDLabel.setText(reservationFormCustomer.getCustomerID());
        customerFullnameLabel.setText(reservationFormCustomer.getFullName());
        cusomerPhoneNumberLabel.setText(reservationFormCustomer.getPhoneNumber());
        customerEmailLabel.setText(reservationFormCustomer.getEmail());
        customerIDCardNumberLabel.setText(reservationFormCustomer.getIdCardNumber());

        employeeFullNameLabel.setText(reservationFormEmployee.getFullName());
        employeePositionLabel.setText(reservationFormEmployee.getPosition().toString());
        employeeIDLabel.setText(reservationFormEmployee.getEmployeeID());
        employeePhoneNumberLabel.setText(reservationFormEmployee.getPhoneNumber());
    }


    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToReservationListPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.setupContext(
                    mainController, employee, reservationForm.getRoom()
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
