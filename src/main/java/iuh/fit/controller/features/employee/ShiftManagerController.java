package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import iuh.fit.controller.MainController;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.RoomStatus;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Le Tran Gia Huy
 * @created 28/10/2024 - 1:59 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features.employee
 */
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

    private Employee employee;
    private ObservableList<Shift> items;

    public void initialize() {
        dialogPane.toFront();
        startTimePicker.setTime(null);
        startTimePicker.setStepRateInMinutes(15);

        loadData();
        setupTable();

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


    }

    public void setupContext(Employee employeee) {
        this.employee = employeee;
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
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                updateButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(shift);
                });

                deleteButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
                    if (!ShiftDAO.checkAllowUpdateAndDelete(shift.getShiftID())) {
                        try {
                            handelShowDetailData(shift, "delete", null);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        handleDeleteAction(shift);
                    }
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton);
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
                LocalTime endTime = startTimePicker.getTime().plusHours(Integer.valueOf(numbOfHourTextField.getText()));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String endTimeString = endTime.format(formatter);
                endTimeTextField.setText(endTimeString);
            }catch (Exception e){
                dialogPane.showWarning("LỖI", e.getMessage());
            }
        }
    }

    private String handleShiftIDGenerate() {
        LocalTime endTime = startTimePicker.getTime().plusHours(Integer.valueOf(numbOfHourTextField.getText()));
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
                    Integer.valueOf(numbOfHourTextField.getText())
            );

            ShiftDAO.createData(shift);

            dialogPane.showInformation("Thông báo", "Ca làm mới được tạo thành công với mã ca: " + newShiftID);

            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handelShowDetailData(Shift shift, String func, String shiftID) throws IOException {
        String source = "/iuh/fit/view/features/employee/ShiftDetailForEachEmployeeDialog.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        ShiftDetailForEachEmployeeDialogController shiftDetailForEachEmployeeDialogController = loader.getController();
        shiftDetailForEachEmployeeDialogController.setController(this);
        shiftDetailForEachEmployeeDialogController.getData(shift, func, shiftID);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Thông tin ca làm");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void handleUpdateBtn(Shift shift) {
        shiftIDTextField.setText(shift.getShiftID());
        startTimePicker.setTime(shift.getStartTime());
        numbOfHourTextField.setText(String.valueOf(shift.getNumberOfHour()));
        endTimeTextField.setText(String.valueOf(shift.getEndTime()));
        scheduleComboBox.setValue(shift.getShiftDaysSchedule());
        modifiedDateTextField.setText(String.valueOf(shift.getUpdatedDate()));

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handelUpdateShift(){
        if (!ShiftDAO.checkAllowUpdateAndDelete(shiftIDTextField.getText())) {
            try {
                handelShowDetailData(null, "update", shiftIDTextField.getText());
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
}
