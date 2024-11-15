package iuh.fit.controller.features.room.room_changing_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.ReservationFormDialogViewController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.checking_in_reservation_list_controllers.ReservationListController;
import iuh.fit.controller.features.room.service_ordering_controllers.ServiceOrderingController;
import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RoomChangingController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
   @FXML
   private Button backBtn, bookingRoomNavigate;
   @FXML
   private Button navigateToCreateReservationFormBtn,
           navigateToReservationListBtn, navigateToServiceOrderingBtn;
    @FXML
    private Button roomDialogBtn;

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

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

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

    @FXML
    private DialogPane dialogPane;
    @FXML
    private TitledPane titledPane;

    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox roomListContainer;
    @FXML
    private GridPane roomGridPane;

    @FXML
    private ComboBox<String> floorNumberCBox;

    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private List<Room> availableRooms;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();

        setupRoomReservationDetailTableView();
    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());
        roomAvailableTitle.setText("Danh sách phòng trống từ hiện tại đến ngày " +
                dateTimeFormatter.format(roomWithReservation.getReservationForm().getCheckOutDate()));

        setupReservationForm();
        setupButtonActions();
        loadData();
    }


    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                // Tải danh sách phòng khả dụng từ cơ sở dữ liệu.
                availableRooms = RoomDAO.getAvailableRoomsUntil(
                        roomWithReservation.getRoom().getRoomID(),
                        roomWithReservation.getRoom().getRoomCategory().getRoomCategoryID(),
                        roomWithReservation.getReservationForm().getCheckOutDate()
                );

                // Tải danh sách chi tiết đặt phòng.
                List<RoomReservationDetail> roomReservationDetails = RoomReservationDetailDAO.getByReservationFormID(
                        roomWithReservation.getReservationForm().getReservationID()
                );

                // Cập nhật bảng chi tiết đặt phòng.
                ObservableList<RoomReservationDetail> data = FXCollections.observableArrayList(roomReservationDetails);
                Platform.runLater(() -> {
                    roomReservationDetailTableView.setItems(data);
                    roomReservationDetailTableView.refresh();
                });

                // Thiết lập danh sách tầng cho ComboBox và chọn giá trị đầu tiên.
                Platform.runLater(() -> {
                    floorNumberCBox.getItems().setAll(getFloorNumbers());
                    floorNumberCBox.getSelectionModel().selectFirst();
                    filterAvailableRoomsByFloor();
                });

                return null;
            }
        };

        loadDataTask.setOnFailed(e -> {
            dialogPane.showWarning("LỖI", "Lỗi khi tải dữ liệu");
            loadDataTask.getException().printStackTrace();
        });

        loadDataTask.setOnSucceeded(e -> displayAvailableRooms(availableRooms));

        new Thread(loadDataTask).start();
    }

    private List<String> getFloorNumbers() {
        return List.of("TẤT CẢ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
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
        floorNumberCBox.setOnAction(e -> filterAvailableRoomsByFloor());
        roomDialogBtn.setOnAction(e -> {
            try {
                handleShowRoomInformationAction();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/creating_reservation_form_panels/CreateReservationFormPanel.fxml"));
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

        LocalDateTime actualCheckInDate = HistoryCheckinDAO.getActualCheckInDate(reservationForm.getReservationID());

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomNumber());
        checkInDateLabel.setText(dateTimeFormatter.format(actualCheckInDate != null ? actualCheckInDate : reservationForm.getCheckInDate()));
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

            roomGridPane.setVisible(true);
            roomGridPane.setManaged(true);
            emptyLabelContainer.setVisible(false);
            emptyLabelContainer.setManaged(false);
            roomListContainer.setAlignment(Pos.TOP_CENTER);
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

    private void filterAvailableRoomsByFloor() {
        String selectedFloor = floorNumberCBox.getSelectionModel().getSelectedItem();

        Task<List<Room>> filterTask = new Task<>() {
            @Override
            protected List<Room> call() {
                if ("TẤT CẢ".equals(selectedFloor)) {
                    return availableRooms;
                } else {
                    // Lọc danh sách phòng theo tầng được chọn.
                    return availableRooms.stream()
                            .filter(room -> String.valueOf(room.getRoomFloorNumber()).equals(selectedFloor))
                            .toList();
                }
            }
        };

        filterTask.setOnSucceeded(e -> displayAvailableRooms(filterTask.getValue()));
        filterTask.setOnFailed(e -> dialogPane.showWarning("LỖI", "Lỗi khi lọc phòng"));

        new Thread(filterTask).start();
    }


    // ==================================================================================================================
    // 5.  Setup table lịch sử dùng phòng
    // ==================================================================================================================
    private void setupRoomReservationDetailTableView() {
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
    // 6.  Xử lý sự kiện thay chuyển phòng
    // ==================================================================================================================
    private void handleChangingRoom(Room newRoom) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime checkOutDate = roomWithReservation.getReservationForm().getCheckOutDate();

            if (now.isAfter(checkOutDate)) {
                dialogPane.showInformation("LỖI", "Thời gian lưu trú đã kết thúc. Không thể chuyển phòng.");
                navigateToRoomBookingPanel();
                return;
            }

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn chuyển phòng?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        RoomReservationDetailDAO.changingRoom(
                                roomWithReservation.getRoom().getRoomID(),
                                newRoom.getRoomID(),
                                roomWithReservation.getReservationForm().getReservationID(),
                                employee.getEmployeeID()
                        );

                        dialogPane.showInformation("Thành Công", "Chuyển phòng thành công!");
                        navigateToRoomBookingPanel();
                    }  catch (Exception e) {
                        e.printStackTrace();
                        dialogPane.showInformation("LỖI", e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dialogPane.showInformation("LỖI", "Có lỗi xảy ra khi xử lý thao tác chuyển phòng.");
        }
    }

    // ==================================================================================================================
    // 7. Chức năng hiện nhật ký
    // ==================================================================================================================
    private void handleShowRoomInformationAction() throws IOException {
        String source = "/iuh/fit/view/features/room/ReservationFormDialogView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load();

        ReservationFormDialogViewController reservationFormDialogViewController = loader.getController();
        reservationFormDialogViewController.setReservationForm(roomWithReservation.getReservationForm());

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        String iconPath = "/iuh/fit/icons/menu_icons/ic_room.png";
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        stage.setTitle("Nhật ký phiếu đặt phòng");

        stage.setScene(scene);
        stage.show();
    }
}
