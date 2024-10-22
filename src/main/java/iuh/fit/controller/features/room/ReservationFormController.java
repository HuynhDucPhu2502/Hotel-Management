package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.TimePicker;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;
import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.utils.CostCalculator;
import iuh.fit.utils.GlobalConstants;
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
    // Buttons
    @FXML
    private Button backBtn;

    // Input Fields
    // 1. Thông tin phòng
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
    // 2. Thông tin khách hàng
    // 3. Thông tin nhân viên
    @FXML
    private Label employeeIDLabel;
    @FXML
    private Label employeeFullNameLabel;

    // Context
    private MainController mainController;
    private Employee employee;
    private Room room;

    //
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // =========================================================
    // 1. Khởi tạo và nạp dữ liệu vào giao diện
    // =========================================================
    public void initialize() {
        setupTimeComponents();
    }

    public void setupContext(MainController mainController, Employee employee, Room room) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        backBtn.setOnAction(e -> handleBackBtn());

        setupRoomInformation();
        setupEmployeeInformation();
    }

    // =========================================================
    // 2. Xử lý đổi các biến thời gian thành chuỗi ngày + giờ
    // =========================================================
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

    // =========================================================
    // 3. Đẩy dữ liệu phòng và nhân viên lên giao diện
    // =========================================================
    // 3.1 Hiển thị dữ liệu phòng và loại phòng lên giao diện
    private void setupRoomInformation() {
        roomNumberLabel.setText(room.getRoomNumber());
        categoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    // 3.2 Hiển thị nhân viên lên giao diện
    private void setupEmployeeInformation() {
        employeeIDLabel.setText(employee.getEmployeeID());
        employeeFullNameLabel.setText(employee.getFullName());
    }

    // =========================================================
    // 4. Xử lý chức năng nút quay lại
    // =========================================================
    private void handleBackBtn() {
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

    // =========================================================
    // 5. Cài đặt các thành phần giao diện liên quan đến thời gian
    // =========================================================

    // 5.1 Cài đặt TimePicker, DateRangePicker và binding Data
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

    // 5.2 Lắng nghe sự kiện thay đổi trên checkInDateTextField,
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

    // 5.3 Tạo các preset cho DateRangePicker
    // 5.3.1 Preset 3 ngày
    private DateRangePreset createThreeDaysPreset() {
        return new DateRangePreset("3 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(3)));
    }

    // 5.3.2 Preset 5 ngày
    private DateRangePreset createFiveDaysPreset() {
        return new DateRangePreset("5 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    // 5.3.3 Preset 7 ngày
    private DateRangePreset createSevenDaysPreset() {
        return new DateRangePreset("7 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(7)));
    }

    // =========================================================
    // 6. Tính toán thời gian lưu trú và tiền đặt cọc
    // =========================================================
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





}
