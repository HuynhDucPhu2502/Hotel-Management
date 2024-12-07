package iuh.fit.controller.features.room.checking_in_reservation_list_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_out_controllers.CheckingOutEarlyReservationFormController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.room_changing_controllers.RoomChangingController;
import iuh.fit.controller.features.room.service_ordering_controllers.ServiceOrderingController;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

public class ReservationListController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, bookingRoomNavigate;

    @FXML
    private Button navigateToCreateReservationFormBtn, navigateToServiceOrdering,
            navigateToRoomChangingBtn, navigateToRoomCheckingOutBtn;

    @FXML
    private TitledPane titledPane;

    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox reservationFormsListContainer;
    @FXML
    private GridPane reservationFormGidPane;

    @FXML
    private DialogPane dialogPane;

    private MainController mainController;
    private Employee employee;
    private Room room;
    private List<ReservationForm> reservationForms;
    private RoomWithReservation roomWithReservation;
    private NotificationButtonController notificationButtonController;


    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
    }

    public void setupContext(
            MainController mainController, Employee employee,
            RoomWithReservation roomWithReservation,
            NotificationButtonController notificationButtonController
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.room = roomWithReservation.getRoom();
        this.notificationButtonController = notificationButtonController;

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());

        loadData();
        displayFilteredRooms(reservationForms);
        setupButtonActions();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        if (room.getRoomStatus() == RoomStatus.AVAILABLE) {
            navigateToServiceOrdering.setDisable(true);
            navigateToRoomChangingBtn.setDisable(true);
            navigateToRoomCheckingOutBtn.setDisable(true);
        } else {
            navigateToRoomChangingBtn.setOnAction(e -> navigateToRoomChangingPanel());
            navigateToServiceOrdering.setOnAction(e -> navigateToServiceOrderingPanel());
            navigateToRoomCheckingOutBtn.setOnAction(e -> navigateToCheckingOutEarlyReservationFormPanel());
        }


    }

    private void loadData() {
        reservationForms = ReservationFormDAO.getUpcomingReservations(room.getRoomID());
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
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

    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
                    mainController, employee, roomWithReservation,
                    null,
                    null,
                    null,
                    notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToRoomChangingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/changing_room_panels/RoomChangingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomChangingController roomChangingController = loader.getController();
            roomChangingController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToServiceOrderingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/ordering_services_panels/ServiceOrderingPanel.fxml"));
            AnchorPane layout = loader.load();

            ServiceOrderingController serviceOrderingController = loader.getController();
            serviceOrderingController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToCheckingOutEarlyReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_out_panels/CheckingOutEarlyReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CheckingOutEarlyReservationFormController checkingOutEarlyReservationFormController = loader.getController();
            checkingOutEarlyReservationFormController.setupContext(
                    mainController, employee, roomWithReservation, notificationButtonController
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 4. Chức năng hiển thị phiếu đặt phòng
    // ==================================================================================================================
    private void displayFilteredRooms(List<ReservationForm> reservationForms) {
        if (!reservationForms.isEmpty()) {
            reservationFormGidPane.getChildren().clear();

            int row = 0, col = 0;

            try {
                for (ReservationForm reservationForm : reservationForms) {
                    FXMLLoader loader;
                    Pane reservationFormItem;

                    loader = new FXMLLoader(getClass().getResource(
                            "/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationFormItem.fxml"));
                    reservationFormItem = loader.load();

                    ReservationFormItemController controller = loader.getController();
                    controller.setupContext(mainController, reservationForm, employee,
                            roomWithReservation, notificationButtonController);

                    reservationFormGidPane.add(reservationFormItem, col, row);

                    col++;
                    if (col == 2) {
                        col = 0;
                        row++;
                    }
                }

                reservationFormGidPane.setVisible(true);
                reservationFormGidPane.setManaged(true);
                emptyLabelContainer.setVisible(false);
                emptyLabelContainer.setManaged(false);
                reservationFormsListContainer.setAlignment(Pos.TOP_CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            reservationFormGidPane.setVisible(false);
            reservationFormGidPane.setManaged(false);

            if (emptyLabelContainer.getChildren().isEmpty()) {
                Label emptyLabel = new Label("Không có phiếu đặt phòng nào.");
                emptyLabel.setStyle("-fx-font-size: 42px; -fx-text-fill: gray;");
                emptyLabelContainer.getChildren().add(emptyLabel);
            }

            emptyLabelContainer.setVisible(true);
            emptyLabelContainer.setManaged(true);

            reservationFormsListContainer.setAlignment(Pos.CENTER);
        }
    }

    // ==================================================================================================================
    // 5. Khác
    // ==================================================================================================================
    public DialogPane getDialogPane() {
        return dialogPane;
    }
}
