package iuh.fit.controller.features.employee_information;

import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;

public class EmployeeInformationController {
    @FXML
    private TextField employeeIDTextField, employeeFullnameTextField,
            employeePhoneNumberTextField, employeeEmailTextField,
            employeeGenderTextField, employeeIDCardNumberTextField,
            employeeDOBTextField, employeeRoleTextField;

    @FXML
    private TextArea employeeAddressTextArea;

    @FXML
    private Button updateEmployeeInformationBtn, updateAccountPasswordBtn;

    private Employee employee;
    private MainController mainController;

    public void initialize() {

    }

    public void setupContext(Employee employee, MainController mainController) {
        this.employee = employee;
        this.mainController = mainController;

        setupButtonActions();
        loadData();
    }

    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                String employeeID = employee.getEmployeeID();
                String fullName = employee.getFullName();
                String phoneNumber = employee.getPhoneNumber();
                String email = employee.getEmail();
                String gender = employee.getGender().toString();
                String idCardNumber = employee.getIdCardNumber();
                String dob = dateTimeFormatter.format(employee.getDob());
                String address = employee.getAddress();
                String role = employee.getPosition().toString();

                Platform.runLater(() -> {
                    employeeIDTextField.setText(employeeID);
                    employeeFullnameTextField.setText(fullName);
                    employeePhoneNumberTextField.setText(phoneNumber);
                    employeeEmailTextField.setText(email);
                    employeeGenderTextField.setText(gender);
                    employeeIDCardNumberTextField.setText(idCardNumber);
                    employeeDOBTextField.setText(dob);
                    employeeAddressTextArea.setText(address);
                    employeeRoleTextField.setText(role);
                });
                return null;
            }
        };

        new Thread(loadDataTask).start();
    }

    private void setupButtonActions() {
        updateEmployeeInformationBtn.setOnAction(e -> navigateToUpdatingEmployeePanel());
        updateAccountPasswordBtn.setOnAction(e -> navigateToPasswordChangingPanel());
    }

    private void navigateToUpdatingEmployeePanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/employee_information/UpdatingEmployeePanel.fxml"));
            AnchorPane layout = loader.load();

            UpdatingEmployeeController updatingEmployeeController = loader.getController();
            updatingEmployeeController.setupContext(employee, mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToPasswordChangingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/employee_information/PasswordChangingPanel.fxml"));
            AnchorPane layout = loader.load();

            PasswordChangingController passwordChangingController = loader.getController();
            passwordChangingController.setupContext(employee, mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
