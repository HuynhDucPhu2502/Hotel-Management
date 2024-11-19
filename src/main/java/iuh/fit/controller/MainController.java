package iuh.fit.controller;

import iuh.fit.controller.features.MenuController;

import iuh.fit.controller.features.employee.ShiftManagerController;

import iuh.fit.controller.features.employee_information.EmployeeInformationController;
import iuh.fit.controller.features.room.InvoiceManagerController;

import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.Position;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class MainController {
    private Account account;
    private Shift shift;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private AnchorPane mainPanel;


    private Stage mainStage;

    private static boolean ROOM_BOOKING_LOADED = false;

    private MenuController menuController;

    private static boolean ROOM_BOOKING_LOADED = true;


    // Không xóa
    public void initialize() throws SQLException, IOException {
        Locale locale = new Locale("vi", "VN");
        Locale.setDefault(locale);
    }


    public void setupContext(Account account, Stage stage) {

    public void setAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Tài khoản không tồn tại");


        this.account = account;
        this.mainStage = stage;
        initializeMenuBar();
    }

    public void setShift(Shift shift){
        this.shift = shift;
    }

    public void initializeMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/MenuPanel.fxml"));
            AnchorPane menuLayout = loader.load();

            MenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.loadData(account);


                if (Objects.requireNonNull(EmployeeDAO.getEmployeeByAccountID(account.getAccountID())).getPosition().equals(Position.MANAGER))
                {
                    // Dashboard
                    menuController.getDashBoardBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/DashboardPanel.fxml"));

                    // Employee
                    menuController.getEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml"));
                    menuController.getAccountOfEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/AccountManagerPanel.fxml"));

                    menuController.getShiftManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/ShiftManagerPanel.fxml"));

                    menuController.getEmployeeSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeSearchingPanel.fxml"));
                    // Room
                    menuController.getPricingManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/PricingManagerPanel.fxml"));
                    menuController.getRoomCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml"));
                    menuController.getRoomManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomManagerPanel.fxml"));
                    menuController.getRoomSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomSearchingPanel.fxml"));
                    menuController.getRoomBookingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
                    menuController.getInvoiceManagerBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/InvoiceManagerPanel.fxml"));
                    // Service
                    menuController.getServiceCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml"));
                    menuController.getHotelServiceManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml"));
                    menuController.getHotelServiceSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml"));
                    // Customer
                    menuController.getCustomerManagerButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerManagerPanel.fxml"));
                    menuController.getCustomerSearchingButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml"));
                    // Account
                    // Statistics
                    menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml"));
                    menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml"));
                    // History

                    // setting
                    menuController.getBackupBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/backup/BackupPanel.fxml"));
                }
                else if (Objects.requireNonNull(EmployeeDAO.getEmployeeByAccountID(account.getAccountID())).getPosition().equals(Position.RECEPTIONIST))
                {
                    // Dashboard
                    menuController.getDashBoardBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/DashboardPanel.fxml"));

                    // Employee
                    menuController.getEmployeeManagerButton().setDisable(true);
                    menuController.getAccountOfEmployeeManagerButton().setDisable(true);
                    menuController.getShiftManagerButton().setDisable(true);
                    menuController.getEmployeeSearchingButton().setDisable(true);

                    // Room
                    menuController.getPricingManagerButton().setDisable(true);
                    menuController.getRoomCategoryManagerButton().setDisable(true);
                    menuController.getRoomManagerButton().setDisable(true);
                    menuController.getRoomSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomSearchingPanel.fxml"));
                    menuController.getRoomBookingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
                    menuController.getInvoiceManagerBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/InvoiceManagerPanel.fxml"));

                    // Service
                    menuController.getServiceCategoryManagerButton().setDisable(true);
                    menuController.getHotelServiceManagerButton().setDisable(true);
                    menuController.getHotelServiceSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml"));

                    // Customer
                    menuController.getCustomerManagerButton().setDisable(true);
                    menuController.getCustomerSearchingButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml"));
                    // Account

                    // Statistics
                    menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml"));
                    menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml"));
                    // History
                }

                menuController.getEmployeeInformationContainer().setOnMouseClicked(event -> loadPanel("/iuh/fit/view/features/employee_information/EmployeeInformationPanel.fxml"));

            menuController = loader.getController();

            menuController.loadData(account);
            setupMenuButtons();

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuLayout.getChildren());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupMenuButtons() {
        Position position = account.getEmployee().getPosition();

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
            menuController.getCustomerManagerButton().setDisable(true);
        }

        // xử lý sự kiện hiện giao diện cho cả lễ tân và quản lý
        // Dashboard
        menuController.getDashBoardBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/DashboardPanel.fxml"));
        // Room
        menuController.getRoomSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomSearchingPanel.fxml"));
        menuController.getRoomBookingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
        // Invoice
        menuController.getInvoiceManagerBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/InvoiceManagerPanel.fxml"));
        // Hotel Service
        menuController.getHotelServiceSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml"));
        // Customer
        menuController.getCustomerSearchingButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml"));
        menuController.getCustomerManagerButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerManagerPanel.fxml"));
        // Statistics
        menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml"));
        menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml"));
        // Employee Information
        menuController.getEmployeeInformationContainer().setOnMouseClicked(event -> loadPanel("/iuh/fit/view/features/employee_information/EmployeeInformationPanel.fxml"));

        // Thêm các sự kiện xử lý giao diện cho quản lý
        if (position.equals(Position.MANAGER))
        {
            // Employee
            menuController.getEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml"));
            menuController.getAccountOfEmployeeManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/AccountManagerPanel.fxml"));
            menuController.getShiftManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/ShiftManagerPanel.fxml"));
            menuController.getEmployeeSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/employee/EmployeeSearchingPanel.fxml"));
            // Room
            menuController.getPricingManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/PricingManagerPanel.fxml"));
            menuController.getRoomCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml"));
            menuController.getRoomManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomManagerPanel.fxml"));
            // Hotel Service
            menuController.getServiceCategoryManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml"));
            menuController.getHotelServiceManagerButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml"));
            // Statistics
            menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml"));
            menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml"));
        }
    }

    private void loadPanel(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();
            if (fxmlPath.contains("RoomBookingPanel")) {
                RoomBookingController roomBookingController = loader.getController();
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                roomBookingController.setupContext(this, employee);
            } else if (fxmlPath.contains("InvoiceManagerPanel")) {
                InvoiceManagerController invoiceManagerController = loader.getController();
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                invoiceManagerController.setupContext(this, employee);
            } else if (fxmlPath.contains("ShiftManagerPanel")) {
                ShiftManagerController shiftManagerController = loader.getController();
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                shiftManagerController.setupContext(employee);
                shiftManagerController.setUpCurrentShift(shift);
            } else if (fxmlPath.contains("EmployeeInformation")) {
                EmployeeInformationController employeeInformationController = loader.getController();
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                employeeInformationController.setupContext(employee, this);
            }

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

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

    public static boolean isRoomBookingLoaded() {
        return ROOM_BOOKING_LOADED;
    }

    public static void setRoomBookingLoaded(boolean roomBookingLoaded) {
        ROOM_BOOKING_LOADED = roomBookingLoaded;
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
