package iuh.fit.controller;

import iuh.fit.controller.features.MenuController;

import iuh.fit.controller.features.employee.ShiftManagerController;

import iuh.fit.controller.features.room.InvoiceManagerController;

import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.Position;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainController {
    private Account account;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private AnchorPane mainPanel;

    @FXML
    public void initialize() {
    }


    public void setAccount(Account account) {
        this.account = account;
        initializeMenuBar();
    }

    private void initializeMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/MenuPanel.fxml"));
            AnchorPane menuLayout = loader.load();

            MenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.setAccount(account);


                if(EmployeeDAO.getEmployeeByAccountID(account.getAccountID()).getPosition().equals(Position.MANAGER)){
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
                    menuController.getRevenueStatisticsButton().setOnAction(e -> {
                        loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml");
                    });
                    menuController.getRateUsingRoomButton().setOnAction(e -> {
                      loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml");
                });
                    // History
                }else{
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
                    menuController.getRevenueStatisticsButton().setOnAction(e -> {
                        loadPanel("/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml");
                    });
                    menuController.getRateUsingRoomButton().setOnAction(e -> {
                        loadPanel("/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml");
                });
                    // History
                }

            }

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPanel(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            if(EmployeeDAO.getEmployeeByAccountID(account.getAccountID()).getPosition().equals(Position.MANAGER)){
                if (fxmlPath.contains("RoomBookingPanel")) {
                    RoomBookingController roomBookingController = loader.getController();

                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());

                    roomBookingController.setupContext(this, employee);
                } else if (fxmlPath.contains("InvoiceManagerPanel")) {
                    InvoiceManagerController invoiceManagerController = loader.getController();

                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());

                    invoiceManagerController.setupContext(this, employee);
                }
                if (fxmlPath.contains("ShiftManagerPanel")) {
                    ShiftManagerController shiftManagerController = loader.getController();

                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());

                    shiftManagerController.setupContext(employee);
                }
            }else{
                if (fxmlPath.contains("RoomBookingPanel")) {
                    RoomBookingController roomBookingController = loader.getController();

                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());

                    roomBookingController.setupContext(this, employee);
                } else if (fxmlPath.contains("InvoiceManagerPanel")) {
                    InvoiceManagerController invoiceManagerController = loader.getController();

                    Employee employee = EmployeeDAO.getEmployeeByAccountID(account.getAccountID());

                    invoiceManagerController.setupContext(this, employee);
                }
            }



            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getMainPanel() {
        return mainPanel;
    }
}
