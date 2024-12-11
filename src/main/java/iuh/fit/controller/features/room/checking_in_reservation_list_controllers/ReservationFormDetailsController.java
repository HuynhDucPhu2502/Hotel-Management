package iuh.fit.controller.features.room.checking_in_reservation_list_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.*;
import iuh.fit.dao.misc.ShiftDetailDAO;
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
    @FXML
    private Button backBtn, reservationFormListNavigate, bookingRoomNavigate,
            reservationFormBtn;

    @FXML
    private Button deleteReservationFormBtn, checkInBtn,
            earlyCheckInBtn;

    @FXML
    private Label roomNumberLabel, roomCategoryLabel, checkInDateLabel,
            checkOutDateLabel, stayLengthLabel, bookingDepositLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel, cusomerPhoneNumberLabel,
            customerEmailLabel, customerIDCardNumberLabel;

    @FXML
    private Label employeeFullNameLabel, employeePositionLabel, employeeIDLabel,
            employeePhoneNumberLabel;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TitledPane titledPane;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));


    private MainController mainController;
    private ReservationForm reservationForm;
    private RoomWithReservation roomWithReservation;
    private Employee employee;
    private NotificationButtonController notificationButtonController;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==========================================s========================================================================
    public void initialize() {
        dialogPane.toFront();
    }

    public void setupContext(
            MainController mainController, ReservationForm reservationForm,
            Employee employee, RoomWithReservation roomWithReservation,
            NotificationButtonController notificationButtonController) {
        this.mainController = mainController;
        this.reservationForm = reservationForm;
        this.roomWithReservation = roomWithReservation;
        this.employee = employee;
        this.notificationButtonController = notificationButtonController;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());

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
        LocalDateTime earlyCheckInStart = checkInTime.minusMinutes(30);
        LocalDateTime earlyCheckInEnd = checkInTime.minusSeconds(10);

        checkInBtn.setDisable(!now.isAfter(checkInTime) || !now.isBefore(checkInTimePlus2Hours));
        earlyCheckInBtn.setDisable(!(now.isAfter(earlyCheckInStart) && now.isBefore(earlyCheckInEnd)));
        checkInBtn.setOnAction(e -> handleCheckIn());
        earlyCheckInBtn.setOnAction(e -> handleEarlyCheckin());
        deleteReservationFormBtn.setOnAction(e -> handleDeleteAction());

        reservationFormBtn.setText("Phiếu đặt phòng " + reservationForm.getReservationID());
    }

    // ==================================================================================================================
    // 3.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        Room reservationFormRoom = reservationForm.getRoom();
        Customer reservationFormCustomer = reservationForm.getCustomer();
        Employee reservationFormEmployee = reservationForm.getEmployee();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomCategory().getRoomCategoryName());
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
    // 4. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee, notificationButtonController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Có tin nhắn
    private void navigateToReservationListPanel(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.getDialogPane().showInformation("Thông Báo", message);
            reservationListController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Không tin nhắn
    private void navigateToReservationListPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 5. Xử lý chức năng xóa phiếu đặt phòng
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
    // 6. Xử lý chức năng CheckIn
    // ==================================================================================================================
    private void handleCheckIn() {
        try {
            RoomReservationDetailDAO.roomCheckingIn(
                    reservationForm.getReservationID(),
                    employee.getEmployeeID()
            );

            roomWithReservation = RoomWithReservationDAO
                    .getRoomWithReservationByID(reservationForm.getReservationID(), roomWithReservation.getRoom().getRoomID());

            ShiftDetailDAO.updateNumbOfCheckInRoom(mainController.getShiftDetailID());
            navigateToReservationListPanel("Check-in thành công tại phòng đã đặt.");
        } catch (Exception e) {
            navigateToReservationListPanel(e.getMessage());
        }
    }

    private void handleEarlyCheckin() {
        if (roomWithReservation.getRoom().getRoomStatus() == RoomStatus.AVAILABLE) {
            try {
                RoomReservationDetailDAO.roomEarlyCheckingIn(
                        reservationForm.getReservationID(),
                        employee.getEmployeeID()
                );

                roomWithReservation = RoomWithReservationDAO
                        .getRoomWithReservationByID(reservationForm.getReservationID(), roomWithReservation.getRoom().getRoomID());

                ShiftDetailDAO.updateNumbOfCheckInRoom(mainController.getShiftDetailID());
                navigateToReservationListPanel("Check-in thành công tại phòng đã đặt.");
            } catch (Exception e) {
                navigateToReservationListPanel(e.getMessage());
            }
        } else {
            navigateToReservationListPanel("Phòng đang được sử dụng.");
        }
    }

}
