package iuh.fit.controller.features.employee_information;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class UpdatingEmployeeController {
    @FXML
    private Button backBtn, employeeInformationNavigate;

    @FXML
    private Button resetBtn, updateBtn;

    @FXML
    private Label employeeIDLabel, employeeIDCardNumberLabel;

    @FXML
    private TextField employeeFullnameTextField, employeePhoneNumberTextField,
            employeeEmailTextField;

    @FXML
    private TextArea employeeAddressTextArea;

    @FXML
    private RadioButton radMale, radFemale;

    @FXML
    private CalendarPicker employeeDOBCalendarPicker;

    @FXML
    private DialogPane dialogPane;

    private Employee employee;
    private MainController mainController;

    public void initialize() {
        dialogPane.toFront();
    }


    public void setupContext(Employee employee, MainController mainController) {
        this.employee = employee;
        this.mainController = mainController;

        setupButtonActions();
        loadData();
    }

    private void loadData() {
        employeeIDLabel.setText(String.valueOf(employee.getEmployeeID()));
        employeeIDCardNumberLabel.setText(String.valueOf(employee.getIdCardNumber()));
        employeeFullnameTextField.setText(employee.getFullName());
        employeePhoneNumberTextField.setText(employee.getPhoneNumber());
        employeeEmailTextField.setText(employee.getEmail());
        employeeAddressTextArea.setText(employee.getAddress());

        if (employee.getGender().equals(Gender.MALE)) radMale.setSelected(true);
        else radFemale.setSelected(true);

        employeeDOBCalendarPicker.setValue(employee.getDob());
    }

    private void setupButtonActions() {
        backBtn.setOnAction(e -> navigateToEmployeeInformationPanel());
        employeeInformationNavigate.setOnAction(e -> navigateToEmployeeInformationPanel());

        resetBtn.setOnAction(e -> loadData());
        updateBtn.setOnAction(e -> handleUpdateAction());
    }

    private void handleUpdateAction() {
        try {
            Employee newEmployee = new Employee(
                    employee.getEmployeeID(),
                    employeeFullnameTextField.getText(),
                    employeePhoneNumberTextField.getText(),
                    employeeEmailTextField.getText(),
                    employeeAddressTextArea.getText(),
                    employee.getIdCardNumber(),
                    radMale.isSelected() ? Gender.MALE : Gender.FEMALE,
                    employeeDOBCalendarPicker.getValue(),
                    employee.getPosition()
            );


            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        EmployeeDAO.updateData(newEmployee);
                        employee = newEmployee;
                        mainController.getAccount().setEmployee(newEmployee);
                        mainController.initializeMenuBar();
                        Platform.runLater(this::navigateToEmployeeInformationPanel);
                    } catch (IllegalArgumentException e) {
                        dialogPane.showWarning("LỖI", e.getMessage());
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void navigateToEmployeeInformationPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/employee_information/EmployeeInformationPanel.fxml"));
            AnchorPane layout = loader.load();

            EmployeeInformationController employeeInformationController = loader.getController();
            employeeInformationController.setupContext(employee, mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
