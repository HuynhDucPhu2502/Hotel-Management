package iuh.fit.controller.features.room.creating_reservation_form_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.RoomStatusHelper;
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

public class RoomOnUseItemController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text roomNumberText, checkOutDateText;
    @FXML
    private Label roomCategoryNameLabel, customerFullNameLabel;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private Employee employee;
    private RoomWithReservation roomWithReservation;

    private Timeline timeline;

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

        initializeRoomOverdueCheck(reservationForm.getCheckOutDate());

    }

    private void initializeRoomOverdueCheck(LocalDateTime checkOutDate) {
        String timelineKey = roomWithReservation.getRoom().getRoomID() + RoomStatus.ON_USE.name();

        if (TimelineManager.getInstance().containsTimeline(timelineKey)) {
            TimelineManager.getInstance().removeTimeline(timelineKey);
        }

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime now = LocalDateTime.now();
                    java.time.Duration duration = java.time.Duration.between(now, checkOutDate);

                    if (!duration.isPositive()) {
                        timeline.stop();
                        TimelineManager.getInstance().removeTimeline(timelineKey);
                        if (MainController.isRoomBookingLoaded()) navigateToRoomBookingPanel(false);
                        else RoomStatusHelper.autoCheckoutOverdueRooms();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        TimelineManager.getInstance().addTimeline(timelineKey, timeline);
    }


    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToCreateReservationFormPanel() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = roomWithReservation.getReservationForm().getCheckOutDate();

        if (now.isAfter(checkOutDate)) {
            navigateToRoomBookingPanel(true);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
                    mainController, employee, roomWithReservation,
                    null, null, null
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
            roomBookingController.setupContext(mainController, employee);
            if (isError)
                roomBookingController.getDialogPane().showInformation(
                        "Không thể thực hiện thao tác",
                        "Phòng này đã quá hạn Checkout. Bạn phải Checkout mới có thể thực hiện chức năng khác."
                );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
