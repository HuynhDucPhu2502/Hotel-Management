package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_out_controllers.CheckingOutReservationFormController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.RoomManagementService;
import iuh.fit.utils.TimelineManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RoomOverDueController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text roomNumberText, checkOutDateText, lateDuration;
    @FXML
    private Label roomCategoryNameLabel, customerFullNameLabel;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;

    private java.time.Duration duration;
    private Timeline timeline;

    private NotificationButtonController notificationButtonController;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation, NotificationButtonController notificationButtonController) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.notificationButtonController = notificationButtonController;

        Room room = roomWithReservation.getRoom();
        Customer customer = roomWithReservation.getReservationForm().getCustomer();
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
        customerFullNameLabel.setText(customer.getFullName());
        checkOutDateText.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));

        initializeOverdueTimeTracking(roomWithReservation.getReservationForm().getCheckOutDate());
    }

    private void initializeOverdueTimeTracking(LocalDateTime checkOutDate) {
        String timelineKey = roomWithReservation.getRoom().getRoomID() + RoomStatus.OVERDUE.name();

        duration = java.time.Duration.between(checkOutDate, LocalDateTime.now());

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        lateDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        if (TimelineManager.getInstance().containsTimeline(timelineKey)) {
            TimelineManager.getInstance().removeTimeline(timelineKey);
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshLateDurationDisplay(checkOutDate)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        TimelineManager.getInstance().addTimeline(timelineKey, timeline);
    }

    private void refreshLateDurationDisplay(LocalDateTime checkOutDate) {
        duration = java.time.Duration.between(checkOutDate, LocalDateTime.now());

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        lateDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        if (hours >= 2) {
            if (timeline != null) {
                if (MainController.isRoomBookingLoaded()) navigateToRoomBookingPanel(false);
                else RoomManagementService.autoCheckoutOverdueRooms(notificationButtonController, mainController);
                TimelineManager.getInstance().removeTimeline(roomWithReservation.getRoom().getRoomID() + RoomStatus.OVERDUE.name());
            }

        }
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToCheckingOutReservationForm() {
        if (duration != null && duration.toHours() >= 2) {
            navigateToRoomBookingPanel(true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/iuh/fit/view/features/room/checking_out_panels/CheckingOutReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CheckingOutReservationFormController checkingOutReservationFormController = loader.getController();
            checkingOutReservationFormController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );


            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
            MainController.setRoomBookingLoaded(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToRoomBookingPanel(boolean isError) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee, notificationButtonController);
            if (isError)
                roomBookingController.getDialogPane().showInformation(
                        "Không thể thực hiện thao tác",
                        "Phòng này đã quá hạn Checkout. Hệ thống đã tự động thực hiện Checkout và xuất hóa đơn."
                );


            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
