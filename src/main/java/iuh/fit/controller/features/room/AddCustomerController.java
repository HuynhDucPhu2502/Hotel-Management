package iuh.fit.controller.features.room;

import iuh.fit.controller.MainController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;

public class AddCustomerController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    // 1.1 Input Fields
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerCCCDTextField;
    @FXML
    private DatePicker customerDOBDatePicker;
    @FXML
    private TextField customerEmailTextField;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerPhoneNumberTextField;
    @FXML
    private ToggleGroup genderToggleGroup;
    @FXML
    private RadioButton radFemale;
    @FXML
    private RadioButton radMale;
    // 1.2 Buttons
    @FXML
    private Button addBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button bookingRoomNavigate;
    @FXML
    private Button reservationFormNavigate;
    @FXML
    private Button backBtn;
    // 1.3 Context
    private MainController mainController;
    private Employee employee;
    private Room room;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private Customer customer;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
    }

    public void setupContext(
            MainController mainController, Employee employee, Room room,
            LocalDateTime checkInTime, LocalDateTime checkOutTime
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;

        bookingRoomNavigate.setOnAction(e -> navigateToRoomBooking());
        reservationFormNavigate.setOnAction(e -> navigateToReservationForm());
        backBtn.setOnAction(e -> navigateToReservationForm());

    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBooking() {
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

    private void navigateToReservationForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/ReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationFormController reservationFormController = loader.getController();
            reservationFormController.setupContext(
                    mainController, employee, room,
                    customer,
                    checkInTime,
                    checkOutTime
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
