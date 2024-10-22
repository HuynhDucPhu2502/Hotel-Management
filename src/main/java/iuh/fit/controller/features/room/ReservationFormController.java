package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.TimePicker;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;
import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
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

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    // =========================================================
    // Khởi tạo và nạp dữ liệu vào giao diện
    // =========================================================
    public void initialize() {
    }

    public void setupContext(MainController mainController, Employee employee, Room room) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        backBtn.setOnAction(e -> handleBackBtn());

        setupTimeComponents();
        setupRoomInformation();
        setupEmployeeInformation();
    }

    // =========================================================
    // Xử lý đổi các biến thời gian thành chuỗi ngày + giờ
    // =========================================================
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    private String formatDateTime(java.time.LocalDate date, java.time.LocalTime time) {
        return dateTimeFormatter.format(date) + " " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    // =========================================================
    // Đẩy dữ liệu phòng và nhân viên lên giao diện
    // =========================================================
    private void setupRoomInformation() {
        roomNumberLabel.setText(room.getRoomNumber());
        categoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    private void setupEmployeeInformation() {
        employeeIDLabel.setText(employee.getEmployeeID());
        employeeFullNameLabel.setText(employee.getFullName());
    }

    // =========================================================
    // Xử lý chức năng nút quay lại
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
    // Cài đặt các thành phần giao diện liên quan đến thời gian
    // =========================================================
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

    private void setupDateTimeListeners() {
        checkInDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkInTime = parseDateTime(newValue);
                System.out.println("Check-in time: " + checkInTime);
            }
        });

        checkOutDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkOutTime = parseDateTime(newValue);
                System.out.println("Check-out time: " + checkOutTime);
            }
        });
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

    private DateRangePreset createThreeDaysPreset() {
        return new DateRangePreset("3 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(3)));
    }

    private DateRangePreset createFiveDaysPreset() {
        return new DateRangePreset("5 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(5)));
    }

    private DateRangePreset createSevenDaysPreset() {
        return new DateRangePreset("7 Ngày", () -> new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(7)));
    }


}
