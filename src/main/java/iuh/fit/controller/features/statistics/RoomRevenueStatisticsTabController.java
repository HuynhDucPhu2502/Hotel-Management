package iuh.fit.controller.features.statistics;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDisplayOnTableDAO;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.ExportExcelCategory;
import iuh.fit.models.wrapper.RoomDisplayOnTable;
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
    @FXML private ComboBox<String> yearsCombobox;
    @FXML private ComboBox<String> quarterCombobox;
    @FXML private RadioButton showTableViewRadioButton;
    @FXML private RadioButton showChartDataRadioButton;
    @FXML private AnchorPane chartViewAnchorPane;
    @FXML private AnchorPane tableViewAnchorPane;
    @FXML private DateRangePicker roomTabDateRangePicker;
    @FXML private ComboBox<String> roomCategoryNameCombobox;
    @FXML private BarChart<String, Double> roomDataBarChart;
    @FXML private Text totalMoneyText;
    @FXML private Text numOfInvoiceText;
    @FXML private CheckBox filterAllTheTimeCheckbox;
    @FXML private CheckBox filterByYearCheckBox;
    @FXML private Button statisticAllTheTimeButton;
    @FXML private Pagination invoicePagination;

    // none value of comboboxes
    private static final String NONE_VALUE_YEAR = "--Năm--";
    private static final String NONE_VALUE_QUARTER = "--Quý--";
    private static final String NONE_VALUE_ROOM_CATEGORY_NAME = "--Chọn loại phòng--";

    // limits of years that show on combobox year
    private static final int COMBO_YEAR_CAPACITY = 3;

    // data of 3 years
    private final List<RoomDisplayOnTable> rooomDisplayOnTableData = RoomDisplayOnTableDAO.getDataThreeYearsLatest();

    // data of all the time
    private final List<RoomDisplayOnTable> allOfData = new ArrayList<>();

    // current data, it shows on table view every time statistics
    private List<RoomDisplayOnTable> currentData = new ArrayList<>();

    // curtain all of years
    public static final List<String> allOfYears = new ArrayList<>();

    // limit of data rows per page on table view
    private static final int ROW_PER_PAGE = 12;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EditDateRangePicker.editDateRangePicker(roomTabDateRangePicker);
        roomDataTableView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        loadDataToRoomCategoryNameCombobox();
        loadDataToComboboxOfYear();
        loadDataToComboboxOfQuarter();
        dateRangeAction();
        statisByDateRangeOption();
        paginationOnAction();
    }

    // handle event for year filter
    @FXML
    void statisticByYear() {
        ObservableList<RoomDisplayOnTable> data;
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        hideOrShowComponents();
        data = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
        currentData = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
    }

    // handle event for quarter filter
    @FXML
    void statisticByQuarter() {
        ObservableList<RoomDisplayOnTable> data;
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        data = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
        currentData = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        showDataToChartView(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
    }

    // handle event for employee filter
    @FXML
    void statisticByRoomCategoryName() {
        ObservableList<RoomDisplayOnTable> data;
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        if (filterByYearCheckBox.isSelected()){
            data = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
            currentData = getDataToTableViewByYearOption(this.rooomDisplayOnTableData, roomCategoryName);
            updateOnTable(0);
            showDataToChartView(0);
        } else if (filterAllTheTimeCheckbox.isSelected()){
            data = getDataToTableViewAllOfTime(roomCategoryName);
            currentData = getDataToTableViewAllOfTime(roomCategoryName);
            updateOnTable(0);
            showDataToChartView(2);
        } else {
            data = getDataToTableViewByDateRangeOption(this.rooomDisplayOnTableData, roomCategoryName);
            currentData = getDataToTableViewByDateRangeOption(this.rooomDisplayOnTableData, roomCategoryName);
            updateOnTable(0);
            showDataToChartView(1);
        }
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
    }

    // handle event for statistic all the time
    @FXML
    void statisticAllTheTime() {
        if (isGetALl()) {
            if(allOfData.isEmpty()){
                allOfData.addAll(RoomDisplayOnTableDAO.getAllData());
            }
            currentData = allOfData;
            updateOnTable(0);
            setNumOfInvoice(String.valueOf(getNumOfInvoice(FXCollections.observableArrayList(currentData))));
            setTotalMoney(formatCurrency(calculateTotalMoney(FXCollections.observableArrayList(currentData))));
            roomCategoryNameCombobox.setValue(NONE_VALUE_ROOM_CATEGORY_NAME);
            showDataToChartView(2);
        }
    }

    // handle event switch UI when statistic all the time checkbox is selected
    @FXML
    void statisticAllTheTimeChecked() {
        if(filterAllTheTimeCheckbox.isSelected()){
            yearsCombobox.setValue(NONE_VALUE_YEAR);
            quarterCombobox.setValue(NONE_VALUE_QUARTER);
            roomCategoryNameCombobox.setValue(NONE_VALUE_ROOM_CATEGORY_NAME);
            roomCategoryNameCombobox.setDisable(false);
            statisticAllTheTimeButton.setDisable(false);
            roomTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(true);
            filterByYearCheckBox.setSelected(false);
        }else {
            statisticAllTheTimeButton.setDisable(true);
            roomTabDateRangePicker.setDisable(false);
            statisByDateRangeOption();
        }
    }

    // handle event switch UI when statistic by year checkbox is selected
    @FXML
    void statisticbyYearChecked() {
        if (filterByYearCheckBox.isSelected()) {
            yearsCombobox.setValue(NONE_VALUE_YEAR);
            quarterCombobox.setValue(NONE_VALUE_QUARTER);
            roomCategoryNameCombobox.setValue(NONE_VALUE_ROOM_CATEGORY_NAME);
            statisticAllTheTimeButton.setDisable(true);
            roomTabDateRangePicker.setDisable(true);
            yearsCombobox.setDisable(false);
            filterAllTheTimeCheckbox.setSelected(false);
            quarterCombobox.setDisable(true);
            roomCategoryNameCombobox.setDisable(true);
            statisticByYear();
        } else {
            yearsCombobox.setDisable(true);
            quarterCombobox.setDisable(true);
            roomTabDateRangePicker.setDisable(false);
            roomCategoryNameCombobox.setDisable(false);
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
        roomTabDateRangePicker.setValue(new DateRange("Hôm nay", LocalDate.now()));
        yearsCombobox.setValue(NONE_VALUE_YEAR);
        roomCategoryNameCombobox.setValue(NONE_VALUE_ROOM_CATEGORY_NAME);
        quarterCombobox.setValue(NONE_VALUE_QUARTER);
        showTableViewRadioButton.setSelected(true);
        switchBetweenTableViewAndChartView();
        if(!filterByYearCheckBox.isSelected() && !filterAllTheTimeCheckbox.isSelected())
            statisByDateRangeOption();
    }

    @FXML
    void showFileAddress() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/statistics/FileAddress.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        FileAddressController fileAddressController = fxmlLoader.getController();

        fileAddressController.initialize(PreferencesKey.EXPORT_ROOM_STATISTIC);

        Stage stage = new Stage();
        stage.setScene(scene);

        fileAddressController.setStage(stage);
        fileAddressController.setPreferencesKey(PreferencesKey.EXPORT_ROOM_STATISTIC);

        stage.show();
    }

    @FXML
    void exportExcelFile() throws IOException {
        TableView<RoomDisplayOnTable> clone = cloneTableView(roomDataTableView);
        clone.getItems().setAll(currentData);
        if (clone.getItems().isEmpty()){
            showMessages("Cảnh báo",
                    "Không có dữ liệu để xuất file excel!!!",
                    "Hãy chọn OK để để hủy.",
                    Alert.AlertType.WARNING);
            return;
        }

        boolean forRoomCategory = roomCategoryNameCombobox.getValue().equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME);
        boolean yearCBBChecked = filterByYearCheckBox.isSelected();
        boolean allOfTimeChecked = filterAllTheTimeCheckbox.isSelected();
        int numOfInvoice = getNumOfInvoice(FXCollections.observableArrayList(currentData));
        double totalMoney = calculateTotalMoney(FXCollections.observableArrayList(currentData));
        if(yearCBBChecked && quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
            ExportFileHelper.exportRoomExcelFile(
                    clone,
                    ExportExcelCategory.ALL_OF_YEAR,
                    forRoomCategory,
                    roomTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else if(yearCBBChecked && !quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)){
            ExportFileHelper.exportRoomExcelFile(
                    clone,
                    ExportExcelCategory.QUARTER,
                    forRoomCategory,
                    roomTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else if(allOfTimeChecked){
            ExportFileHelper.exportRoomExcelFile(
                    clone,
                    ExportExcelCategory.ALL_OF_TIME,
                    forRoomCategory,
                    roomTabDateRangePicker.getValue(),
                    numOfInvoice, totalMoney);
        } else {
            LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
            LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
            if(isAMonth(startDate, endDate))
                ExportFileHelper.exportRoomExcelFile(
                        clone,
                        ExportExcelCategory.ALL_OF_MONTH,
                        forRoomCategory,
                        roomTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isADay(startDate, endDate))
                ExportFileHelper.exportRoomExcelFile(
                        clone,
                        ExportExcelCategory.DAY_OF_MONTH,
                        forRoomCategory,
                        roomTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else if(isManyYear(startDate, endDate))
                ExportFileHelper.exportRoomExcelFile(
                        clone,
                        ExportExcelCategory.MANY_YEAR,
                        forRoomCategory,
                        roomTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
            else ExportFileHelper.exportRoomExcelFile(
                        clone,
                        ExportExcelCategory.DATE_RANGE,
                        forRoomCategory,
                        roomTabDateRangePicker.getValue(),
                        numOfInvoice,
                        totalMoney);
        }
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
        List<RoomDisplayOnTable> data = currentData.subList(from, to);

        // Set place order of table view
        roomDataTableView.setPlaceholder(new Label("Không có dữ liệu"));

        // Set data on columns
        roomIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        roomCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("roomCategory"));
        numOfPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPeople"));

        // Format date column
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy", new Locale("vi", "VN"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        bookingDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        // Format date column
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkInDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        // Format date column
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        checkOutDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(dateFormatter));
            }
        });

        // Format currency columns (similar to the previous step)
        totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalMoney"));
        totalMoneyColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null : formatCurrency(amount));
            }
        });

        // Set data on table
        roomDataTableView.getItems().setAll(data);
    }

    // set action for date range picker
    private void dateRangeAction(){
        roomTabDateRangePicker.valueProperty().addListener((obs, oldRange, newRange) -> {
            if (newRange != null) statisByDateRangeOption();
        });

    }

    private void statisByDateRangeOption(){
        ObservableList<RoomDisplayOnTable> data;
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        data = getDataToTableViewByDateRangeOption(this.rooomDisplayOnTableData, roomCategoryName);
        currentData = getDataToTableViewByDateRangeOption(this.rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        setNumOfInvoice(String.valueOf(getNumOfInvoice(data)));
        setTotalMoney(formatCurrency(calculateTotalMoney(data)));
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
    private ObservableList<RoomDisplayOnTable> getDataToTableViewByYearOption(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName) {
        ObservableList<RoomDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            rooomDisplayOnTableData.stream()
                    .filter(i -> i.getBookingDate().getYear() == Integer.parseInt(yearsCombobox.getSelectionModel().getSelectedItem()))
                    .filter(i -> quarterCombobox.getSelectionModel().getSelectedItem()
                            .equalsIgnoreCase(NONE_VALUE_QUARTER)
                            || QuarterChecker.isQuarter(i.getBookingDate(),
                            quarterCombobox.getValue(),
                            quarterCombobox.getValue().equalsIgnoreCase(NONE_VALUE_QUARTER)))
                    .filter(i -> roomCategoryName.equals(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
        return filteredData;
    }

    // get data of all the time and show to table view
    private ObservableList<RoomDisplayOnTable> getDataToTableViewAllOfTime(String roomCategoryName) {
        ObservableList<RoomDisplayOnTable> filteredData = FXCollections.observableArrayList();
        try{
            allOfData.stream()
                    .filter(i -> roomCategoryName.equals(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                    .forEach(filteredData::add);
            return filteredData;
        }catch (Exception ignored){

        }
        return filteredData;
    }

    // filter data by daterange and employee name and show to table
    private ObservableList<RoomDisplayOnTable> getDataToTableViewByDateRangeOption(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName) {
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        ObservableList<RoomDisplayOnTable> filteredData = FXCollections.observableArrayList();

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            rooomDisplayOnTableData.stream()
                    .filter(i -> i.getBookingDate().toLocalDate().equals(startDate.toLocalDate()))
                    .filter(i -> roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                    .forEach(filteredData::add);
        } else {
            rooomDisplayOnTableData.stream()
                    .filter(i -> (i.getBookingDate().isAfter(startDate) || i.getBookingDate().isEqual(startDate))
                            && (i.getBookingDate().isBefore(endDate) || i.getBookingDate().isEqual(endDate)))
                    .filter(i -> roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                    .forEach(filteredData::add);
        }
        return filteredData;
    }

    // if flat equal 0, that means statistics by YEAR
    // if flat equal 1, that means statistics by DATE RANGE PICKER
    // if flat equal 2, that means statistics all of time
    private void showDataToChartView(int flat) {
        String roomCategoryName = roomCategoryNameCombobox.getValue();
        roomDataBarChart.getXAxis().setLabel(getChartTitle());
        roomDataBarChart.getYAxis().setLabel("Tiền (NVĐ)");
        roomDataBarChart.getData().clear();
        if(flat == 0) roomDataBarChart.getData().add(getDataByYear(this.rooomDisplayOnTableData, roomCategoryName));
        else if(flat == 1) roomDataBarChart.getData().add(getDataByDateRange(this.rooomDisplayOnTableData, roomCategoryName));
        else if (flat == 2)roomDataBarChart.getData().add(getDataForAllOfTime(roomCategoryName));
        else throw new IllegalArgumentException("Errors flat for statistic");
    }

    // get data of all the time to show on bar chart
    private XYChart.Series<String, Double> getDataForAllOfTime(String employeeName){
        allOfYears.clear();
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");

        Map<String, Double> hashTable = allOfData.stream()
                .filter(i -> employeeName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME)
                        || i.getRoomCategory().equalsIgnoreCase(employeeName))
                .collect(Collectors.groupingBy(
                        i -> String.valueOf(i.getBookingDate().getYear()),
                        Collectors.summingDouble(RoomDisplayOnTable::getTotalMoney)
                ));

        Map<String, Double> sortedHashTable = new TreeMap<>(hashTable);
        sortedHashTable.forEach((year, totalMoney) ->{
            data.getData().add(new XYChart.Data<>(year, totalMoney));
            allOfYears.add(year);
        });
        return data;
    }

    // same as above
    private XYChart.Series<String, Double> getDataByYear(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName){
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
                double netDueOfMonth = getNetDueByMonthOfYear(rooomDisplayOnTableData, Integer.parseInt(yearsCombobox.getValue()), i, roomCategoryName);
                data.getData().add(new XYChart.Data<>(month, netDueOfMonth));
            }
        }catch (Exception ignored){

        }
        return data;
    }

    private XYChart.Series<String, Double> getDataByDateRange(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName) {
        XYChart.Series<String, Double> data = new XYChart.Series<>();
        data.setName("Doanh thu");
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        // thong ke cho ngay hien tai
        if(isToday(startDate, endDate)) return getDataForToday(rooomDisplayOnTableData, roomCategoryName, data);
            // thong ke cho ngay hom truoc
        else if(isYesterDay(startDate, endDate)) return getDataForYesterday(rooomDisplayOnTableData, roomCategoryName, data);
            // thong ke cho tuan nay
        else if(isThisWeek(startDate, endDate)) return getDataForThisWeek(rooomDisplayOnTableData, roomCategoryName, data);
            // thong ke cho thang nay
        else if(isThisMonth(startDate, endDate)) return getDataForThisMonth(rooomDisplayOnTableData, roomCategoryName, data);
            // thong ke cho thang truoc
        else if(isLastMonth(startDate, endDate)) return getDataForLastMonth(rooomDisplayOnTableData, roomCategoryName, data);
            // thong ke cho khoang thoi gian cu the
        else return getDataForAnyTime(rooomDisplayOnTableData, roomCategoryName, data);
    }

    private XYChart.Series<String, Double> getDataForToday(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        double netDueAve = rooomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(LocalDate.now()))
                .filter(i -> (roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(roomCategoryName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(roomTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        currentData = getDataToTableViewByDateRangeOption(rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        return data;
    }

    private XYChart.Series<String, Double> getDataForYesterday(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        double netDueAve = rooomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(LocalDate.now().minusDays(1)))
                .filter(i -> (roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(roomCategoryName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
        data.getData().add(new XYChart.Data<>(roomTabDateRangePicker.getValue().getStartDate().toString(), netDueAve));
        currentData = getDataToTableViewByDateRangeOption(rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisWeek(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        LocalDateTime monday = LocalDate.now().with(DayOfWeek.MONDAY).atTime(0, 0, 0);
        LocalDateTime sunday = LocalDate.now().with(DayOfWeek.SUNDAY).atTime(23, 59, 59);
        ObservableList<RoomDisplayOnTable> invoiceOfWeek = FXCollections.observableArrayList();
        rooomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(monday) || i.getBookingDate().isEqual(monday))
                        && (i.getBookingDate().isBefore(sunday) || i.getBookingDate().isEqual(sunday)))
                .filter(i -> roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                .forEach(invoiceOfWeek::add);

        for (RoomDisplayOnTable i : invoiceOfWeek) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueOfDay(invoiceOfWeek, i.getBookingDate().toLocalDate())
            ));
        }
        currentData = getDataToTableViewByDateRangeOption(rooomDisplayOnTableData, roomCategoryName);
        updateOnTable(0);
        return data;
    }

    private XYChart.Series<String, Double> getDataForThisMonth(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59);

        rooomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(firstDayOfMonth) || i.getBookingDate().isEqual(firstDayOfMonth))
                        && (i.getBookingDate().isBefore(lastDayOfMonth) || i.getBookingDate().isEqual(lastDayOfMonth)))
                .filter(i -> roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                .forEach(invoiceOfMonth::add);

        for (RoomDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueOfDay(invoiceOfMonth, i.getBookingDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String,Double> getDataForLastMonth(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfMonth = FXCollections.observableArrayList();
        LocalDateTime firstDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0, 0);
        LocalDateTime lastDayOfMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);

        rooomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(firstDayOfMonth) || i.getBookingDate().isEqual(firstDayOfMonth))
                        && (i.getBookingDate().isBefore(lastDayOfMonth) || i.getBookingDate().isEqual(lastDayOfMonth)))
                .filter(i -> roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName))
                .forEach(invoiceOfMonth::add);

        for (RoomDisplayOnTable i : invoiceOfMonth) {
            data.getData().add(new XYChart.Data<>(
                    i.getBookingDate().toLocalDate().toString(),
                    getNetDueOfDay(invoiceOfMonth, i.getBookingDate().toLocalDate())
            ));
        }
        return data;
    }

    private XYChart.Series<String, Double> getDataForAnyTime(List<RoomDisplayOnTable> rooomDisplayOnTableData, String roomCategoryName, XYChart.Series<String, Double> data) {
        ObservableList<RoomDisplayOnTable> invoiceOfRange = FXCollections.observableArrayList();
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0,0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59,59);
        rooomDisplayOnTableData.stream()
                .filter(i -> (i.getBookingDate().isAfter(startDate) || i.getBookingDate().isEqual(startDate))
                        && (i.getBookingDate().isBefore(endDate) || i.getBookingDate().isEqual(endDate)))
                .filter(i -> (roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) || i.getRoomCategory().equalsIgnoreCase(roomCategoryName)))
                .forEach(invoiceOfRange::add);
        for(RoomDisplayOnTable i : invoiceOfRange){
            data.getData().add(new XYChart.Data<>(i.getBookingDate().toLocalDate().toString(),
                    getNetDueOfDay(invoiceOfRange, i.getBookingDate().toLocalDate())));
        }
        return data;
    }

    private double getNetDueOfDay(ObservableList<RoomDisplayOnTable> rooomDisplayOnTableData, LocalDate date){
        return rooomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().toLocalDate().equals(date))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
    }

    private double getNetDueByMonthOfYear(List<RoomDisplayOnTable> rooomDisplayOnTableData, int year, int month, String roomCategoryName) {
        return rooomDisplayOnTableData.stream()
                .filter(i -> i.getBookingDate().getYear() == year)
                .filter(i -> i.getBookingDate().getMonthValue() == month
                        && (roomCategoryName.equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ||
                        i.getRoomCategory().equalsIgnoreCase(roomCategoryName)))
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
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
        LocalDateTime startDate = roomTabDateRangePicker.getValue().getStartDate().atTime(0, 0, 0);
        LocalDateTime endDate = roomTabDateRangePicker.getValue().getEndDate().atTime(23, 59, 59);
        String roomCategoryName = roomCategoryNameCombobox.getSelectionModel().getSelectedItem()
                .equalsIgnoreCase(NONE_VALUE_ROOM_CATEGORY_NAME) ? "" : roomCategoryNameCombobox.getSelectionModel().getSelectedItem();
        String baseTitle = "Thống kê ";
        String employeeTitle = roomCategoryName.isBlank() ? "" : " của nhân viên : " + roomCategoryName;

        if (filterByYearCheckBox.isSelected()){
            String year = yearsCombobox.getSelectionModel().getSelectedItem()
                    .equalsIgnoreCase(NONE_VALUE_YEAR) ? "" : yearsCombobox.getSelectionModel().getSelectedItem();
            String quarter = quarterCombobox.getSelectionModel().getSelectedItem()
                    .equalsIgnoreCase(NONE_VALUE_QUARTER) ? "" : quarterCombobox.getSelectionModel().getSelectedItem();

            if (year.isBlank() && quarter.isBlank() && roomCategoryName.isBlank()) {
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

    public TableView<RoomDisplayOnTable> cloneTableView(TableView<RoomDisplayOnTable> originalTableView) {

        TableView<RoomDisplayOnTable> clonedTableView = new TableView<>();

        for (TableColumn<RoomDisplayOnTable, ?> column : originalTableView.getColumns()) {
            TableColumn<RoomDisplayOnTable, ?> clonedColumn = new TableColumn<>(column.getText());
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
            roomCategoryNameCombobox.setDisable(true);
        }else{
            quarterCombobox.setDisable(false);
            roomCategoryNameCombobox.setDisable(false);
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
    private void loadDataToRoomCategoryNameCombobox() {
        List<RoomCategory> employeeList = RoomCategoryDAO.getRoomCategory();
        ObservableList<String> roomCategoryName = FXCollections.observableArrayList(NONE_VALUE_ROOM_CATEGORY_NAME);
        employeeList.forEach(e -> roomCategoryName.add(e.getRoomCategoryName()));
        roomCategoryNameCombobox.setItems(roomCategoryName);
        roomCategoryNameCombobox.setValue(roomCategoryName.getFirst());
    }

    // show num of invoice on text
    private void setNumOfInvoice(String num){
        numOfInvoiceText.setText(num);
    }

    private int getNumOfInvoice(ObservableList<RoomDisplayOnTable> rooomDisplayOnTableData){
        return rooomDisplayOnTableData.size();
    }

    // show total money of invoice on text
    private void setTotalMoney(String totalMoney){
        totalMoneyText.setText(totalMoney);
    }

    private double calculateTotalMoney(ObservableList<RoomDisplayOnTable> rooomDisplayOnTableData){
        return rooomDisplayOnTableData.stream()
                .mapToDouble(RoomDisplayOnTable::getTotalMoney)
                .sum();
    }
}
