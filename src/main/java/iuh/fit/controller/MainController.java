package iuh.fit.controller;

import iuh.fit.controller.features.DashboardController;
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

import java.util.Locale;

public class MainController {
    private Account account;
    private Shift shift;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private AnchorPane mainPanel;

    private MenuController menuController;

    private static boolean ROOM_BOOKING_LOADED = true;

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
        }

        // xử lý sự kiện hiện giao diện cho cả lễ tân và quản lý
        // Dashboard
        menuController.getDashBoardBtn().setOnAction(e -> loadPanel("/iuh/fit/view/features/DashboardPanel.fxml", this, account));
        // Room
        menuController.getRoomSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomSearchingPanel.fxml", this, account));
        menuController.getRoomBookingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/RoomBookingPanel.fxml", this, account));
        // Invoice
        menuController.getInvoiceBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/room/InvoiceManagerPanel.fxml", this, account));
        // Hotel Service
        menuController.getHotelServiceSearchingButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml", this, account));
        // Customer
        menuController.getCustomerSearchingButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml", this, account));
        menuController.getCustomerManagerButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/customer/CustomerManagerPanel.fxml", this, account));
        // Statistics
        menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml", this, account));
        menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml", this, account));
        // Employee Information
        menuController.getEmployeeInformationContainer().setOnMouseClicked(event -> loadPanel("/iuh/fit/view/features/employee_information/EmployeeInformationPanel.fxml", this, account));

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
            menuController.getRevenueStatisticsButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml", this, account));
            menuController.getRateUsingRoomButton().setOnAction(e -> loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml", this, account));
            // Settings

            menuController.getBackupBtn().setOnAction(event -> loadPanel("/iuh/fit/view/features/backup_restore_database/Backup_Restore_Panel.fxml", this, account));



        }
    }

    public void loadPanel(String fxmlPath, MainController mainController, Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            // Lấy controller của giao diện được load và truyền các tham số cần thiết
            Object controller = loader.getController();
            if (controller instanceof RoomBookingController) {
                ((RoomBookingController) controller).setupContext(mainController, account.getEmployee());
            } else if (controller instanceof InvoiceManagerController) {
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                ((InvoiceManagerController) controller).setupContext(mainController, employee);
            } else if (controller instanceof ShiftManagerController) {
                Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());
                ((ShiftManagerController) controller).setupContext(employee);
                ((ShiftManagerController) controller).setUpCurrentShift(shift);
            } else if (controller instanceof EmployeeInformationController) {
                ((EmployeeInformationController) controller).setupContext(account.getEmployee(), mainController);
            } else if (controller instanceof DashboardController) {
                ((DashboardController) controller).setupContext(account, mainController);
            }

            ROOM_BOOKING_LOADED = fxmlPath.contains("RoomBookingPanel");

            // Thay đổi giao diện trong mainPanel
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

}
