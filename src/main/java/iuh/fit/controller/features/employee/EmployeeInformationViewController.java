package iuh.fit.controller.features.employee;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public class EmployeeInformationViewController {
    @FXML
    private TextArea addressTextAria;

    @FXML
    private TextField employeeIDTextField, fullNameTextField,
            phoneNumberTextField, emailTextField,
            genderTextField, cardIDTextField,
            dobTextField, positionTextField;


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

    }
}
