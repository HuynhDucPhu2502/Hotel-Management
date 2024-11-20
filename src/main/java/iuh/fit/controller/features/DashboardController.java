package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Account;
import iuh.fit.models.enums.Position;
import iuh.fit.models.enums.RoomStatus;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label roomAvailabelCountLabel, roomOnUseCountLabel,
            roomOverdueCountLabel;

    @FXML
    private GridPane featureGridPane;

    @FXML
    private TextField inputTextField;

    private Account account;
    private MainController mainController;

    // 2 phần:
    // Phần 1: tên chức năng, danh sách từ khoá
    // Phần 2: đường dẫn FXML
    private HashMap<HashMap<String, String>, String> featureKeywordFXMLMapping;

    public void initialize() {
    }

    public void setupContext(Account accoun, MainController mainController) {
        this.account = accoun;
        this.mainController = mainController;

        loadData();
    }

    private void loadData() {
        String empName = account.getEmployee().getFullName();
        welcomeLabel.setText("Xin chào, " + empName);

        HashMap<RoomStatus, Integer> roomStatusCount = RoomDAO.getRoomStatusCount();
        roomAvailabelCountLabel.setText(String.valueOf(roomStatusCount.get(RoomStatus.AVAILABLE)));
        roomOnUseCountLabel.setText(String.valueOf(roomStatusCount.get(RoomStatus.ON_USE)));
        roomOverdueCountLabel.setText(String.valueOf(roomStatusCount.get(RoomStatus.OVERDUE)));

        loadDataIntoKeywords();
        loadFeaturesIntoGridPane();
        bindSearchFunctionality();
    }

    private void loadDataIntoKeywords() {
        featureKeywordFXMLMapping = new HashMap<>();
        Position position = account.getEmployee().getPosition();

        // Chung cho tất cả vị trí
        featureKeywordFXMLMapping.put(createKeyword("Trang chủ", "Trang chủ, trang chu, tc, dashboard"), "/iuh/fit/view/features/DashboardPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm phòng", "Tìm kiếm phòng, tim kiem phong, tkp, room_search"), "/iuh/fit/view/features/room/RoomSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Đặt phòng", "Đặt phòng, dat phong, dp, room_booking"), "/iuh/fit/view/features/room/RoomBookingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Quản lý hóa đơn", "Quản lý hóa đơn, quan ly hoa don, qlhd, invoice_manager"), "/iuh/fit/view/features/room/InvoiceManagerPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm dịch vụ", "Tìm kiếm dịch vụ, tim kiem dich vu, tkdv, hotel_service_search"), "/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Tìm kiếm khách hàng", "Tìm kiếm khách hàng, tim kiem khach hang, tkkh, customer_search"), "/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Quản lý khách hàng", "Quản lý khách hàng, quan ly khach hang, qlkh, customer_manager"), "/iuh/fit/view/features/customer/CustomerManagerPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Thống kê doanh thu", "Thống kê doanh thu, thong ke doanh thu, tkdt, revenue_statistics"), "/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml");
        featureKeywordFXMLMapping.put(createKeyword("Thống kê tỉ lệ sử dụng phòng", "Thống kê tỉ lệ sử dụng phòng, thong ke ti le su dung phong, tktlsdp, rate_using_room"), "/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml");

        // Thêm chức năng riêng cho MANAGER
        if (position.equals(Position.MANAGER)) {
            featureKeywordFXMLMapping.put(createKeyword("Quản lý nhân viên", "Quản lý nhân viên, quan ly nhan vien, qlnv, employee_manager"), "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý tài khoản", "Quản lý tài khoản, quan ly tai khoan, qltk, account_manager"), "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý ca làm", "Quản lý ca làm, quan ly ca lam, qlcl, shift_manager"), "/iuh/fit/view/features/employee/ShiftManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý giá phòng", "Quản lý giá phòng, quan ly gia phong, qlgp, pricing_manager"), "/iuh/fit/view/features/room/PricingManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý loại phòng", "Quản lý loại phòng, quan ly loai phong, qllp, room_category_manager"), "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý phòng", "Quản lý phòng, quan ly phong, qlp, room_manager"), "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý loại dịch vụ", "Quản lý loại dịch vụ, quan ly loai dich vu, qlldv, service_category_manager"), "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Quản lý dịch vụ", "Quản lý dịch vụ, quan ly dich vu, qldv, hotel_service_manager"), "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
            featureKeywordFXMLMapping.put(createKeyword("Sao lưu dữ liệu", "Sao lưu dữ liệu, sao luu du lieu, backup"), "/iuh/fit/view/features/backup/BackupPanel.fxml");
        }
    }

    private HashMap<String, String> createKeyword(String functionName, String keyword) {
        HashMap<String, String> map = new HashMap<>();
        map.put(functionName, keyword);
        return map;
    }

    private void loadFeaturesIntoGridPane() {
        featureGridPane.getChildren().clear();
        int row = 0;
        int col = 0;

        for (HashMap<String, String> featureMap : featureKeywordFXMLMapping.keySet()) {
            String functionName = featureMap.keySet().iterator().next();
            String fxmlPath = featureKeywordFXMLMapping.get(featureMap);

            HBox featureBox = createFeatureBox(functionName, fxmlPath);

            featureGridPane.add(featureBox, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    private HBox createFeatureBox(String functionName, String fxmlPath) {
        HBox featureBox = new HBox();
        featureBox.setAlignment(Pos.CENTER);
        featureBox.setStyle("-fx-border-color: #ccc; -fx-padding: 20; -fx-background-color: #f0f0f0;");
        featureBox.setPrefSize(200, 100);

        Label label = new Label(functionName);
        label.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        label.setWrapText(true);
        featureBox.getChildren().add(label);

        featureBox.setOnMouseEntered(event -> featureBox.setStyle(
                "-fx-border-color: #0078d7; -fx-border-width: 2; -fx-padding: 20; -fx-background-color: #e6f7ff; -fx-cursor: hand;"));
        featureBox.setOnMouseExited(event -> featureBox.setStyle(
                "-fx-border-color: #ccc; -fx-padding: 20; -fx-background-color: #f0f0f0;"));

        featureBox.setOnMouseClicked(event -> mainController.loadPanel(fxmlPath, mainController, account));

        return featureBox;
    }


    private void bindSearchFunctionality() {
        inputTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            featureGridPane.getChildren().clear();

            if (newValue == null || newValue.trim().isEmpty()) {
                loadFeaturesIntoGridPane();
            } else {
                int row = 0;
                int col = 0;

                for (HashMap<String, String> featureMap : featureKeywordFXMLMapping.keySet()) {
                    String functionName = featureMap.keySet().iterator().next();
                    String keywords = featureMap.values().iterator().next().toLowerCase();
                    newValue = newValue.toLowerCase();

                    if (functionName.toLowerCase().contains(newValue) || keywords.contains(newValue)) {
                        String fxmlPath = featureKeywordFXMLMapping.get(featureMap);
                        HBox featureBox = createFeatureBox(functionName, fxmlPath);

                        featureGridPane.add(featureBox, col, row);

                        col++;
                        if (col == 4) {
                            col = 0;
                            row++;
                        }
                    }
                }
            }
        });
    }




}
