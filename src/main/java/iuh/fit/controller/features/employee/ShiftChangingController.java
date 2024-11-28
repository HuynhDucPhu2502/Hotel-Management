package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ShiftAssignmentDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.Position;
import iuh.fit.models.wrapper.CheckedEmployee;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Le Tran Gia Huy
 * @created 18/11/2024 - 4:29 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features.employee
 */
public class ShiftChangingController {
    @FXML
    private TextField shiftIDTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField scheduleTextField;
    @FXML
    private TextField numberOfHourTextField;

    @FXML
    private TextField employeeSearchTextField;
    @FXML
    private TextField shiftSearchTextField;

    @FXML
    private TableView<CheckedEmployee> employeeTableView;
    @FXML
    private TableView<Shift> shiftTableView;
    @FXML
    private TableColumn<CheckedEmployee, String> employeeIDColumn;
    @FXML
    private TableColumn<CheckedEmployee, String> fullNameColumn;
    @FXML
    private TableColumn<CheckedEmployee, Position> positionColumn;
    @FXML
    private TableColumn<CheckedEmployee, String> phoneNumberColumn;

    @FXML
    private TableColumn<CheckedEmployee, Void> isCheckedEmployeeColumn;
    @FXML
    private TableColumn<Shift, String> shiftIDColumn;
    @FXML
    private TableColumn<Shift, String> startTimeColumn;
    @FXML
    private TableColumn<Shift, String> numberOfHourColumn;
    @FXML
    private TableColumn<Shift, String> endTimeColumn;
    @FXML
    private TableColumn<Shift, String> scheduleColumn;

    @FXML
    private Button employeeSearchBtn;
    @FXML
    private Button shiftSearchBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private DialogPane dialogPane;
    @FXML
    private AnchorPane anchorPane;

    private ObservableList<CheckedEmployee> employeeItems;
    private ObservableList<Shift> shiftItems;
    private CheckBox checkBox;
    ArrayList<CheckedEmployee> checkedEmployees = new ArrayList<CheckedEmployee>();
    ArrayList<Shift> shifts = new ArrayList<Shift>();

    private Shift chosenShift;
    private Shift currentShift;
    private Employee currentEmployee;

    public void initialize(Shift shift, Shift currentShift, Employee currentEmployee){
        dialogPane.toFront();
        setFocusTraverse();

        this.chosenShift = shift;
        this.currentShift = currentShift;
        this.currentEmployee = currentEmployee;


        employeeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        shiftTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        showOnLabel(chosenShift);
        loadEmployeeData(chosenShift);
        setupEmployeeTable();

        loadShiftData(chosenShift);
        setupShiftTable();

        submitBtn.getStyleClass().add("button-update");
        cancelBtn.getStyleClass().add("button-delete");
        submitBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());
        cancelBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

        employeeSearchBtn.setOnAction(e -> handleEmployeeSearchAction());
        shiftSearchBtn.setOnAction(e -> handleShiftSearchAction());
        submitBtn.setOnAction(e-> handleChangeShiftAction(shift));
        cancelBtn.setOnAction(e->handleCancelAction());
        anchorPane.setOnMouseClicked(e->disableFocusForEmployeeIDTextField());
    }

    private void setFocusTraverse() {
        shiftIDTextField.setFocusTraversable(false);
        scheduleTextField.setFocusTraversable(false);
        startTimeTextField.setFocusTraversable(false);
        endTimeTextField.setFocusTraversable(false);
        numberOfHourTextField.setFocusTraversable(false);
        employeeTableView.setFocusTraversable(false);
        shiftTableView.setFocusTraversable(false);
    }

    public void loadEmployeeData(Shift shift) {
        ArrayList<Employee> employees = (ArrayList<Employee>) ShiftDAO.getEmployeeListByShift(shift.getShiftID());
        employees.remove(currentEmployee);

        employees.forEach(x->{
            checkedEmployees.add(new CheckedEmployee(x, false));
        });

        employeeItems = FXCollections.observableArrayList(checkedEmployees);
        employeeTableView.setItems(employeeItems);
        employeeTableView.refresh();
    }

    private void showOnLabel(Shift shift){
        shiftIDTextField.setText(shift.getShiftID());
        scheduleTextField.setText(shift.getShiftDaysSchedule().toString());
        startTimeTextField.setText(shift.getStartTime().toString());
        endTimeTextField.setText(shift.getEndTime().toString());
        numberOfHourTextField.setText(String.valueOf(shift.getNumberOfHour()));

        employeeSearchTextField.requestFocus();
    }

    private void setupEmployeeTable() {
        employeeIDColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getEmployeeID()));
        fullNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getFullName()));
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getPosition()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getPhoneNumber()));

        setupActionColumnForEmployee();
        employeeTableView.setItems(employeeItems);

    }

    private void setupActionColumnForEmployee() {
        Callback<TableColumn<CheckedEmployee, Void>, TableCell<CheckedEmployee, Void>> cellFactory = param -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox hBox = new HBox(10);

            {
                checkBox.setOnAction(event -> {
                    CheckedEmployee checkedEmployee = getTableView().getItems().get(getIndex());
                    handelCheckedEmployee(checkedEmployee, checkBox);
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(checkBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CheckedEmployee checkedEmployee = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(checkedEmployee.isChecked());
                    checkBox.setFocusTraversable(false);
                    setGraphic(hBox);
                }
            }
        };
        isCheckedEmployeeColumn.setCellFactory(cellFactory);
    }

    private void loadShiftData(Shift currentShift) {
        List<Shift> shiftList = ShiftDAO.getShifts();
        shiftList.remove(currentShift);
        shifts.clear();
        shifts.addAll(shiftList);

        shiftItems = FXCollections.observableArrayList(shiftList);
        shiftTableView.setItems(shiftItems);
        shiftTableView.refresh();
    }

    private void setupShiftTable() {
        shiftIDColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getShiftID()));
        startTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStartTime().toString()));
        numberOfHourColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(String.valueOf(cellData.getValue().getNumberOfHour())));
        endTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEndTime().toString()));
        scheduleColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getShiftDaysSchedule().toString()));

        shiftTableView.setItems(shiftItems);
    }


    private void handelCheckedEmployee(CheckedEmployee checkedEmployee, CheckBox checkBox){
        if(checkBox.isSelected()){
            checkedEmployees.stream().filter(x-> x.equals(checkedEmployee)).forEach(x->x.setChecked(true));
            resetEmployeeTableView();
            cancelBtn.requestFocus();
        }else{
            checkedEmployees.stream().filter(x-> x.equals(checkedEmployee)).forEach(x->x.setChecked(false));
            resetEmployeeTableView();
            cancelBtn.requestFocus();
        }
    }

    public void resetEmployeeTableView(){
        employeeTableView.getItems().clear();
        ArrayList<CheckedEmployee>  checkedIsTrueEmpl = new ArrayList<>();
        checkedEmployees.stream().filter(CheckedEmployee::isChecked).forEach(checkedIsTrueEmpl::add);
        ArrayList<CheckedEmployee>  checkedIsFalseEmpl = new ArrayList<>();
        checkedEmployees.stream().filter(x-> !x.isChecked()).forEach(checkedIsFalseEmpl::add);
        ArrayList<CheckedEmployee> combineList = new ArrayList<>();
        combineList.addAll(checkedIsTrueEmpl);
        combineList.addAll(checkedIsFalseEmpl);

        employeeItems = FXCollections.observableArrayList(combineList);
        employeeTableView.setItems(employeeItems);
        employeeTableView.refresh();
    }

    public void resetShiftTableView(){
        shiftTableView.getItems().clear();

        loadShiftData(chosenShift);
    }

    private void disableFocusForEmployeeIDTextField(){
        cancelBtn.requestFocus();
    }

    private void handleCancelAction(){
        // Tắt cửa sổ hiện tại
        Stage stage = (Stage) shiftIDTextField.getScene().getWindow(); // Lấy `Stage` chứa `scheduleLabel`
        stage.close();
    }

    private void handleChangeShiftAction(Shift currentShift) {
        if(checkedEmployees.stream().noneMatch(x -> x.isChecked())){
            dialogPane.showInformation("Thông báo","Vui lòng chọn ít nhất một nhân viên để chuyển ca làm");
        }else if(shiftTableView.getSelectionModel().getSelectedItem() == null){
            dialogPane.showInformation("Thông báo","Vui lòng chọn một ca làm");
        }else{
            try{
                DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                        "XÁC NHẬN",
                        "Bạn có chắc chắn muốn chuyển nhân viên sang ca làm khác?"
                );

                dialog.onClose(buttonType -> {
                    if (buttonType == ButtonType.YES) {
                        Shift theShift = shiftTableView.getSelectionModel().getSelectedItem();
                        checkedEmployees.stream().filter(CheckedEmployee::isChecked).forEach(x->{
                            ShiftAssignmentDAO.updateShiftAssignmentForOneEmployee(currentShift, theShift, x.getEmployee());
                        });
                        DialogPane.Dialog<ButtonType> dialog2 = dialogPane.showInformation(
                                "XÁC NHẬN",
                                "Đã chuyển ca thành công"
                        );
                        dialog2.setOnClose(x->{
                            handleCancelAction();
                        });
                    }
                });

            }catch(Exception e){
                dialogPane.showWarning("LỖI", e.getMessage());
            }
        }
    }

    private void handleEmployeeSearchAction() {
        String searchText = employeeSearchTextField.getText();

        ArrayList<CheckedEmployee> searchResult;
        if (searchText == null || searchText.isEmpty()) {
            resetEmployeeTableView();
        } else {
            searchResult = new ArrayList<>();
            checkedEmployees.stream().filter(x -> x.getEmployee().getEmployeeID().contains(searchText)).forEach(searchResult::add);
            if (!searchResult.isEmpty()) {
                employeeItems.setAll(searchResult);
                employeeTableView.setItems(employeeItems);
            }else{
                resetEmployeeTableView();
            }
        }
    }

    private void handleShiftSearchAction() {
        String searchText = shiftSearchTextField.getText();

        ArrayList<Shift> searchResult;
        if (searchText == null || searchText.isEmpty()) {
            resetShiftTableView();
        } else {
            searchResult = new ArrayList<>();
            shifts.stream().filter(x -> x.getShiftID().contains(searchText)).forEach(searchResult::add);
            if (!searchResult.isEmpty()) {
                shiftItems.setAll(searchResult);
                shiftTableView.setItems(shiftItems);
            }else{
                resetShiftTableView();
            }
        }
    }
}
