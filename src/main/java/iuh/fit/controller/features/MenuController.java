package iuh.fit.controller.features;

import iuh.fit.models.Account;
import iuh.fit.utils.ConvertImage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void loadData(Account account) throws Exception {
        String base64 = account.getEmployee().getAvatar();
        File img = ConvertImage.decodeBase64ToImage(base64);
        if (img.exists()) {
            Image image = new Image(img.toURI().toString());
            avatar.setFill(new ImagePattern(image));
        } else {
            System.err.println("Không thể tạo ảnh từ Base64 hoặc file không tồn tại.");
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/HelpCenterWebUI.fxml"));

            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Trung tâm hỗ trợ");
            stage.setScene(new Scene(root));
            stage.setHeight(800);
            stage.setWidth(1000);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

}

