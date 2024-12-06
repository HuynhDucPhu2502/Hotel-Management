package iuh.fit.controller.features.room.group_booking_controllers;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.utils.Calculator;
import iuh.fit.utils.GlobalConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupBookingController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, bookingRoomNavigate, addReservationFormBtn;

    @FXML
    private DateRangePicker bookDateRangePicker;

    @FXML
    private TimePicker checkInTimePicker, checkOutTimePicker;

    @FXML
    private TextField checkInDateTextField, checkOutDateTextField, stayLengthLabel;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TitledPane mainTitledPane, roomSelectorTitledPane;

    @FXML
    private GridPane reservationFormGridPane, roomBookingGridPane;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private Employee employee;

    private LocalDateTime checkInTime, checkOutTime;

    private final List<ReservationForm> reservationFormsList = new ArrayList<>();

    private static NotificationButtonController topBarController;
    public static void setupController(NotificationButtonController controller){
        topBarController = controller;
    }
    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        mainTitledPane.toFront();
        roomSelectorTitledPane.toBack();
        setupTimeComponents();
    }

    public void setupContext(MainController mainController, Employee employee, NotificationButtonController controller) {
        this.mainController = mainController;
        this.employee = employee;
        setupController(controller);

        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Current Panel Button
        addReservationFormBtn.setOnAction(e -> addReservationForm());
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

    // ==================================================================================================================
    // 4. Cài đặt các thành phần giao diện liên quan đến thời gian
    // ==================================================================================================================
    private void setupTimeComponents() {
        checkInTimePicker.setTime(null);
        checkInTimePicker.setStepRateInMinutes(5);
        checkOutTimePicker.setTime(null);
        checkOutTimePicker.setStepRateInMinutes(5);

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
                updateStayLengthAndCost();
                updateCheckInDateForAllForms(); // Cập nhật CheckInDate cho tất cả các phiếu
            }
        });

        checkOutDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkOutTime = parseDateTime(newValue);
                updateStayLengthAndCost();
                updateCheckOutDateForAllForms(); // Cập nhật CheckOutDate cho tất cả các phiếu
            }
        });
    }

    private void updateStayLengthAndCost() {
        if (checkInTime != null && checkOutTime != null) {
            String stayLength = Calculator.calculateStayLengthToString(checkInTime, checkOutTime);
            stayLengthLabel.setText(stayLength);
        } else {
            stayLengthLabel.setText(GlobalConstants.STAY_LENGTH_EMPTY);
        }
    }

    private void updateCheckInDateForAllForms() {
        if (checkInTime != null) {
            for (ReservationForm form : reservationFormsList) {
                form.setCheckInDate(checkInTime);
            }
        }
    }

    private void updateCheckOutDateForAllForms() {
        if (checkOutTime != null) {
            for (ReservationForm form : reservationFormsList) {
                form.setCheckOutDate(checkOutTime);
            }
        }
    }

    private String formatDateTime(LocalDate date, LocalTime time) {
        return dateTimeFormatter.format(date) + " " + time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.forLanguageTag("vi-VN")));
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

    // ==================================================================================================================
    // 5. Xử lý sự kiện thêm phiếu đặt phòng
    // ==================================================================================================================
    private void addReservationForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/group_booking_panels/ReservationItem.fxml"));
            AnchorPane reservationItem = loader.load();

            ReservationForm newReservationForm = new ReservationForm();
            reservationFormsList.add(newReservationForm);

            ReservationItemController controller = loader.getController();

            int nextRow = reservationFormGridPane.getRowCount();
            reservationFormGridPane.add(reservationItem, 0, nextRow);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 6. Phương thức xóa phiếu đặt phòng
    // ==================================================================================================================
    private void deleteReservationForm(AnchorPane reservationItem, ReservationForm reservationForm) {
        reservationFormGridPane.getChildren().remove(reservationItem); // Xóa khỏi GridPane
        reservationFormsList.remove(reservationForm); // Xóa ReservationForm khỏi danh sách
    }

    public void debugPrintReservationForms() {
        System.out.println("Danh sách ReservationForm hiện tại:");
        for (ReservationForm form : reservationFormsList) {
            if (form.getCustomer() != null) {
                System.out.println("Customer ID: " + form.getCustomer().getIdCardNumber());
                System.out.println("Full Name: " + form.getCustomer().getFullName());
                System.out.println("Phone Number: " + form.getCustomer().getPhoneNumber());
                System.out.println("------------");
                System.out.println(form.getCheckOutDate());
                System.out.println(form.getCheckInDate());
            } else {
                System.out.println("ReservationForm chưa có thông tin khách hàng.");
            }
        }
    }

    private void showRoomSelectorPane() {
        mainTitledPane.toBack();
        roomSelectorTitledPane.toFront();
        loadRoomsToSelectorGrid(); // Tải danh sách phòng vào roomBookingGridPane
    }

    private void loadRoomsToSelectorGrid() {
        roomBookingGridPane.getChildren().clear();

        // Lấy khoảng ngày từ bookDateRangePicker
        DateRange selectedRange = bookDateRangePicker.getValue();
        if (selectedRange == null) {
            dialogPane.showInformation("Thông báo", "Vui lòng chọn khoảng ngày hợp lệ.");
            return;
        }

        LocalTime defaultCheckInTime = checkInTimePicker.getTime() != null ? checkInTimePicker.getTime() : LocalTime.of(14, 0);
        LocalTime defaultCheckOutTime = checkOutTimePicker.getTime() != null ? checkOutTimePicker.getTime() : LocalTime.of(12, 0);
        LocalDateTime checkInDateTime = LocalDateTime.of(selectedRange.getStartDate(), defaultCheckInTime);
        LocalDateTime checkOutDateTime = LocalDateTime.of(selectedRange.getEndDate(), defaultCheckOutTime);

        List<Room> availableRooms = RoomDAO.getAvailableRoomsInDateRange(checkInDateTime, checkOutDateTime);

        try {
            int col = 0, row = 0;
            for (Room room : availableRooms) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/group_booking_panels/RoomGroupBookingItem.fxml"));
                AnchorPane roomItem = loader.load();

                RoomGroupBookingItemController roomGroupBookingItemController = loader.getController();
                roomGroupBookingItemController.setupContext(room);

                roomBookingGridPane.add(roomItem, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
