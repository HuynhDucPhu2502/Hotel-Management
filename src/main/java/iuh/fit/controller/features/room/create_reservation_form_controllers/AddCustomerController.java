package iuh.fit.controller.features.room.create_reservation_form_controllers;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.enums.Gender;
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
    // 1.1 Input Fields
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerIDCardNumberTextField;
    @FXML
    private CalendarPicker customerDOBCalendarPicker;
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

    // 1.3 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.4
    @FXML
    private TitledPane titledPane;

    // 1.5 Context
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
        dialogPane.toFront();

        customerIDTextField.setText(CustomerDAO.getNextCustomerID());

        addBtn.setOnAction(e -> handleAddAction());
        resetBtn.setOnAction(e -> handleResetAction());
    }

    public void setupContext(
            MainController mainController, Employee employee, Room room,
            LocalDateTime checkInTime, LocalDateTime checkOutTime
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;

        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());
        reservationFormNavigate.setOnAction(e -> navigateToCreateReservationFormPanel());
        backBtn.setOnAction(e -> navigateToCreateReservationFormPanel());

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

    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/create_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
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
    // ==================================================================================================================
    // 4. Thêm khách hàng
    // ==================================================================================================================
    // Chức năng 2: Thêm
    public void handleAddAction() {
        try{
            Customer customer = createCustomer();
            CustomerDAO.createData(customer);
            handleResetAction();
            dialogPane.showInformation("Thành công", "Đã thêm khách hàng thành công");
            this.customer = customer;
            navigateToCreateReservationFormPanel();
        }catch (Exception e){
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
        RadioButton rad =  (RadioButton) genderToggleGroup.getSelectedToggle();
        if (rad == null) throw new IllegalArgumentException(ErrorMessages.CUS_GENDER_NOT_SELECTED);
        else if (rad.getText().equalsIgnoreCase("NAM")) gender = Gender.MALE;
        else gender = Gender.FEMALE;

        String idCardNumber = customerIDCardNumberTextField.getText();
        LocalDate dob = customerDOBCalendarPicker.getValue();

        return new Customer(id, name, phone, email, address, gender, idCardNumber, dob);
    }

    public void handleResetAction() {
        customerIDTextField.setText(CustomerDAO.getNextCustomerID());
        customerIDCardNumberTextField.setText("");
        customerNameTextField.setText("");
        customerPhoneNumberTextField.setText("");
        customerEmailTextField.setText("");
        customerAddressTextField.setText("");
        Toggle rad =  genderToggleGroup.getSelectedToggle();
        if (rad != null) rad.setSelected(false);
        customerDOBCalendarPicker.setValue(null);
        customerNameTextField.requestFocus();
    }

}
