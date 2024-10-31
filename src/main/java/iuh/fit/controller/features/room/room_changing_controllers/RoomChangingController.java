package iuh.fit.controller.features.room.room_changing_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.create_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.reservation_list_controllers.ReservationListController;
import iuh.fit.controller.features.room.service_ordering_controllers.ServiceOrderingController;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomReservationDetailDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
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
           navigateToReservationListBtn, navigateToServiceOrderingBtn;

    // 1.2 Labels
    @FXML
    private Label roomNumberLabel, roomCategoryLabel,
            checkInDateLabel, checkOutDateLabel,
            stayLengthLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel,
            cusomerPhoneNumberLabel, customerEmailLabel,
            customerIDCardNumberLabel;

    @FXML
    private Text roomAvailableTitle;

    // 1.3 Formatter
    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

   // 1.4 Table View
   @FXML
   private TableView<RoomReservationDetail> roomReservationDetailTableView;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailID;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailDateChanged;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailRoomNumber;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationEmployeeFullname;

    // 1.5 Dialog Pane
    @FXML
    private DialogPane dialogPane;

    // 1.6 Titled Pane
    @FXML
    private TitledPane titledPane;

    // 1.7 Container
    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox roomListContainer;
    @FXML
    private GridPane roomGridPane;

    // 1.8 Context
    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private List<Room> availableRooms;
    private List<RoomReservationDetail> roomReservationDetails;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();

        setupTable();
    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());
        roomAvailableTitle.setText("Danh sách phòng trống từ hiện tại đến ngày " + dateTimeFormatter.format(roomWithReservation.getReservationForm().getCheckOutDate()));

        setupReservationForm();
        setupButtonActions();
        loadData();
        displayAvailableRooms(availableRooms);
    }


    private void loadData() {
        availableRooms = RoomDAO.getAvailableRoomsUntil(
                roomWithReservation.getRoom().getRoomID(),
                roomWithReservation.getRoom().getRoomCategory().getRoomCategoryID(),
                roomWithReservation.getReservationForm().getCheckOutDate()

        );
        roomReservationDetails = RoomReservationDetailDAO.getByReservationFormID(
                roomWithReservation.getReservationForm().getReservationID()
        );

        ObservableList<RoomReservationDetail> data = FXCollections.observableArrayList(roomReservationDetails);

        roomReservationDetailTableView.setItems(data);
        roomReservationDetailTableView.refresh();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        navigateToReservationListBtn.setOnAction(e -> navigateToReservationListPanel());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        navigateToServiceOrderingBtn.setOnAction(e -> navigateToServiceOrderingPanel());

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

    private void navigateToServiceOrderingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/ordering_services_panels/ServiceOrderingPanel.fxml"));
            AnchorPane layout = loader.load();

            ServiceOrderingController serviceOrderingController = loader.getController();
            serviceOrderingController.setupContext(
                    mainController, employee, roomWithReservation
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 4.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        Room reservationFormRoom = roomWithReservation.getRoom();
        Customer reservationFormCustomer = roomWithReservation.getReservationForm().getCustomer();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomNumber());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkOutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
        stayLengthLabel.setText(Calculator.calculateStayLengthToString(
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

                    RoomAvailableChangingItemController controller = loader.getController();
                    controller.setupContext(room);
                    controller.getChangingBtn().setOnAction(e -> handleChangingRoom(room));

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

    private void setupTable() {
        roomReservationDetailID.setCellValueFactory(new PropertyValueFactory<>("roomReservationDetailID"));
        roomReservationDetailDateChanged.setCellValueFactory(data -> {
            LocalDateTime dateChanged = data.getValue().getDateChanged();
            String formattedDate = dateChanged != null ? dateChanged.format(dateTimeFormatter) : "KHÔNG CÓ";
            return new SimpleStringProperty(formattedDate);
        });
        roomReservationDetailRoomNumber.setCellValueFactory(data -> {
            Room room = data.getValue().getRoom();
            String roomNumber = (room != null && room.getRoomNumber() != null) ? room.getRoomNumber() : "KHÔNG CÓ";
            return new SimpleStringProperty(roomNumber);
        });
        roomReservationEmployeeFullname.setCellValueFactory(data -> {
            Employee employee = data.getValue().getEmployee();
            String fullName = (employee != null && employee.getFullName() != null) ? employee.getFullName() : "KHÔNG CÓ";
            return new SimpleStringProperty(fullName);
        });

    }



    // ==================================================================================================================
    // 5.  Xử lý sự kiện thay chuyển phòng
    // ==================================================================================================================
    private void handleChangingRoom(Room newRoom) {
        try {
            System.out.println("test");
            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn chuyển phòng?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    // 1. Cập nhật trạng thái phòng cũ thành AVAILABLE
                    RoomDAO.updateRoomStatus(roomWithReservation.getRoom().getRoomID(), RoomStatus.AVAILABLE);

                    // 2. Cập nhật reservationForm với roomID mới
                    ReservationFormDAO.updateRoomInReservationForm(
                            roomWithReservation.getReservationForm().getReservationID(),
                            newRoom.getRoomID()
                    );

                    // 3. Cập nhật trạng thái phòng mới thành ON_USE
                    RoomDAO.updateRoomStatus(newRoom.getRoomID(), RoomStatus.ON_USE);

                    // 4. Tạo bản ghi trong RoomReservationDetail để lưu lại lịch sử chuyển phòng
                    RoomReservationDetail detail = new RoomReservationDetail();
                    detail.setDateChanged(LocalDateTime.now());
                    detail.setRoom(newRoom);
                    detail.setReservationForm(roomWithReservation.getReservationForm());
                    detail.setEmployee(employee);
                    RoomReservationDetailDAO.createData(detail);

                    dialogPane.showInformation("Thành Công","Chuyển phòng thành công!");
                    navigateToRoomBookingPanel();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            dialogPane.showInformation("LỖI",e.getMessage());
        }
    }


}
