package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.Position;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShiftInformationViewCotroller {

    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> employeeIDColumn, fullNameColumn, phoneNumberColumn;
    @FXML
    private TableColumn<Employee, Position> positionColumn;

    @FXML
    private Label shiftIDLabel, startTimeLabel, endTimeLabel, scheduleLabel, numberOfHourLabel, modifiedDateLabel;

    @FXML
    private DialogPane dialogPane;

    private ObservableList<Employee> employeeItems;

    private Shift theShift;

    public void initialize(Shift shift) {
        dialogPane.toFront();
        this.theShift = shift;

        if(shift==null){
            dialogPane.showInformation("Thông báo","Không tìm thấy thông tin ca làm này");
        }else{
            setUpLabel();
            loadEmployeeData();
            setupEmployeeTable();
        }

    }

    public void loadEmployeeData() {
        ArrayList<Employee> employees = (ArrayList<Employee>) ShiftDAO.getEmployeeListByShift(theShift.getShiftID());

        employeeItems = FXCollections.observableArrayList(employees);
        employeeTableView.setItems(employeeItems);
        employeeTableView.refresh();
    }

    private void setupEmployeeTable() {
        employeeIDColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEmployeeID()));
        fullNameColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFullName()));
        positionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPosition()));
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPhoneNumber()));

        employeeTableView.setItems(employeeItems);

    }

    private void setUpLabel(){
        shiftIDLabel.setText(theShift.getShiftID());
        startTimeLabel.setText(theShift.getStartTime().toString());
        endTimeLabel.setText(theShift.getEndTime().toString());
        scheduleLabel.setText(theShift.getShiftDaysSchedule().toString());
        numberOfHourLabel.setText(String.valueOf(theShift.getNumberOfHour()));
        String format = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        modifiedDateLabel.setText(theShift.getUpdatedDate().format(formatter));
    }
}
