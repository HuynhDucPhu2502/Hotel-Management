package iuh.fit.controller;

import iuh.fit.controller.features.DashboardController;
import iuh.fit.controller.features.MenuController;

import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.TopController;
import iuh.fit.controller.features.customer.CustomerManagerController;
import iuh.fit.controller.features.customer.CustomerSearchingController;
import iuh.fit.controller.features.employee.EmployeeManagerController;
import iuh.fit.controller.features.employee.EmployeeSearchingController;
import iuh.fit.controller.features.employee.ShiftManagerController;

import iuh.fit.controller.features.employee_information.EmployeeInformationController;
import iuh.fit.controller.features.invoice.InvoiceManagerController;

import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.RoomManagerController;
import iuh.fit.controller.features.room.RoomSearchingController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.CreateReservationFormController;
import iuh.fit.controller.features.service.HotelServiceManagerController;
import iuh.fit.controller.features.service.HotelServiceSearchingController;
import iuh.fit.controller.features.statistics.AnalyzeBeforeLogOutController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.Position;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.BackupDatabase;
import iuh.fit.utils.RoomManagementService;
import iuh.fit.utils.TimelineManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;

import static iuh.fit.dao.misc.ShiftDetailDAO.createStartingPoint;

public class MainController {
    private Account account;
    private Shift shift;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private AnchorPane mainPanel;
    @FXML
    private AnchorPane topPanel;

    private Button informationBtn;

    private MenuController menuController;

    private static boolean ROOM_BOOKING_LOADED = true;

    private static NotificationButtonController notificationButtonController;

    private int shiftDetailID;

    // Không xóa
    public void initialize() {
        Locale locale = new Locale("vi", "VN");
        Locale.setDefault(locale);

    }

    public void setAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Tài khoản không tồn tại");

        this.account = account;
        initializeDashboard();
        initializeMenuBar();
        initializeTopBar();
        shiftDetailID = createStartingPoint();

        Platform.runLater(this::handelCloseEvent);
    }

    private void handelCloseEvent(){
        mainPanel.getScene().getWindow().setOnCloseRequest(e->{
            e.consume();
            try {
                BackupDatabase.backupData(null);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String source = "/iuh/fit/view/features/statistics/AnalyzeBeforeLogOut.fxml";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
            AnchorPane layout = null; // Gọi load() trước khi getController()
            try {
                layout = loader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            AnalyzeBeforeLogOutController analyzeBeforeLogOutController = loader.getController();
            analyzeBeforeLogOutController.initialize(this, null);

            Scene scene = new Scene(layout);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thống kê ca làm");
            stage.setScene(scene);
            stage.setResizable(false);
            setStage(stage);
            stage.show();
        });
    }

    private void setStage(Stage stage) {
    }

    public void setShift(Shift shift){
        this.shift = shift;
    }

    public void initializeDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/DashboardPanel.fxml"));
            AnchorPane dashboardLayout = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.setupContext(account, this);

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(dashboardLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/MenuPanel.fxml"));
            AnchorPane menuLayout = loader.load();

            menuController = loader.getController();

            menuController.loadData(account);
            setupMenuButtons();

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuLayout.getChildren());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeTopBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/TopPanel.fxml"));
            AnchorPane topLayout = loader.load();

            TopController topController = loader.getController();
            notificationButtonController = topController.initialize(account, this);

            RoomManagementService.startAutoCheckoutScheduler(notificationButtonController, this);

            topPanel.getChildren().clear();
            topPanel.getChildren().addAll(topLayout.getChildren());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMenuButtons() {
        Position position = account.getEmployee().getPosition();
        informationBtn = menuController.getInformationBtn();
        handleTooltips();

        // Tắt các button menu không thuộc về lễ tân
        if (position.equals(Position.RECEPTIONIST)) {
            menuController.getEmployeeManagerButton().setDisable(true);
            menuController.getAccountOfEmployeeManagerButton().setDisable(true);
            menuController.getShiftManagerButton().setDisable(true);
            menuController.getEmployeeSearchingButton().setDisable(true);
            menuController.getPricingManagerButton().setDisable(true);
            menuController.getRoomCategoryManagerButton().setDisable(true);
            menuController.getRoomManagerButton().setDisable(true);
            menuController.getServiceCategoryManagerButton().setDisable(true);
            menuController.getHotelServiceManagerButton().setDisable(true);
            menuController.getBackupBtn().setDisable(true);
        }

        // xử lý sự kiện hiện giao diện cho cả lễ tân và quản lý
        // Dashboard
        menuController.getDashBoardBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/DashboardPanel.fxml", this, account));
        // Room
        menuController.getRoomSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomSearchingPanel.fxml", this, account));
        menuController.getRoomBookingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml", this, account));
        // Invoice
        menuController.getInvoiceBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/invoice/InvoiceManagerPanel.fxml", this, account));
        // Hotel Service
        menuController.getHotelServiceSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml", this, account));
        // Customer
        menuController.getCustomerSearchingButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml", this, account));
        menuController.getCustomerManagerButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerManagerPanel.fxml", this, account));
        // Statistics
        menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RevenueStatisticalPanel.fxml", this, account));
        menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsTab.fxml", this, account));
        // Employee Information
        menuController.getEmployeeInformationContainer().setOnMouseClicked(event -> loadPanel("/iuh/fit/view/features/employee_information/EmployeeInformationPanel.fxml", this, account));

        informationBtn.setOnAction(event -> loadPanelInformation("/iuh/fit/view/features/InformationPanel.fxml"));


        // Thêm các sự kiện xử lý giao diện cho quản lý
        if (position.equals(Position.MANAGER)) {
            // Employee
            menuController.getEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml", this, account));
            menuController.getAccountOfEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/AccountManagerPanel.fxml", this, account));
            menuController.getShiftManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/ShiftManagerPanel.fxml", this, account));
            menuController.getEmployeeSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeSearchingPanel.fxml", this, account));
            // Room
            menuController.getPricingManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/PricingManagerPanel.fxml", this, account));
            menuController.getRoomCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml", this, account));
            menuController.getRoomManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomManagerPanel.fxml", this, account));
            // Hotel Service
            menuController.getServiceCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml", this, account));
            menuController.getHotelServiceManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml", this, account));
            // Statistics
            menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RevenueStatisticalPanel.fxml", this, account));
            menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsTab.fxml", this, account));
            // Settings

            menuController.getBackupBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/backup_restore_database/Backup_Restore_Panel.fxml", this, account));
        }
    }

    private void handleTooltips() {
        // Tạo Tooltip
        Tooltip tooltip = new Tooltip("Về phần mềm");
        Tooltip.install(informationBtn, tooltip); // Gắn Tooltip vào Button

        // Thêm Tooltip bằng cách setTooltip
        informationBtn.setTooltip(tooltip);
        tooltip.setShowDelay(javafx.util.Duration.millis(400));
    }

    public void loadPanel(String fxmlPath, MainController mainController, Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            Object controller = loader.getController();

            switch (controller) {
                case RoomBookingController roomBookingController ->
                        roomBookingController.setupContext(mainController, account.getEmployee(), notificationButtonController);

                case InvoiceManagerController invoiceManagerController -> {
                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                    invoiceManagerController.setupContext(mainController, employee, notificationButtonController);
                }

                case ShiftManagerController shiftManagerController -> {
                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                    shiftManagerController.setupContext(employee);
                    shiftManagerController.setUpCurrentShift(shift);
                }

                case EmployeeInformationController employeeInformationController ->
                        employeeInformationController.setupContext(account.getEmployee(), mainController);

                case DashboardController dashboardController ->
                    dashboardController.setupContext(account, mainController);

                case EmployeeSearchingController employeeSearchingController ->
                        employeeSearchingController.setupContext(this);

                case HotelServiceSearchingController hotelServiceSearchingController ->
                        hotelServiceSearchingController.setupContext(this, account);

                case CustomerSearchingController customerSearchingController ->
                        customerSearchingController.setupContext(this, account);

                case RoomSearchingController roomSearchingController ->
                        roomSearchingController.setupContext(this, account);

                default -> {}
            }

            if (!fxmlPath.contains("RoomBookingPanel") && !fxmlPath.contains("DashBoardPanel")) {
                TimelineManager.getInstance().removeTimeline("REALTIME_DASHBOARD");
                TimelineManager.getInstance().stopAllTimelines();
            }
            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadPanelEmployeeManagerController(String fxmlPath, Employee emp){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            EmployeeManagerController controller = loader.getController();

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());

            Platform.runLater(() -> controller.setInformation(emp));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanelRoomManagerController(String fxmlPath, Room room){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            RoomManagerController controller = loader.getController();

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());

            Platform.runLater(() -> controller.setInformation(room));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanelCreateReservationFormController(String fxmlPath, MainController mainController, Account account, RoomWithReservation room){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            CreateReservationFormController controller = loader.getController();

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
            MainController.setRoomBookingLoaded(false);
            Platform.runLater(() -> controller.setupContext(
                    mainController, account.getEmployee(), room,
                    null, null, null, notificationButtonController
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanelHotelServiceManagerController(String fxmlPath, HotelService service){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            HotelServiceManagerController controller = loader.getController();

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");
            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
            Platform.runLater(() -> controller.setInformation(service));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanelCustomerManagerController(String fxmlPath, Customer customer){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            CustomerManagerController controller = loader.getController();

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");
            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
            Platform.runLater(() -> controller.setInformation(customer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanelInformation(String fxmlPath){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getMainPanel() {
        return mainPanel;
    }

    public Account getAccount() {
        return account;
    }

    public Shift getShift(){
        return shift;
    }

    public int getShiftDetailID(){
        return shiftDetailID;
    }

    public static boolean isRoomBookingLoaded() {
        return ROOM_BOOKING_LOADED;
    }

    public static void setRoomBookingLoaded(boolean roomBookingLoaded) {
        ROOM_BOOKING_LOADED = roomBookingLoaded;
    }

}
