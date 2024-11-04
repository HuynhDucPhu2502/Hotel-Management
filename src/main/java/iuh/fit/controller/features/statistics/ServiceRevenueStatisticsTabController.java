package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.dao.ServiceDisplayOnTableDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.wrapper.ServiceDisplayOnTable;
import iuh.fit.utils.EditDateRangePicker;
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

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ServiceRevenueStatisticsTabController implements Initializable {
    public ToggleGroup showDataViewToggleGroup;

    // Variables for revenue statistics view components
    @FXML
    private TableView<ServiceDisplayOnTable> serviceDataTableView;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceIDColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceCategoryNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> employeeNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, LocalDateTime> dateAddedColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Integer> quantityColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Double> unitPriceColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Double> totalMoneyColumn;
    @FXML private ComboBox<Integer> yearsCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private DateRangePicker serviceTabDateRangePicker;
    @FXML private ComboBox<String> employeeNameCombobox;
    @FXML private ComboBox<String> serviceCategoryNameCombobox;
    @FXML private BarChart<String, Double> invoiceDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text numOfInvoiceText;
    private static final String NONE_VALUE_SERVICE_CATEGORY = "Loại dịch vụ";
    private static final String NONE_VALUE_EMPLOYEE_NAME = "Chọn nhân viên";
    private static final String CHART_TITLE = "Thống kê doanh thu";
    private static final int COMBO_YEAR_CAPACITY = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EditDateRangePicker.editDateRangePicker(serviceTabDateRangePicker);
        List<ServiceDisplayOnTable> serviceDisplayOnTableData = ServiceDisplayOnTableDAO.getData();
        loadDataToEmployeeNameCombobox();
        loadDataToCategoryServiceCombobox();
        loadDataToComboboxOfYear();
        showDataToChartView(false);
        showDataToTableView(serviceDisplayOnTableData);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(serviceDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(serviceDisplayOnTableData))));
    }

    // show num of invoice on text
    private void setNumOfInvoice(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfInvoice(ObservableList<ServiceDisplayOnTable> serviceDisplayOnTableData){
        return serviceDisplayOnTableData.size();
    }

    // show total money of invoice on text
    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double caculateTotalMoney(ObservableList<ServiceDisplayOnTable> serviceDisplayOnTableData){
        return serviceDisplayOnTableData.stream()
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
    }
    // load data to table
    private void showDataToTableView(List<ServiceDisplayOnTable> serviceDisplayOnTableData) {
        // set data to ObservableList
        ObservableList<ServiceDisplayOnTable> dataOfTableView = FXCollections.observableArrayList();
        dataOfTableView.addAll(serviceDisplayOnTableData);
        // set data on column
        serviceIDColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, String>("serviceId"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, String>("serviceName"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, String>("serviceCategory"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, String>("employeeName"));
        dateAddedColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, LocalDateTime>("dateAdded"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, Integer>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, Double>("unitPrice"));
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<ServiceDisplayOnTable, Double>("totalMoney"));

        // set data to table
        serviceDataTableView.setItems(dataOfTableView);
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

    // set data to combox of category service
    private void loadDataToCategoryServiceCombobox() {
        List<ServiceCategory> serviceCategoryList = ServiceCategoryDAO.getServiceCategory();
        ObservableList<String> categoryNames = FXCollections.observableArrayList(NONE_VALUE_SERVICE_CATEGORY);
        serviceCategoryList.forEach(c -> categoryNames.add(c.getServiceCategoryName()));
        serviceCategoryNameCombobox.setItems(categoryNames);
        serviceCategoryNameCombobox.setValue(categoryNames.getFirst());
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
            serviceTabDateRangePicker.setDisable(true);
        } else {
            yearsCombobox.setDisable(true);
            serviceTabDateRangePicker.setDisable(false);
        }
    }

    // handle statistics event
    @FXML
    void revenueStatisticsAction() {
        ObservableList<ServiceDisplayOnTable> data;
        List<ServiceDisplayOnTable> serviceDisplayOnTableData = ServiceDisplayOnTableDAO.getData();
        String empName = employeeNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()) {
            data = getDataToTableViewByYearOption(serviceDisplayOnTableData, empName);
            showDataToTableView(data);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToChartView(true);
        } else {
            data = getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
            setTotalMoney(String.valueOf(caculateTotalMoney(data)));
            showDataToTableView(data);
            showDataToChartView(false);
        }
    }

    // filter data by year and employee name and show to table
    private ObservableList<ServiceDisplayOnTable> getDataToTableViewByYearOption(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName) {
        int year = yearsCombobox.getSelectionModel().getSelectedItem();
        ObservableList<ServiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        serviceDisplayOnTableData.stream()
                .filter(s -> s.getDateAdded().getYear() == year)
                .filter(s -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || s.getEmployeeName().equalsIgnoreCase(empName))
                .forEach(filteredData::add);
        return filteredData;
    }

    // filter data by daterange and employee name and show to table
    private ObservableList<ServiceDisplayOnTable> getDataToTableViewByDateRangeOption(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName) {
        LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<ServiceDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            serviceDisplayOnTableData.stream()
                    .filter(i -> i.getDateAdded().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        } else {
            serviceDisplayOnTableData.stream()
                    .filter(i -> (i.getDateAdded().isAfter(startDate) || i.getDateAdded().isEqual(startDate))
                            && (i.getDateAdded().isBefore(endDate) || i.getDateAdded().isEqual(endDate)))
                    .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
        }
        return filteredData;
    }

    // if flat equal TRUE, that means statistics by YEAR
    // if flat equal FALSE, that means statistics by DATE RANGE PICKER
    private void showDataToChartView(boolean flat) {
        List<ServiceDisplayOnTable> serviceDisplayOnTableData = ServiceDisplayOnTableDAO.getData();
        String empName = employeeNameCombobox.getValue();
        invoiceDataBarChart.setTitle(CHART_TITLE);
        invoiceDataBarChart.getData().clear();
        if(flat) invoiceDataBarChart.getData().add(getDataByYear(serviceDisplayOnTableData, empName));
        else invoiceDataBarChart.getData().add(getDataByDateRange(serviceDisplayOnTableData, empName));
    }

    private XYChart.Series<String, Double> getDataByYear(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName){
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        for (int i = 1; i <= Month.values().length; i++) {
            String month = iuh.fit.models.enums.Month
                    .valueOf(Arrays.stream(iuh.fit.models.enums.Month.values())
                            .toList().get(i - 1)
                            .toString()).getName();
            double netDueOfMonth = getAvgNetDueByMonthOfYear(serviceDisplayOnTableData, yearsCombobox.getValue(), i, empName);
            data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        // thong ke cho ngay hien tai
        if(isToday(startDate, endDate)) return getDataForToday(serviceDisplayOnTableData, empName, data);
            // thong ke cho ngay hom truoc
        else if(isYesterDay(startDate, endDate)) return getDataForYesterday(serviceDisplayOnTableData, empName, data);
            // thong ke cho tuan nay
        else if(isThisWeek(startDate, endDate)) return getDataForThisWeek(serviceDisplayOnTableData, empName, data);
            // thong ke cho thang nay
        else if(isThisMonth(startDate, endDate)) return getDataForThisMonth(serviceDisplayOnTableData, empName, data);
            // thong ke cho thang truoc
        else if(isLastMonth(startDate, endDate)) return getDataForLastMonth(serviceDisplayOnTableData, empName, data);
            // thong ke cho khoang thoi gian cu the
        else return getDataForAnyTime(serviceDisplayOnTableData, empName, data);
    }

    private XYChart.Series<String, Double> getDataForToday(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().toLocalDate().equals(LocalDate.now()))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmployeeName().equalsIgnoreCase(empName)))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(serviceTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForYesterday(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        double netDueAve = serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmployeeName().equalsIgnoreCase(empName)))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(serviceTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        showDataToTableView(getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisWeek(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        LocalDateTime monday = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0, 0);
        LocalDateTime sunday = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        ObservableList<ServiceDisplayOnTable> serviceOfWeek = FXCollections.observableArrayList();
        serviceDisplayOnTableData.stream()
                .filter(i -> (i.getDateAdded().isAfter(monday) || i.getDateAdded().isEqual(monday))
                        && (i.getDateAdded().isBefore(sunday) || i.getDateAdded().isEqual(sunday)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                .forEach(serviceOfWeek::add);

        for (ServiceDisplayOnTable i : serviceOfWeek) {
            data.getData().add(new XYChart.Data<>(
                    i.getDateAdded().toLocalDate().toString(),
                    getNetDueAvgOfDay(serviceOfWeek, i.getDateAdded().toLocalDate())
            ));
        }
        showDataToTableView(getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName));
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisMonth(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<ServiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        serviceDisplayOnTableData.stream()
                .filter(i -> (i.getDateAdded().isAfter(firstDayOfMonth) || i.getDateAdded().isEqual(firstDayOfMonth))
                        && (i.getDateAdded().isBefore(lastDayOfMonth) || i.getDateAdded().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (ServiceDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getDateAdded().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getDateAdded().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String,Double> getDataForLastMonth(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<ServiceDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);

        serviceDisplayOnTableData.stream()
                .filter(i -> (i.getDateAdded().isAfter(firstDayOfMonth) || i.getDateAdded().isEqual(firstDayOfMonth))
                        && (i.getDateAdded().isBefore(lastDayOfMonth) || i.getDateAdded().isEqual(lastDayOfMonth)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                .forEach(invoiceOfMonth::add);

        for (ServiceDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getDateAdded().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfMonth, i.getDateAdded().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataForAnyTime(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        ObservableList<ServiceDisplayOnTable> invoiceOfRange = FXCollections.observableArrayList();
        LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        serviceDisplayOnTableData.stream()
                .filter(i -> (i.getDateAdded().isAfter(startDate) || i.getDateAdded().isEqual(startDate))
                        && (i.getDateAdded().isBefore(endDate) || i.getDateAdded().isEqual(endDate)))
                .filter(i -> (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName)))
                .forEach(invoiceOfRange::add);
        for(ServiceDisplayOnTable i : invoiceOfRange){
            data.getData().add(new XYChart.Data<>(i.getDateAdded().toLocalDate().toString(),
                    getNetDueAvgOfDay(invoiceOfRange, i.getDateAdded().toLocalDate())));
        }
        return data;
    }

    private double getNetDueAvgOfDay(ObservableList<ServiceDisplayOnTable> serviceDisplayOnTableData, LocalDate date){
        return serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().toLocalDate().equals(date))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
    }

    private double getAvgNetDueByMonthOfYear(List<ServiceDisplayOnTable> serviceDisplayOnTableData, int year, int month, String empName) {
        return serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().getYear() == year)
                .filter(i -> i.getDateAdded().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmployeeName().equalsIgnoreCase(empName)))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
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
        List<ServiceDisplayOnTable> serviceDisplayOnTableData = ServiceDisplayOnTableDAO.getData();
        serviceTabDateRangePicker.setValue(new DateRange(LocalDate.now()));
        showDataToTableView(FXCollections.observableArrayList(serviceDisplayOnTableData));
        yearsCombobox.setValue(LocalDate.now().getYear());
        employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
        serviceCategoryNameCombobox.setValue(NONE_VALUE_SERVICE_CATEGORY);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(serviceDisplayOnTableData))));
        setTotalMoney(String.valueOf(caculateTotalMoney(FXCollections.observableArrayList(serviceDisplayOnTableData))));
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    @FXML
//    void exportExcelFile() throws IOException {
//        boolean forEmployee = employeeNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME);
//        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
//        int numOfInvoice = getNumOfInvoice(serviceDataTableView.getItems());
//        double totalMoney = caculateTotalMoney(serviceDataTableView.getItems());
//        if(yearCBBChecked){
//            ExportFileHelper.exportExcelFile(invoiceDataTableView,
//                    ExportExcelCategory.ALL_OF_YEAR,
//                    forEmployee,
//                    serviceTabDateRangePicker.getValue(),
//                    numOfInvoice, totalMoney);
//        } else{
//            LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
//            LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
//            if(isAMonth(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.ALL_OF_MONTH,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isADay(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.DAY_OF_MONTH,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isManyYear(startDate, endDate))
//                ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.MANY_YEAR,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else ExportFileHelper.exportExcelFile(
//                        invoiceDataTableView,
//                        ExportExcelCategory.DATE_RANGE,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
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
