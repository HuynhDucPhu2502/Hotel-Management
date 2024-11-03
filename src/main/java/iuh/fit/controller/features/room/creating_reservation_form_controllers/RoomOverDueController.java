package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_out_controllers.CheckingOutReservationFormController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.wrapper.RoomWithReservation;
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

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void setupContext(MainController mainController, Employee employee, RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        Room room = roomWithReservation.getRoom();
        Customer customer = roomWithReservation.getReservationForm().getCustomer();
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        roomCategoryNameLabel.setText(room.getRoomCategory().getRoomCategoryName());
        roomNumberText.setText(room.getRoomNumber());
        customerFullNameLabel.setText(customer.getFullName());
        checkOutDateText.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));

        startLateDurationCountdown(roomWithReservation.getReservationForm().getCheckOutDate());
    }

    private void startLateDurationCountdown(LocalDateTime checkOutDate) {
        updateLateDurationText(checkOutDate);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateLateDurationText(checkOutDate)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateLateDurationText(LocalDateTime checkOutDate) {
        LocalDateTime now = LocalDateTime.now();
        duration = java.time.Duration.between(checkOutDate, now);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        lateDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToCheckingOutReservationForm() {
        if (duration != null && duration.toHours() >= 2) {
            navigateToRoomBookingPanel();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/iuh/fit/view/features/room/checking_out_panels/CheckingOutReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CheckingOutReservationFormController checkingOutReservationFormController = loader.getController();
            checkingOutReservationFormController.setupContext(
                    mainController, employee, roomWithReservation
            );


            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee);
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
