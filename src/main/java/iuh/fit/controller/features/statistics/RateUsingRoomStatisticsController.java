package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.UsingRoomDisplayOnTableDAO;
import iuh.fit.models.Room;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import iuh.fit.utils.ExportFileHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private TableColumn<UsingRoomDetailDisplayOnTable, String> roomIDDetailColumn;
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
    private TableColumn<UsingRoomDisplayOnTable, Double> taxColumn;
    @FXML
    private TableColumn<UsingRoomDisplayOnTable, Double> netDueColumn;
    @FXML
    private ComboBox<Integer> yearsCombobox;
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
    private Button resetBtn;

    @FXML
    private Button exportExcelBtn;

    @FXML
    private DialogPane dialogPane;

    private static final String NONE_VALUE_ROOM_NAME = "Chọn mã phòng";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 4;

    private static List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableList = new ArrayList<>();
    private static List<UsingRoomDetailDisplayOnTable> usingRoomDetailDisplayOnTableList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();

        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataByDateRange(startDate, endDate);
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetailDateRange(startDate, endDate);
        setUpTable(usingRoomDisplayOnTableList, usingRoomDetailDisplayOnTableList);


        dateRangeRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());
        yearRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());
        allStatictisRad.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());

        yearsCombobox.setOnAction(e -> statictisDataByYear());
        QCbox.setOnAction(e -> statictisDataByYear());
        invoiceTabDateRangePicker.valueProperty().addListener((obs, oldRange, newRange) -> {
            if (newRange != null) statictisByDateRange();
        });

        roomIDCombobox.setOnAction(e -> chooseID());
        resetBtn.setOnAction(e -> handleResetAction());
        exportExcelBtn.setOnAction(e -> exportExcelFile());
    }

    private void loadData() {
        loadDataToComboboxOfYear();
        loadDataToRoomNameCombobox();
        invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        if(invoiceTabDateRangePicker.getValue().getStartDate().equals(invoiceTabDateRangePicker.getValue().getEndDate())){
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate());
        } else{
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate() + " - " + invoiceTabDateRangePicker.getValue().getEndDate());
        }
    }

    private void setUpTable(List<UsingRoomDisplayOnTable> list, List<UsingRoomDetailDisplayOnTable> listDetail){

        showDataToTableView(list);
        showDataDetailToTableView(listDetail);
        showPieChart(listDetail);

        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list)));
        setTotalMoney(String.valueOf(caculateTotalMoney(list)));
    }

    private void showDataToTableView(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData) {
        ObservableList<UsingRoomDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(usingRoomDisplayOnTableData);

        roomIDColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("roomID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("cusName"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("empName"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, LocalDateTime>("createDate"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("deposit"));
        serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("serviceCharge"));
        roomChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("roomCharge"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("tax"));
        netDueColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("netDue"));

        usingRateRoomDataTableView.getItems().clear();
        usingRateRoomDataTableView.setItems(dataOfTableView);

    }

    private void showDataDetailToTableView(List<UsingRoomDetailDisplayOnTable> usingRoomDetailDisplayOnTableData) {

        ObservableList<UsingRoomDetailDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(usingRoomDetailDisplayOnTableData);

        roomIDDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, String>("roomID"));
        timesUsingColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Integer>("timesUsing"));
        netDueDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Double>("netDue"));
        percentUsingColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Float>("percentUsing"));
        percentNetDueColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Float>("percentNetDue"));

        usingRateRoomDetailDataTableView.getItems().clear();
        usingRateRoomDetailDataTableView.setItems(dataOfTableView);

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

    private void loadDataToRoomNameCombobox() {
        List<Room> roomsList = RoomDAO.getRoom();
        ObservableList<String> roomID = FXCollections.observableArrayList(NONE_VALUE_ROOM_NAME);
        roomsList.forEach(e -> roomID.add(e.getRoomID()));
        roomIDCombobox.setItems(roomID);
        roomIDCombobox.setValue(roomID.getFirst());
    }

    private void setNumOfUsingRoom(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfUsingRoom(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData){
        return usingRoomDisplayOnTableData.size();
    }

    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double caculateTotalMoney(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData){
        return usingRoomDisplayOnTableData.stream()
                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                .sum();
    }

    private void handleResetAction() {
        if(yearRad.isSelected()){
            yearsCombobox.getSelectionModel().selectFirst();
            QCbox.getSelectionModel().selectFirst();
            roomIDCombobox.getSelectionModel().selectFirst();
            statictisDataByYear();
            chooseID();
        } else if(dateRangeRad.isSelected()){
            invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
            roomIDCombobox.getSelectionModel().selectFirst();
            statictisByDateRange();
            chooseID();
        } else if (allStatictisRad.isSelected()){
            roomIDCombobox.getSelectionModel().selectFirst();
            statictisAll();
            chooseID();
        }
    }

    private void switchBetweenCBBYearAndDateRangePicker() {
        if(yearRad.isSelected()){
            yearsCombobox.setDisable(false);
            QCbox.setDisable(false);
            invoiceTabDateRangePicker.setDisable(true);
            statictisDataByYear();
            chooseID();
        } else if(dateRangeRad.isSelected()){
            yearsCombobox.setDisable(true);
            QCbox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
            statictisByDateRange();
            chooseID();
        } else if (allStatictisRad.isSelected()){
            yearsCombobox.setDisable(true);
            QCbox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(true);
            statictisAll();
            chooseID();
        }
    }

    private void statictisByDateRange(){
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);

        usingRoomDisplayOnTableList.clear();
        usingRoomDetailDisplayOnTableList.clear();

        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataByDateRange(startDate, endDate);
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetailDateRange(startDate, endDate);

        showDataToTableView(usingRoomDisplayOnTableList);
        showDataDetailToTableView(usingRoomDetailDisplayOnTableList);
        showPieChart(usingRoomDetailDisplayOnTableList);

        if(invoiceTabDateRangePicker.getValue().getStartDate().equals(invoiceTabDateRangePicker.getValue().getEndDate())){
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate());
        } else{
            timeText.setText("Ngày " + invoiceTabDateRangePicker.getValue().getStartDate() + " - " + invoiceTabDateRangePicker.getValue().getEndDate());
        }

        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(usingRoomDisplayOnTableList)));
        setTotalMoney(String.valueOf(caculateTotalMoney(usingRoomDisplayOnTableList)));
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
                            UsingRoomDisplayOnTable::getRoomID,
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
                                                list.get(0).getRoomID(),
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

        showDataToTableView(usingRoomDisplayOnTableList);
        showDataDetailToTableView(usingRoomDetailDisplayOnTableList);
        showPieChart(usingRoomDetailDisplayOnTableList);

        if (quy.equalsIgnoreCase("Quý")) {
            timeText.setText("Năm " + yearsCombobox.getSelectionModel().getSelectedItem());
        } else {
            timeText.setText("Năm " + yearsCombobox.getSelectionModel().getSelectedItem() + " " + quy);
        }


        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(usingRoomDisplayOnTableList)));
        setTotalMoney(String.valueOf(caculateTotalMoney(usingRoomDisplayOnTableList)));
    }

    private void statictisAll(){
        usingRoomDisplayOnTableList.clear();
        usingRoomDetailDisplayOnTableList.clear();
        usingRoomDisplayOnTableList = UsingRoomDisplayOnTableDAO.getData();
        usingRoomDetailDisplayOnTableList = UsingRoomDisplayOnTableDAO.getDataDetail();

        showDataToTableView(usingRoomDisplayOnTableList);
        showDataDetailToTableView(usingRoomDetailDisplayOnTableList);
        showPieChart(usingRoomDetailDisplayOnTableList);

        timeText.setText("Tất cả");

        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(usingRoomDisplayOnTableList)));
        setTotalMoney(String.valueOf(caculateTotalMoney(usingRoomDisplayOnTableList)));
    }

    private void chooseID(){
        String id = roomIDCombobox.getSelectionModel().getSelectedItem();
        if(id.equals(NONE_VALUE_ROOM_NAME)){
            showDataToTableView(usingRoomDisplayOnTableList);
            showDataDetailToTableView(usingRoomDetailDisplayOnTableList);
            showPieChart(usingRoomDetailDisplayOnTableList);

            setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(usingRoomDisplayOnTableList)));
            setTotalMoney(String.valueOf(caculateTotalMoney(usingRoomDisplayOnTableList)));
        } else {
            List<UsingRoomDisplayOnTable> list;
            List<UsingRoomDetailDisplayOnTable> listDetail;
            list = usingRoomDisplayOnTableList.stream().filter(x -> id.equals(x.getRoomID())).toList();
            listDetail = usingRoomDetailDisplayOnTableList.stream().filter(x -> id.equals(x.getRoomID())).toList();

            showDataToTableView(list);
            showDataDetailToTableView(listDetail);
            showPieChart(listDetail);

            setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(list)));
            setTotalMoney(String.valueOf(caculateTotalMoney(list)));
        }
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

    private void exportExcelFile() {
        if (usingRoomDisplayOnTableList.isEmpty()){
            dialogPane.showWarning("Lỗi", "Không có dữ liệu");
            return;
        }
        if (showTableViewRadioButton.isSelected()){
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase(NONE_VALUE_ROOM_NAME);
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
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase(NONE_VALUE_ROOM_NAME);
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

    private void showPieChart(List<UsingRoomDetailDisplayOnTable> list){
        timesUsingPiechart.getData().clear();
        netDuePiechart.getData().clear();

        list.forEach(x -> {
            timesUsingPiechart.getData().add(new PieChart.Data(x.getRoomID() + "(" + x.getPercentUsing() + "%)", x.getTimesUsing()));
            netDuePiechart.getData().add(new PieChart.Data(x.getRoomID() + "(" + x.getPercentNetDue() + "%)", x.getNetDue()));
        });
    }
}
