package iuh.fit.controller.features.room.create_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RoomOverDueController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text roomNumberText;
    @FXML
    private Text checkOutDateText;
    @FXML
    private Label roomCategoryNameLabel;
    @FXML
    private Label customerFullNameLabel;
    @FXML
    private Text lateDuration;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    private Timeline timeline;

    // Context
    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;

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

    private void startLateDurationCountdown(java.time.LocalDateTime checkOutDate) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.Duration duration = java.time.Duration.between(checkOutDate, now);

            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;

            lateDuration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopLateDurationCountdown() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
