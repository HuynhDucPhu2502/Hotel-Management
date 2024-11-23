package iuh.fit.controller.features;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
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
    // Dashboard
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
    // Settings
    @FXML
    private Button settingBtn;
    @FXML
    private HBox backupSettingContainer;
    @FXML
    private Button backupBtn;
    @FXML
    private ImageView arrowUpForSetting;

//  =====================================================
    // Help
    @FXML
    private Button helpBtn;

//  =====================================================

    @FXML
    private ScrollPane scrollPane;

    private final Map<String, Boolean> buttonStates = new HashMap<>();

    @FXML
    public void initialize() {

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        buttonStates.put("employee", false);
        buttonStates.put("room", false);
        buttonStates.put("service", false);
        buttonStates.put("customer", false);
        buttonStates.put("statistics", false);
        buttonStates.put("history", false);
        buttonStates.put("invoice", false);
        buttonStates.put("setting", false);

        employeeBtn.setOnAction(e -> dropDownMenuEvent(List.of(employeeManagerContainer, accountOfEmployeeManagerContainer, employeeSearchingContainer, shiftManagerContainer), arrowUpForEmpBtn, "employee"));
        roomBtn.setOnAction(e -> dropDownMenuEvent(List.of(pricingManagerContainer, roomCategoryManagerContainer, roomManagerContainer, roomSearchingContainer, roomBookingContainer), arrowUpForRoom, "room"));
        invoiceBtn.setOnAction(e -> dropDownMenuEvent(List.of(invoiceManagerContainer), arrowUpForInvoice, "invoice"));
        serviceBtn.setOnAction(e -> dropDownMenuEvent(List.of(serviceCategoryManagerContainer, hotelServiceManagerContainer, hotelServiceSearchingContainer), arrowUpForService, "service"));
        customerBtn.setOnAction(e -> dropDownMenuEvent(List.of(customerManagerContainer, customerSearchingContainer), arrowUpForCustomer, "customer"));
        statisticsBtn.setOnAction(e -> dropDownMenuEvent(List.of(revenueStatisticsContainer, rateUsingRoomContainer), arrowUpForStatistics, "statistics"));
        settingBtn.setOnAction(e -> dropDownMenuEvent(List.of(backupSettingContainer), arrowUpForSetting, "setting"));
        helpBtn.setOnAction(e -> openHelpCenter());
    }



    public void loadData(Account account) {

        System.out.println(account.getEmployee());
        String path = account.getEmployee().getAvatar();

        int index = path.indexOf("/iuh");
        if (index != -1) {
            path = path.substring(index);
        } else {
            return;
        }

        try {
            URL resourceUrl = getClass().getResource(path);

            System.out.println(path);
            System.out.println(resourceUrl);


            if (resourceUrl == null || path == null) {
                System.err.println("Không tìm thấy file ảnh: " + path);
                return;
            }

            Image image = new Image(resourceUrl.toExternalForm());
            if (image.isError()) {
                System.err.println("Lỗi load ảnh: " + image.getException());
                return;
            }

            avatar.setFill(new ImagePattern(image));
            System.out.println("Load ảnh thành công!");

        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }

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

    private void openHelpCenter() {
        String helpFilePath = Objects.requireNonNull(getClass().getResource(
                "/iuh/fit/help-center-website/html/index.html"
        )).toExternalForm();

        if (helpFilePath != null) {
            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();
            engine.load(helpFilePath);

            // URL Label
            Label locationLabel = new Label();
            locationLabel.textProperty().bind(engine.locationProperty());

            Stage helpStage = new Stage();
            helpStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ chính
            helpStage.setTitle("Help Center");

            VBox vbox = new VBox(10, locationLabel, webView);
            Scene scene = new Scene(vbox, 800, 600);
            helpStage.setScene(scene);

            helpStage.show();
        } else {
            throw new IllegalArgumentException("Không tìm thấy file HTML");
        }
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

    public Button getBackupBtn() {
        return backupBtn;
    }

    public Circle getAvatar(){
        return avatar;
    }

}

