package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ShiftAssignmentDAO;
import iuh.fit.models.wrapper.CheckedEmployee;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.ShiftAssignment;
import iuh.fit.models.enums.Position;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Objects;

import static iuh.fit.dao.ShiftAssignmentDAO.createNextShiftAssignmentID;

/**
 * @author Le Tran Gia Huy
 * @created 10/11/2024 - 8:06 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features.employee
 */
public class ShiftAssignmentController {
    @FXML
    private TextField shiftIDTextField;
    @FXML
    private TextField scheduleTextField;
    @FXML
    private TextField startTimeTextField;
    @FXML
    private TextField endTimeTextField;
    @FXML
    private TextField numberOfHourTextField;
    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button searchEmployeeBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private TableView<CheckedEmployee> employeeTableView;
    @FXML
    private TableColumn<CheckedEmployee, String> employeeIDColumn;
    @FXML
    private TableColumn<CheckedEmployee, String> fullNameColumn;
    @FXML
    private TableColumn<CheckedEmployee, Position> positionColumn;
    @FXML
    private TableColumn<CheckedEmployee, String> phoneNumberColumn;
    @FXML
    private TableColumn<CheckedEmployee, Void> isCheckedColumn;
    @FXML
    private CheckBox showAllEmployeeOption;

    @FXML
    private DialogPane dialogPane;
    @FXML
    private AnchorPane anchorPane;

    private ObservableList<CheckedEmployee> items;
    private CheckBox checkBox;
    ArrayList<CheckedEmployee> checkedEmployees = new ArrayList<CheckedEmployee>();

    public void initialize(Shift shift) {
        dialogPane.toFront();
        setFocusTraverse();


        showOnLabel(shift);
        handelShowAllEmployee(shift);
        setupTable();
        employeeTableView.setFixedCellSize(25);

        showAllEmployeeOption.setOnAction(e->handelShowAllEmployee(shift));

        submitBtn.getStyleClass().add("button-update");
        cancelBtn.getStyleClass().add("button-delete");
        submitBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());
        cancelBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

        searchEmployeeBtn.setOnAction(e -> handleSearchAction());
        submitBtn.setOnAction(e->handleAddAction(shift));
        cancelBtn.setOnAction(e->handleCancelAction());
        anchorPane.setOnMouseClicked(e->disableFocusForEmployeeIDTextField());

        employeeIDTextField.setOnKeyReleased((keyEvent) -> handleSearchActionWithKey(keyEvent.getCode()));

    }

    private void handelShowAllEmployee(Shift shift) {
        if(showAllEmployeeOption.isSelected()){
            ArrayList<Employee> employeeList = EmployeeDAO.getEmployeesWithoutSpecificShift(shift);
            if(checkedEmployees.isEmpty()){
                loadData(employeeList);
            }else{
                checkedEmployees.clear();
                loadData(employeeList);
            }
        }else{
            ArrayList<Employee> employeeList = EmployeeDAO.getEmployeesWithoutAllShift();
            if(checkedEmployees.isEmpty()){
                loadData(employeeList);
            }else{
                checkedEmployees.clear();
                loadData(employeeList);
            }
        }
    }

//    public void setupContext(Employee employeee) {
//        this.employee = employeee;
//    }

//  Phương thức load dữ liệu lên giao diện
    public void loadData(ArrayList<Employee> employeeList) {
        if(!employeeList.isEmpty()){
            employeeList.forEach(x->{
                CheckedEmployee checkedEmployee = new CheckedEmployee(x, false);
                checkedEmployees.add(checkedEmployee);
            });

            ArrayList<CheckedEmployee>  checkedIsTrueEmpl = new ArrayList<>();
            checkedEmployees.stream().filter(CheckedEmployee::isChecked).forEach(checkedIsTrueEmpl::add);
            ArrayList<CheckedEmployee>  checkedIsFalseEmpl = new ArrayList<>();
            checkedEmployees.stream().filter(x-> !x.isChecked()).forEach(checkedIsFalseEmpl::add);
            ArrayList<CheckedEmployee> combineList = new ArrayList<>();
            combineList.addAll(checkedIsTrueEmpl);
            combineList.addAll(checkedIsFalseEmpl);

            items = FXCollections.observableArrayList(combineList);
            employeeTableView.setItems(items);
            employeeTableView.refresh();
        }
    }

    public void deleteAllTable(){
        employeeTableView.getItems().clear();
    }

    public void resetTableView(){
        employeeTableView.getItems().clear();
        ArrayList<CheckedEmployee>  checkedIsTrueEmpl = new ArrayList<>();
        checkedEmployees.stream().filter(CheckedEmployee::isChecked).forEach(checkedIsTrueEmpl::add);
        ArrayList<CheckedEmployee>  checkedIsFalseEmpl = new ArrayList<>();
        checkedEmployees.stream().filter(x-> !x.isChecked()).forEach(checkedIsFalseEmpl::add);
        ArrayList<CheckedEmployee> combineList = new ArrayList<>();
        combineList.addAll(checkedIsTrueEmpl);
        combineList.addAll(checkedIsFalseEmpl);

        items = FXCollections.observableArrayList(combineList);
        employeeTableView.setItems(items);
        employeeTableView.refresh();
    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        employeeIDColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getEmployeeID()));
        fullNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getFullName()));
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getPosition()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployee().getPhoneNumber()));

        setupActionColumn();
        employeeTableView.setItems(items);

    }

    private void setupActionColumn() {
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
                    setGraphic(hBox);
                }
            }
        };
        isCheckedColumn.setCellFactory(cellFactory);
    }

    private void showOnLabel(Shift shift){
        shiftIDTextField.setText(shift.getShiftID());
        scheduleTextField.setText(shift.getShiftDaysSchedule().toString());
        startTimeTextField.setText(shift.getStartTime().toString());
        endTimeTextField.setText(shift.getEndTime().toString());
        numberOfHourTextField.setText(String.valueOf(shift.getNumberOfHour()));

        employeeIDTextField.requestFocus();
    }

    private void setFocusTraverse(){
        shiftIDTextField.setFocusTraversable(false);
        scheduleTextField.setFocusTraversable(false);
        startTimeTextField.setFocusTraversable(false);
        endTimeTextField.setFocusTraversable(false);
        numberOfHourTextField.setFocusTraversable(false);
    }

    private void disableFocusForEmployeeIDTextField(){
        cancelBtn.requestFocus();
    }

    private void handelCheckedEmployee(CheckedEmployee checkedEmployee, CheckBox checkBox){
        if(checkBox.isSelected()){
            checkedEmployees.stream().filter(x-> x.equals(checkedEmployee)).forEach(x->x.setChecked(true));
            resetTableView();
            cancelBtn.requestFocus();
        }else{
            checkedEmployees.stream().filter(x-> x.equals(checkedEmployee)).forEach(x->x.setChecked(false));
            resetTableView();
            cancelBtn.requestFocus();
        }
    }

    private void handleSearchAction() {
        String searchText = employeeIDTextField.getText();

        ArrayList<CheckedEmployee> searchResult;
        if (searchText == null || searchText.isEmpty()) {
            resetTableView();
        } else {
            searchResult = new ArrayList<>();
            checkedEmployees.stream().filter(x -> x.getEmployee().getEmployeeID().contains(searchText)).forEach(searchResult::add);
            if (!searchResult.isEmpty()) {
                items.setAll(searchResult);
                employeeTableView.setItems(items);
            }else{
                resetTableView();
            }
        }
    }

    private void handleSearchActionWithKey(KeyCode code){
        if(code == KeyCode.ENTER){
            handleSearchAction();
        }
    }

    private void handleCancelAction(){
        // Tắt cửa sổ hiện tại
        Stage stage = (Stage) shiftIDTextField.getScene().getWindow(); // Lấy `Stage` chứa `scheduleLabel`
        stage.close();
    }

    private void handleAddAction(Shift shift){
        try{

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn phân công ca làm?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ArrayList<CheckedEmployee>  checkedIsTrueEmpl = new ArrayList<>();
                    checkedEmployees.stream().filter(CheckedEmployee::isChecked).forEach(checkedIsTrueEmpl::add);

                    if(checkedIsTrueEmpl.isEmpty()){
                        dialogPane.showWarning("LỖI", "Vui lòng chọn nhân viên để phân ca");
                    }else{
                        String descriptionText = descriptionTextArea.getText();
                        if(descriptionText.isEmpty()){
                            dialogPane.showWarning("LỖI", "Vui lòng không để trống mô tả / ghi chú");
                        }else{
                            checkedIsTrueEmpl.forEach(x->{
                                String currentID = ShiftAssignmentDAO.getShiftAssignmentId();
                                ShiftAssignmentDAO.createData(new ShiftAssignment(currentID
                                        ,descriptionText, shift, x.getEmployee()));
                                createNextShiftAssignmentID(currentID);
                            });
                            DialogPane.Dialog<ButtonType> dialog2 = dialogPane.showInformation("Thông báo", "Đã phân công thành công " );
                            dialog2.onClose(btn->{
                                handleCancelAction();
                            });
                        }
                    }
                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

}
