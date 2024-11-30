package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import com.dlsc.gemsfx.daterange.DateRangePreset;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_in_reservation_list_controllers.ReservationListController;
import iuh.fit.controller.features.room.room_changing_controllers.RoomChangingController;
import iuh.fit.controller.features.room.service_ordering_controllers.ServiceOrderingController;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

public class CreateReservationFormController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML private Button backBtn, bookingRoomNavigate, navigateToCreateCustomerBtn;

    @FXML
    private Button navigateToReservationListBtn, navigateToServiceOrdering,
            navigateToRoomChanging;

    @FXML private Button addBtn, reservationCheckDateBtn;

    @FXML private Label roomNumberLabel, categoryNameLabel;
    @FXML private DateRangePicker bookDateRangePicker;
    @FXML private TimePicker checkInTimePicker, checkOutTimePicker;
    @FXML private TextField checkInDateTextField, checkOutDateTextField;
    @FXML private Label stayLengthLabel, bookingDepositLabel;

    @FXML private TextField customerIDCardNumberTextField, customerFullnameTextField,
            customerPhoneNumberTextField;

    @FXML private Label employeeIDLabel, employeeFullNameLabel,
            employeePositionLabel, employeePhoneNumberLabel;

    @FXML private DialogPane dialogPane;
    @FXML private TitledPane titledPane;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;
    private Room room;

    private LocalDateTime checkInTime, checkOutTime;
    private Customer customer;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
        setupTimeComponents();
        setupCustomerIDCardValidation();
    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation, Customer customer,
                             LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.room = roomWithReservation.getRoom();

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        setupRoomInformation();
        setupEmployeeInformation();

        if (customer != null) customerIDCardNumberTextField.setText(customer.getIdCardNumber());
        if (checkInTime != null && checkOutTime != null) {
            this.checkInTime = checkInTime;
            this.checkOutTime = checkOutTime;
            setBookingDates(checkInTime, checkOutTime);
        }

        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        navigateToReservationListBtn.setOnAction(e -> navigateToReservationListPanel());

        if (room.getRoomStatus() == RoomStatus.AVAILABLE) {
            navigateToServiceOrdering.setDisable(true);
            navigateToRoomChanging.setDisable(true);
        } else {
            navigateToRoomChanging.setOnAction(e -> navigateToRoomChangingPanel());
            navigateToServiceOrdering.setOnAction(e -> navigateToServiceOrderingPanel());
        }

        // Current Panel Button
        navigateToCreateCustomerBtn.setOnAction(e -> navigateToAddCustomerPanel());
        addBtn.setOnAction(e -> handleCreateReservationRoom());
        reservationCheckDateBtn.setOnAction(e -> openCalendarViewStage());
    }

    // ==================================================================================================================
    // 3. Hiển thị thông tin phòng và nhân viên
    // ==================================================================================================================
    private void setupRoomInformation() {
        roomNumberLabel.setText(room.getRoomNumber());
        categoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
    }

    private void setupEmployeeInformation() {
        employeeIDLabel.setText(employee.getEmployeeID());
        employeeFullNameLabel.setText(employee.getFullName());
        employeePositionLabel.setText(employee.getPosition().toString());
        employeePhoneNumberLabel.setText(employee.getPhoneNumber());
    }

    // ==================================================================================================================
    // 4. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml", RoomBookingController.class);
    }

    private void navigateToAddCustomerPanel() {
        loadPanel("/iuh/fit/view/features/room/creating_reservation_form_panels/AddCustomerPanel.fxml", AddCustomerController.class);
    }

    private void navigateToReservationListPanel() {
        loadPanel("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml", ReservationListController.class);
    }

    private void navigateToRoomChangingPanel() {
        loadPanel("/iuh/fit/view/features/room/changing_room_panels/RoomChangingPanel.fxml", RoomChangingController.class);
    }

    private void navigateToServiceOrderingPanel() {
        loadPanel("/iuh/fit/view/features/room/ordering_services_panels/ServiceOrderingPanel.fxml", RoomChangingController.class);
    }

    private <T> void loadPanel(String path, Class<T> ignoredControllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane layout = loader.load();
            T controller = loader.getController();

            if (controller instanceof RoomBookingController rbc)
                rbc.setupContext(mainController, employee);
            else if (controller instanceof AddCustomerController acc)
                acc.setupContext(mainController, employee, roomWithReservation, checkInTime, checkOutTime);
            else if (controller instanceof  ReservationListController rlc)
                rlc.setupContext(mainController, employee, roomWithReservation);
            else if (controller instanceof RoomChangingController rcc)
                rcc.setupContext(mainController, employee, roomWithReservation);
            else if (controller instanceof ServiceOrderingController soc)
                soc.setupContext(mainController, employee, roomWithReservation);


            mainController.getMainPanel().getChildren().setAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 5. Cài đặt các thành phần giao diện liên quan đến thời gian
    // ==================================================================================================================
    private void setupTimeComponents() {
        checkInTimePicker.setTime(null);
        checkInTimePicker.setStepRateInMinutes(5);
        checkOutTimePicker.setTime(null);
        checkOutTimePicker.setStepRateInMinutes(5);

        bookDateRangePicker.setValue(new DateRange("Chọn Lịch Đặt Phòng", LocalDate.now(), LocalDate.now().plusDays(7)));
        bookDateRangePicker.setFormatter(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

        bookDateRangePicker.getDateRangeView().presetTitleProperty().set("Chọn Lịch Đặt Phòng");
        bookDateRangePicker.getDateRangeView().toTextProperty().set("Đến");
        bookDateRangePicker.getDateRangeView().cancelTextProperty().set("Hủy");
        bookDateRangePicker.getDateRangeView().applyTextProperty().set("Áp dụng");

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
            }
        });

        checkOutDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                checkOutTime = parseDateTime(newValue);
                updateStayLengthAndCost();
            }
        });
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
    // 6. Tính toán thời gian lưu trú và tiền đặt cọc
    // ==================================================================================================================
    private void updateStayLengthAndCost() {
        if (checkInTime != null && checkOutTime != null) {
            String stayLength = Calculator.calculateStayLengthToString(checkInTime, checkOutTime);
            stayLengthLabel.setText(stayLength);

            try {
                double totalCost = Calculator.calculateBookingDeposit(room, checkInTime, checkOutTime);
                bookingDepositLabel.setText(String.format("%.2f VND", totalCost));
            } catch (IllegalArgumentException e) {
                bookingDepositLabel.setText(e.getMessage());
            }
        } else {
            stayLengthLabel.setText(GlobalConstants.STAY_LENGTH_EMPTY);
            bookingDepositLabel.setText(GlobalConstants.BOOKING_DEPOSIT_EMPTY);
        }
    }

    // ==================================================================================================================
    // 7. Cài đặt các thành phần giao diện liên quan đến Khách Hàng
    // ==================================================================================================================
    private void setupCustomerIDCardValidation() {
        customerIDCardNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) clearCustomerInfo();
            else if (newValue.length() > 12) {
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
    // 7. Đẩy thời gian lên giao diện nếu checkInDate và checkOutDate không NULL
    // ==================================================================================================================
    private void setBookingDates(LocalDateTime checkInDate, LocalDateTime checkOutDate) {
            bookDateRangePicker.setValue(new DateRange("Chọn Lịch Đặt Phòng",
                    checkInDate.toLocalDate(), checkOutDate.toLocalDate()));

            checkInTimePicker.setTime(checkInDate.toLocalTime());
            checkOutTimePicker.setTime(checkOutDate.toLocalTime());
    }

    // ==================================================================================================================
    // 8. Đẩy thời gian lên giao diện nếu checkInDate và checkOutDate không NULL
    // ==================================================================================================================
    private void handleCreateReservationRoom() {
        try {
            if (checkInTime == null)
                throw new IllegalArgumentException(
                        ErrorMessages.RESERVATION_FORM_INVALID_CHECKIN_DATE_ISNULL);

            if (checkOutTime == null)
                throw new IllegalArgumentException(
                        ErrorMessages.RESERVATION_FORM_INVALID_CHECKOUT_DATE_ISNULL);

            if (Calculator.calculateStayLengthToDouble(checkInTime, checkOutTime) <= 0)
                throw new IllegalArgumentException(
                        ErrorMessages.RESERVATION_FORM_STAY_LENGTH_INVALID);

            ReservationForm reservationForm = new ReservationForm(
                    LocalDateTime.now(), checkInTime, checkOutTime,
                    employee, room, customer);
            ReservationFormDAO.createData(reservationForm);

            dialogPane.showInformation("Thành công", "Đã thêm phiếu đặt phòng thành công");
            handleResetAction();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleResetAction() {
        try {
            // Unbind các TextField trước khi reset giá trị
            checkInDateTextField.textProperty().unbind();
            checkOutDateTextField.textProperty().unbind();

            bookDateRangePicker.setValue(null);
            checkInTimePicker.setTime(null);
            checkOutTimePicker.setTime(null);
            customerIDCardNumberTextField.setText("");
            checkInDateTextField.setText("");
            checkOutDateTextField.setText("");
            stayLengthLabel.setText("Chưa Đặt Lịch");
            bookingDepositLabel.setText("0 VND");

            customer = null;
            checkOutTime = null;
            checkInTime = null;

            setupTimeComponents();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 9. Chức năng xem lịch phòng
    // ==================================================================================================================
    private void openCalendarViewStage() {
        CalendarView calendarView = new CalendarView();
        List<ReservationForm> reservations = ReservationFormDAO.getUpcomingReservations(room.getRoomID());
        Calendar<String> calendar = new Calendar<>("Lịch Đặt Phòng");

        reservations.forEach(res -> {
            Entry<String> entry = new Entry<>(res.getReservationID());
            entry.changeStartDate(res.getCheckInDate().toLocalDate());
            entry.changeEndDate(res.getCheckOutDate().toLocalDate());
            calendar.addEntry(entry);
        });

        calendarView.getCalendarSources().add(new com.calendarfx.model.CalendarSource("Nguồn") {{
            getCalendars().add(calendar);
        }});

        Stage stage = new Stage();
        stage.setScene(new Scene(calendarView, 800, 800));
        stage.show();
    }



}
