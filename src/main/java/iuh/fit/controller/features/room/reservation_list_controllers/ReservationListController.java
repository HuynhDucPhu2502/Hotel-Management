package iuh.fit.controller.features.room.reservation_list_controllers;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.create_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
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
    // 1.1 Buttons
    @FXML
    private Button backBtn;
    @FXML
    private Button bookingRoomNavigate;
    @FXML
    private Button navigateToCreateReservationFormBtn;

    // 1.2 Titled Pane
    @FXML
    private TitledPane titledPane;

    // 1.3 GridPane
    @FXML
    private GridPane reservationFormGidPane;

    // 1.4 Label
    @FXML
    private HBox emptyLabelContainer;

    // 1.5 Contained;
    @FXML
    private VBox reservationFormsListContainer;

    // 1.6 Context
    private MainController mainController;
    private Employee employee;
    private Room room;
    private List<ReservationForm> reservationForms;
    private RoomWithReservation roomWithReservation;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
    }

    public void setupContext(
            MainController mainController, Employee employee,
            RoomWithReservation roomWithReservation
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.room = roomWithReservation.getRoom();

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());

        loadData();
        displayFilteredRooms(reservationForms);
    }

    private void loadData() {
        reservationForms = ReservationFormDAO.getUpcomingReservations(room.getRoomID());
    }

    // ==================================================================================================================
    // 3.. Xử lý chức năng hiển thị panel khác
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

    private void navigateToCreateReservationFormPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/create_reservation_form_panels/CreateReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            CreateReservationFormController createReservationFormController = loader.getController();
            createReservationFormController.setupContext(
                    mainController, employee, roomWithReservation,
                    null,
                    null,
                    null
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 3. Chức năng hiển thị phiếu đặt phòng
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
                            "/iuh/fit/view/features/room/reservation_list_panels/ReservationFormItem.fxml"));
                    reservationFormItem = loader.load();

                    ReservationFormItemController controller = loader.getController();
                    controller.setupContext(mainController, reservationForm, employee, roomWithReservation);

                    reservationFormGidPane.add(reservationFormItem, col, row);

                    col++;
                    if (col == 2) {
                        col = 0;
                        row++;
                    }
                }
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

}
