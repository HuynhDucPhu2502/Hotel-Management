package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.InvoiceDisplayOnTableDAO;
import iuh.fit.models.*;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticalController implements Initializable {

    // Variables for revenue statistics view components
    @FXML private TableView<InvoiceDisplayOnTable> revenueTableView;
    @FXML private TableColumn<InvoiceDisplayOnTable, String> invoiceIDColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, String> customerNameColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, String> roomIDColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, String> employeeNameColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, LocalDateTime> invoiceDateColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, Double> depositColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, Double> serviceChargeColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, Double> roomChargeColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, Double> taxColumn;
    @FXML private TableColumn<InvoiceDisplayOnTable, Double> netDueColumn;
    @FXML private ComboBox<Integer> cbbYear;
    @FXML private RadioButton tableViewRevenueRadioButton;
    @FXML private RadioButton chartViewRevenueRadioButton;
    @FXML private AnchorPane chartView;
    @FXML private AnchorPane tableView;
    @FXML private CheckBox checkBoxStatisticsByYear;
    @FXML private DateRangePicker dateRangePicker;
    @FXML private ComboBox<String> revenueEmployeeCombobox;
    @FXML private BarChart<String, Double> revenueBarchart;
    private static final String NONE_VALUE_CUSTOMER_NAME = "Chọn nhân viên";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        loadDataToTable(invoiceDisplayOnTableData);
        loadDataToEmployeeNameCombobox();
        loadDataToComboboxOfYear();
    }

    // load data to table
    private void loadDataToTable(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData) {
        // set data to ObservableList
        ObservableList<InvoiceDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(invoiceDisplayOnTableData);
        // set data on column
        invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, String>("invoiceID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, String>("cusName"));
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, String>("roomID"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, String>("empName"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, LocalDateTime>("createDate"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, Double>("deposit"));
        serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, Double>("serviceCharge"));
        roomChargeColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, Double>("roomCharge"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, Double>("tax"));
        netDueColumn.setCellValueFactory(new PropertyValueFactory<InvoiceDisplayOnTable, Double>("netDue"));
        // set data to table
        revenueTableView.setItems(dataOfTableView);
    }

    // set date to combox of year
    private void loadDataToComboboxOfYear() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 0; i < COMBO_YEAR_CAPACITY; i++)
            years.add(LocalDate.now().getYear() - i);
        cbbYear.setItems(years);
        cbbYear.setValue(years.getFirst());
    }

    // set data to combox of employee name
    private void loadDataToEmployeeNameCombobox() {
        List<Employee> employeeList = EmployeeDAO.getEmployees();
        ObservableList<String> empNames = FXCollections.observableArrayList(NONE_VALUE_CUSTOMER_NAME);
        employeeList.forEach(e -> empNames.add(e.getFullName()));
        revenueEmployeeCombobox.setItems(empNames);
        revenueEmployeeCombobox.setValue(empNames.getFirst());
    }

    // switch between table view and chart view
    @FXML
    void switchBetweenTableViewAndChartView() {
        if (tableViewRevenueRadioButton.isSelected()) {
            tableView.setVisible(true);
            chartView.setVisible(false);
        }
        if (chartViewRevenueRadioButton.isSelected()) {
            chartView.setVisible(true);
            tableView.setVisible(false);
        }
    }

    // switch between year combobox and daterangepicker
    @FXML
    void switchBetweenCBBYearAndDateRangePicker() {
        if (checkBoxStatisticsByYear.isSelected()) {
            cbbYear.setDisable(false);
            dateRangePicker.setDisable(true);
        } else {
            cbbYear.setDisable(true);
            dateRangePicker.setDisable(false);
        }
    }

    // handle statistics event
    @FXML
    void revenueStatisticsAction() {
        String empName = revenueEmployeeCombobox.getValue();
        if (checkBoxStatisticsByYear.isSelected()) {
            showDataToTableViewByYearOption(empName);
            showDataToChartView(true);
        } else {
            showDataToTableViewByDateRangeOption(empName);
            showDataToChartView(false);
        }
    }

    // filter data by year and employee name and show to table
    private void showDataToTableViewByYearOption(String empName) {
        int year = cbbYear.getSelectionModel().getSelectedItem();
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        InvoiceDisplayOnTableDAO.getData().stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> empName.equals(NONE_VALUE_CUSTOMER_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(filteredData::add);
        loadDataToTable(filteredData);
    }

    // filter data by daterange and employee name and show to table
    private void showDataToTableViewByDateRangeOption(String empName) {
        LocalDateTime startDate = dateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = dateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        InvoiceDisplayOnTableDAO.getData().stream()
                .filter(i -> i.getCreateDate().isAfter(startDate) && i.getCreateDate().isBefore(endDate))
                .filter(i -> empName.equals(NONE_VALUE_CUSTOMER_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(filteredData::add);
        loadDataToTable(filteredData);
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        String empName = revenueEmployeeCombobox.getValue();
        LocalDate startDate = dateRangePicker.getValue().getStartDate();
        LocalDate endDate = dateRangePicker.getValue().getEndDate();

        // thong ke cho ngay hien tai
        if(startDate.equals(LocalDate.now())){
            statisticsForToday(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        // thong ke cho ngay hom truoc
        else if(startDate.equals(LocalDate.now().minusDays(1))){
            statisticsForYesterday(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        // thong ke cho tuan nay
        else if(startDate.equals(LocalDate.now().with(DayOfWeek.MONDAY))
                && endDate.equals(LocalDate.now().with(DayOfWeek.SUNDAY))){
            statisticsForThisWeek(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        // thong ke cho thang nay
        else if(startDate.equals(LocalDate.now().withDayOfMonth(1))
                && endDate.equals(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))){
            statisticsForThisMonth(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        // thong ke cho thang truoc
        else if(startDate.equals(LocalDate.now().minusMonths(1).withDayOfMonth(1))
                && endDate.equals(LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()))){
            statisticsForLastMonth(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        // thong ke cho khoang thoi gian cu the
        else{
            //show data on table
            statisticsForAnyTime(invoiceDisplayOnTableData, startDate, endDate, empName, data);
        }
        return data;
    }

    private void statisticsForAnyTime(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        showDataToTableViewByDateRangeOption(empName);
        ObservableList<InvoiceDisplayOnTable> invoiceOfRange = FXCollections.observableArrayList();
        list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().isAfter(startDate)
                        && i.getCreateDate().toLocalDate().isBefore(endDate))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfRange::add);
        for(InvoiceDisplayOnTable i : invoiceOfRange){
            data.getData().add(new XYChart.Data<>(i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfRange, i.getCreateDate().toLocalDate())));
        }
    }

    private void statisticsForLastMonth(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        //show data on table
        showDataToTableViewByDateRangeOption(empName);
        ObservableList<InvoiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().isAfter(LocalDate.now().minusMonths(1).withDayOfMonth(1))
                        && i.getCreateDate().toLocalDate().isBefore(LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth())))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfMonth::add);
        for(InvoiceDisplayOnTable i : invoiceOfMonth){
            data.getData().add(new XYChart.Data<>(i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())));
        }
    }

    private void statisticsForThisMonth(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        //show data on table
        showDataToTableViewByDateRangeOption(empName);
        ObservableList<InvoiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().isAfter(LocalDate.now().withDayOfMonth(1))
                        && i.getCreateDate().toLocalDate().isBefore(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfMonth::add);
        for(InvoiceDisplayOnTable i : invoiceOfMonth){
            data.getData().add(new XYChart.Data<>(i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())));
        }
    }

    private void statisticsForThisWeek(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        //show data on table
        showDataToTableViewByDateRangeOption(empName);
        ObservableList<InvoiceDisplayOnTable> invoiceOfWeek = FXCollections.observableArrayList();
        list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().isAfter(LocalDate.now().with(DayOfWeek.MONDAY))
                        && i.getCreateDate().toLocalDate().isBefore(LocalDate.now().with(DayOfWeek.SUNDAY)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfWeek::add);
        for(InvoiceDisplayOnTable i : invoiceOfWeek){
            data.getData().add(new XYChart.Data<>(i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfWeek, i.getCreateDate().toLocalDate())));
        }
    }

    private void statisticsForYesterday(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .average().orElse(0.0);
        data.getData().add(new XYChart.Data<>(dateRangePicker.getValue().getStartDate().toString(), netDueAve));
        loadDataToTable(getInvoiceDisplayByDay(empName));
    }

    private void statisticsForToday(List<InvoiceDisplayOnTable> list, LocalDate startDate, LocalDate endDate, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(LocalDate.now()))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .average().orElse(0.0);
        data.getData().add(new XYChart.Data<>(dateRangePicker.getValue().getStartDate().toString(), netDueAve));
        //loadDataTable(getInvoiceDisplayByDay(empName));
    }

    private double getNetDueAvgOfDay(ObservableList<InvoiceDisplayOnTable> list, LocalDate date){
        return list.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(date))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .average().orElse(0.0);
    }

    // when flat equal TRUE, that means statistics by YEAR
    // when flat equal FALSE, that means statistics by DATE RANGE PICKER
    private void showDataToChartView(boolean flat) {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        revenueBarchart.setTitle(CHART_TITLE);
        revenueBarchart.getData().clear();
        if(flat) revenueBarchart.getData().add(getDataByYear(invoiceDisplayOnTableData));
        else revenueBarchart.getData().add(getDataByDateRange(invoiceDisplayOnTableData));
    }

    private XYChart.Series<String, Double> getDataByYear(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        String empName = revenueEmployeeCombobox.getValue();

        for (int i = 1; i <= 12; i++) {
            String month = getMonthName(i);
            double netDueOfMonth = getAvgNetDueOfMonthByYear(invoiceDisplayOnTableData, cbbYear.getValue(), i, empName);
            data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
        }
        return data;
    }

    private double getAvgNetDueOfMonthByYear(List<InvoiceDisplayOnTable> list, int year, int month, String empName) {
        return list.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> i.getCreateDate().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .average()
                .orElse(0.0);
    }

    private ObservableList<InvoiceDisplayOnTable> getInvoiceDisplayByDay(String empName){
        ObservableList<InvoiceDisplayOnTable> list = FXCollections.observableArrayList();
        LocalDate startDate = dateRangePicker.getValue().getStartDate();
        for(InvoiceDisplayOnTable i : InvoiceDisplayOnTableDAO.getData()){
            if(i.getCreateDate().toLocalDate().equals(startDate)
            && (empName.equalsIgnoreCase(NONE_VALUE_CUSTOMER_NAME) ||
                    i.getEmpName().equalsIgnoreCase(empName)))
                list.add(i);
        }
        return list;
    }

    private String getMonthName(int monthIndex) {
        return switch (monthIndex) {
            case 1 -> "Tháng 1";
            case 2 -> "Tháng 2";
            case 3 -> "Tháng 3";
            case 4 -> "Tháng 4";
            case 5 -> "Tháng 5";
            case 6 -> "Tháng 6";
            case 7 -> "Tháng 7";
            case 8 -> "Tháng 8";
            case 9 -> "Tháng 9";
            case 10 -> "Tháng 10";
            case 11 -> "Tháng 11";
            case 12 -> "Tháng 12";
            default -> throw new IllegalArgumentException("Invalid month index: " + monthIndex);
        };
    }

    private void showMessages(String title, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void refreshData() {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        dateRangePicker.setValue(new DateRange(LocalDate.now()));
        loadDataToTable(FXCollections.observableArrayList(invoiceDisplayOnTableData));
        cbbYear.setValue(LocalDate.now().getYear());
        revenueEmployeeCombobox.setValue(NONE_VALUE_CUSTOMER_NAME);
        statisticsForToday(invoiceDisplayOnTableData,
                dateRangePicker.getValue().getStartDate(),
                dateRangePicker.getValue().getEndDate(),
                revenueEmployeeCombobox.getValue(),
                getDataByDateRange(invoiceDisplayOnTableData));
    }
}
