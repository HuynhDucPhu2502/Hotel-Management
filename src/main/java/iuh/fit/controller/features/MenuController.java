package iuh.fit.controller.features;

import iuh.fit.models.Account;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuController {
    @FXML
    private Circle avatar;
    @FXML
    private Text employeePositionText;
    @FXML
    private Label employeeFullNameLabel;
    @FXML
    private VBox employeeInformationContainer;

//  =====================================================
    // dasnhBoardBtn
    @FXML
    private Button dashBoardBtn;

//  =====================================================
    // Employee
    @FXML
    private Button employeeBtn;

    @FXML
    private HBox employeeManagerContainer;
    @FXML
    private Button employeeManagerButton;

    @FXML
    private HBox accountOfEmployeeManagerContainer;
    @FXML
    private Button accountOfEmployeeManagerButton;

    @FXML

    private HBox shiftManagerContainer;
    @FXML
    private Button shiftManagerButton;

    @FXML
    private HBox employeeSearchingContainer;
    @FXML
    private Button employeeSearchingButton;


    @FXML
    private ImageView arrowUpForEmpBtn;
//  =====================================================
    // Room
    @FXML
    private Button roomBtn;

    @FXML
    private HBox pricingManagerContainer;
    @FXML
    private Button pricingManagerButton;

    @FXML
    private HBox roomCategoryManagerContainer;
    @FXML
    private Button roomCategoryManagerButton;

    @FXML
    private HBox roomManagerContainer;
    @FXML
    private Button roomManagerButton;

    @FXML
    private HBox roomSearchingContainer;
    @FXML
    private Button roomSearchingButton;

    @FXML
    private HBox roomBookingContainer;
    @FXML
    private Button roomBookingButton;

    @FXML
    private ImageView arrowUpForRoom;

//  =====================================================
    // Invoice
    @FXML
    private Button invoiceBtn;

    @FXML
    private HBox invoiceManagerContainer;
    @FXML
    private Button invoiceManagerBtn;

    @FXML
    private ImageView arrowUpForInvoice;


//  =====================================================
    // Service
    @FXML
    private Button serviceBtn;

    @FXML
    private HBox serviceCategoryManagerContainer;
    @FXML
    private Button serviceCategoryManagerButton;

    @FXML
    private HBox hotelServiceManagerContainer;
    @FXML
    private Button hotelServiceManagerButton;

    @FXML
    private HBox hotelServiceSearchingContainer;
    @FXML
    private Button hotelServiceSearchingButton;

    @FXML
    private ImageView arrowUpForService;

//  =====================================================
    // Customer
    @FXML
    private Button customerBtn;

    @FXML
    private HBox customerManagerContainer;
    @FXML
    public Button customerManagerButton;

    @FXML
    private HBox customerSearchingContainer;
    @FXML
    public Button customerSearchingButton;

    @FXML
    private ImageView arrowUpForCustomer;


//  =====================================================
    // Statistics
    @FXML
    private Button statisticsBtn;
    @FXML
    private HBox revenueStatisticsContainer;
    @FXML
    private Button revenueStatisticsButton;
    @FXML
    private HBox rateUsingRoomContainer;
    @FXML
    private Button rateUsingRoomButton;
    @FXML
    private ImageView arrowUpForStatistics;


//  =====================================================

    @FXML
    private ScrollPane scrollPane;

    private final Map<String, Boolean> buttonStates = new HashMap<>();

    @FXML
    public void initialize() {
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/iuh/fit/imgs/default_avatar.png")).toExternalForm());
        avatar.setFill(new ImagePattern(image));

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        buttonStates.put("employee", false);
        buttonStates.put("room", false);
        buttonStates.put("service", false);
        buttonStates.put("customer", false);
        buttonStates.put("statistics", false);
        buttonStates.put("history", false);
        buttonStates.put("invoice", false);

        employeeBtn.setOnAction(e -> dropDownMenuEvent(List.of(employeeManagerContainer, accountOfEmployeeManagerContainer, employeeSearchingContainer, shiftManagerContainer), arrowUpForEmpBtn, "employee"));
        roomBtn.setOnAction(e -> dropDownMenuEvent(List.of(pricingManagerContainer, roomCategoryManagerContainer, roomManagerContainer, roomSearchingContainer, roomBookingContainer), arrowUpForRoom, "room"));
        invoiceBtn.setOnAction(e -> dropDownMenuEvent(List.of(invoiceManagerContainer), arrowUpForInvoice, "invoice"));
        serviceBtn.setOnAction(e -> dropDownMenuEvent(List.of(serviceCategoryManagerContainer, hotelServiceManagerContainer, hotelServiceSearchingContainer), arrowUpForService, "service"));
        customerBtn.setOnAction(e -> dropDownMenuEvent(List.of(customerManagerContainer, customerSearchingContainer), arrowUpForCustomer, "customer"));
        statisticsBtn.setOnAction(e -> dropDownMenuEvent(List.of(revenueStatisticsContainer, rateUsingRoomContainer), arrowUpForStatistics, "statistics"));
    }

    public void loadData(Account account) {

        employeePositionText.setText(account.getEmployee().getPosition().toString());
        employeeFullNameLabel.setText(account.getEmployee().getFullName());
    }



    private void dropDownMenuEvent(List<HBox> buttons, ImageView arrow, String stateKey) {
        Boolean state = buttonStates.get(stateKey);

        if (!state) {
            for (HBox button : buttons) {
                button.setVisible(true);
                button.setManaged(true);
            }
            arrow.setRotate(180);
        } else {
            for (HBox button : buttons) {
                button.setVisible(false);
                button.setManaged(false);
            }
            arrow.setRotate(0);
        }

        buttonStates.put(stateKey, !state);
    }

    public Button getDashBoardBtn() {
        return dashBoardBtn;
    }

    public VBox getEmployeeInformationContainer() {
        return employeeInformationContainer;
    }

    public Button getServiceCategoryManagerButton() {
        return serviceCategoryManagerButton;
    }

    public Button getHotelServiceManagerButton() {
        return hotelServiceManagerButton;
    }

    public Button getPricingManagerButton() {
        return pricingManagerButton;
    }

    public Button getHotelServiceSearchingButton() {
        return hotelServiceSearchingButton;
    }

    public Button getRoomCategoryManagerButton() {
        return roomCategoryManagerButton;
    }

    public Button getRoomManagerButton(){
        return roomManagerButton;
    }

    public Button getRoomSearchingButton() {
        return roomSearchingButton;
    }

    public Button getCustomerManagerButton(){ return customerManagerButton; }

    public Button getRoomBookingButton() {
        return roomBookingButton;
    }

    public Button getRevenueStatisticsButton(){return revenueStatisticsButton; }

    public  Button getEmployeeManagerButton(){
        return employeeManagerButton;
    }

    public  Button getAccountOfEmployeeManagerButton(){
        return accountOfEmployeeManagerButton;
    }

    public Button getShiftManagerButton() {
        return shiftManagerButton;
    }

    public Button getEmployeeSearchingButton(){
        return employeeSearchingButton;
    }

    public Button getCustomerSearchingButton(){
        return customerSearchingButton;

    }

    public Button getInvoiceManagerBtn() {
        return invoiceManagerBtn;
    }

    public Button getRateUsingRoomButton(){
        return rateUsingRoomButton;
    }


}

