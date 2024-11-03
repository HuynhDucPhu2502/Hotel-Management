package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.InvoiceDisplayOnTableDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
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

public class InvoiceRevenueStatisticsTabController implements Initializable {
    public ToggleGroup showDataViewToggleGroup;
    // Variables for revenue statistics view components
    @FXML private TableView<InvoiceDisplayOnTable> invoiceDataTableView;
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
    @FXML private ComboBox<Integer> yearsCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private DateRangePicker invoiceTabDateRangePicker;
    @FXML private ComboBox<String> employeeNameCombobox;
    @FXML private BarChart<String, Double> invoiceDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text numOfInvoiceText;
    private static final String NONE_VALUE_EMPLOYEE_NAME = "Chọn nhân viên";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        loadDataToEmployeeNameCombobox();
        loadDataToComboboxOfYear();
        showDataToChartView(false);
        showDataToTableView(invoiceDisplayOnTableData);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(invoiceDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(invoiceDisplayOnTableData))));
    }

    // show num of invoice on text
    private void setNumOfInvoice(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfInvoice(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.size();
    }

    // show total money of invoice on text
    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double caculateTotalMoney(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.stream()
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
    }
    // load data to table
    private void showDataToTableView(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData) {
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
        invoiceDataTableView.setItems(dataOfTableView);
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
    private void loadDataToEmployeeNameCombobox() {
        List<Employee> employeeList = EmployeeDAO.getEmployees();
        ObservableList<String> empNames = FXCollections.observableArrayList(NONE_VALUE_EMPLOYEE_NAME);
        employeeList.forEach(e -> empNames.add(e.getFullName()));
        employeeNameCombobox.setItems(empNames);
        employeeNameCombobox.setValue(empNames.getFirst());
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
            invoiceTabDateRangePicker.setDisable(true);
        } else {
            yearsCombobox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
        }
    }

    // handle statistics event
    @FXML
    void revenueStatisticsAction() {
        ObservableList<InvoiceDisplayOnTable> data;
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        String empName = employeeNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()) {
            data = getDataToTableViewByYearOption(invoiceDisplayOnTableData, empName);
            showDataToTableView(data);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToChartView(true);
        } else {
            data = getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToTableView(data);
            showDataToChartView(false);
        }
    }

    // filter data by year and employee name and show to table
    private ObservableList<InvoiceDisplayOnTable> getDataToTableViewByYearOption(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName) {
        int year = yearsCombobox.getSelectionModel().getSelectedItem();
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(filteredData::add);
        return filteredData;
    }

    // filter data by daterange and employee name and show to table
    private ObservableList<InvoiceDisplayOnTable> getDataToTableViewByDateRangeOption(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName) {
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            invoiceDisplayOnTableData.stream()
                    .filter(i -> i.getCreateDate().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        } else {
            invoiceDisplayOnTableData.stream()
                    .filter(i -> (i.getCreateDate().isAfter(startDate) || i.getCreateDate().isEqual(startDate))
                            && (i.getCreateDate().isBefore(endDate) || i.getCreateDate().isEqual(endDate)))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        }
        return filteredData;
    }

    // if flat equal TRUE, that means statistics by YEAR
    // if flat equal FALSE, that means statistics by DATE RANGE PICKER
    private void showDataToChartView(boolean flat) {
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        String empName = employeeNameCombobox.getValue();
        invoiceDataBarChart.setTitle(CHART_TITLE);
        invoiceDataBarChart.getData().clear();
        if(flat) invoiceDataBarChart.getData().add(getDataByYear(invoiceDisplayOnTableData, empName));
        else invoiceDataBarChart.getData().add(getDataByDateRange(invoiceDisplayOnTableData, empName));
    }

    private XYChart.Series<String, Double> getDataByYear(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName){
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        for (int i = 1; i <= Month.values().length; i++) {
            String month = iuh.fit.models.enums.Month
                    .valueOf(Arrays.stream(iuh.fit.models.enums.Month.values())
                            .toList().get(i - 1)
                            .toString()).getName();
            double netDueOfMonth = getAvgNetDueByMonthOfYear(invoiceDisplayOnTableData, yearsCombobox.getValue(), i, empName);
            data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        // thong ke cho ngay hien tai
        if(isToday(startDate, endDate)) return getDataForToday(invoiceDisplayOnTableData, empName, data);
        // thong ke cho ngay hom truoc
        else if(isYesterDay(startDate, endDate)) return getDataForYesterday(invoiceDisplayOnTableData, empName, data);
        // thong ke cho tuan nay
        else if(isThisWeek(startDate, endDate)) return getDataForThisWeek(invoiceDisplayOnTableData, empName, data);
        // thong ke cho thang nay
        else if(isThisMonth(startDate, endDate)) return getDataForThisMonth(invoiceDisplayOnTableData, empName, data);
        // thong ke cho thang truoc
        else if(isLastMonth(startDate, endDate)) return getDataForLastMonth(invoiceDisplayOnTableData, empName, data);
        // thong ke cho khoang thoi gian cu the
        else return getDataForAnyTime(invoiceDisplayOnTableData, empName, data);
    }

    private XYChart.Series<String, Double> getDataForToday(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(LocalDate.now()))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
        data.getData().add(new XYChart.Data<>(invoiceTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForYesterday(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
        data.getData().add(new XYChart.Data<>(invoiceTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisWeek(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        LocalDateTime monday = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0, 0);
        LocalDateTime sunday = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        ObservableList<InvoiceDisplayOnTable> invoiceOfWeek = FXCollections.observableArrayList();
        invoiceDisplayOnTableData.stream()
                .filter(i -> (i.getCreateDate().isAfter(monday) || i.getCreateDate().isEqual(monday))
                        && (i.getCreateDate().isBefore(sunday) || i.getCreateDate().isEqual(sunday)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(invoiceOfWeek::add);

        for (InvoiceDisplayOnTable i : invoiceOfWeek) {
            data.getData().add(new XYChart.Data<>(
                    i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfWeek, i.getCreateDate().toLocalDate())
            ));
        }
        showDataToTableView(getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisMonth(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<InvoiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        invoiceDisplayOnTableData.stream()
                .filter(i -> (i.getCreateDate().isAfter(firstDayOfMonth) || i.getCreateDate().isEqual(firstDayOfMonth))
                        && (i.getCreateDate().isBefore(lastDayOfMonth) || i.getCreateDate().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (InvoiceDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String,Double> getDataForLastMonth(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<InvoiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);

        invoiceDisplayOnTableData.stream()
                .filter(i -> (i.getCreateDate().isAfter(firstDayOfMonth) || i.getCreateDate().isEqual(firstDayOfMonth))
                        && (i.getCreateDate().isBefore(lastDayOfMonth) || i.getCreateDate().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (InvoiceDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataForAnyTime(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<InvoiceDisplayOnTable> invoiceOfRange = FXCollections.observableArrayList();
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        invoiceDisplayOnTableData.stream()
                .filter(i -> (i.getCreateDate().isAfter(startDate) || i.getCreateDate().isEqual(startDate))
                        && (i.getCreateDate().isBefore(endDate) || i.getCreateDate().isEqual(endDate)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfRange::add);
        for(InvoiceDisplayOnTable i : invoiceOfRange){
            data.getData().add(new XYChart.Data<>(i.getCreateDate().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfRange, i.getCreateDate().toLocalDate())));
        }
        return data;
    }

    private double getNetDueAvgOfDay(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData, LocalDate date){
        return invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(date))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
    }

    private double getAvgNetDueByMonthOfYear(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, int year, int month, String empName) {
        return invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> i.getCreateDate().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
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
        List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getData();
        invoiceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        showDataToTableView(FXCollections.observableArrayList(invoiceDisplayOnTableData));
        yearsCombobox.setValue(LocalDate.now().getYear());
        employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(invoiceDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(invoiceDisplayOnTableData))));
    }

    @FXML
    void exportExcelFile() throws IOException {
        boolean forEmployee = employeeNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME);
        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
        int numOfInvoice = getNumOfInvoice(invoiceDataTableView.getItems());
        double totalMoney = caculateTotalMoney(invoiceDataTableView.getItems());
        if(yearCBBChecked){
            ExportFileHelper.exportExcelFile(invoiceDataTableView,
                    ExportExcelCategory.ALL_OF_YEAR,
                    forEmployee,
                    invoiceTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else{
            LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
            LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
            if(isAMonth(startDate, endDate))
                ExportFileHelper.exportExcelFile(
                        invoiceDataTableView,
                        ExportExcelCategory.ALL_OF_MONTH,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isADay(startDate, endDate))
                ExportFileHelper.exportExcelFile(
                        invoiceDataTableView,
                        ExportExcelCategory.DAY_OF_MONTH,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isManyYear(startDate, endDate))
                ExportFileHelper.exportExcelFile(
                        invoiceDataTableView,
                        ExportExcelCategory.MANY_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else ExportFileHelper.exportExcelFile(
                        invoiceDataTableView,
                        ExportExcelCategory.DATE_RANGE,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
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
