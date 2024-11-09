package iuh.fit.controller.features.room.service_ordering_controllers;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.room.checking_in_reservation_list_controllers.ReservationListController;
import iuh.fit.controller.features.room.room_changing_controllers.RoomChangingController;
import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceOrderingController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, bookingRoomNavigate;

    @FXML
    private Button navigateToCreateReservationFormBtn,
            navigateToReservationListBtn, navigateToRoomChanging;


    @FXML
    private Label roomNumberLabel, roomCategoryLabel,
            checkInDateLabel, checkOutDateLabel,
            stayLengthLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel,
            cusomerPhoneNumberLabel, customerEmailLabel,
            customerIDCardNumberLabel;

    @FXML
    private ComboBox<String> serviceCategoryCBox;


    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));


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
    private DialogPane dialogPane;

    @FXML
    private TitledPane titledPane;

    @FXML
    private HBox emptyLabelContainer;
    @FXML
    private VBox serviceListContainer;
    @FXML
    private GridPane serviceGridPane;

    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private List<HotelService> hotelServiceList;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        dialogPane.toFront();
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

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Box Navigate Button
        navigateToReservationListBtn.setOnAction(e -> navigateToReservationListPanel());
        navigateToCreateReservationFormBtn.setOnAction(e -> navigateToCreateReservationFormPanel());
        navigateToRoomChanging.setOnAction(e -> navigateToRoomChanging());

        serviceCategoryCBox.setOnAction(e -> filterServicesByCategory());
    }

    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                hotelServiceList = HotelServiceDAO.getHotelService();

                loadTable();

                // Tải tên danh mục dịch vụ
                ArrayList<String> categoryNames = (ArrayList<String>) ServiceCategoryDAO.getServiceCategoryNames();
                categoryNames.addFirst("TẤT CẢ");

                Platform.runLater(() -> {
                    serviceCategoryCBox.getItems().setAll(categoryNames);
                    serviceCategoryCBox.getSelectionModel().selectFirst();
                });

                // Sau khi tải dữ liệu xong, gọi hàm lọc dịch vụ
                Platform.runLater(() -> filterServicesByCategory());

                return null;
            }
        };

        loadDataTask.setOnFailed(e -> {
            dialogPane.showWarning("LỖI", "Lỗi khi tải dữ liệu");
            loadDataTask.getException().printStackTrace();
        });

        loadDataTask.setOnSucceeded(e -> displayServices(hotelServiceList));

        new Thread(loadDataTask).start();
    }

    private void loadTable() {
        List<RoomUsageService> roomUsageServices = RoomUsageServiceDAO.getByReservationFormID(roomWithReservation.getReservationForm().getReservationID());
        ObservableList<RoomUsageService> data = FXCollections.observableArrayList(roomUsageServices);

        Platform.runLater(() -> {
            roomUsageServiceTableView.setItems(data);
            roomUsageServiceTableView.refresh();
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

    private void navigateToRoomChanging() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/changing_room_panels/RoomChangingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomChangingController roomChangingController = loader.getController();
            roomChangingController.setupContext(
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

    private void displayServices(List<HotelService> services) {
        if (!services.isEmpty()) {
            serviceGridPane.getChildren().clear();

            int row = 0, col = 0;

            try {
                for (HotelService service : services) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/iuh/fit/view/features/room/ordering_services_panels/ServiceItem.fxml"));
                    Pane serviceItem = loader.load();
                    System.out.println(service.getServiceCategory().getServiceCategoryName());
                    ServiceItemController controller = loader.getController();
                    controller.setupContext(service);
                    controller.getAddServiceBtn().setOnAction(e ->
                            handleAddService(service, controller.getAmountField().getValue(), controller.getAmountField())
                    );

                    serviceGridPane.add(serviceItem, col, row);

                    col++;
                    if (col == 4) {
                        col = 0;
                        row++;
                    }
                }

                serviceGridPane.setVisible(true);
                serviceGridPane.setManaged(true);
                emptyLabelContainer.setVisible(false);
                emptyLabelContainer.setManaged(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            serviceGridPane.setVisible(false);
            serviceGridPane.setManaged(false);

            if (emptyLabelContainer.getChildren().isEmpty()) {
                Label emptyLabel = new Label("Không có dịch vụ nào khả dụng.");
                emptyLabel.setStyle("-fx-font-size: 42px; -fx-text-fill: gray;");
                emptyLabelContainer.getChildren().add(emptyLabel);
            }

            emptyLabelContainer.setVisible(true);
            emptyLabelContainer.setManaged(true);

            serviceListContainer.setAlignment(Pos.CENTER);
        }
    }

    // ==================================================================================================================
    // 5.  Setup table lịch sử dùng dịch vụ
    // ==================================================================================================================
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
    // 6. Xử lý sự kiện thêm dịch vụ
    // ==================================================================================================================
    private void handleAddService(HotelService service, int amount, Spinner<Integer> amountField) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = roomWithReservation.getReservationForm().getCheckOutDate();

        if (now.isAfter(checkOutDate)) {
            dialogPane.showInformation("LỖI", "Thời gian lưu trú đã kết thúc. Không thể thêm dịch vụ.");
            navigateToRoomBookingPanel();
            return;
        }

        com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                "XÁC NHẬN",
                "Bạn có chắc chắn muốn thêm dịch vụ: " + service.getServiceName() + " với số lượng: " + amount + " không?"
        );

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                handleAddServiceToDB(service, amount, amountField);
                dialogPane.showInformation("Thành Công", "Dịch vụ đã được thêm thành công!");
                loadTable();
            }
        });
    }

    private void handleAddServiceToDB(HotelService service, int amount, Spinner<Integer> amountField) {
        try {
            RoomUsageService roomUsageService = new RoomUsageService();
            roomUsageService.setRoomUsageServiceId(RoomUsageServiceDAO.getNextRoomUsageServiceID());
            roomUsageService.setQuantity(amount);
            roomUsageService.setUnitPrice(service.getServicePrice());
            roomUsageService.setHotelService(service);
            roomUsageService.setReservationForm(roomWithReservation.getReservationForm());
            roomUsageService.setEmployee(employee);
            roomUsageService.setDateAdded(LocalDateTime.now());

            RoomUsageServiceDAO.createData(roomUsageService);

            amountField.getValueFactory().setValue(1);
        } catch (Exception e) {
            dialogPane.showInformation("LỖI", e.getMessage());
        }
    }

    // ==================================================================================================================
    // 7. Chức năng tìm kiếm
    // ==================================================================================================================
    private void filterServicesByCategory() {
        Task<List<HotelService>> filterTask = new Task<>() {
            @Override
            protected List<HotelService> call() {
                String selectedCategory = serviceCategoryCBox.getSelectionModel().getSelectedItem();
                if ("TẤT CẢ".equals(selectedCategory)) {
                    return hotelServiceList;
                } else {
                    return hotelServiceList.stream()
                            .filter(service -> service.getServiceCategory().getServiceCategoryName().equalsIgnoreCase(selectedCategory))
                            .toList();
                }
            }
        };

        filterTask.setOnSucceeded(e -> displayServices(filterTask.getValue()));
        filterTask.setOnFailed(e -> dialogPane.showWarning("LỖI", "Lỗi khi lọc dịch vụ"));

        new Thread(filterTask).start();
    }


}
