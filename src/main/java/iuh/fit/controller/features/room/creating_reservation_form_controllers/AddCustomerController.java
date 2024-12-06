package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.ErrorMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AddCustomerController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private TextField customerIDTextField, customerNameTextField,
            customerPhoneNumberTextField, customerEmailTextField,
            customerAddressTextField, customerIDCardNumberTextField;

    @FXML
    private CalendarPicker customerDOBCalendarPicker;

    @FXML
    private ToggleGroup genderToggleGroup;

    @FXML
    private Button addBtn, resetBtn;

    @FXML
    private Button backBtn, bookingRoomNavigate,
            reservationFormNavigate;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TitledPane titledPane;

    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Customer customer;

    private static NotificationButtonController topBarController;

    public static void setController(NotificationButtonController controller){
        topBarController = controller;
    }

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
        customerIDTextField.setText(CustomerDAO.getNextCustomerID());
    }

    public void setupContext(
            MainController mainController, Employee employee, RoomWithReservation roomWithReservation,
            LocalDateTime checkInTime, LocalDateTime checkOutTime,
            NotificationButtonController controller
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        setController(controller);
        Room room = roomWithReservation.getRoom();

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;

        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());
        reservationFormNavigate.setOnAction(e -> navigateToCreateReservationFormPanel());
        backBtn.setOnAction(e -> navigateToCreateReservationFormPanel());

        // Current Panel Button
        addBtn.setOnAction(e -> handleAddAction());
        resetBtn.setOnAction(e -> handleResetAction());
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee, topBarController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
                    mainController, employee, roomWithReservation, customer, checkInTime, checkOutTime, topBarController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 4. Xử lý sự kiện thêm khách hàng và sự kiện đặt lại nhập liệu
    // ==================================================================================================================
    public void handleAddAction() {
        try {
            Customer customer = createCustomer();
            CustomerDAO.createData(customer);
            handleResetAction();
            dialogPane.showInformation("Thành công", "Đã thêm khách hàng thành công");
            this.customer = customer;
            navigateToCreateReservationFormPanel();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private Customer createCustomer() {
        String id = customerIDTextField.getText();
        String name = customerNameTextField.getText();
        String phone = customerPhoneNumberTextField.getText();
        String email = customerEmailTextField.getText();
        String address = customerAddressTextField.getText();

        Gender gender;
        RadioButton selectedGender = (RadioButton) genderToggleGroup.getSelectedToggle();
        if (selectedGender == null) {
            throw new IllegalArgumentException(ErrorMessages.CUS_GENDER_NOT_SELECTED);
        }
        gender = selectedGender.getText().equalsIgnoreCase("NAM") ? Gender.MALE : Gender.FEMALE;

        String idCardNumber = customerIDCardNumberTextField.getText();
        LocalDate dob = customerDOBCalendarPicker.getValue();

        return new Customer(id, name, phone, email, address, gender, idCardNumber, dob, ObjectStatus.ACTIVATE);
    }

    public void handleResetAction() {
        customerIDTextField.setText(CustomerDAO.getNextCustomerID());
        customerIDCardNumberTextField.clear();
        customerNameTextField.clear();
        customerPhoneNumberTextField.clear();
        customerEmailTextField.clear();
        customerAddressTextField.clear();

        Toggle selectedToggle = genderToggleGroup.getSelectedToggle();
        if (selectedToggle != null) selectedToggle.setSelected(false);

        customerDOBCalendarPicker.setValue(null);
        customerNameTextField.requestFocus();
    }
}
