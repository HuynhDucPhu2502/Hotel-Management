package iuh.fit.controller.features.room.checking_out_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_in_reservation_list_controllers.ReservationListController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.room_changing_controllers.RoomChangingController;
import iuh.fit.controller.features.room.service_ordering_controllers.ServiceOrderingController;
import iuh.fit.dao.HistoryCheckinDAO;
import iuh.fit.dao.RoomReservationDetailDAO;
import iuh.fit.dao.RoomUsageServiceDAO;
import iuh.fit.dao.misc.ShiftDetailDAO;
import iuh.fit.dao.misc.ShiftDetailForInvoiceDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import iuh.fit.utils.GlobalMessage;
import iuh.fit.utils.RoomManagementService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


public class CheckingOutReservationFormController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, bookingRoomNavigateLabel;

    @FXML
    private Button navigateToCreateReservationFormBtn,
            navigateToReservationListBtn, navigateToRoomChangingBtn,
            navigateToServiceOrderingBtn;

    @FXML
    private Button checkOutBtn, checkOutEarlyBtn;

    @FXML
    private Label roomNumberLabel, roomCategoryLabel, checkInDateLabel,
            checkOutDateLabel, stayLengthLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel,
            cusomerPhoneNumberLabel, customerEmailLabel,
            customerIDCardNumberLabel;

    @FXML
    private TableView<RoomUsageService> roomUsageServiceTableView;
    @FXML
    private TableColumn<RoomUsageService, String> roomUsageServiceIDColumn;
    @FXML
    private TableColumn<RoomUsageService, String> serviceNameColumn;
    @FXML
    private TableColumn<RoomUsageService, Integer> quantityColumn;
    @FXML
    private TableColumn<RoomUsageService, Double> unitPriceColumn;
    @FXML
    private TableColumn<RoomUsageService, Double> totalPriceColumn;
    @FXML
    private TableColumn<RoomUsageService, String> dateAddedColumn;
    @FXML
    private TableColumn<RoomUsageService, String> employeeAddedColumn;

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
    private TitledPane titledPane;

    @FXML
    private DialogPane dialogPane;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private NotificationButtonController notificationButtonController;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
        setupRoomReservationDetailTableView();
        setupRoomUsageServiceTableView();
    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation,
                             NotificationButtonController notificationButtonController) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;
        this.notificationButtonController = notificationButtonController;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());

        setupReservationForm();
        setupButtonActions();
        loadData();
    }

    private void loadData() {
        List<RoomReservationDetail> roomReservationDetails = RoomReservationDetailDAO.getByReservationFormID(
                roomWithReservation.getReservationForm().getReservationID()
        );
        ObservableList<RoomReservationDetail> roomReservationDetailsData = FXCollections.observableArrayList(roomReservationDetails);
        roomReservationDetailTableView.setItems(roomReservationDetailsData);
        roomReservationDetailTableView.refresh();

        List<RoomUsageService> roomUsageServices = RoomUsageServiceDAO.getByReservationFormID(roomWithReservation.getReservationForm().getReservationID());
        ObservableList<RoomUsageService> roomUsageServicesData = FXCollections.observableArrayList(roomUsageServices);
        roomUsageServiceTableView.setItems(roomUsageServicesData);
        roomUsageServiceTableView.refresh();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigateLabel.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        navigateToReservationListBtn.setOnAction(e -> navigateToReservationListPanel());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        RoomStatus roomStatus = roomWithReservation.getRoom().getRoomStatus();

        switch (roomStatus) {
            case OVERDUE -> {
                navigateToServiceOrderingBtn.setDisable(true);
                navigateToRoomChangingBtn.setDisable(true);
            }
            case ON_USE -> {
                navigateToRoomChangingBtn.setOnAction(e -> navigateToRoomChangingPanel());
                navigateToServiceOrderingBtn.setOnAction(e -> navigateToServiceOrderingPanel());
            }
        }
        navigateToRoomChangingBtn.setOnAction(e -> navigateToRoomChangingPanel());
        navigateToServiceOrderingBtn.setOnAction(e -> navigateToServiceOrderingPanel());

        // Current Panel Button
        if (!isOverdueTime()) {
            checkOutBtn.setDisable(true);
            checkOutEarlyBtn.setOnAction(e -> handleCheckOutEarly());
        } else {
            checkOutEarlyBtn.setDisable(true);
            checkOutBtn.setOnAction(e -> handleCheckOut());

        }
    }

    private void setupReservationForm() {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        Room reservationFormRoom = roomWithReservation.getRoom();
        Customer reservationFormCustomer = roomWithReservation.getReservationForm().getCustomer();

        LocalDateTime actualCheckInDate = HistoryCheckinDAO.getActualCheckInDate(reservationForm.getReservationID());

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomCategory().getRoomCategoryName());
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

    private void navigateToReservationListPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/checking_in_reservation_list_panels/ReservationListPanel.fxml"));
            AnchorPane layout = loader.load();

            ReservationListController reservationListController = loader.getController();
            reservationListController.setupContext(
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

    // ==================================================================================================================
    // 4.  Setup 2 table lịch sử dùng phòng và lịch sử dùng dịch vụ
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

    private void setupRoomUsageServiceTableView() {
        roomUsageServiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomUsageServiceId"));
        serviceNameColumn.setCellValueFactory(data -> {
            HotelService service = data.getValue().getHotelService();
            String serviceName = (service != null && service.getServiceName() != null) ? service.getServiceName() : "KHÔNG CÓ";
            return new SimpleStringProperty(serviceName);
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        totalPriceColumn.setCellValueFactory(data -> {
            double totalPrice = data.getValue().getQuantity() * data.getValue().getUnitPrice();
            return new SimpleDoubleProperty(totalPrice).asObject();
        });
        dateAddedColumn.setCellValueFactory(data -> {
            LocalDateTime dateAdded = data.getValue().getDateAdded();
            String formattedDate = (dateAdded != null) ? dateAdded.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "Không có";
            return new SimpleStringProperty(formattedDate);
        });
        employeeAddedColumn.setCellValueFactory(data -> {
            Employee employee = data.getValue().getEmployee();
            String employeeName = (employee != null && employee.getFullName() != null) ? employee.getFullName() : "Không có";
            return new SimpleStringProperty(employeeName);
        });
    }

    // ==================================================================================================================
    // 5. Xử lý sự kiện checkout
    // ==================================================================================================================
    private void handleCheckOut() {
        try {
            DialogPane.Dialog<ButtonType> confirmDialog = dialogPane.showConfirmation(
                    "XÁC NHẬN CHECK-OUT",
                    "Bạn có chắc chắn muốn thực hiện check-out cho phòng này không?"
            );

            confirmDialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        RoomManagementService.handleCheckOut(roomWithReservation, employee);

                        dialogPane.showInformation("THÀNH CÔNG", "Check-out và tạo hóa đơn thành công!");
                        notificationButtonController.getInfo(
                                GlobalMessage.MANUALLY_CHECKOUT,
                                "Phòng " + roomWithReservation.getRoom().getRoomID() + " đã được checkout thủ công"
                        );
                        ShiftDetailDAO.updateNumbOfCheckOutRoom(mainController.getShiftDetailID());
                        ShiftDetailForInvoiceDAO.addInvoiceID(mainController.getShiftDetailID(), roomWithReservation.getReservationForm().getReservationID());
                        navigateToRoomBookingPanel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dialogPane.showInformation("LỖI", ex.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dialogPane.showInformation("LỖI", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại!");
        }
    }

    private void handleCheckOutEarly() {
        try {
            DialogPane.Dialog<ButtonType> confirmDialog = dialogPane.showConfirmation(
                    "XÁC NHẬN CHECK-OUT",
                    "Bạn có chắc chắn muốn thực hiện check-out SỚM cho phòng này không?"
            );

            confirmDialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        RoomManagementService.handleCheckoutEarly(roomWithReservation, employee);

                        dialogPane.showInformation("THÀNH CÔNG", "Check-out và tạo hóa đơn thành công!");
                        notificationButtonController.getInfo(
                                GlobalMessage.MANUALLY_CHECKOUT,
                                "Phòng " + roomWithReservation.getRoom().getRoomID() + " đã được checkout SỚM thủ công"
                        );
                        ShiftDetailDAO.updateNumbOfCheckOutRoom(mainController.getShiftDetailID());
                        ShiftDetailForInvoiceDAO.addInvoiceID(mainController.getShiftDetailID(), roomWithReservation.getReservationForm().getReservationID());
                        navigateToRoomBookingPanel();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dialogPane.showInformation("LỖI", ex.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dialogPane.showInformation("LỖI", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại!");
        }
    }

    // ==================================================================================================================
    // 6. Kiểm tra thời gian có phù hợp
    // ==================================================================================================================
    private boolean isOverdueTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = roomWithReservation.getReservationForm().getCheckOutDate();
        return now.isAfter(checkOutDate);
    }
}
