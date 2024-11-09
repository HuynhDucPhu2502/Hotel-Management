package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ServiceDisplayOnTableDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.wrapper.ServiceDisplayOnTable;
import iuh.fit.utils.EditDateRangePicker;
import iuh.fit.utils.QuarterChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceRevenueStatisticsTabController implements Initializable {

    public ToggleGroup showDataViewToggleGroup;

    // Variables for revenue statistics view components
    @FXML private TableView<ServiceDisplayOnTable> serviceDataTableView;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceIDColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> serviceCategoryNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, String> employeeNameColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, LocalDateTime> dateAddedColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Integer> quantityColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Double> unitPriceColumn;
    @FXML private TableColumn<ServiceDisplayOnTable, Double> totalMoneyColumn;

    @FXML private ComboBox<String> yearsCombobox;
    @FXML private ComboBox<String> serviceCategoryNameCombobox;
    @FXML private ComboBox<String> quarterCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private CheckBox filterAllTheTimeCheckbox;
    @FXML private DateRangePicker serviceTabDateRangePicker;
    @FXML private ComboBox<String> employeeNameCombobox;
    @FXML private BarChart<String, Double> invoiceDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text numOfInvoiceText;
    @FXML private Pagination invoicePagination;
    @FXML private Button statisticAllTheTimeButton;

    // limits of years that show on combobox year
    private static final int COMBO_YEAR_CAPACITY = 3;

    // none value of comboboxes
    private static final String NONE_VALUE_EMPLOYEE_NAME = "--Chọn nhân viên--";
    private static final String NONE_VALUE_YEAR = "--Năm--";
    private static final String NONE_VALUE_QUARTER = "--Quý--";

    // data of 3 years
    private final List<ServiceDisplayOnTable> serviceDisplayOnTableData = ServiceDisplayOnTableDAO.getDataThreeYearsLatest();

    // data of all the time
    private final List<ServiceDisplayOnTable> allOfData = new ArrayList<>();

    // current data, it shows on table view every time statistics
    private List<ServiceDisplayOnTable> currentData = new ArrayList<>();

    // curtain all of years
    public static final List<String> allOfYears = new ArrayList<>();

    // limit of data rows per page on table view
    private static final int ROW_PER_PAGE = 12;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EditDateRangePicker.editDateRangePicker(serviceTabDateRangePicker);
        serviceDataTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        loadDataToEmployeeNameCombobox();
        loadDataToComboboxOfYear();
        loadDataToComboboxOfQuarter();
        dateRangeAction();
        statisByDateRangeOption();
        paginationOnAction();
    }

    // handle event for year filter
    @FXML
    void statisticByYear() {
        ObservableList<ServiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        hideOrShowComponents();
        data = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
        currentData = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    // handle event for quarter filter
    @FXML
    void statisticByQuarter() {
        ObservableList<ServiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        data = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
        currentData = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    // handle event for employee filter
    @FXML
    void statisticByEmployeeName() {
        ObservableList<ServiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()){
            data = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
            currentData = getDataToTableViewByYearOption(this.serviceDisplayOnTableData, empName);
            updateOnTable(0);
            showDataToChartView(0);
        } else if (filterAllTheTimeCheckbox.isSelected()){
            data = getDataToTableViewAllOfTime(empName);
            currentData = getDataToTableViewAllOfTime(empName);
            updateOnTable(0);
            showDataToChartView(2);
        } else {
            data = getDataToTableViewByDateRangeOption(this.serviceDisplayOnTableData, empName);
            currentData = getDataToTableViewByDateRangeOption(this.serviceDisplayOnTableData, empName);
            updateOnTable(0);
            showDataToChartView(1);
        }
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    // handle event for statistic all the time
    @FXML
    void statisticAllTheTime() {
        if (isGetALl()) {
            if(allOfData.isEmpty()){
                allOfData.addAll(ServiceDisplayOnTableDAO.getAllData());
            }
            currentData = allOfData;
            updateOnTable(0);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(currentData))));
            setTotalMoney(formatCurrency(calculateTotalMoney(FXCollections.observableArrayList(currentData))));
            employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
            showDataToChartView(2);
        }
    }

    // handle event switch UI when statistic all the time checkbox is selected
    @FXML
    void statisticAllTheTimeChecked() {
        if(filterAllTheTimeCheckbox.isSelected()){
            yearsCombobox.setValue(NONE_VALUE_YEAR);
            quarterCombobox.setValue(NONE_VALUE_QUARTER);
            employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
            employeeNameCombobox.setDisable(false);
            statisticAllTheTimeButton.setDisable(false);
            serviceTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(true);
            filterByYearCheckBox.setSelected(false);
        }else {
            statisticAllTheTimeButton.setDisable(true);
            serviceTabDateRangePicker.setDisable(false);
            statisByDateRangeOption();
        }
    }

    // handle event switch UI when statistic by year checkbox is selected
    @FXML
    void statisticbyYearChecked() {
        if (filterByYearCheckBox.isSelected()) {
            yearsCombobox.setValue(NONE_VALUE_YEAR);
            quarterCombobox.setValue(NONE_VALUE_QUARTER);
            employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
            statisticAllTheTimeButton.setDisable(true);
            serviceTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(false);
            filterAllTheTimeCheckbox.setSelected(false);
            quarterCombobox.setDisable(true);
            employeeNameCombobox.setDisable(true);
            statisticByYear();
        } else {
            yearsCombobox.setDisable(true);
            quarterCombobox.setDisable(true);
            serviceTabDateRangePicker.setDisable(false);
            employeeNameCombobox.setDisable(false);
            statisByDateRangeOption();
        }
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

    @FXML
    void refreshData() {
        serviceTabDateRangePicker.setValue(new DateRange("Hôm nay", LocalDate.now()));
        yearsCombobox.setValue(NONE_VALUE_YEAR);
        employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
        quarterCombobox.setValue(NONE_VALUE_QUARTER);
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
        if(!filterByYearCheckBox.isSelected() && !filterAllTheTimeCheckbox.isSelected())
            statisByDateRangeOption();
    }

    @FXML
//    void exportExcelFile() {
//        TableView<ServiceDisplayOnTable> clone = cloneTableView(serviceDataTableView);
//        clone.getItems().setAll(currentData);
//        if (clone.getItems().isEmpty()){
//            showMessages("Cảnh báo",
//                    "Không có dữ liệu để xuất file excel!!!",
//                    "Hãy chọn OK để để hủy.",
//                    Alert.AlertType.WARNING);
//            return;
//        }
//
//        boolean forEmployee = employeeNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME);
//        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
//        boolean allOfTimeChecked = filterAllTheTimeCheckbox.isSelected();
//        int numOfInvoice = getNumOfInvoice(FXCollections.observableArrayList(currentData));
//        double totalMoney = calculateTotalMoney(FXCollections.observableArrayList(currentData));
//        if(yearCBBChecked && quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
//            ExportFileHelper.exportServiceExcelFile(
//                    clone,
//                    ExportExcelCategory.ALL_OF_YEAR,
//                    forEmployee,
//                    serviceTabDateRangePicker.getValue(),
//                    numOfInvoice, totalMoney);
//        } else if(yearCBBChecked && !quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
//            ExportFileHelper.exportServiceExcelFile(
//                    clone,
//                    ExportExcelCategory.QUARTER,
//                    forEmployee,
//                    serviceTabDateRangePicker.getValue(),
//                    numOfInvoice, totalMoney);
//        } else if(allOfTimeChecked){
//            ExportFileHelper.exportServiceExcelFile(
//                    clone,
//                    ExportExcelCategory.ALL_OF_TIME,
//                    forEmployee,
//                    serviceTabDateRangePicker.getValue(),
//                    numOfInvoice, totalMoney);
//        } else {
//            LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
//            LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
//            if(isAMonth(startDate, endDate))
//                ExportFileHelper.exportServiceExcelFile(
//                        clone,
//                        ExportExcelCategory.ALL_OF_MONTH,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isADay(startDate, endDate))
//                ExportFileHelper.exportServiceExcelFile(
//                        clone,
//                        ExportExcelCategory.DAY_OF_MONTH,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else if(isManyYear(startDate, endDate))
//                ExportFileHelper.exportServiceExcelFile(
//                        clone,
//                        ExportExcelCategory.MANY_YEAR,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//            else ExportFileHelper.exportServiceExcelFile(
//                        clone,
//                        ExportExcelCategory.DATE_RANGE,
//                        forEmployee,
//                        serviceTabDateRangePicker.getValue(),
//                        numOfInvoice,
//                        totalMoney);
//        }
//    }

    // set action for pagination page, change data on table when choose another page
    private void paginationOnAction(){
        invoicePagination.setCurrentPageIndex(0);
        invoicePagination.currentPageIndexProperty().addListener(
                (observable, oldIndex, newIndex)-> updateOnTable(newIndex.intValue())
        );
        updateOnTable(0);
    }

    // set current data for table view every time get new statistics
    private void updateOnTable(int pageIndex) {
        int numOfPage = (int) Math.ceil((double) currentData.size() / ROW_PER_PAGE) > 0
                ? (int) Math.ceil((double) currentData.size() / ROW_PER_PAGE)
                : (int) Math.ceil((double) currentData.size() / ROW_PER_PAGE) + 1;
        invoicePagination.setPageCount(numOfPage);

        int from = pageIndex * ROW_PER_PAGE;
        int to = Math.min(from + ROW_PER_PAGE, currentData.size());
        List<ServiceDisplayOnTable> data = currentData.subList(from, to);

        // Set place order of table view
        serviceDataTableView.setPlaceholder(new Label("Không có dữ liệu"));

        // Set data on columns
        serviceIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategory"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("employeeName"));

        // Format date column
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy", new Locale("vi", "VN"));
        dateAddedColumn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        dateAddedColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Format currency columns (similar to the previous step)
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        unitPriceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        totalMoneyColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        // Set data on table
        serviceDataTableView.getItems().setAll(data);
    }

    // set action for date range picker
    private void dateRangeAction(){
        serviceTabDateRangePicker.valueProperty().addListener((obs, oldRange, newRange) -> {
            if (newRange != null) statisByDateRangeOption();
        });

    }

    private void statisByDateRangeOption(){
        ObservableList<ServiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        data = getDataToTableViewByDateRangeOption(this.serviceDisplayOnTableData, empName);
        currentData = getDataToTableViewByDateRangeOption(this.serviceDisplayOnTableData, empName);
        updateOnTable(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        showDataToChartView(1);
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
    }

    // check if user want to statistic all the time
    private boolean isGetALl(){
        if(allOfData.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn muốn thực hiện hành động này không?");
            alert.setContentText("Hãy chọn OK để tiếp tục, hoặc Cancel để hủy.");

            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }
        return true;
    }

    // filter data by year and employee name and show to table
    private ObservableList<ServiceDisplayOnTable> getDataToTableViewByYearOption(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName) {
        ObservableList<ServiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            serviceDisplayOnTableData.stream()
                    .filter(i -> i.getDateAdded().getYear() == Integer.parseInt(yearsCombobox.getSelectionModel().getSelectedItem()))
                    .filter(i -> quarterCombobox.getSelectionModel().getSelectedItem()
                            .equalsIgnoreCase(NONE_VALUE_QUARTER)
                            || QuarterChecker.isQuarter(i.getDateAdded(),
                            quarterCombobox.getValue(),
                            quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)))
                    .filter(i -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
        return filteredData;
    }

    // get data of all the time and show to table view
    private ObservableList<ServiceDisplayOnTable> getDataToTableViewAllOfTime(String empName) {
        ObservableList<ServiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            allOfData.stream()
                    .filter(i -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
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

    // if flat equal 0, that means statistics by YEAR
    // if flat equal 1, that means statistics by DATE RANGE PICKER
    // if flat equal 2, that means statistics all of time
    private void showDataToChartView(int flat) {
        String empName = employeeNameCombobox.getValue();
        invoiceDataBarChart.getXAxis().setLabel("Mốc thời gian");
        invoiceDataBarChart.getYAxis().setLabel("Tiền (NVĐ)");
        invoiceDataBarChart.getData().clear();
        if(flat == 0) invoiceDataBarChart.getData().add(getDataByYear(this.serviceDisplayOnTableData, empName));
        else if(flat == 1) invoiceDataBarChart.getData().add(getDataByDateRange(this.serviceDisplayOnTableData, empName));
        else if (flat == 2)invoiceDataBarChart.getData().add(getDataForAllOfTime(empName));
        else throw new IllegalArgumentException("Errors flat for statistic");

        invoiceDataBarChart.setTitle(getChartTitle());
    }

    // get data of all the time to show on bar chart
    private XYChart.Series<String, Double> getDataForAllOfTime(String employeeName){
        allOfYears.clear();
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");

        Map<String, Double> hashTable = allOfData.stream()
                .filter(i -> employeeName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME)
                        || i.getEmployeeName().equalsIgnoreCase(employeeName))
                .collect(Collectors.groupingBy(
                        i -> String.valueOf(i.getDateAdded().getYear()),
                        Collectors.summingDouble(ServiceDisplayOnTable::getTotalMoney)
                ));

        Map<String, Double> sortedHashTable = new TreeMap<>(hashTable);
        sortedHashTable.forEach((year, totalMoney) ->{
            data.getData().add(new XYChart.Data<>(year, totalMoney));
            allOfYears.add(year);
        });
        return data;
    }

    // same as above
    private XYChart.Series<String, Double> getDataByYear(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName){
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");
        try{
            for (int i = 1; i <= Month.values().length; i++) {
                if(!QuarterChecker.isQuarter(i,
                        quarterCombobox.getValue(),
                        quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER))) continue;
                String month = iuh.fit.models.enums.Month
                        .valueOf(Arrays.stream(iuh.fit.models.enums.Month.values())
                                .toList().get(i - 1)
                                .toString()).getName();
                double netDueOfMonth = getNetDueByMonthOfYear(serviceDisplayOnTableData, Integer.parseInt(yearsCombobox.getValue()), i, empName);
                data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
            }
        }catch (Exception ignored){

        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");
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
        currentData = getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName);
        updateOnTable(0);
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
        currentData = getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName);
        updateOnTable(0);
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisWeek(List<ServiceDisplayOnTable> serviceDisplayOnTableData, String empName, XYChart.Series<String, Double> data) {
        LocalDateTime monday = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0, 0);
        LocalDateTime sunday = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        ObservableList<ServiceDisplayOnTable> invoiceOfWeek = FXCollections.observableArrayList();
        serviceDisplayOnTableData.stream()
                .filter(i -> (i.getDateAdded().isAfter(monday) || i.getDateAdded().isEqual(monday))
                        && (i.getDateAdded().isBefore(sunday) || i.getDateAdded().isEqual(sunday)))
                .filter(i -> empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) || i.getEmployeeName().equalsIgnoreCase(empName))
                .forEach(invoiceOfWeek::add);

        for (ServiceDisplayOnTable i : invoiceOfWeek) {
            data.getData().add(new XYChart.Data<>(
                    i.getDateAdded().toLocalDate().toString(),
                    getNetDueOfDay(invoiceOfWeek, i.getDateAdded().toLocalDate())
            ));
        }
        currentData = getDataToTableViewByDateRangeOption(serviceDisplayOnTableData, empName);
        updateOnTable(0);
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
                    getNetDueOfDay(invoiceOfMonth, i.getDateAdded().toLocalDate())
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
                    getNetDueOfDay(invoiceOfMonth, i.getDateAdded().toLocalDate())
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
                    getNetDueOfDay(invoiceOfRange, i.getDateAdded().toLocalDate())));
        }
        return data;
    }

    private double getNetDueOfDay(ObservableList<ServiceDisplayOnTable> serviceDisplayOnTableData, LocalDate date){
        return serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().toLocalDate().equals(date))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
    }

    private double getNetDueByMonthOfYear(List<ServiceDisplayOnTable> serviceDisplayOnTableData, int year, int month, String empName) {
        return serviceDisplayOnTableData.stream()
                .filter(i -> i.getDateAdded().getYear() == year)
                .filter(i -> i.getDateAdded().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmployeeName().equalsIgnoreCase(empName)))
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
    }

    private void showMessages(String title, String header, String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
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

    // get title for bar chart every time statistic
    private String getChartTitle(){
        LocalDateTime startDate = serviceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = serviceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        String empName = employeeNameCombobox.getSelectionModel().getSelectedItem()
                .equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ? "" : employeeNameCombobox.getSelectionModel().getSelectedItem();
        String baseTitle = "Thống kê ";
        String employeeTitle = empName.isBlank() ? "" : " của nhân viên : " + empName;

        if (filterByYearCheckBox.isSelected()){
            String year = yearsCombobox.getSelectionModel().getSelectedItem()
                    .equalsIgnoreCase(NONE_VALUE_YEAR) ? "" : yearsCombobox.getSelectionModel().getSelectedItem();
            String quarter = quarterCombobox.getSelectionModel().getSelectedItem()
                    .equalsIgnoreCase(NONE_VALUE_QUARTER) ? "" : quarterCombobox.getSelectionModel().getSelectedItem();

            if (year.isBlank() && quarter.isBlank() && empName.isBlank()) {
                return "Chưa chọn thời điểm thống kê";
            } else if (quarter.isBlank()) {
                return baseTitle + "cho năm " + year + employeeTitle;
            } else {
                return baseTitle + "cho năm " + year + ", quý " + quarter + employeeTitle;
            }
        } else if (filterAllTheTimeCheckbox.isSelected()){
            return baseTitle + " toàn bộ từ năm " + allOfYears.getFirst() + " đến năm " + allOfYears.getLast() + employeeTitle;
        } else  {
            if (isToday(startDate, endDate)) {
                return baseTitle + "cho ngày " + startDate.toLocalDate().toString() + employeeTitle;
            } else if (isYesterDay(startDate, endDate)) {
                return baseTitle + "cho ngày " + startDate.toLocalDate().toString() + employeeTitle;
            } else if (isThisWeek(startDate, endDate)) {
                return baseTitle + "cho tuần này, bắt đầu từ " + startDate.toLocalDate().toString() + employeeTitle;
            } else if (isThisMonth(startDate, endDate)) {
                return baseTitle + "cho tháng " + startDate.getMonthValue() + employeeTitle;
            } else if (isLastMonth(startDate, endDate)) {
                return baseTitle + "cho tháng " + startDate.getMonthValue() + employeeTitle;
            } else {
                return baseTitle + "từ ngày " + startDate.toLocalDate().toString()
                        + " đến ngày " + endDate.toLocalDate().toString() + employeeTitle;
            }
        }
    }

    public TableView<ServiceDisplayOnTable> cloneTableView(TableView<ServiceDisplayOnTable> originalTableView) {

        TableView<ServiceDisplayOnTable> clonedTableView = new TableView<>();

        for (TableColumn<ServiceDisplayOnTable, ?> column : originalTableView.getColumns()) {
            TableColumn<ServiceDisplayOnTable, ?> clonedColumn = new TableColumn<>(column.getText());
            clonedColumn.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
            clonedTableView.getColumns().add(clonedColumn);
        }
        return clonedTableView;
    }

    private static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    // if combobox year was selected NONE_VALUE
    // can't choose QUARTER or Employee Name for filter
    private void hideOrShowComponents() {
        if(yearsCombobox.getSelectionModel().getSelectedItem().equalsIgnoreCase(NONE_VALUE_YEAR)){
            quarterCombobox.setDisable(true);
            employeeNameCombobox.setDisable(true);
        }else{
            quarterCombobox.setDisable(false);
            employeeNameCombobox.setDisable(false);
        }
    }

    // set date to combox of year
    private void loadDataToComboboxOfYear() {
        ObservableList<String> years = FXCollections.observableArrayList();
        years.addFirst(NONE_VALUE_YEAR);
        for (int i = 0; i < COMBO_YEAR_CAPACITY; i++)
            years.add(String.valueOf(LocalDate.now().getYear() - i));
        yearsCombobox.setItems(years);
        yearsCombobox.setValue(years.getFirst());
    }

    private void loadDataToComboboxOfQuarter() {
        ObservableList<String> quarter = FXCollections.observableArrayList();
        quarter.addFirst(NONE_VALUE_QUARTER);
        for (int i = 1; i <= 4; i++)
            quarter.add(String.valueOf(i));
        quarterCombobox.setItems(quarter);
        quarterCombobox.setValue(quarter.getFirst());
    }

    // set data to combox of employee name
    private void loadDataToEmployeeNameCombobox() {
        List<Employee> employeeList = EmployeeDAO.getEmployees();
        ObservableList<String> empNames = FXCollections.observableArrayList(NONE_VALUE_EMPLOYEE_NAME);
        employeeList.forEach(e -> empNames.add(e.getFullName()));
        employeeNameCombobox.setItems(empNames);
        employeeNameCombobox.setValue(empNames.getFirst());
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

    private double calculateTotalMoney(ObservableList<ServiceDisplayOnTable> serviceDisplayOnTableData){
        return serviceDisplayOnTableData.stream()
                .mapToDouble(ServiceDisplayOnTable::getTotalMoney)
                .sum();
    }
}
