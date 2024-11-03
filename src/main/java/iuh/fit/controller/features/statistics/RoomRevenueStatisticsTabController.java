package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDisplayOnTableDAO;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.wrapper.RoomDisplayOnTable;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RoomRevenueStatisticsTabController implements Initializable {
    public ToggleGroup showDataViewToggleGroup;
    // Variables for revenue statistics view components
    @FXML
    private TableView<RoomDisplayOnTable> roomDataTableView;
    @FXML private TableColumn<RoomDisplayOnTable, String> roomIDColumn;
    @FXML private TableColumn<RoomDisplayOnTable, String> roomCategoryColumn;
    @FXML private TableColumn<RoomDisplayOnTable, Integer> numOfPeopleColumn;
    @FXML private TableColumn<RoomDisplayOnTable, LocalDateTime> bookingDateColumn;
    @FXML private TableColumn<RoomDisplayOnTable, LocalDateTime> checkInDateColumn;
    @FXML private TableColumn<RoomDisplayOnTable, LocalDateTime> checkOutDateColumn;
    @FXML private TableColumn<RoomDisplayOnTable, Double> totalMoneyColumn;
    @FXML private ComboBox<Integer> yearsCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private DateRangePicker roomTabDateRangePicker;
    @FXML private ComboBox<String> roomCategoryNameCombobox;
    @FXML private BarChart<String, Double> roomDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text numOfInvoiceText;
    private static final String NONE_VALUE_ROOM_CATEGORY_NAME = "Chọn loại phòng";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<RoomDisplayOnTable> roomDisplayOnTableData = RoomDisplayOnTableDAO.getData();
        loadDataToRoomCategoryNameCombobox();
        loadDataToComboboxOfYear();
        showDataToChartView(false);
        showDataToTableView(roomDisplayOnTableData);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(roomDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(roomDisplayOnTableData))));
    }

    // show num of invoice on text
    private void setNumOfInvoice(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfInvoice(ObservableList<RoomDisplayOnTable> roomDisplayOnTableData){
        return roomDisplayOnTableData.size();
    }

    // show total money of invoice on text
    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double caculateTotalMoney(ObservableList<RoomDisplayOnTable> roomDisplayOnTableData){
        return roomDisplayOnTableData.stream()
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
    }
    // load data to table
    private void showDataToTableView(List<RoomDisplayOnTable> roomDisplayOnTableData) {
        // set data to ObservableList
        ObservableList<RoomDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(roomDisplayOnTableData);
        // set data on column
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, String>("roomID"));
        roomCategoryColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, String>("roomCategory"));
        numOfPeopleColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, Integer>("numOfPeople"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, LocalDateTime>("bookingDate"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, LocalDateTime>("checkInDate"));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, LocalDateTime>("checkOutDate"));
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<RoomDisplayOnTable, Double>("totalMoney"));
        // set data to table
        roomDataTableView.setItems(dataOfTableView);
    }

    // set date to combox of year
    private void loadDataToComboboxOfYear() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < COMBO_YEAR_CAPACITY; i++)
            years.add(LocalDate.now().getYear() - i);
        yearsCombobox.setItems(years);
        yearsCombobox.setValue(years.getFirst());
    }

    // set data to combox of employee name
    private void loadDataToRoomCategoryNameCombobox() {
        List<RoomCategory> roomCategoryList = RoomCategoryDAO.getRoomCategory();
        ObservableList<String> roomCategoryName = FXCollections.observableArrayList(NONE_VALUE_ROOM_CATEGORY_NAME);
        roomCategoryList.forEach(r -> roomCategoryName.add(r.getRoomCategoryName()));
        roomCategoryNameCombobox.setItems(roomCategoryName);
        roomCategoryNameCombobox.setValue(roomCategoryName.getFirst());
    }

    // switch between table view and chart view
    @FXML
    void switchBetweenTableViewAndChartView() {
        if (showTableViewRadioButton.isSelected()) {
            tableViewAnchorPane.setVisible(true);
            chartViewAnchorPane.setVisible(false);
        }
        if (showChartDataRadioButton.isSelected()) {
            chartViewAnchorPane.setVisible(true);
            tableViewAnchorPane.setVisible(false);
        }
    }

    // switch between year combobox and daterangepicker
    @FXML
    void switchBetweenCBBYearAndDateRangePicker() {
        if (filterByYearCheckBox.isSelected()) {
            yearsCombobox.setDisable(false);
            roomTabDateRangePicker.setDisable(true);
        } else {
            yearsCombobox.setDisable(true);
            roomTabDateRangePicker.setDisable(false);
        }
    }

    // handle statistics event
    @FXML
    void revenueStatisticsAction() {
        ObservableList<RoomDisplayOnTable> data;
        List<RoomDisplayOnTable> roomDisplayOnTableData = RoomDisplayOnTableDAO.getData();
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()) {
            data = getDataToTableViewByYearOption(roomDisplayOnTableData, roomCategoryName);
            showDataToTableView(data);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToChartView(true);
        } else {
            data = getDataToTableViewByDateRangeOption(roomDisplayOnTableData, roomCategoryName);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToTableView(data);
            showDataToChartView(false);
        }
    }

    // filter data by year and employee name and show to table
    private ObservableList<RoomDisplayOnTable> getDataToTableViewByYearOption(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName) {
        int year = yearsCombobox.getSelectionModel().getSelectedItem();
        ObservableList<RoomDisplayOnTable> filteredData = FXCollections.observableArrayList();
        roomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().getYear() == year)
                .filter(i -> empName.equals(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                .forEach(filteredData::add);
        return filteredData;
    }

    // filter data by daterange and employee name and show to table
    private ObservableList<RoomDisplayOnTable> getDataToTableViewByDateRangeOption(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName) {
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<RoomDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            roomDisplayOnTableData.stream()
                    .filter(i -> i.getBookingDate().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        } else {
            roomDisplayOnTableData.stream()
                    .filter(i -> (i.getBookingDate().isAfter(startDate) || i.getBookingDate().isEqual(startDate))
                            && (i.getBookingDate().isBefore(endDate) || i.getBookingDate().isEqual(endDate)))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        }
        return filteredData;
    }

    // if flat equal TRUE, that means statistics by YEAR
    // if flat equal FALSE, that means statistics by DATE RANGE PICKER
    private void showDataToChartView(boolean flat) {
        List<RoomDisplayOnTable> roomDisplayOnTableData = RoomDisplayOnTableDAO.getData();
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        roomDataBarChart.setTitle(CHART_TITLE);
        roomDataBarChart.getData().clear();
        if(flat) roomDataBarChart.getData().add(getDataByYear(roomDisplayOnTableData, roomCategoryName));
        else roomDataBarChart.getData().add(getDataByDateRange(roomDisplayOnTableData, roomCategoryName));
    }

    private XYChart.Series<String, Double> getDataByYear(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName){
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        for (int i = 1; i <= Month.values().length; i++) {
            String month = iuh.fit.models.enums.Month
                    .valueOf(Arrays.stream(iuh.fit.models.enums.Month.values())
                            .toList().get(i - 1)
                            .toString()).getName();
            double netDueOfMonth = getAvgNetDueByMonthOfYear(roomDisplayOnTableData, yearsCombobox.getValue(), i, empName);
            data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        // thong ke cho ngay hien tai
        if(isToday(startDate, endDate)) return getDataForToday(roomDisplayOnTableData, empName, data);
            // thong ke cho ngay hom truoc
        else if(isYesterDay(startDate, endDate)) return getDataForYesterday(roomDisplayOnTableData, empName, data);
            // thong ke cho tuan nay
        else if(isThisWeek(startDate, endDate)) return getDataForThisWeek(roomDisplayOnTableData, empName, data);
            // thong ke cho thang nay
        else if(isThisMonth(startDate, endDate)) return getDataForThisMonth(roomDisplayOnTableData, empName, data);
            // thong ke cho thang truoc
        else if(isLastMonth(startDate, endDate)) return getDataForLastMonth(roomDisplayOnTableData, empName, data);
            // thong ke cho khoang thoi gian cu the
        else return getDataForAnyTime(roomDisplayOnTableData, empName, data);
    }

    private XYChart.Series<String, Double> getDataForToday(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = roomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(LocalDate.now()))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(empName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(roomTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(roomDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForYesterday(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = roomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(empName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(roomTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(roomDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisWeek(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        LocalDateTime monday = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0, 0);
        LocalDateTime sunday = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        ObservableList<RoomDisplayOnTable> invoiceOfWeek = FXCollections.observableArrayList();
        roomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(monday) || i.getBookingDate().isEqual(monday))
                        && (i.getBookingDate().isBefore(sunday) || i.getBookingDate().isEqual(sunday)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                .forEach(invoiceOfWeek::add);

        for (RoomDisplayOnTable i : invoiceOfWeek) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfWeek, i.getBookingDate().toLocalDate())
            ));
        }
        showDataToTableView(getDataToTableViewByDateRangeOption(roomDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisMonth(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        roomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(firstDayOfMonth) || i.getBookingDate().isEqual(firstDayOfMonth))
                        && (i.getBookingDate().isBefore(lastDayOfMonth) || i.getBookingDate().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (RoomDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getBookingDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String,Double> getDataForLastMonth(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);

        roomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(firstDayOfMonth) || i.getBookingDate().isEqual(firstDayOfMonth))
                        && (i.getBookingDate().isBefore(lastDayOfMonth) || i.getBookingDate().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (RoomDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getBookingDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataForAnyTime(List<RoomDisplayOnTable> roomDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfRange = FXCollections.observableArrayList();
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        roomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(startDate) || i.getBookingDate().isEqual(startDate))
                        && (i.getBookingDate().isBefore(endDate) || i.getBookingDate().isEqual(endDate)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(empName)))
                .forEach(invoiceOfRange::add);
        for(RoomDisplayOnTable i : invoiceOfRange){
            data.getData().add(new XYChart.Data<>(i.getBookingDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfRange, i.getBookingDate().toLocalDate())));
        }
        return data;
    }

    private double getNetDueAvgOfDay(ObservableList<RoomDisplayOnTable> roomDisplayOnTableData, LocalDate date){
        return roomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(date))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
    }

    private double getAvgNetDueByMonthOfYear(List<RoomDisplayOnTable> roomDisplayOnTableData, int year, int month, String empName) {
        return roomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().getYear() == year)
                .filter(i -> i.getBookingDate().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(empName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
    }

    private void showMessages(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void refreshData() {
        List<RoomDisplayOnTable> roomDisplayOnTableData = RoomDisplayOnTableDAO.getData();
        roomTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        showDataToTableView(FXCollections.observableArrayList(roomDisplayOnTableData));
        yearsCombobox.setValue(LocalDate.now().getYear());
        roomCategoryNameCombobox.setValue(NONE_VALUE_ROOM_CATEGORY_NAME);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(roomDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(roomDisplayOnTableData))));
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    @FXML
//    void exportExcelFile() throws IOException {
//        boolean forEmployee = employeeNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME);
//        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
//        int numOfInvoice = getNumOfInvoice(invoiceDataTableView.getItems());
//        double totalMoney = caculateTotalMoney(invoiceDataTableView.getItems());
//        if(yearCBBChecked){
//            ExportFileHelper.exportExcelFile(invoiceDataTableView,
//                    ExportExcelCategory.ALL_OF_YEAR,
//                    forEmployee,
//                    invoiceTabDateRangePicker.getValue(),
//                    numOfInvoice, totalMoney);
//        } else{
//            LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
//            LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
//            if(isAMonth(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.ALL_OF_MONTH,
//                        forEmployee,
//                        invoiceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isADay(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.DAY_OF_MONTH,
//                        forEmployee,
//                        invoiceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isManyYear(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.MANY_YEAR,
//                        forEmployee,
//                        invoiceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.DATE_RANGE,
//                        forEmployee,
//                        invoiceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//        }
//    }

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
