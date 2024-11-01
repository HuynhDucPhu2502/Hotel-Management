package iuh.fit.controller.features.room.checking_in_reservation_list_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
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
    @FXML
    private Button reservationFormBtn;
    @FXML
    private Button deleteReservationFormBtn;
    @FXML
    private Button checkInBtn;

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
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    // 1.4 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.5 Titled Pane
    @FXML
    private TitledPane titledPane;

    // 1.6 Context
    private MainController mainController;
    private ReservationForm reservationForm;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==========================================s========================================================================
    public void initialize() {
        dialogPane.toFront();
    }

    public void setupContext(
            MainController mainController, ReservationForm reservationForm,
            Employee employee, RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.roomWithReservation = roomWithReservation;
        this.employee = employee;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());
        System.out.println(reservationForm.getReservationID());

        setupReservationForm();
        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToReservationListPanel());
        reservationFormListNavigate.setOnAction(e -> navigateToReservationListPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Current Panel Button
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInTime = reservationForm.getCheckInDate();
        LocalDateTime checkInTimePlus2Hours = checkInTime.plusHours(2);

        checkInBtn.setDisable(!now.isAfter(checkInTime) || !now.isBefore(checkInTimePlus2Hours));
        checkInBtn.setOnAction(e -> handleCheckIn());
        deleteReservationFormBtn.setOnAction(e -> handleDeleteAction());

        reservationFormBtn.setText("Phiếu đặt phòng " + reservationForm.getReservationID());
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
        stayLengthLabel.setText(Calculator.calculateStayLengthToString(
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.setupContext(
                    mainController, employee, roomWithReservation
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 4. Xử lý chức năng xóa phiếu đặt phòng
    // ==================================================================================================================
    private void handleDeleteAction() {
        try{

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn xóa phiếu đặt phòng này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ReservationFormDAO.deleteData(reservationForm.getReservationID());
                    navigateToReservationListPanel();
                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // ==================================================================================================================
    // 5. Xử lý chức năng CheckIn
    // ==================================================================================================================
    private void handleCheckIn() {
        try {
            // Lấy thông tin cần thiết từ reservationForm và employee
            Room room = reservationForm.getRoom();
            Employee employee = this.employee;
            LocalDateTime now = LocalDateTime.now();

            // 1. Tạo đối tượng RoomReservationDetail
            RoomReservationDetail detail = new RoomReservationDetail(
                    RoomReservationDetailDAO.getNextID(),
                    now,
                    room,
                    reservationForm,
                    employee
            );

            // 2. Tạo đối tượng HistoryCheckIn
            HistoryCheckIn historyCheckIn = new HistoryCheckIn(
                    HistoryCheckinDAO.getNextID(),
                    LocalDateTime.now(),
                    reservationForm,
                    employee
            );

            // 3. Thêm dữ liệu vào RoomReservationDetail và HistoryCheckIn
            RoomReservationDetailDAO.createData(detail);
            HistoryCheckinDAO.createData(historyCheckIn);

            // 4. Cập nhật trạng thái phòng thành "ON_USE"
            room.setRoomStatus(RoomStatus.ON_USE);
            RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.ON_USE);

            navigateToReservationListPanel();

            roomWithReservation = RoomWithReservationDAO
                    .getRoomWithReservationByID(reservationForm.getReservationID(), room.getRoomID());
            navigateToReservationListPanel();
        } catch (Exception e) {
            dialogPane.showWarning("Lỗi", e.getMessage());
        }
    }



}
