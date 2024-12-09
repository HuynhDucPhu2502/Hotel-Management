package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.InvoiceDisplayOnTableDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.EditDateRangePicker;
import iuh.fit.utils.ExportFileHelper;
import iuh.fit.utils.QuarterChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    @FXML private ComboBox<String> yearsCombobox;
    @FXML private ComboBox<String> quarterCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private CheckBox filterAllTheTimeCheckbox;
    @FXML private DateRangePicker invoiceTabDateRangePicker;
    @FXML private ComboBox<String> employeeNameCombobox;
    @FXML private BarChart<String, Double> invoiceDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text depositText;
    @FXML private Text serviceChargeText;
    @FXML private Text roomChargeText;
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
    private final List<InvoiceDisplayOnTable> invoiceDisplayOnTableData = InvoiceDisplayOnTableDAO.getDataThreeYearsLatest();

    // data of all the time
    private final List<InvoiceDisplayOnTable> allOfData = new ArrayList<>();

    // current data, it shows on table view every time statistics
    private List<InvoiceDisplayOnTable> currentData = new ArrayList<>();

    // curtain all of years
    public static final List<String> allOfYears = new ArrayList<>();

    // limit of data rows per page on table view
    private static final int ROW_PER_PAGE = 12;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EditDateRangePicker.editDateRangePicker(invoiceTabDateRangePicker);
        invoiceDataTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
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
        ObservableList<InvoiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        hideOrShowComponents();
        data = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
        currentData = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        setDeposit(formatCurrency(calculateDeposit(data)));
        setServiceCharge(formatCurrency(calculateServiceCharge(data)));
        setRoomCharge(formatCurrency(calculateRoomCharge(data)));
    }

    // handle event for quarter filter
    @FXML
    void statisticByQuarter() {
        ObservableList<InvoiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        data = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
        currentData = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        setDeposit(formatCurrency(calculateDeposit(data)));
        setServiceCharge(formatCurrency(calculateServiceCharge(data)));
        setRoomCharge(formatCurrency(calculateRoomCharge(data)));
    }

    // handle event for employee filter
    @FXML
    void statisticByEmployeeName() {
        ObservableList<InvoiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()){
            data = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
            currentData = getDataToTableViewByYearOption(this.invoiceDisplayOnTableData, empName);
            updateOnTable(0);
            showDataToChartView(0);
        } else if (filterAllTheTimeCheckbox.isSelected()){
            data = getDataToTableViewAllOfTime(empName);
            currentData = getDataToTableViewAllOfTime(empName);
            updateOnTable(0);
            showDataToChartView(2);
        } else {
            data = getDataToTableViewByDateRangeOption(this.invoiceDisplayOnTableData, empName);
            currentData = getDataToTableViewByDateRangeOption(this.invoiceDisplayOnTableData, empName);
            updateOnTable(0);
            showDataToChartView(1);
        }
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        setDeposit(formatCurrency(calculateDeposit(data)));
        setServiceCharge(formatCurrency(calculateServiceCharge(data)));
        setRoomCharge(formatCurrency(calculateRoomCharge(data)));
    }

    // handle event for statistic all the time
    @FXML
    void statisticAllTheTime() {
        if (isGetALl()) {
            if(allOfData.isEmpty()){
                allOfData.addAll(InvoiceDisplayOnTableDAO.getAllData());
            }
            currentData = allOfData;
            updateOnTable(0);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(currentData))));
            setTotalMoney(formatCurrency(calculateTotalMoney(FXCollections.observableArrayList(currentData))));
            setDeposit(formatCurrency(calculateDeposit(FXCollections.observableArrayList(currentData))));
            setServiceCharge(formatCurrency(calculateServiceCharge(FXCollections.observableArrayList(currentData))));
            setRoomCharge(formatCurrency(calculateRoomCharge(FXCollections.observableArrayList(currentData))));
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
            invoiceTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(true);
            filterByYearCheckBox.setSelected(false);
        }else {
            statisticAllTheTimeButton.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
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
            invoiceTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(false);
            filterAllTheTimeCheckbox.setSelected(false);
            quarterCombobox.setDisable(true);
            employeeNameCombobox.setDisable(true);
            statisticByYear();
        } else {
            yearsCombobox.setDisable(true);
            quarterCombobox.setDisable(true);
            invoiceTabDateRangePicker.setDisable(false);
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
        invoiceTabDateRangePicker.setValue(new DateRange("Hôm nay", LocalDate.now()));
        yearsCombobox.setValue(NONE_VALUE_YEAR);
        employeeNameCombobox.setValue(NONE_VALUE_EMPLOYEE_NAME);
        quarterCombobox.setValue(NONE_VALUE_QUARTER);
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
        if(!filterByYearCheckBox.isSelected() && !filterAllTheTimeCheckbox.isSelected())
            statisByDateRangeOption();
    }

    @FXML
    void exportExcelFile() {
        TableView<InvoiceDisplayOnTable> clone = cloneTableView(invoiceDataTableView);
        clone.getItems().setAll(currentData);
        if (clone.getItems().isEmpty()){
            showMessages("Cảnh báo",
                    "Không có dữ liệu để xuất file excel!!!",
                    "Hãy chọn OK để để hủy.",
                    Alert.AlertType.WARNING);
            return;
        }

        boolean forEmployee = employeeNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME);
        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
        boolean allOfTimeChecked = filterAllTheTimeCheckbox.isSelected();
        int numOfInvoice = getNumOfInvoice(FXCollections.observableArrayList(currentData));
        double totalMoney = calculateTotalMoney(FXCollections.observableArrayList(currentData));
        if(yearCBBChecked && quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
            ExportFileHelper.exportInvoiceExcelFile(
                    clone,
                    ExportExcelCategory.ALL_OF_YEAR,
                    forEmployee,
                    invoiceTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else if(yearCBBChecked && !quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
            ExportFileHelper.exportInvoiceExcelFile(
                    clone,
                    ExportExcelCategory.QUARTER,
                    forEmployee,
                    invoiceTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else if(allOfTimeChecked){
            ExportFileHelper.exportInvoiceExcelFile(
                    clone,
                    ExportExcelCategory.ALL_OF_TIME,
                    forEmployee,
                    invoiceTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else {
            LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
            LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
            if(isAMonth(startDate, endDate))
                ExportFileHelper.exportInvoiceExcelFile(
                        clone,
                        ExportExcelCategory.ALL_OF_MONTH,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isADay(startDate, endDate))
                ExportFileHelper.exportInvoiceExcelFile(
                        clone,
                        ExportExcelCategory.DAY_OF_MONTH,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isManyYear(startDate, endDate))
                ExportFileHelper.exportInvoiceExcelFile(
                        clone,
                        ExportExcelCategory.MANY_YEAR,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else ExportFileHelper.exportInvoiceExcelFile(
                        clone,
                        ExportExcelCategory.DATE_RANGE,
                        forEmployee,
                        invoiceTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
        }
    }


    @FXML
    void showFileAddress() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/statistics/FileAddress.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        FileAddressController fileAddressController = fxmlLoader.getController();

        fileAddressController.initialize(PreferencesKey.EXPORT_INVOICE_STATISTIC);

        Stage stage = new Stage();
        stage.setScene(scene);

        fileAddressController.setStage(stage);
        fileAddressController.setPreferencesKey(PreferencesKey.EXPORT_INVOICE_STATISTIC);

        stage.show();
    }

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
        List<InvoiceDisplayOnTable> data = currentData.subList(from, to);

        // Set place order of table view
        invoiceDataTableView.setPlaceholder(new Label("Không có dữ liệu"));

        // Set data on columns
        invoiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("empName"));

        // Format date column
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy", new Locale("vi", "VN"));
        invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        invoiceDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        // Format currency columns (similar to the previous step)
        depositColumn.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        depositColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        serviceChargeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCharge"));
        serviceChargeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        roomChargeColumn.setCellValueFactory(new PropertyValueFactory<>("roomCharge"));
        roomChargeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        netDueColumn.setCellValueFactory(new PropertyValueFactory<>("netDue"));
        netDueColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        // Set data on table
        invoiceDataTableView.getItems().setAll(data);
    }

    // set action for date range picker
    private void dateRangeAction(){
        invoiceTabDateRangePicker.valueProperty().addListener((obs, oldRange, newRange) -> {
            if (newRange != null) statisByDateRangeOption();
        });

    }

    private void statisByDateRangeOption(){
        ObservableList<InvoiceDisplayOnTable> data;
        String empName = employeeNameCombobox.getValue();
        data = getDataToTableViewByDateRangeOption(this.invoiceDisplayOnTableData, empName);
        currentData = getDataToTableViewByDateRangeOption(this.invoiceDisplayOnTableData, empName);
        updateOnTable(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
        setDeposit(formatCurrency(calculateDeposit(data)));
        setServiceCharge(formatCurrency(calculateServiceCharge(data)));
        setRoomCharge(formatCurrency(calculateRoomCharge(data)));
        showDataToChartView(1);
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
    private ObservableList<InvoiceDisplayOnTable> getDataToTableViewByYearOption(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName) {
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            invoiceDisplayOnTableData.stream()
                    .filter(i -> i.getCreateDate().getYear() == Integer.parseInt(yearsCombobox.getSelectionModel().getSelectedItem()))
                    .filter(i -> quarterCombobox.getSelectionModel().getSelectedItem()
                            .equalsIgnoreCase(NONE_VALUE_QUARTER)
                            || QuarterChecker.isQuarter(i.getCreateDate(),
                            quarterCombobox.getValue(),
                            quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)))
                    .filter(i -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
        return filteredData;
    }

    // get data of all the time and show to table view
    private ObservableList<InvoiceDisplayOnTable> getDataToTableViewAllOfTime(String empName) {
        ObservableList<InvoiceDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            allOfData.stream()
                    .filter(i -> empName.equals(NONE_VALUE_EMPLOYEE_NAME) || i.getEmpName().equalsIgnoreCase(empName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
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

    // if flat equal 0, that means statistics by YEAR
    // if flat equal 1, that means statistics by DATE RANGE PICKER
    // if flat equal 2, that means statistics all of time
    private void showDataToChartView(int flat) {
        String empName = employeeNameCombobox.getValue();
        invoiceDataBarChart.getXAxis().setLabel(getChartTitle());
        invoiceDataBarChart.getYAxis().setLabel("Tiền (NVĐ)");
        invoiceDataBarChart.getData().clear();
        if(flat == 0) invoiceDataBarChart.getData().add(getDataByYear(this.invoiceDisplayOnTableData, empName));
        else if(flat == 1) invoiceDataBarChart.getData().add(getDataByDateRange(this.invoiceDisplayOnTableData, empName));
        else if (flat == 2)invoiceDataBarChart.getData().add(getDataForAllOfTime(empName));
        else throw new IllegalArgumentException("Errors flat for statistic");
    }

    // get data of all the time to show on bar chart
    private XYChart.Series<String, Double> getDataForAllOfTime(String employeeName){
        allOfYears.clear();
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");

        Map<String, Double> hashTable = allOfData.stream()
                .filter(i -> employeeName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME)
                        || i.getEmpName().equalsIgnoreCase(employeeName))
                        .collect(Collectors.groupingBy(
                                i -> String.valueOf(i.getCreateDate().getYear()),
                                Collectors.summingDouble(InvoiceDisplayOnTable::getNetDue)
                        ));

        Map<String, Double> sortedHashTable = new TreeMap<>(hashTable);
        sortedHashTable.forEach((year, totalMoney) ->{
            data.getData().add(new XYChart.Data<>(year, totalMoney));
            allOfYears.add(year);
        });
        return data;
    }

    // same as above
    private XYChart.Series<String, Double> getDataByYear(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName){
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
                double netDueOfMonth = getNetDueByMonthOfYear(invoiceDisplayOnTableData, Integer.parseInt(yearsCombobox.getValue()), i, empName);
                data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
            }
        }catch (Exception ignored){

        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, String empName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");
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
        currentData = getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName);
        updateOnTable(0);
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
        currentData = getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName);
        updateOnTable(0);
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
                    getNetDueOfDay(invoiceOfWeek, i.getCreateDate().toLocalDate())
            ));
        }
        currentData = getDataToTableViewByDateRangeOption(invoiceDisplayOnTableData, empName);
        updateOnTable(0);
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
                    getNetDueOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())
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
                    getNetDueOfDay(invoiceOfMonth, i.getCreateDate().toLocalDate())
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
                    getNetDueOfDay(invoiceOfRange, i.getCreateDate().toLocalDate())));
        }
        return data;
    }

    private double getNetDueOfDay(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData, LocalDate date){
        return invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().toLocalDate().equals(date))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
    }

    private double getNetDueByMonthOfYear(List<InvoiceDisplayOnTable> invoiceDisplayOnTableData, int year, int month, String empName) {
        return invoiceDisplayOnTableData.stream()
                .filter(i -> i.getCreateDate().getYear() == year)
                .filter(i -> i.getCreateDate().getMonthValue() == month
                        && (empName.equalsIgnoreCase(NONE_VALUE_EMPLOYEE_NAME) ||
                        i.getEmpName().equalsIgnoreCase(empName)))
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
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
        LocalDateTime startDate = invoiceTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = invoiceTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
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
            if(allOfYears.isEmpty())
                return "Không có dữ liệu" + employeeTitle;
            if(allOfYears.size() == 1)
                return baseTitle + " toàn bộ từ năm " + allOfYears.getFirst()  + employeeTitle;
            else
                return baseTitle + " toàn bộ từ năm " + allOfYears.getFirst() + " đến năm " + allOfYears.getLast()  + employeeTitle;
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

    public TableView<InvoiceDisplayOnTable> cloneTableView(TableView<InvoiceDisplayOnTable> originalTableView) {

        TableView<InvoiceDisplayOnTable> clonedTableView = new TableView<>();

        for (TableColumn<InvoiceDisplayOnTable, ?> column : originalTableView.getColumns()) {
            TableColumn<InvoiceDisplayOnTable, ?> clonedColumn = new TableColumn<>(column.getText());
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

    private int getNumOfInvoice(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.size();
    }

    // show total money of invoice on text
    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double calculateTotalMoney(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.stream()
                .mapToDouble(InvoiceDisplayOnTable::getNetDue)
                .sum();
    }

    private void setDeposit(String totalDeposit){
        depositText.setText(totalDeposit);
    }

    private double calculateDeposit(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.stream()
                .mapToDouble(InvoiceDisplayOnTable::getDeposit)
                .sum();
    }

    private void setServiceCharge(String totalServiceCharge){
        serviceChargeText.setText(totalServiceCharge);
    }

    private double calculateServiceCharge(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.stream()
                .mapToDouble(InvoiceDisplayOnTable::getServiceCharge)
                .sum();
    }

    private void setRoomCharge(String totalRoomCharge){
        roomChargeText.setText(totalRoomCharge);
    }

    private double calculateRoomCharge(ObservableList<InvoiceDisplayOnTable> invoiceDisplayOnTableData){
        return invoiceDisplayOnTableData.stream()
                .mapToDouble(InvoiceDisplayOnTable::getRoomCharge)
                .sum();
    }
}
