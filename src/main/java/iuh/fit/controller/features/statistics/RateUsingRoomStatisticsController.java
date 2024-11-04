package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.InvoiceDisplayOnTableDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.UsingRoomDisplayOnTableDAO;
import iuh.fit.models.Room;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import iuh.fit.utils.ExportFileHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private AnchorPane chartViewAnchorPane;
    @FXML
    private AnchorPane tableViewAnchorPane;
    @FXML
    private AnchorPane tableViewDetailAnchorPane;
    @FXML
    private CheckBox filterByYearCheckBox;
    @FXML
    private DateRangePicker invoiceTabDateRangePicker;
    @FXML
    private ComboBox<String> roomIDCombobox;
    @FXML
    private BarChart<String, Double> invoiceDataBarChart;
    @FXML
    private Text totalMoneyText;
    @FXML
    private Text numOfInvoiceText;

    @FXML
    private Button resetBtn;
    @FXML
    private Button statisticsBtn;
    @FXML
    private Button exportExcelBtn;

    private static final String NONE_VALUE_ROOM_NAME = "Chọn mã phòng";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        setUpTable();

        filterByYearCheckBox.setOnAction(e -> switchBetweenCBBYearAndDateRangePicker());
        resetBtn.setOnAction(e -> handleResetAction());
        statisticsBtn.setOnAction(e -> statisticsAction());
        exportExcelBtn.setOnAction(e -> exportExcelFile());
    }

    private void loadData() {
        loadDataToComboboxOfYear();
        loadDataToRoomNameCombobox();
        invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData = UsingRoomDisplayOnTableDAO.getData();
        setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(FXCollections.observableArrayList(usingRoomDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(usingRoomDisplayOnTableData))));
    }

    private void setUpTable(){
        List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData = UsingRoomDisplayOnTableDAO.getData();
        usingRoomDisplayOnTableData.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID));
        showDataToTableView(usingRoomDisplayOnTableData);


        List<UsingRoomDetailDisplayOnTable> usingRoomDetailDisplayOnTableData = UsingRoomDisplayOnTableDAO.getDataDetail();
        usingRoomDetailDisplayOnTableData.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomID));
        showDataDetailToTableView(usingRoomDetailDisplayOnTableData);

        //        showDataToChartView(false);
    }

    private void showDataToTableView(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData) {
        // set data to ObservableList
        ObservableList<UsingRoomDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(usingRoomDisplayOnTableData);
        // set data on column
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("roomID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("cusName"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, String>("empName"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, LocalDateTime>("createDate"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("deposit"));
        serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("serviceCharge"));
        roomChargeColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("roomCharge"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("tax"));
        netDueColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDisplayOnTable, Double>("netDue"));
        // set data to table
        usingRateRoomDataTableView.setItems(dataOfTableView);

    }

    private void showDataDetailToTableView(List<UsingRoomDetailDisplayOnTable> usingRoomDetailDisplayOnTableData) {
        // set data to ObservableList
        ObservableList<UsingRoomDetailDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(usingRoomDetailDisplayOnTableData);
        // set data on column
        roomIDDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, String>("roomID"));
        timesUsingColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Integer>("timesUsing"));
        netDueDetailColumn.setCellValueFactory(new PropertyValueFactory<UsingRoomDetailDisplayOnTable, Double>("netDue"));
        // set data to table
        usingRateRoomDetailDataTableView.setItems(dataOfTableView);

    }

    private void loadDataToComboboxOfYear() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < COMBO_YEAR_CAPACITY; i++)
            years.add(LocalDate.now().getYear() - i);
        yearsCombobox.setItems(years);
        yearsCombobox.getItems().addFirst(null);
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

    private int getNumOfUsingRoom(ObservableList<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData){
        return usingRoomDisplayOnTableData.size();
    }

    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double caculateTotalMoney(ObservableList<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData){
        return usingRoomDisplayOnTableData.stream()
                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                .sum();
    }

    private void handleResetAction() {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();

        showTableViewRadioButton.setSelected(true);
//        switchBetweenTableViewAndChartView();
        loadData();
        setUpTable();
    }

    private void switchBetweenCBBYearAndDateRangePicker() {
        if (filterByYearCheckBox.isSelected()) {
            yearsCombobox.setDisable(false);
            invoiceTabDateRangePicker.setDisable(true);
        } else {
            yearsCombobox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
        }
    }

    private void statisticsAction() {
        ObservableList<UsingRoomDisplayOnTable> data;
        ObservableList<UsingRoomDetailDisplayOnTable> data1;
        List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData = UsingRoomDisplayOnTableDAO.getData();
        usingRoomDisplayOnTableData.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID));

        String roomID = roomIDCombobox.getValue();
        if (filterByYearCheckBox.isSelected()) {
            if(yearsCombobox.getSelectionModel().getSelectedItem() == null && roomIDCombobox.getSelectionModel().getSelectedItem().equals(NONE_VALUE_ROOM_NAME)){
                setUpTable();
                setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(FXCollections.observableArrayList(usingRoomDisplayOnTableData))));
                setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(usingRoomDisplayOnTableData))));
                return;
            }

            data = getDataToTableViewByYearOption(usingRoomDisplayOnTableData, roomID);
            showDataToTableView(data);


            data1 = getDataDetailToTableViewByYearOption(usingRoomDisplayOnTableData, roomID);
            showDataDetailToTableView(data1);

            setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));

//            showDataToChartView(true);



        } else {
            data = getDataToTableViewByDateRangeOption(usingRoomDisplayOnTableData, roomID);
            data1 = getDataDetailToTableViewByDateRangeOption(usingRoomDisplayOnTableData, roomID);
            showDataToTableView(data);
            showDataDetailToTableView(data1);

            setNumOfUsingRoom(String.valueOf(getNumOfUsingRoom(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
//            showDataToChartView(false);
        }
    }

    private ObservableList<UsingRoomDisplayOnTable> getDataToTableViewByYearOption(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData, String roomID) {

        ObservableList<UsingRoomDisplayOnTable> filteredData = FXCollections.observableArrayList();
        if (yearsCombobox.getSelectionModel().getSelectedItem() == null){
            usingRoomDisplayOnTableData.stream()
                    .filter(i -> i.getRoomID().equalsIgnoreCase(roomID) || roomID.equals(NONE_VALUE_ROOM_NAME))
                    .forEach(filteredData::add);
            return filteredData;
        }
        int year = yearsCombobox.getSelectionModel().getSelectedItem();
        usingRoomDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> i.getRoomID().equalsIgnoreCase(roomID) || roomID.equals(NONE_VALUE_ROOM_NAME))
                .forEach(filteredData::add);
        return filteredData;
    }

    private ObservableList<UsingRoomDetailDisplayOnTable> getDataDetailToTableViewByYearOption(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData, String roomID) {
        ObservableList<UsingRoomDetailDisplayOnTable> filteredData = FXCollections.observableArrayList();
        if (yearsCombobox.getSelectionModel().getSelectedItem() == null){
            ObservableList<UsingRoomDetailDisplayOnTable> observableList = usingRoomDisplayOnTableData.stream()
                    .filter(i -> i.getRoomID().equalsIgnoreCase(roomID) || roomID.equals(NONE_VALUE_ROOM_NAME))
                    .collect(Collectors.groupingBy(
                            UsingRoomDisplayOnTable::getRoomID,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> {
                                        int count = list.size();
                                        double totalNetDue = list.stream()
                                                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                                                .sum();
                                        return new UsingRoomDetailDisplayOnTable(
                                                list.get(0).getRoomID(),
                                                count,
                                                totalNetDue
                                        );
                                    }
                            )
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            filteredData.addAll(observableList);
            return filteredData;
        }
        int year = yearsCombobox.getSelectionModel().getSelectedItem();
        ObservableList<UsingRoomDetailDisplayOnTable> observableList = usingRoomDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> i.getRoomID().equalsIgnoreCase(roomID) || roomID.equals(NONE_VALUE_ROOM_NAME))
                .collect(Collectors.groupingBy(
                        UsingRoomDisplayOnTable::getRoomID,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    int count = list.size();
                                    double totalNetDue = list.stream()
                                            .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                                            .sum();
                                    return new UsingRoomDetailDisplayOnTable(
                                            list.get(0).getRoomID(),
                                            count,
                                            totalNetDue
                                    );
                                }
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        filteredData.addAll(observableList);
        return filteredData;
    }

    private ObservableList<UsingRoomDisplayOnTable> getDataToTableViewByDateRangeOption(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData, String roomID) {
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<UsingRoomDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            usingRoomDisplayOnTableData.stream()
                    .filter(i -> i.getCreateDate().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> roomID.equalsIgnoreCase(NONE_VALUE_ROOM_NAME) || i.getRoomID().equalsIgnoreCase(roomID))
                    .forEach(filteredData::add);
        } else {
            usingRoomDisplayOnTableData.stream()
                    .filter(i -> (i.getCreateDate().isAfter(startDate) || i.getCreateDate().isEqual(startDate))
                            && (i.getCreateDate().isBefore(endDate) || i.getCreateDate().isEqual(endDate)))
                    .filter(i -> roomID.equalsIgnoreCase(NONE_VALUE_ROOM_NAME) || i.getRoomID().equalsIgnoreCase(roomID))
                    .forEach(filteredData::add);
        }
        return filteredData;
    }

    private ObservableList<UsingRoomDetailDisplayOnTable> getDataDetailToTableViewByDateRangeOption(List<UsingRoomDisplayOnTable> usingRoomDisplayOnTableData, String roomID) {
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<UsingRoomDetailDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            ObservableList<UsingRoomDetailDisplayOnTable> observableList = usingRoomDisplayOnTableData.stream()
                    .filter(i -> i.getCreateDate().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> roomID.equalsIgnoreCase(NONE_VALUE_ROOM_NAME) || i.getRoomID().equalsIgnoreCase(roomID))
                    .collect(Collectors.groupingBy(
                            UsingRoomDisplayOnTable::getRoomID,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> {
                                        int count = list.size();
                                        double totalNetDue = list.stream()
                                                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                                                .sum();
                                        return new UsingRoomDetailDisplayOnTable(
                                                list.get(0).getRoomID(),
                                                count,
                                                totalNetDue
                                        );
                                    }
                            )
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            filteredData.addAll(observableList);
        } else {
            ObservableList<UsingRoomDetailDisplayOnTable> observableList = usingRoomDisplayOnTableData.stream()
                    .filter(i -> (i.getCreateDate().isAfter(startDate) || i.getCreateDate().isEqual(startDate))
                            && (i.getCreateDate().isBefore(endDate) || i.getCreateDate().isEqual(endDate)))
                    .filter(i -> roomID.equalsIgnoreCase(NONE_VALUE_ROOM_NAME) || i.getRoomID().equalsIgnoreCase(roomID))
                    .collect(Collectors.groupingBy(
                            UsingRoomDisplayOnTable::getRoomID,
                            Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    list -> {
                                        int count = list.size();
                                        double totalNetDue = list.stream()
                                                .mapToDouble(UsingRoomDisplayOnTable::getNetDue)
                                                .sum();
                                        return new UsingRoomDetailDisplayOnTable(
                                                list.get(0).getRoomID(),
                                                count,
                                                totalNetDue
                                        );
                                    }
                            )
                    ))
                    .values()
                    .stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            filteredData.addAll(observableList);
        }
        return filteredData;
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
        if (showTableViewRadioButton.isSelected()){
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase(NONE_VALUE_ROOM_NAME);
            boolean yearCBBChecked = filterByYearCheckBox.isSelected();
            int numOfInvoice = getNumOfUsingRoom(usingRateRoomDataTableView.getItems());
            double totalMoney = caculateTotalMoney(usingRateRoomDataTableView.getItems());
            if(yearCBBChecked){
                ExportFileHelper.exportUsingRoomExcelFile(usingRateRoomDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            } else{
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
            }
        }
        if(showTableViewDetailRadioButton.isSelected()){
            boolean forEmployee = roomIDCombobox.getValue().equalsIgnoreCase(NONE_VALUE_ROOM_NAME);
            boolean yearCBBChecked = filterByYearCheckBox.isSelected();
            int numOfInvoice = getNumOfUsingRoom(usingRateRoomDataTableView.getItems());
            double totalMoney = caculateTotalMoney(usingRateRoomDataTableView.getItems());
            if(yearCBBChecked){
                ExportFileHelper.exportUsingRoomDetailExcelFile(usingRateRoomDetailDataTableView,
                        ExportExcelCategory.ALL_OF_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice, totalMoney);
            } else{
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
            }
        }
    }
    private boolean isToday(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(LocalDate.now()) && endDate.toLocalDate().equals(LocalDate.now());
    }

    private boolean isYesterDay(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(LocalDate.now().minusDays(1)) && endDate.toLocalDate().equals(LocalDate.now().minusDays(1));
    }

    private boolean isThisWeek(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(LocalDate.now().with(DayOfWeek.MONDAY)) && endDate.toLocalDate().equals(LocalDate.now().with(DayOfWeek.SUNDAY));
    }

    private boolean isThisMonth(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(LocalDate.now().withDayOfMonth(1)) && endDate.toLocalDate().equals(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()));
    }

    private boolean isLastMonth(LocalDateTime startDate, LocalDateTime endDate){
        return startDate.toLocalDate().equals(LocalDate.now().minusMonths(1).withDayOfMonth(1)) && endDate.toLocalDate().equals(LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()));
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
}
