package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.UsingRoomDisplayOnTableDAO;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import iuh.fit.utils.ExportFileHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import java.util.List;

public class RateUsingRoomStatisticsController implements Initializable {
    public ToggleGroup showDataViewToggleGroup;
    // Variables for revenue statistics view components
    @FXML
    private TableView<UsingRoomDisplayOnTable> usingRateRoomDataTableView;
    @FXML
    private TableView<UsingRoomDetailDisplayOnTable> usingRateRoomDetailDataTableView;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, Double> netDueDetailColumn;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, Integer> timesUsingColumn;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, Float> percentUsingColumn;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, Float> percentNetDueColumn;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, String> roomCategoryIDDetailColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, String> roomCategoryIDColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, String> nameRoomCategoryColumn;
    @FXML
    private TableColumn<UsingRoomDetailDisplayOnTable, String> nameRoomCategoryDetailColumn;
    //table
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, String> customerNameColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, String> roomIDColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, String> employeeNameColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, LocalDateTime> invoiceDateColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, Double> depositColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, Double> serviceChargeColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, Double> roomChargeColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, Double> netDueColumn;
    @FXML
    private ComboBox<Integer> yearsCombobox;
    @FXML
    private ComboBox<String> roomCategoryCBox;
    @FXML
    private RadioButton showTableViewRadioButton;
    @FXML
    private RadioButton showTableViewDetailRadioButton;
    @FXML
    private RadioButton showChartDataRadioButton;
    @FXML
    private HBox chartViewAnchorPane;
    @FXML
    private AnchorPane tableViewAnchorPane;
    @FXML
    private AnchorPane tableViewDetailAnchorPane;

    @FXML
    private PieChart timesUsingPiechart;
    @FXML
    private PieChart netDuePiechart;


    @FXML
    private Text timeText;
    @FXML ComboBox<String> QCbox;

    @FXML
    private RadioButton yearRad;
    @FXML
    private RadioButton dateRangeRad;
    @FXML
    private RadioButton allStatictisRad;

    @FXML
    private DateRangePicker invoiceTabDateRangePicker;
    @FXML
    private ComboBox<String> roomIDCombobox;

    @FXML
    private Text totalMoneyText;
    @FXML
    private Text numOfInvoiceText;
    @FXML
    Text titleStatictis;

    @FXML
    private Button resetBtn;

    @FXML
    private Button exportExcelBtn;

    @FXML
    private DialogPane dialogPane;
    private static final int COMBO_YEAR_CAPACITY = 4;

    private static List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableList = new ArrayList<>();
    private static List<UsingRoomDetailDisplayOnTable> usingRoomDetailDisplayOnTableList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataByDateRange(startDate, endDate);
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetailDateRange(startDate, endDate);
        loadData();

        setUpTable(usingRoomDisplayOnTableList, usingRoomDetailDisplayOnTableList);


        dateRangeRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());
        yearRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());
        allStatictisRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());

        yearsCombobox.setOnAction(e -> statictisDataByYear());
        QCbox.setOnAction(e -> statictisDataByYear());

        invoiceTabDateRangePicker.valueProperty().addListener((obs, oldRange, newRange) -> {
            if (newRange != null) statictisByDateRange();
        });

        roomCategoryCBox.setOnAction(e -> chooseRoomCategoryID());
        roomIDCombobox.setOnAction(e -> statictis());

        resetBtn.setOnAction(e -> handleResetAction());
        exportExcelBtn.setOnAction(e -> exportExcelFile());
    }

    @FXML
    void switchBetweenTableViewAndChartView() {
        if (showTableViewRadioButton.isSelected()) {
            tableViewAnchorPane.setVisible(true);
            tableViewDetailAnchorPane.setVisible(false);
            chartViewAnchorPane.setVisible(false);
        }
        if (showTableViewDetailRadioButton.isSelected()) {
            tableViewAnchorPane.setVisible(false);
            tableViewDetailAnchorPane.setVisible(true);
            chartViewAnchorPane.setVisible(false);
        }
        if (showChartDataRadioButton.isSelected()) {
            tableViewAnchorPane.setVisible(false);
            tableViewDetailAnchorPane.setVisible(false);
            chartViewAnchorPane.setVisible(true);
        }
    }

    private void switchBetweenCBBYearAndDateRangePicker() {
        if(yearRad.isSelected()){
            yearsCombobox.setDisable(false);
            QCbox.setDisable(false);
            invoiceTabDateRangePicker.setDisable(true);
            statictisDataByYear();
            loadDataToRoomCategoryCombobox();
        } else if(dateRangeRad.isSelected()){
            yearsCombobox.setDisable(true);
            QCbox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
            statictisByDateRange();
            loadDataToRoomCategoryCombobox();
        } else if (allStatictisRad.isSelected()){
            yearsCombobox.setDisable(true);
            QCbox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(true);
            statictisAll();
            loadDataToRoomCategoryCombobox();
        }
    }

    private void loadData() {
        loadDataToComboboxOfYear();
        loadDataToRoomCategoryCombobox();
        invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        if(invoiceTabDateRangePicker.getValue().getStartDate().equals(invoiceTabDateRangePicker.getValue().getEndDate())){
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate());
        } else{
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate() + " - " + invoiceTabDateRangePicker.getValue().getEndDate());
        }
    }

    private void loadDataToComboboxOfYear() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < COMBO_YEAR_CAPACITY; i++){
            years.add(LocalDate.now().getYear() - i);
            QCbox.getItems().add("Quý " + (i+1));
        }
        QCbox.getItems().addFirst("Quý");
        QCbox.getSelectionModel().selectFirst();

        yearsCombobox.setItems(years);
        yearsCombobox.setValue(years.getFirst());
    }

    private void loadDataToRoomCategoryCombobox() {
        roomCategoryCBox.getItems().clear();

        List<UsingRoomDisplayOnTable> roomCategoryList = usingRoomDisplayOnTableList.stream()
                .collect(Collectors.toMap(
                        UsingRoomDisplayOnTable::getRoomCategoryID,
                        room -> room,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

        roomCategoryList.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomCategoryID));

        ObservableList<String> roomCategoryID = FXCollections.observableArrayList("Tất cả");
        roomCategoryList.forEach(e -> roomCategoryID.add(e.getRoomCategoryID() + ": " + e.getNameRoomCategory()));
        roomCategoryCBox.setItems(roomCategoryID);
        roomCategoryCBox.setValue(roomCategoryID.getFirst());
        roomIDCombobox.getItems().clear();
        chooseRoomCategoryID();
    }

    private void chooseRoomCategoryID(){
        String roomCategoryID = splitRoomCategoryID(roomCategoryCBox.getSelectionModel().getSelectedItem());
        if(roomCategoryID == null){
            List<UsingRoomDisplayOnTable> roomsList = usingRoomDisplayOnTableList.stream()
                    .collect(Collectors.toMap(
                            UsingRoomDisplayOnTable::getRoomID,
                            room -> room,
                            (existing, replacement) -> existing
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            roomsList.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID));

            ObservableList<String> roomID = FXCollections.observableArrayList("Tất cả");
            roomsList.forEach(e -> roomID.add(e.getRoomID()));
            roomIDCombobox.setItems(roomID);
            roomIDCombobox.setValue(roomID.getFirst());

            statictis();
        } else {
            List<UsingRoomDisplayOnTable> roomsList = usingRoomDisplayOnTableList.stream()
                    .filter(x -> x.getRoomCategoryID() != null && x.getRoomCategoryID().equalsIgnoreCase(roomCategoryID))
                    .collect(Collectors.toMap(
                            UsingRoomDisplayOnTable::getRoomID,
                            room -> room,
                            (existing, replacement) -> existing
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            roomsList.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID));
            ObservableList<String> roomID = FXCollections.observableArrayList("Tất cả");
            roomsList.forEach(e -> roomID.add(e.getRoomID()));
            roomIDCombobox.setItems(roomID);
            roomIDCombobox.setValue(roomID.getFirst());

            statictis();
        }
    }

    private void setUpTable(List<UsingRoomDisplayOnTable> list, List<UsingRoomDetailDisplayOnTable> listDetail){
        showDataToTableView(list);
        showDataDetailToTableView(listDetail);
        showPieChart(listDetail);

        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list)));
        setTotalMoney(String.valueOf(caculateTotalMoney(list)));
    }

    private void showDataToTableView(List<UsingRoomDisplayOnTable> list) {
        ObservableList<UsingRoomDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(list);

        roomCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("roomCategoryID"));
        nameRoomCategoryColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("nameRoomCategory"));
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("roomID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("cusName"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("empName"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, LocalDateTime>("createDate"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("deposit"));
        serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("serviceCharge"));
        roomChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("roomCharge"));
        netDueColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("netDue"));

        usingRateRoomDataTableView.getItems().clear();
        usingRateRoomDataTableView.setItems(dataOfTableView);

    }

    private void showDataDetailToTableView(List<UsingRoomDetailDisplayOnTable> list) {

        ObservableList<UsingRoomDetailDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(list);

        roomCategoryIDDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, String>("roomCategoryID"));
        nameRoomCategoryDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, String>("nameRoomCategory"));
        timesUsingColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Integer>("timesUsing"));
        netDueDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Double>("netDue"));
        percentUsingColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Float>("percentUsing"));
        percentNetDueColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Float>("percentNetDue"));

        usingRateRoomDetailDataTableView.getItems().clear();
        usingRateRoomDetailDataTableView.setItems(dataOfTableView);

    }



    public String splitRoomCategoryID(String input) {
        if (input != null && input.contains(":")) {
            return input.split(":")[0].trim();
        }
        return null;
    }

    private void setNumOfUsingRoom(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfUsingRoom(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData){
        return usingRoomDisplayOnTableData.size();
    }

    private static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(formatCurrency(Double.valueOf(totalMoney)));
    }

    private double caculateTotalMoney(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData) {
        return usingRoomDisplayOnTableData.stream()
                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                .sum();


    }

    private void handleResetAction() {
        if(yearRad.isSelected()){
            yearsCombobox.getSelectionModel().selectFirst();
            QCbox.getSelectionModel().selectFirst();
            statictisDataByYear();
        } else if(dateRangeRad.isSelected()){
            invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
            roomIDCombobox.getSelectionModel().selectFirst();
            statictisByDateRange();
        } else if (allStatictisRad.isSelected()){
            roomIDCombobox.getSelectionModel().selectFirst();
            statictisAll();
        }
    }



    private void statictisByDateRange(){
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);

        usingRoomDisplayOnTableList.clear();
        usingRoomDetailDisplayOnTableList.clear();

        List<UsingRoomDisplayOnTable> list1 = UsingRoomDisplayOnTableDAO.getDataByDateRange(startDate, endDate);
        List<UsingRoomDetailDisplayOnTable> list2 = UsingRoomDisplayOnTableDAO.getDataDetailDateRange(startDate, endDate);

        usingRoomDisplayOnTableList.addAll(list1);
        usingRoomDetailDisplayOnTableList.addAll(list2);

        loadDataToRoomCategoryCombobox();

        if(invoiceTabDateRangePicker.getValue().getStartDate().equals(invoiceTabDateRangePicker.getValue().getEndDate())){
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate());
        } else{
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate() + " - " + invoiceTabDateRangePicker.getValue().getEndDate());
        }
    }

    private void statictisDataByYear(){
        String quy = QCbox.getSelectionModel().getSelectedItem();
        int year = yearsCombobox.getSelectionModel().getSelectedItem();

        usingRoomDisplayOnTableList.clear();
        usingRoomDetailDisplayOnTableList.clear();

        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataByYear(year);
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetailByYear(year);

        if (!quy.equalsIgnoreCase("Quý")){
            List<UsingRoomDisplayOnTable> list1 = usingRoomDisplayOnTableList.stream()
                    .filter(x -> {
                        if (quy.equals("Quý 1")) {
                            return x.getCreateDate().getMonthValue() == 1 || x.getCreateDate().getMonthValue() == 2 || x.getCreateDate().getMonthValue() == 3;
                        } else if (quy.equals("Quý 2")){
                            return x.getCreateDate().getMonthValue() == 4 || x.getCreateDate().getMonthValue() == 5 || x.getCreateDate().getMonthValue() == 6;
                        } else if (quy.equals("Quý 3")){
                            return x.getCreateDate().getMonthValue() == 7 || x.getCreateDate().getMonthValue() == 8 || x.getCreateDate().getMonthValue() == 9;
                        } else {
                            return x.getCreateDate().getMonthValue() == 10 || x.getCreateDate().getMonthValue() == 11 || x.getCreateDate().getMonthValue() == 12;
                        }
                    })
                    .toList();

            int totalUsageCount = list1.size();
            double totalNetDueSum = list1.stream()
                    .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                    .sum();

            List<UsingRoomDetailDisplayOnTable> list2 = usingRoomDisplayOnTableList.stream()
                    .filter(x -> {
                        if (quy.equals("Quý 1")) {
                            return x.getCreateDate().getMonthValue() == 1 || x.getCreateDate().getMonthValue() == 2 || x.getCreateDate().getMonthValue() == 3;
                        } else if (quy.equals("Quý 2")){
                            return x.getCreateDate().getMonthValue() == 4 || x.getCreateDate().getMonthValue() == 5 || x.getCreateDate().getMonthValue() == 6;
                        } else if (quy.equals("Quý 3")){
                            return x.getCreateDate().getMonthValue() == 7 || x.getCreateDate().getMonthValue() == 8 || x.getCreateDate().getMonthValue() == 9;
                        } else {
                            return x.getCreateDate().getMonthValue() == 10 || x.getCreateDate().getMonthValue() == 11 || x.getCreateDate().getMonthValue() == 12;
                        }
                    })
                    .collect(Collectors.groupingBy(
                    UsingRoomDisplayOnTable::getRoomCategoryID,
                    Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                int count = list.size();
                                double totalNetDue = list.stream()
                                        .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                                        .sum();
                                float usagePercentage = ((float) count / totalUsageCount) * 100;
                                float netDuePercentage = (float) (totalNetDue / totalNetDueSum * 100);
                                return new UsingRoomDetailDisplayOnTable(
                                        list.get(0).getRoomCategoryID(),
                                        list.get(0).getNameRoomCategory(),
                                        count,
                                        usagePercentage,
                                        totalNetDue,
                                        netDuePercentage
                                );
                            }
                    )
            ))
                    .values()
                    .stream()
                    .collect(Collectors.toList());

            usingRoomDisplayOnTableList.clear();
            usingRoomDetailDisplayOnTableList.clear();

            usingRoomDisplayOnTableList.addAll(list1);
            usingRoomDetailDisplayOnTableList.addAll(list2);
        }
        loadDataToRoomCategoryCombobox();
        if (quy.equalsIgnoreCase("Quý")) {
            timeText.setText("Năm " + yearsCombobox.getSelectionModel().getSelectedItem());
        } else {
            timeText.setText("Năm " + yearsCombobox.getSelectionModel().getSelectedItem() + " - " + quy);
        }
    }

    private void statictisAll(){
        usingRoomDisplayOnTableList.clear();
        usingRoomDetailDisplayOnTableList.clear();
        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getData();
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetail();
        loadDataToRoomCategoryCombobox();

        timeText.setText("Tất cả");
    }

    private void statictis(){
        String roomID = roomIDCombobox.getSelectionModel().getSelectedItem();
        String roomCategoryID = splitRoomCategoryID(roomCategoryCBox.getSelectionModel().getSelectedItem());

        if(roomCategoryID == null){
            if(roomID == null || roomID.equalsIgnoreCase("Tất cả")){
                showDataToTableView(usingRoomDisplayOnTableList);
                showDataDetailToTableView(usingRoomDetailDisplayOnTableList);
                showPieChart(usingRoomDetailDisplayOnTableList);

                setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(usingRoomDisplayOnTableList)));
                setTotalMoney(String.valueOf(caculateTotalMoney(usingRoomDisplayOnTableList)));
                titleStatictis.setText("THỐNG KÊ TỈ LỆ SỬ DỤNG THEO LOẠI PHÒNG");
            } else {
                List<UsingRoomDisplayOnTable> list1;
                List<UsingRoomDetailDisplayOnTable> listDetail;


                list1 = usingRoomDisplayOnTableList.stream()
                        .filter(x -> roomID.equals(x.getRoomID()))
                        .toList();

                int totalUsageCount = list1.size();
                double totalNetDueSum = list1.stream()
                        .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                        .sum();

                int totalCount = usingRoomDisplayOnTableList.size();
                double totalNetDue = usingRoomDisplayOnTableList.stream()
                        .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                        .sum();

                float usagePercentage = (float) totalUsageCount / totalCount * 100;
                float netDuePercentage = (float) (totalNetDueSum / totalNetDue * 100);

                String tmpRoomCategoryID;
                String tmpRoomCategoryName;
                if (roomID.contains("T1")) {
                    tmpRoomCategoryID = "RC-000001";
                    tmpRoomCategoryName = "Phòng Thường Giường Đơn";
                } else if (roomID.contains("T2")) {
                    tmpRoomCategoryID = "RC-000002";
                    tmpRoomCategoryName = "Phòng Thường Giường Đôi";
                } else if (roomID.contains("V1")) {
                    tmpRoomCategoryID = "RC-000003";
                    tmpRoomCategoryName = "Phòng VIP Giường Đơn";
                } else {
                    tmpRoomCategoryID = "RC-000004";
                    tmpRoomCategoryName = "Phòng VIP Giường Đôi";
                }


                listDetail = Collections.singletonList(
                        new UsingRoomDetailDisplayOnTable(
                                tmpRoomCategoryID,
                                tmpRoomCategoryName,
                                totalUsageCount,
                                usagePercentage,
                                totalNetDueSum,
                                netDuePercentage
                        )
                );

                showDataToTableView(list1);
                showDataDetailToTableView(listDetail);
                showPieChart(listDetail);

                setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list1)));
                setTotalMoney(String.valueOf(caculateTotalMoney(list1)));
                titleStatictis.setText("THỐNG KÊ TỈ LỆ SỬ DỤNG PHÒNG " + roomID);
            }
        } else {
            if(roomID == null || roomID.equalsIgnoreCase("Tất cả")){
                List<UsingRoomDisplayOnTable> list1;
                List<UsingRoomDetailDisplayOnTable> listDetail;

                list1 = usingRoomDisplayOnTableList.stream()
                                .filter(x -> x.getRoomCategoryID().equalsIgnoreCase(roomCategoryID))
                                .toList();

                listDetail = usingRoomDetailDisplayOnTableList.stream()
                        .filter(x -> x.getRoomCategoryID().equalsIgnoreCase(roomCategoryID))
                        .toList();


                showDataToTableView(list1);
                showDataDetailToTableView(listDetail);
                showPieChart(listDetail);

                setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list1)));
                setTotalMoney(String.valueOf(caculateTotalMoney(list1)));
                titleStatictis.setText("THỐNG KÊ TỈ LỆ SỬ DỤNG THEO LOẠI PHÒNG " + roomCategoryCBox.getSelectionModel().getSelectedItem());
            } else {
                List<UsingRoomDisplayOnTable> list1;
                List<UsingRoomDetailDisplayOnTable> listDetail;


                list1 = usingRoomDisplayOnTableList.stream()
                        .filter(x -> roomID.equals(x.getRoomID()))
                        .toList();

                int totalUsageCount = list1.size();
                double totalNetDueSum = list1.stream()
                        .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                        .sum();

                int totalCount = usingRoomDisplayOnTableList.stream()
                        .filter(x -> x.getRoomCategoryID().equals(roomCategoryID))
                        .toList()
                        .size();
                double totalNetDue = usingRoomDisplayOnTableList.stream()
                        .filter(x -> x.getRoomCategoryID().equals(roomCategoryID))
                        .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                        .sum();

                float usagePercentage = (float) totalUsageCount / totalCount * 100;
                float netDuePercentage = (float) (totalNetDueSum / totalNetDue * 100);

                String tmpRoomCategoryID;
                String tmpRoomCategoryName;
                if (roomID.contains("T1")) {
                    tmpRoomCategoryID = "RC-000001";
                    tmpRoomCategoryName = "Phòng Thường Giường Đơn";
                } else if (roomID.contains("T2")) {
                    tmpRoomCategoryID = "RC-000002";
                    tmpRoomCategoryName = "Phòng Thường Giường Đôi";
                } else if (roomID.contains("V1")) {
                    tmpRoomCategoryID = "RC-000003";
                    tmpRoomCategoryName = "Phòng VIP Giường Đơn";
                } else {
                    tmpRoomCategoryID = "RC-000004";
                    tmpRoomCategoryName = "Phòng VIP Giường Đôi";
                }


                listDetail = Collections.singletonList(
                        new UsingRoomDetailDisplayOnTable(
                                tmpRoomCategoryID,
                                tmpRoomCategoryName,
                                totalUsageCount,
                                usagePercentage,
                                totalNetDueSum,
                                netDuePercentage
                        )
                );

                showDataToTableView(list1);
                showDataDetailToTableView(listDetail);
                showPieChart(listDetail);

                setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list1)));
                setTotalMoney(String.valueOf(caculateTotalMoney(list1)));
                titleStatictis.setText("THỐNG KÊ TỈ LỆ SỬ DỤNG PHÒNG " + roomID + " THEO LOẠI PHÒNG " + roomCategoryCBox.getSelectionModel().getSelectedItem());
            }
        }


    }



    private void exportExcelFile() {
        if (usingRoomDisplayOnTableList.isEmpty()){
            dialogPane.showWarning("Lỗi", "Không có dữ liệu");
            return;
        }
        if (showTableViewRadioButton.isSelected()){
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase("Tất cả");
            int numOfInvoice = getNumOfUsingRoom(usingRateRoomDataTableView.getItems());
            double totalMoney = caculateTotalMoney(usingRateRoomDataTableView.getItems());
            if(yearRad.isSelected()){
                ExportFileHelper.exportUsingRoomExcelFile(usingRateRoomDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            } else if (dateRangeRad.isSelected()){
                LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
                LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
                if(isAMonth(startDate, endDate))
                    ExportFileHelper.exportUsingRoomExcelFile(
                            usingRateRoomDataTableView,
                            ExportExcelCategory.ALL_OF_MONTH,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else if(isADay(startDate, endDate))
                    ExportFileHelper.exportUsingRoomExcelFile(
                            usingRateRoomDataTableView,
                            ExportExcelCategory.DAY_OF_MONTH,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else if(isManyYear(startDate, endDate))
                    ExportFileHelper.exportUsingRoomExcelFile(
                            usingRateRoomDataTableView,
                            ExportExcelCategory.MANY_YEAR,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else ExportFileHelper.exportUsingRoomExcelFile(
                            usingRateRoomDataTableView,
                            ExportExcelCategory.DATE_RANGE,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
            } else {
                ExportFileHelper.exportUsingRoomExcelFile(usingRateRoomDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            }
        }
        if(showTableViewDetailRadioButton.isSelected() || showChartDataRadioButton.isSelected()){
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase("Tất cả");
            int numOfInvoice = getNumOfUsingRoom(usingRateRoomDataTableView.getItems());
            double totalMoney = caculateTotalMoney(usingRateRoomDataTableView.getItems());
            if(yearRad.isSelected()){
                ExportFileHelper.exportUsingRoomDetailExcelFile(usingRateRoomDetailDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            } else if (dateRangeRad.isSelected()){
                LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
                LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
                if(isAMonth(startDate, endDate))
                    ExportFileHelper.exportUsingRoomDetailExcelFile(
                            usingRateRoomDetailDataTableView,
                            ExportExcelCategory.ALL_OF_MONTH,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else if(isADay(startDate, endDate))
                    ExportFileHelper.exportUsingRoomDetailExcelFile(
                            usingRateRoomDetailDataTableView,
                            ExportExcelCategory.DAY_OF_MONTH,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else if(isManyYear(startDate, endDate))
                    ExportFileHelper.exportUsingRoomDetailExcelFile(
                            usingRateRoomDetailDataTableView,
                            ExportExcelCategory.MANY_YEAR,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
                else ExportFileHelper.exportUsingRoomDetailExcelFile(
                            usingRateRoomDetailDataTableView,
                            ExportExcelCategory.DATE_RANGE,
                            forEmployee,
                            invoiceTabDateRangePicker.getValue(),
                            numOfInvoice,
                            totalMoney);
            } else {
                ExportFileHelper.exportUsingRoomExcelFile(usingRateRoomDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            }
        }
    }

    private boolean isAMonth(LocalDateTime startDate, LocalDateTime endDate){
        List<Integer> lastDayOfMonth = new ArrayList<>(Arrays.asList(28, 30, 31));
        List<Integer> lastDayOfMonthLeapYear = new ArrayList<>(Arrays.asList(28, 29, 30, 31));
        if(startDate.getYear() != endDate.getYear()) return false;
        if(startDate.getYear() % 4 == 0)
            return (startDate.getDayOfMonth() == 1 && lastDayOfMonthLeapYear.contains(endDate.getDayOfMonth()));
        return (startDate.getDayOfMonth() == 1 && lastDayOfMonth.contains(endDate.getDayOfMonth()));
    }

    private boolean isADay(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(endDate.toLocalDate());
    }

    private boolean isManyYear(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().getYear() != endDate.toLocalDate().getYear();
    }

    private void showPieChart(List<UsingRoomDetailDisplayOnTable> list) {
        timesUsingPiechart.getData().clear();
        netDuePiechart.getData().clear();
        String roomID = roomIDCombobox.getSelectionModel().getSelectedItem();
        String roomCategoryID = splitRoomCategoryID(roomCategoryCBox.getSelectionModel().getSelectedItem());

        if(roomCategoryID == null){
            if(roomID == null || roomID.equalsIgnoreCase("Tất cả")){
                list.forEach(x -> {
                    PieChart.Data timesData = new PieChart.Data(
                            x.getRoomCategoryID() + "(" + x.getTimesUsing() + ")",
                            x.getTimesUsing()
                    );
                    PieChart.Data netDueData = new PieChart.Data(
                            x.getRoomCategoryID() + "(" + x.getNetDue() + ")",
                            x.getNetDue()
                    );

                    timesUsingPiechart.getData().add(timesData);
                    netDuePiechart.getData().add(netDueData);
                });
            } else {
                list.forEach(x -> {
                    PieChart.Data timesData = new PieChart.Data(
                            roomID + "\n(" + x.getTimesUsing() + "-" + x.getPercentUsing() + "%)", x.getTimesUsing()
                    );
                    PieChart.Data netDueData = new PieChart.Data(
                            roomID + "\n(" + x.getNetDue() + "-" + x.getPercentNetDue() + "%)", x.getNetDue()
                    );

                    timesUsingPiechart.getData().add(timesData);
                    netDuePiechart.getData().add(netDueData);
                });
            }
        } else {
            if(roomID == null || roomID.equalsIgnoreCase("Tất cả")){
                list.forEach(x -> {
                    PieChart.Data timesData = new PieChart.Data(
                            roomCategoryID + "\n(" + x.getTimesUsing() + "-" + x.getPercentUsing() + "%)",
                            x.getTimesUsing()
                    );
                    PieChart.Data netDueData = new PieChart.Data(
                            roomCategoryID + "\n(" + x.getNetDue() + "-" + x.getPercentNetDue() + "%)",
                            x.getNetDue()
                    );

                    timesUsingPiechart.getData().add(timesData);
                    netDuePiechart.getData().add(netDueData);
                });
            } else {
                list.forEach(x -> {
                    PieChart.Data timesData = new PieChart.Data(
                            roomID + "\n(" + x.getTimesUsing() + "-" + x.getPercentUsing() + "%)", x.getTimesUsing()
                    );
                    PieChart.Data netDueData = new PieChart.Data(
                            roomID + "\n(" + x.getNetDue() + "-" + x.getPercentNetDue() + "%)", x.getNetDue()
                    );

                    timesUsingPiechart.getData().add(timesData);
                    netDuePiechart.getData().add(netDueData);
                });
            }
        }



        for (PieChart.Data data : timesUsingPiechart.getData()) {
            Tooltip tooltip = new Tooltip();
            tooltip.setShowDelay(javafx.util.Duration.ZERO);
            tooltip.setHideDelay(javafx.util.Duration.ZERO);

            data.getNode().setOnMouseEntered(e -> {
                double total = getTotalValue(timesUsingPiechart);
                double percentage = (data.getPieValue() / total) * 100;
                tooltip.setText(String.format("%.1f%%", percentage));
                tooltip.show(data.getNode(), e.getScreenX() + 10, e.getScreenY() + 10);
            });

            data.getNode().setOnMouseExited(e -> tooltip.hide());

            data.getNode().setOnMouseMoved(e -> {
                tooltip.setX(e.getScreenX() + 10);
                tooltip.setY(e.getScreenY() + 10);
            });
        }

        for (PieChart.Data data : netDuePiechart.getData()) {
            Tooltip tooltip = new Tooltip();
            tooltip.setShowDelay(javafx.util.Duration.ZERO);
            tooltip.setHideDelay(javafx.util.Duration.ZERO);

            data.getNode().setOnMouseEntered(e -> {
                double total = getTotalValue(netDuePiechart);
                double percentage = (data.getPieValue() / total) * 100;
                tooltip.setText(String.format("%.1f%%", percentage));
                tooltip.show(data.getNode(), e.getScreenX() + 10, e.getScreenY() + 10);
            });

            data.getNode().setOnMouseExited(e -> tooltip.hide());

            data.getNode().setOnMouseMoved(e -> {
                tooltip.setX(e.getScreenX() + 10);
                tooltip.setY(e.getScreenY() + 10);
            });
        }
    }

    private double getTotalValue(PieChart chart) {
        return chart.getData().stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();
    }
}
