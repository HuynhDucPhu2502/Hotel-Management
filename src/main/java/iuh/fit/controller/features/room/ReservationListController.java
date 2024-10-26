package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.reservation_form_items.ReservationFormItemController;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

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

    // 1.2 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.3 Titled Pane
    @FXML
    private TitledPane titledPane;

    // 1.4 GridPane
    @FXML
    private GridPane reservationFormGidPane;

    // 1.5 Context
    private MainController mainController;
    private Employee employee;
    private Room room;
    private List<ReservationForm> reservationForms;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
    }

    public void setupContext(
            MainController mainController, Employee employee, Room room
    ) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;

        titledPane.setText("Quản lý đặt phòng " + room.getRoomNumber());

        bookingRoomNavigate.setOnAction(e -> navigateToRoomBooking());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToReservationForm());

        loadData();
        displayFilteredRooms(reservationForms);
    }

    private void loadData() {
        reservationForms = ReservationFormDAO.getReservationFormByRoomID(room.getRoomID());

    }

    // ==================================================================================================================
    // 3.. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBooking() {
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

    private void navigateToReservationForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/ReservationFormPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationFormController reservationFormController = loader.getController();
            reservationFormController.setupContext(
                    mainController, employee, room,
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
        reservationFormGidPane.getChildren().clear();

        int row = 0, col = 0;

        try {
            for (ReservationForm reservationForm : reservationForms) {
                FXMLLoader loader;
                Pane reservationFormItem;

                loader = new FXMLLoader(getClass().getResource(
                        "/iuh/fit/view/features/room/reservation_form_items/ReservationFormItem.fxml"));
                reservationFormItem = loader.load();

                ReservationFormItemController controller = loader.getController();
                controller.setupContext(mainController, reservationForm);

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
    }

}
