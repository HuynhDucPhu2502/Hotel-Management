package iuh.fit.controller.features.room.checking_out_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.HistoryCheckOutDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomReservationDetailDAO;
import iuh.fit.dao.RoomUsageServiceDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
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
    private Button backBtn, bookingRoomNavigate, checkOutBtn;

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

    // Context
    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
        setupRoomReservationDetailTableView();
        setupRoomUsageServiceTableView();
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
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());
        checkOutBtn.setOnAction(e -> handleCheckOut());

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
    }

    private void handleCheckOut() {
        try {
            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> confirmDialog = dialogPane.showConfirmation(
                    "XÁC NHẬN CHECK-OUT",
                    "Bạn có chắc chắn muốn thực hiện check-out cho phòng này không?"
            );

            confirmDialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        HistoryCheckOut historyCheckOut = new HistoryCheckOut();
                        historyCheckOut.setHistoryCheckOutID(HistoryCheckOutDAO.getNextID());
                        historyCheckOut.setCheckOutDate(LocalDateTime.now());
                        historyCheckOut.setReservationForm(roomWithReservation.getReservationForm());
                        historyCheckOut.setEmployee(employee);

                        HistoryCheckOutDAO.createData(historyCheckOut);

                        Room room = roomWithReservation.getRoom();
                        RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);

                        dialogPane.showInformation("THÀNH CÔNG", "Check-out thành công!");
                        navigateToRoomBookingPanel();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        dialogPane.showInformation("LỖI", "Đã xảy ra lỗi trong quá trình check-out. Vui lòng thử lại!");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dialogPane.showInformation("LỖI", "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại!");
        }
    }








}
