package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {
    @FXML
    private Text dateText;

    @FXML
    private TextField findTextField;
    @FXML
    private Label findBtn;
    @FXML
    private HBox container;

    private static Map<String, String> keywordMap = new LinkedHashMap<>();
    private static Map<String, String> findKeywordMap = new LinkedHashMap<>();
    private static Map<Label, String> listLabel = new LinkedHashMap<>();


    public void initialize() {
        setupDateText();
    }

    private void setupDateText() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'Tháng' MM 'Năm' yyyy");
        String formattedDate = currentDate.format(formatter);
        dateText.setText(formattedDate);

    }

    public void setupKey(){
        keywordMap.clear();
        //Nhân viên
        keywordMap.put("Quản lý nhân viên, quan ly nhan vien, nv", "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
        keywordMap.put("Thêm nhân viên, them nhan vien, nv", "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
        keywordMap.put("Sửa thông tin nhân viên, sua thong tin nhan vien, nv", "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");

        keywordMap.put("Quản lý tài khoản, quan ly tai khoan, tk, account", "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");
        keywordMap.put("Sửa thông tin tài khoản, sua thong tin tai khoan, tk", "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");

        keywordMap.put("Tìm kiếm nhân viên, tim kiem nhan vien, tk, nv", "/iuh/fit/view/features/employee/EmployeeSearchingPanel.fxml");

        keywordMap.put("Quản lý ca làm, quan ly ca lam, cl", "/iuh/fit/view/features/employee/ShiftManagerPanel.fxml");

        //Phòng
        keywordMap.put("Quản lý giá loại phòng,", "/iuh/fit/view/features/room/PricingManagerPanel.fxml");
        keywordMap.put("Quản lý loại phòng,", "/iuh/fit/view/features/room/RoomCategoryManagerPanel.fxml");
        keywordMap.put("Quản lý phòng,", "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
        keywordMap.put("Quản lý đặt phòng,", "/iuh/fit/view/features/room/RoomSearchingPanel.fxml");
        keywordMap.put("Tìm kiếm phòng,", "/iuh/fit/view/features/room/RoomBookingPanel.fxml");

        //Hóa đơn
        keywordMap.put("Quản lý hóa đơn,", "/iuh/fit/view/features/room/InvoiceManagerPanel.fxml");

        //Dịch vụ
        keywordMap.put("Quản lý loại dịch vụ,", "/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml");
        keywordMap.put("Quản lý dịch vụ,", "/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml");
        keywordMap.put("Tìm kiếm dịch vụ,", "/iuh/fit/view/features/service/HotelServiceSearchingPanel.fxml");

        //Khách hàng
        keywordMap.put("Quản lí khách hàng,", "/iuh/fit/view/features/customer/CustomerManagerPanel.fxml");
        keywordMap.put("Tìm kiếm khách hàng,", "/iuh/fit/view/features/customer/CustomerSearchingPanel.fxml");

        //Thống kê
        keywordMap.put("Thống kê doanh thu,", "/iuh/fit/view/features/statistics/revenueStatisticalPanel.fxml");
        keywordMap.put("Thống kê tỉ lệ sử dụng phòng,", "/iuh/fit/view/features/statistics/RateUsingRoomStatisticsPanel.fxml");

        listLabel.clear();
        Label label1 = new Label("Quản lý nhân viên");
        label1.setId("1");
        Label label2 = new Label("Quản lý tài khoản");
        label2.setId("2");
        Label label3 = new Label("Quản lý ca làm");
        label3.setId("3");
        Label label4 = new Label("Qua lý phòng");
        label4.setId("4");
        Label label5 = new Label("Quản lý đặt phòng");
        label5.setId("5");
        listLabel.put(label1, "/iuh/fit/view/features/employee/EmployeeManagerPanel.fxml");
        listLabel.put(label2, "/iuh/fit/view/features/employee/AccountManagerPanel.fxml");
        listLabel.put(label3, "/iuh/fit/view/features/employee/ShiftManagerPanel.fxml");
        listLabel.put(label4, "/iuh/fit/view/features/room/RoomManagerPanel.fxml");
        listLabel.put(label5, "/iuh/fit/view/features/room/RoomSearchingPanel.fxml");
        listLabel.forEach((key, value) -> {
            key.setStyle("-fx-padding: 5px 25px 5px 0; -fx-font-size: 16px; " +
                    "-fx-font-style: italic; " +
                    "-fx-font-weight: bold; " +
                    "-fx-underline: true; " +
                    "-fx-cursor: hand;");
            container.getChildren().add(key);
        });
    }

    public void addKey(){
        findKeyWord();
        removeLabelById();
        int[] i = {1};
        findKeywordMap.forEach((key, value) -> {
            Label label = new Label(key);
            label.setStyle("-fx-padding: 5px 25px 5px 0; -fx-font-size: 16px; " +
                    "-fx-font-style: italic; " +
                    "-fx-font-weight: bold; " +
                    "-fx-underline: true; " +
                    "-fx-cursor: hand;");
            label.setId(i[0]++ + "");
            container.getChildren().add(label);
            listLabel.put(label, value);
        });
    }

    private String removeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", "");
    }

    private String getFirstPhrase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String[] parts = input.split(",", 2);
        return parts[0].trim();
    }

    private void findKeyWord(){
        String key = removeSpaces(findTextField.getText()).toUpperCase();
        findKeywordMap.clear();
        if (!key.isBlank()){
            findKeywordMap = keywordMap.entrySet().stream()
                    .filter(entry -> removeSpaces(entry.getKey()).toUpperCase().contains(key.toUpperCase()))
                    .limit(5)
                    .collect(Collectors.toMap(
                            entry -> getFirstPhrase(entry.getKey()),
                            Map.Entry::getValue,
                            (existing, replacement) -> existing,
                            LinkedHashMap::new
                    ));
        }
    }

    private void removeLabelById() {
        int i = 1;
        List<Label> labelsToRemove = new ArrayList<>();

        for (Node node : container.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (label.getId().equals(i + "")) {
                    labelsToRemove.add(label);
                    i++;
                }
            }
        }
        container.getChildren().removeAll(labelsToRemove);
        listLabel.clear();
    }

    public TextField getFindTextField() {
        return findTextField;
    }

    public Label getFindBtn() {
        return findBtn;
    }

    public Map<Label, String> getListLabel() {
        return listLabel;
    }
}
