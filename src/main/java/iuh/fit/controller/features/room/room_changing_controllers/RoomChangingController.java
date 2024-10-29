package iuh.fit.controller.features.room.room_changing_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.create_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.reservation_list_controllers.ReservationListController;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RoomChangingController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    // 1.1 Buttons
   @FXML
   private Button backBtn, bookingRoomNavigate;
   @FXML
   private Button navigateToCreateReservationFormBtn,
           navigateToReservationListBtn, navigateToServiceOrdering,
           navigateToRoomChanging;

    // 1.2 Labels
    @FXML
    private Label roomNumberLabel, roomCategoryLabel,
            checkInDateLabel, checkOutDateLabel,
            stayLengthLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel,
            cusomerPhoneNumberLabel, customerEmailLabel,
            customerIDCardNumberLabel;

    // 1.3 Formatter
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    // 1.4 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.5 Titled Pane
    @FXML
    private TitledPane titledPane;

    // 1.6 Container
    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox roomListContainer;
    @FXML
    private GridPane roomGridPane;

    // 1.6 Context
    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private List<Room> availableRooms;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {

    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());

        setupReservationForm();
        setupButtonActions();
        loadData();
        displayAvailableRooms(availableRooms);
    }

    private void loadData() {
        availableRooms = RoomDAO.getAvailableRoomsUntil(
                roomWithReservation.getRoom().getRoomID(),
                roomWithReservation.getReservationForm().getCheckOutDate()
        );
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        navigateToReservationListBtn.setOnAction(e -> navigateToReservationListPanel());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());

        // Current Panel Button
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/reservation_list_panels/ReservationListPanel.fxml"));
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
    // 3.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        Room reservationFormRoom = roomWithReservation.getRoom();
        Customer reservationFormCustomer = roomWithReservation.getReservationForm().getCustomer();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomNumber());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkOutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
        stayLengthLabel.setText(Calculator.calculateStayLength(
                reservationForm.getCheckInDate(),
                reservationForm.getCheckOutDate()
        ));
        customerIDLabel.setText(reservationFormCustomer.getCustomerID());
        customerFullnameLabel.setText(reservationFormCustomer.getFullName());
        cusomerPhoneNumberLabel.setText(reservationFormCustomer.getPhoneNumber());
        customerEmailLabel.setText(reservationFormCustomer.getEmail());
        customerIDCardNumberLabel.setText(reservationFormCustomer.getIdCardNumber());
    }


    private void displayAvailableRooms(List<Room> availableRooms) {
        if (!availableRooms.isEmpty()) {
            roomGridPane.getChildren().clear();

            int row = 0, col = 0;

            try {
                for (Room room : availableRooms) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/iuh/fit/view/features/room/changing_room_panels/RoomAvailableChangingItem.fxml"));
                    Pane roomItem = loader.load();

                    RoomAvailableChangingController controller = loader.getController();
                    controller.setupContext(room);


                    roomGridPane.add(roomItem, col, row);

                    col++;
                    if (col == 3) {
                        col = 0;
                        row++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            roomGridPane.setVisible(false);
            roomGridPane.setManaged(false);

            if (emptyLabelContainer.getChildren().isEmpty()) {
                Label emptyLabel = new Label("Không có phòng nào khả dụng.");
                emptyLabel.setStyle("-fx-font-size: 42px; -fx-text-fill: gray;");
                emptyLabelContainer.getChildren().add(emptyLabel);
            }

            emptyLabelContainer.setVisible(true);
            emptyLabelContainer.setManaged(true);

            roomListContainer.setAlignment(Pos.CENTER);
        }
    }

}
