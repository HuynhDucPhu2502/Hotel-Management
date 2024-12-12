package iuh.fit.controller.features.employee;

import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.ShiftAssignmentDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.Shift;
import iuh.fit.models.ShiftAssignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeeInformationViewController {
    @FXML
    private TextArea addressTextAria;

    @FXML
    private TextField employeeIDTextField, fullNameTextField,
            phoneNumberTextField, emailTextField,
            genderTextField, cardIDTextField,
            dobTextField, positionTextField;
    @FXML
    private TableView<Shift> shiftTableView;
    @FXML
    private TableColumn<Shift, String> shiftIDColumn;
    @FXML
    private TableColumn<Shift, String> startTimeColumn;
    @FXML
    private TableColumn<Shift, String> hourOfWorkColumn;
    @FXML
    private TableColumn<Shift, String> endTimeColumn;
    @FXML
    private TableColumn<Shift, String> scheduleColumn;

    private ObservableList<Shift> items;


    public void setEmployee(Employee employee) {
        employeeIDTextField.setText(employee.getEmployeeID());
        fullNameTextField.setText(employee.getFullName());
        emailTextField.setText(employee.getEmail());
        phoneNumberTextField.setText(employee.getPhoneNumber());
        addressTextAria.setText(employee.getAddress());
        cardIDTextField.setText(employee.getIdCardNumber());
        positionTextField.setText(employee.getPosition().name());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        dobTextField.setText(dateTimeFormatter.format(employee.getDob()));
        genderTextField.setText(employee.getGender().name());

        loadTable(employee);

    }

    private void loadTable(Employee employee){
        List<Shift> shifts = ShiftAssignmentDAO.getShiftIDByEmployeeID(employee.getEmployeeID());
        items = FXCollections.observableArrayList(shifts);

        shiftIDColumn.setCellValueFactory(new PropertyValueFactory<>("shiftID"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        hourOfWorkColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfHour"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("shiftDaysSchedule"));

        shiftTableView.setItems(items);
    }
}
