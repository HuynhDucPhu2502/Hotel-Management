package iuh.fit.controller.features.employee_information;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.utils.PasswordHashing;
import iuh.fit.utils.RegexChecker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class PasswordChangingController {
    @FXML
    private Button backBtn, employeeInformationNavigate;

    @FXML
    private Button updateBtn;

    @FXML
    private PasswordField currentPasswordField, newPasswordField,
            confirmNewPasswordField;

    @FXML
    private DialogPane dialogPane;

    private Employee employee;
    private MainController mainController;
    private Account account;

    public void initialize() {
        dialogPane.toFront();
    }

    public void setupContext(Employee employee, MainController mainController) {
        this.employee = employee;
        this.mainController = mainController;
        this.account = AccountDAO.getAccountByEmployeeID(employee.getEmployeeID());

        if (account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản");
        }

        setupButtonActions();
    }

    public void setupButtonActions() {
        backBtn.setOnAction(e -> navigateToEmployeeInformationPanel());
        employeeInformationNavigate.setOnAction(e -> navigateToEmployeeInformationPanel());

        updateBtn.setOnAction(e -> handleUpdateAction());
    }

    public void handleUpdateAction() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmNewPasswordField.getText();

        if (!PasswordHashing.hashPassword(currentPassword).equals(account.getPassword())) {
            dialogPane.showWarning("Lỗi", "Mật khẩu hiện tại không đúng.");
            return;
        }

        if (!RegexChecker.isValidPassword(newPassword)) {
            dialogPane.showWarning("Lỗi", "Mật khẩu mới không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            dialogPane.showWarning("Lỗi", "Mật khẩu xác nhận không khớp với mật khẩu mới.");
            return;
        }

        String hashedNewPassword = PasswordHashing.hashPassword(newPassword);
        account.setPassword(hashedNewPassword);

        try {
            AccountDAO.updateData(account);
            Platform.runLater(() -> dialogPane.showInformation("Thành công", "Đã đổi mật khẩu thành công"));
            navigateToEmployeeInformationPanel();
        } catch (Exception e) {
            dialogPane.showWarning("Lỗi", "Có lỗi xảy ra khi cập nhật mật khẩu.");
            e.printStackTrace();
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
