package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.ShiftDaysSchedule;
import iuh.fit.utils.GlobalConstants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ShiftManagerController {
    //Non-input field
    @FXML
    private TextField shiftIDTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField modifiedDateTextField;

    //Input field
    @FXML
    private TimePicker startTimePicker;
    @FXML
    private TextField numbOfHourTextField;
    @FXML
    private ComboBox<ShiftDaysSchedule> scheduleComboBox;

    //Search Field
    @FXML
    private ComboBox<String> shiftIDSearchField;
    @FXML
    private TextField startTimeSearchField;
    @FXML
    private TextField numbOfHourSearchField;
    @FXML
    private TextField endTimeSearchField;
    @FXML
    private TextField scheduleSearchField;

    //Table
    @FXML
    private TableView<Shift> shiftTableView;
    @FXML
    private TableColumn<Shift, String> shiftIDColumn;
    @FXML
    private TableColumn<Shift, LocalTime> startTimeColumn;
    @FXML
    private TableColumn<Shift, Integer> numbOfHourColumn;
    @FXML
    private TableColumn<Shift, LocalTime> endTimeColumn;
    @FXML
    private TableColumn<Shift, String> scheduleColumn;
    @FXML
    private TableColumn<Shift, Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // Dialog Pane
    @FXML
    private DialogPane dialogPane;

    private Employee currentEmployee;
    private ObservableList<Shift> items;
    private Shift currentShift;

    private Stage stage;

    public void initialize() {
        dialogPane.toFront();
        startTimePicker.setTime(null);
        startTimePicker.setStepRateInMinutes(15);

        loadData();
        setupTable();
        shiftTableView.setFixedCellSize(40);


        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        startTimePicker.setOnAction(e->handleCalcEndTime());
        numbOfHourTextField.setOnAction(e->handleCalcEndTime());

        numbOfHourTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Kiểm tra nếu TextField mất focus
                // Thực hiện hành động khi unfocus
                handleCalcEndTime();
            }
        });

        updateBtn.setOnAction(e -> handelUpdateShift());
        shiftIDSearchField.setOnKeyReleased((keyEvent) -> handleSearchAction());
        shiftIDSearchField.setOnAction(event -> handleSearchAction());
        shiftTableView.setRowFactory(x->{
            TableRow<Shift> shiftRow = new TableRow<>();

            shiftRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !shiftRow.isEmpty()) { // Kiểm tra double-click và dòng không trống
                    Shift rowData = shiftRow.getItem();
                    // Thực hiện hành động khi double-click
                    try {
                        handleShowShiftInformation(rowData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            return shiftRow;
        });


    }

    public void setupContext(Employee employeee) {
        this.currentEmployee = employeee;
    }
    public void setUpCurrentShift(Shift shift){
        this.currentShift = shift;
    }

    public void setStage (Stage stage){
        this.stage = stage;
    }

    public Stage getStage(){
        return stage;
    }

    // Phương thức load dữ liệu lên giao diện
    public void loadData() {
        List<Shift> shiftList = ShiftDAO.getShifts();
        List<String> shiftID = shiftList.stream().map(Shift::getShiftID).collect(Collectors.toList());

        shiftIDSearchField.getItems().setAll(shiftID);

        items = FXCollections.observableArrayList(shiftList);
        shiftTableView.setItems(items);
        shiftTableView.refresh();

        List<ShiftDaysSchedule> shiftDaysScheduleList = List.of(ShiftDaysSchedule.MON_WED_FRI, ShiftDaysSchedule.TUE_THU_SAT, ShiftDaysSchedule.SUNDAY);
        scheduleComboBox.getItems().setAll(shiftDaysScheduleList);
        scheduleComboBox.getSelectionModel().selectFirst();

    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        shiftIDColumn.setCellValueFactory(new PropertyValueFactory<>("shiftID"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        numbOfHourColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfHour"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("shiftDaysSchedule"));

        setupActionColumn();
        shiftTableView.setItems(items);
    }

    private void setupActionColumn() {
        Callback<TableColumn<Shift, Void>, TableCell<Shift, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final Button assignmentButton = new Button("Phân công");
            private final Button changeShiftButton = new Button("Chuyển ca");
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                assignmentButton.getStyleClass().add("button-view");
                changeShiftButton.getStyleClass().add("button-view");
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/item_styles/ButtonForShiftAssignment.css")).toExternalForm());


                updateButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    if(shift.equals(currentShift)){
                        dialogPane.showInformation("Thông báo","Không thể xóa hay chỉnh sửa ca làm vì bạn " +
                                "đang thuộc ca làm này");
                    }else{
                        handleUpdateBtn(shift);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    if(shift.equals(currentShift)){
                        dialogPane.showInformation("Thông báo","Không thể xóa hay chỉnh sửa ca làm vì bạn " +
                                "đang thuộc ca làm này");
                    }else{
                        if (!ShiftDAO.checkAllowUpdateOrDelete(shift.getShiftID())) {
                            try {
                                handelShowDetailData(shift, "delete", null, currentEmployee);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            handleDeleteAction(shift);
                        }
                    }
                });

                assignmentButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    try {
                        handelShowShiftAssignmentPanel(shift);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                changeShiftButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    if(shift == currentShift){
                        List<Employee> tempList = ShiftDAO.getEmployeeListByShift(shift.getShiftID());
                        tempList.remove(currentEmployee);
                        if(tempList.isEmpty()){
                            dialogPane.showInformation("Thông báo", "Ca làm này hiện không có nhân viên để chuyển ca");
                        }
                    }else {
                        List<Employee> tempList = ShiftDAO.getEmployeeListByShift(shift.getShiftID());
                        tempList.remove(currentEmployee);
                        if(tempList.isEmpty()){
                            dialogPane.showInformation("Thông báo", "Ca làm này hiện không có nhân viên để chuyển ca");
                        }else{
                            try {
                                handelShowShiftChangingPanel(shift);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton, assignmentButton, changeShiftButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        };
        actionColumn.setCellFactory(cellFactory);
    }

    private void handleResetAction() {
        shiftIDTextField.clear();
        startTimePicker.setTime(null);
        numbOfHourTextField.clear();
        endTimeTextField.clear();
        scheduleComboBox.getSelectionModel().selectFirst();
        modifiedDateTextField.clear();

        shiftTableView.getSelectionModel().clearSelection();

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);

        startTimePicker.requestFocus();
    }

    private void handleCalcEndTime(){
        if(numbOfHourTextField.getText().equalsIgnoreCase("")){
            dialogPane.showInformation("Thông báo", "Không để trống số giờ làm việc");
        }else{
            try{
                LocalTime endTime = startTimePicker.getTime().plusHours(Integer.parseInt(numbOfHourTextField.getText()));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String endTimeString = endTime.format(formatter);
                endTimeTextField.setText(endTimeString);
            }catch (Exception e){
                dialogPane.showWarning("LỖI", e.getMessage());
            }
        }
    }

    private String handleShiftIDGenerate() {
        LocalTime endTime = startTimePicker.getTime().plusHours(Integer.parseInt(numbOfHourTextField.getText()));
        return ShiftDAO.shiftIDGenerate(endTime);
    }

    private void handleAddAction() {
        try {
            String newShiftID = handleShiftIDGenerate();
            Shift shift = new Shift(
                    newShiftID,
                    startTimePicker.getTime(),
                    LocalDateTime.now(),
                    scheduleComboBox.getSelectionModel().getSelectedItem(),
                    Integer.parseInt(numbOfHourTextField.getText())
            );

            ShiftDAO.createData(shift);

            dialogPane.showInformation("Thông báo", "Ca làm mới được tạo thành công với mã ca: " + newShiftID);

            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handleUpdateBtn(Shift shift) {
        shiftIDTextField.setText(shift.getShiftID());
        startTimePicker.setTime(shift.getStartTime());
        numbOfHourTextField.setText(String.valueOf(shift.getNumberOfHour()));
        endTimeTextField.setText(String.valueOf(shift.getEndTime()));
        scheduleComboBox.setValue(shift.getShiftDaysSchedule());
        String format = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        modifiedDateTextField.setText(shift.getUpdatedDate().format(formatter));

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handelUpdateShift(){
        if (!ShiftDAO.checkAllowUpdateOrDelete(shiftIDTextField.getText())) {
            try {
                handelShowDetailData(null, "update", shiftIDTextField.getText(), currentEmployee);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try{
                Shift newShift = getShiftInfo();

                DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                        "XÁC NHẬN",
                        "Bạn có chắc chắn muốn cập nhật ca làm này?"
                );

                dialog.onClose(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        ShiftDAO.updateDataWithOldID(getOldShiftID(),newShift);

                        handleResetAction();
                        loadData();
                    }
                });
            }catch (Exception e){
                dialogPane.showWarning("LỖI", e.getMessage());
            }
        }
    }

    public String getOldShiftID(){
        return shiftIDTextField.getText();
    }

    public Shift getShiftInfo(){
        String shiftID = shiftIDTextField.getText();
        LocalTime endTime = startTimePicker.getTime().plusHours(Integer.parseInt(numbOfHourTextField.getText()));
        String timeCode = endTime.getHour() > 12 ? "PM" : "AM";
        String nextIDNumb = shiftID.substring(shiftID.length()-4);
        String newShiftID = String.format("%s%s%s", GlobalConstants.SHIFT_PREFIX + "-", timeCode + "-", nextIDNumb);
        Shift newShift = new Shift(newShiftID, startTimePicker.getTime(), LocalDateTime.now(), scheduleComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(numbOfHourTextField.getText()));
        return newShift;
    }

    private void handleDeleteAction(Shift shift){
        try{
            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn xóa ca làm này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ShiftDAO.deleteData(shift.getShiftID());
                    handleResetAction();
                    loadData();
                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handleSearchAction(){
        startTimeSearchField.setText("");
        numbOfHourSearchField.setText("");
        endTimeSearchField.setText("");
        scheduleSearchField.setText("");

        String searchText = shiftIDSearchField.getEditor().getText();
        List<Shift> shift;

        if (searchText == null || searchText.isEmpty()) {
            shift = ShiftDAO.getShifts();
        } else {
            shift = ShiftDAO.findDataByAnyContainsId(searchText);
            if (!shift.isEmpty()) {
                if(shift.size()==1){
                    startTimeSearchField.setText(shift.getFirst().getStartTime().toString());
                    numbOfHourSearchField.setText(String.valueOf(shift.getFirst().getNumberOfHour()));
                    endTimeSearchField.setText(shift.getFirst().getEndTime().toString());
                    scheduleSearchField.setText(shift.getFirst().getShiftDaysSchedule().toString());
                }
            }else{
                startTimeSearchField.setText("rỗng");
                numbOfHourSearchField.setText("rỗng");
                endTimeSearchField.setText("rỗng");
                scheduleSearchField.setText("rỗng");
            }
        }

        items.setAll(shift);
        shiftTableView.setItems(items);
    }

    private void handelShowDetailData(Shift shift, String func, String shiftID, Employee employee) throws IOException {
        String source = "/iuh/fit/view/features/employee/ShiftDetailForEachEmployeeDialog.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        ShiftDetailForEachEmployeeDialogController shiftDetailForEachEmployeeDialogController = loader.getController();
        shiftDetailForEachEmployeeDialogController.setController(this);
        shiftDetailForEachEmployeeDialogController.getData(shift, func, shiftID);
        shiftDetailForEachEmployeeDialogController.setEmployee(employee);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Thông tin ca làm");
        stage.setScene(scene);
        stage.setResizable(false);
        setStage(stage);
        shiftDetailForEachEmployeeDialogController.getComponentFormController();
        stage.show();
    }

    private void handelShowShiftAssignmentPanel(Shift shift) throws IOException {
        String source = "/iuh/fit/view/features/employee/ShiftAssignmentPanel.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        ShiftAssignmentController shiftAssignmentController = loader.getController();
        shiftAssignmentController.initialize(shift);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Phân công ca làm");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void handelShowShiftChangingPanel(Shift shift) throws IOException {
        String source = "/iuh/fit/view/features/employee/ShiftChangingPanel.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        ShiftChangingController shiftChangingController = loader.getController();
        shiftChangingController.initialize(shift, currentShift, currentEmployee);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Chuyển ca làm");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void handleShowShiftInformation(Shift shift) throws IOException {
        String source = "/iuh/fit/view/features/employee/ShiftInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        ShiftInformationViewCotroller shiftChangingController = loader.getController();
        shiftChangingController.initialize(shift);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.setTitle("Chuyển ca làm");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
