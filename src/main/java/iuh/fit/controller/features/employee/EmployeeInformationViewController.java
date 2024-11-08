package iuh.fit.controller.features.employee;

import iuh.fit.models.Account;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.utils.ConvertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmployeeInformationViewController {
    @FXML
    private TextArea addressTextAria;

    @FXML
    private TextField cardIDTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField employeeIDTextField;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField genderTextField;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField statusTextField;

    public void setEmployee(Employee employee, Account account) {
        employeeIDTextField.setText(employee.getEmployeeID());
        fullNameTextField.setText(employee.getFullName());
        emailTextField.setText(employee.getEmail());
        phoneNumberTextField.setText(employee.getPhoneNumber());
        addressTextAria.setText(employee.getAddress());
        cardIDTextField.setText(employee.getIdCardNumber());
        positionTextField.setText(employee.getPosition().name().equalsIgnoreCase("MANAGER")?"QUẢN LÝ":"LỄ TÂN");

        dobDatePicker.setValue(employee.getDob());
        genderTextField.setText(employee.getGender().name().equalsIgnoreCase("MALE")?"NAM":"NỮ");

        if(account != null){
            passwordTextField.setText(account.getPassword());
            usernameTextField.setText(account.getUserName());
            String status = switch (account.getAccountStatus().name()) {
                case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
                case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
                case "LOCKED" -> "BỊ KHÓA";
                default -> account.getAccountStatus().name();
            };

            statusTextField.setText(status);
        }
    }
}
