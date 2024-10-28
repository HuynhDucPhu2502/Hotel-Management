package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.TimePicker;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.ShiftDaysSchedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

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
//        updateBtn.setOnAction(e -> handleUpdateAction());
//        roomIDSearchField.setOnKeyReleased((keyEvent) -> handleSearchAction());
//        roomIDSearchField.setOnAction(event -> handleSearchAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
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
//                    handleUpdateBtn(room);
                });

                deleteButton.setOnAction(event -> {
                    Shift shift = getTableView().getItems().get(getIndex());
//                    handleDeleteAction(room);
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
                    Shift shift = getTableView().getItems().get(getIndex());

//                    if (room.getRoomStatus() != RoomStatus.AVAILABLE && room.getRoomStatus() != RoomStatus.UNAVAILABLE) {
//                        updateButton.setDisable(true);
//                        deleteButton.setDisable(true);
//                    } else {
//                        updateButton.setDisable(false);
//                        deleteButton.setDisable(false);
//                    }

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
        LocalTime endTime = startTimePicker.getTime().plusHours(Integer.valueOf(numbOfHourTextField.getText()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        String endTimeString = endTime.format(formatter);
        endTimeTextField.setText(endTimeString);
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
}
