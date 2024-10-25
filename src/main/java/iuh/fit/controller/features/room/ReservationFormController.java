package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;
import iuh.fit.controller.MainController;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.utils.CostCalculator;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class ReservationFormController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    // 1.1 Buttons
    @FXML
    private Button backBtn;
    @FXML
    private Button bookingRoomNavigate;
    @FXML
    private Button createCustomerBtn;
    @FXML
    private Button addBtn;
    // 1.2 Input Fields
    // 1.2.1 Fields cho phiếu đặt phòng
    @FXML
    private Label roomNumberLabel;
    @FXML
    private Label categoryNameLabel;
    @FXML
    private DateRangePicker bookDateRangePicker;
    @FXML
    private TimePicker checkInTimePicker;
    @FXML
    private TextField checkInDateTextField;
    @FXML
    private TimePicker checkOutTimePicker;
    @FXML
    private TextField checkOutDateTextField;
    @FXML
    private Label stayLengthLabel;
    @FXML
    private Label bookingDepositLabel;
    // 1.2.2 Fields cho thông tin khách hàng đặt phòng
    @FXML
    private TextField customerIDCardNumberTextField;
    @FXML
    private TextField customerFullnameTextField;
    @FXML
    private TextField customerPhoneNumberTextField;

    // 1.2.3 Fields cho thông tin tiếp tân làm phiếu đặt phòng
    @FXML
    private Label employeeIDLabel;
    @FXML
    private Label employeeFullNameLabel;
    @FXML
    private Label employeePositionLabel;
    @FXML
    private Label employeePhoneNumberLabel;

    // 1.3 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.4. Context và dữ liệu binding
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
        setupTimeComponents();
        setupCustomerIDCardValidation();
    }

    public void setupContext(
            MainController mainController, Employee employee, Room room,
            Customer customer, LocalDateTime checkInTime, LocalDateTime checkOutTime
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        backBtn.setOnAction(e -> navigateToRoomBooking());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBooking());
        createCustomerBtn.setOnAction(e -> navigateToAddCustomer());

        setupRoomInformation();
        setupEmployeeInformation();

        if (customer != null) {
            customerIDCardNumberTextField.setText(customer.getIdCardNumber());
        }

        if (checkInTime != null && checkOutTime != null) {
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
            setBookingDates(checkInTime, checkOutTime);
        }

        addBtn.setOnAction(e -> handleCreateReservationRoom());
    }

    // ==================================================================================================================
    // 3. Xử lý đổi các biến thời gian thành chuỗi ngày + giờ
    // ==================================================================================================================
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    private String formatDateTime(java.time.LocalDate date, java.time.LocalTime time) {
        return dateTimeFormatter.format(date) + " " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.forLanguageTag("vi-VN"));
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==================================================================================================================
    // 4. Đẩy dữ liệu phòng và nhân viên lên giao diện
    // ==================================================================================================================
    // 4.1 Hiển thị dữ liệu phòng và loại phòng lên giao diện
    private void setupRoomInformation() {
        roomNumberLabel.setText(room.getRoomNumber());
        categoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    // 4.2 Hiển thị nhân viên lên giao diện
    private void setupEmployeeInformation() {
        employeeIDLabel.setText(employee.getEmployeeID());
        employeeFullNameLabel.setText(employee.getFullName());
        employeePositionLabel.setText(employee.getPosition().toString());
        employeePhoneNumberLabel.setText(employee.getPhoneNumber());
    }

    // ==================================================================================================================
    // 5. Xử lý chức năng hiển thị panel khác
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

    private void navigateToAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/AddCustomerPanel.fxml"));
            AnchorPane layout = loader.load();

            AddCustomerController addCustomerController = loader.getController();
            addCustomerController.setupContext(
                    mainController, employee, room,
                    checkInTime, checkOutTime
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // ==================================================================================================================
    // 6. Cài đặt các thành phần giao diện liên quan đến thời gian
    // ==================================================================================================================

    // 6.1 Cài đặt TimePicker, DateRangePicker và binding Data
    // vào checkInDateTextField và checkOutDateTextField
    private void setupTimeComponents() {
        checkInTimePicker.setTime(null);
        checkInTimePicker.setStepRateInMinutes(15);
        checkOutTimePicker.setTime(null);
        checkOutTimePicker.setStepRateInMinutes(15);

        bookDateRangePicker.setValue(new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(7)));
        bookDateRangePicker.setFormatter(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        bookDateRangePicker.getDateRangeView().getPresets().setAll(
                createThreeDaysPreset(),
                createFiveDaysPreset(),
                createSevenDaysPreset()
        );

        ObjectProperty<DateRange> selectedRangeProperty = bookDateRangePicker.valueProperty();

        checkInDateTextField.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    DateRange range = bookDateRangePicker.getValue();
                    LocalTime checkInTime = checkInTimePicker.getTime();
                    if (range != null && checkInTime != null) {
                        return formatDateTime(range.getStartDate(), checkInTime);
                    }
                    return "";
                }, selectedRangeProperty, checkInTimePicker.timeProperty())
        );

        checkOutDateTextField.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    DateRange range = bookDateRangePicker.getValue();
                    LocalTime checkOutTime = checkOutTimePicker.getTime();
                    if (range != null && checkOutTime != null) {
                        return formatDateTime(range.getEndDate(), checkOutTime);
                    }
                    return "";
                }, selectedRangeProperty, checkOutTimePicker.timeProperty())
        );

        setupDateTimeListeners();
    }

    // 6.2 Lắng nghe sự kiện thay đổi trên checkInDateTextField,
    // checkOutDateTextField để kích hoạt sự kiện tính toán ngày
    // lưu trú và số tiền đặt cọc
    private void setupDateTimeListeners() {
        checkInDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkInTime = parseDateTime(newValue);
                updateStayLengthAndCost();
            }
        });

        checkOutDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkOutTime = parseDateTime(newValue);
                updateStayLengthAndCost();
            }
        });
    }

    // 6.3 Tạo các preset cho DateRangePicker
    // 6.3.1 Preset 3 ngày
    private DateRangePreset createThreeDaysPreset() {
        return new DateRangePreset("3 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(3)));
    }

    // 6.3.2 Preset 5 ngày
    private DateRangePreset createFiveDaysPreset() {
        return new DateRangePreset("5 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    // 6.3.3 Preset 7 ngày
    private DateRangePreset createSevenDaysPreset() {
        return new DateRangePreset("7 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    // ==================================================================================================================
    // 7. Tính toán thời gian lưu trú và tiền đặt cọc
    // ==================================================================================================================
    private void updateStayLengthAndCost() {
        if (checkInTime != null && checkOutTime != null) {
            String stayLength = calculateStayLength();
            stayLengthLabel.setText(stayLength);

            try {
                double totalCost = CostCalculator.calculateBookingDeposit(room, checkInTime, checkOutTime);
                bookingDepositLabel.setText(String.format("%.2f VND", totalCost));
            } catch (IllegalArgumentException e) {
                bookingDepositLabel.setText(e.getMessage());
            }
        } else {
            stayLengthLabel.setText(GlobalConstants.STAY_LENGTH_EMPTY);
            bookingDepositLabel.setText(GlobalConstants.BOOKING_DEPOSIT_EMPTY);
        }
    }

    private String calculateStayLength() {
        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        double days = hours / 24.0;

        if (hours < 12) {
            return hours + " giờ";
        } else {
            double roundedDays = Math.ceil(days * 2) / 2.0;
            return roundedDays + " ngày";
        }
    }

    // ==================================================================================================================
    // 8. Cài đặt các thành phần giao diện liên quan đến Khách Hàng
    // ==================================================================================================================
    private void setupCustomerIDCardValidation() {
        customerIDCardNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 12) {
                handleInputExceedsLimit(oldValue);
            } else if (newValue.length() == 12) {
                validateIDCardNumber(newValue);
            } else if (customer != null) {
                clearCustomerInfo();
            }
        });
    }

    private void handleInputExceedsLimit(String oldValue) {
        Platform.runLater(() -> customerIDCardNumberTextField.setText(oldValue));
        dialogPane.showError("LỖI", ErrorMessages.ID_CARD_NUMBER_OVER_LIMIT);
    }

    private void validateIDCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidIDCardNumber(idCardNumber)) {
            dialogPane.showError("LỖI", ErrorMessages.INVALID_ID_CARD_NUMBER);
        } else {
            customer = CustomerDAO.getDataByIDCardNumber(idCardNumber);
            if (customer == null) {
                dialogPane.showError("LỖI", ErrorMessages.CUS_NOT_FOUND);
                clearCustomerInfo();
            } else {
                setCustomerInfo();
            }
        }
    }

    private void clearCustomerInfo() {
        customer = null;
        customerFullnameTextField.clear();
        customerPhoneNumberTextField.clear();
        dialogPane.requestFocus();
    }

    private void setCustomerInfo() {
        customerFullnameTextField.setText(customer.getFullName());
        customerPhoneNumberTextField.setText(customer.getPhoneNumber());
    }

    // ==================================================================================================================
    // 9. Đẩy thời gian lên giao diện nếu checkInDate và checkOutDate không NULL
    // ==================================================================================================================
    private void setBookingDates(LocalDateTime checkInDate, LocalDateTime checkOutDate) {
            bookDateRangePicker.setValue(new DateRange("Chọn Lịch Đặt Phòng",
                    checkInDate.toLocalDate(), checkOutDate.toLocalDate()));

            checkInTimePicker.setTime(checkInDate.toLocalTime());
            checkOutTimePicker.setTime(checkOutDate.toLocalTime());
    }

    // ==================================================================================================================
    // 10. Đẩy thời gian lên giao diện nếu checkInDate và checkOutDate không NULL
    // ==================================================================================================================
    private void handleCreateReservationRoom() {
        try {
            ReservationForm reservationForm = new ReservationForm(
                    ReservationFormDAO.getNextReservationFormID(),
                    LocalDateTime.now(),
                    checkInTime,
                    checkOutTime,
                    employee,
                    room,
                    customer
            );
            ReservationFormDAO.createData(reservationForm);
            dialogPane.showInformation("Thành công", "Đã thêm phiếu đặt phòng thành công");
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }
}
