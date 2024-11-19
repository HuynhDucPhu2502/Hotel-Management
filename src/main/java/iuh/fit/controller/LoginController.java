package iuh.fit.controller;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.PasswordHashing;
import iuh.fit.utils.RegexChecker;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.util.Objects;

public class LoginController {
    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField hiddenPasswordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button ShowPasswordBtn;

    @FXML
    private Text errorMessage;

    @FXML
    private Button signInButton;

    @FXML
    private ImageView showPassButton;

    @FXML
    private DialogPane dialogPane;

    @FXML
    private Text forgotPasswordBtn;
    @FXML
    private Label loginBtn;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button resetBtn;

    @FXML
    private GridPane loginGrid;

    @FXML
    private GridPane forgotPasswordGrid;

    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField cardIDTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField usernameTextField;

    private boolean isDefaultIcon = true;

    @FXML
    private void initialize() {
        dialogPane.toFront();
        registerEventEnterKey();
        hiddenPasswordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        ShowPasswordBtn.setOnAction(event -> {
            PasswordVisibility();
            changeButtonIconForShowPasswordBtn();
        });
        signInButton.setOnAction(event -> signIn());

        Image defaultIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_show_password.png")));
        showPassButton.setImage(defaultIcon);

        forgotPasswordBtn.setOnMouseClicked(event -> forgotPass());
        loginBtn.setOnMouseClicked(event -> login());
        confirmBtn.setOnAction(event -> changePassword());
        resetBtn.setOnAction(event -> resetAction());
    }

    private void registerEventEnterKey() {
        userNameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) signIn();
        });

        hiddenPasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) signIn();
        });

        visiblePasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) signIn();
        });
    }

    @FXML
    private void changeButtonIconForShowPasswordBtn() {
        if (isDefaultIcon) {
            Image newIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_hidden_password.png")));
            showPassButton.setImage(newIcon);
        } else {
            Image defaultIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_show_password.png")));
            showPassButton.setImage(defaultIcon);
        }

        isDefaultIcon = !isDefaultIcon;
    }

    private void PasswordVisibility() {
        if (hiddenPasswordField.isVisible()) {
            hiddenPasswordField.setVisible(false);
            hiddenPasswordField.setManaged(false);
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
        } else {
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            hiddenPasswordField.setVisible(true);
            hiddenPasswordField.setManaged(true);
        }
    }

    private void signIn() {
        String userName = userNameField.getText();
        if (userName.isEmpty()) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_USERNAME);
            return;
        }

        String password = hiddenPasswordField.getText();
        if (password.isEmpty()) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_PASSWORD);
            return;
        }

        Account account = AccountDAO.getLogin(userName, password);

        // Kiểm tra tài khoản có tồn tại
        if (account == null) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_ACCOUNT);
        } else {
            if(account.getAccountStatus().equals(AccountStatus.INACTIVE)||
                    account.getAccountStatus().equals(AccountStatus.LOCKED)){
                dialogPane.showInformation("Thông báo", "Tài khoản bị khóa hoặc không có hiệu lực" +
                        "\nVui lòng báo người quản lý khách sạn để biết thêm thông tin");

            }else{
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
                    AnchorPane mainPanel = fxmlLoader.load();

                    MainController mainController = fxmlLoader.getController();

                    mainController.setupContext(account, mainController.getMainStage());


                    Scene scene = new Scene(mainPanel);
                    Stage currentStage = (Stage) signInButton.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.show();
                    currentStage.setResizable(true);
                    currentStage.setMaximized(true);
                    currentStage.centerOnScreen();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }

        // Kiểm tra trạng thái tài khoản
        if (
                account.getAccountStatus().equals(AccountStatus.INACTIVE)
                || account.getAccountStatus().equals(AccountStatus.LOCKED)
        ) {
            dialogPane.showInformation(
                    "Thông báo",
                    "Tài khoản bị khóa hoặc không có hiệu lực.\n" +
                            "Vui lòng báo người quản lý khách sạn để biết thêm thông tin."
            );
            return;
        }

        // Lấy thông tin cần thiết
        Position position = account.getEmployee().getPosition();
        Shift currentShift = ShiftDAO.getCurrentShiftForLogin(account.getEmployee());

        if (position.equals(Position.RECEPTIONIST)) {
            if (currentShift == null)
                dialogPane.showInformation(
                        "Thông báo",
                        "Nhân viên không thuộc ca làm việc hiện tại\n" +
                                "Không thể đăng nhập"
                );
            else loadMainUI(account, currentShift);
        } else if (position.equals(Position.MANAGER)) {
            loadMainUI(account, currentShift);
        }
    }

    private void loadMainUI(Account account, Shift currentShift) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
            AnchorPane mainPanel = fxmlLoader.load();

            MainController mainController = fxmlLoader.getController();

            mainController.setAccount(account);
            mainController.setShift(currentShift);


            Scene scene = new Scene(mainPanel);
            Stage currentStage = (Stage) signInButton.getScene().getWindow();

            currentStage.setWidth(1200);
            currentStage.setHeight(680);
            currentStage.setScene(scene);
            currentStage.setResizable(true);
            currentStage.setMaximized(true);

            currentStage.centerOnScreen();

            currentStage.show();
        } catch (Exception e) {
            errorMessage.setText(e.getMessage());
        }
    }

    private void forgotPass(){
        slideOutGridFromBot(loginGrid, forgotPasswordGrid);
    }

    private void login(){
        slideOutGridFromTop(forgotPasswordGrid, loginGrid);
    }

    public void slideOutGridFromBot(GridPane gridPaneOut, GridPane gridPaneIn) {
        gridPaneIn.setVisible(true);
        gridPaneIn.setTranslateY(gridPaneOut.getHeight());
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), gridPaneOut);
        slideOut.setFromY(0);
        slideOut.setToY(-gridPaneOut.getHeight());
        slideOut.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), gridPaneIn);
        slideIn.setFromY(gridPaneOut.getHeight());
        slideIn.setToY(0);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        ParallelTransition parallelTransition = new ParallelTransition(slideOut, slideIn);
        parallelTransition.setOnFinished(event -> {
            gridPaneOut.setVisible(false);
            gridPaneOut.setTranslateY(0);
        });

        parallelTransition.play();
    }

    public void slideOutGridFromTop(GridPane gridPaneOut, GridPane gridPaneIn) {
        gridPaneIn.setVisible(true);
        gridPaneIn.setTranslateY(-gridPaneOut.getHeight());
        TranslateTransition slideOut = new TranslateTransition(Duration.millis(500), gridPaneOut);
        slideOut.setFromY(0);
        slideOut.setToY(gridPaneOut.getHeight());
        slideOut.setInterpolator(Interpolator.EASE_BOTH);
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), gridPaneIn);
        slideIn.setFromY(-gridPaneOut.getHeight());
        slideIn.setToY(0);
        slideIn.setInterpolator(Interpolator.EASE_BOTH);
        ParallelTransition parallelTransition = new ParallelTransition(slideOut, slideIn);
        parallelTransition.setOnFinished(event -> {
            gridPaneOut.setVisible(false);
            gridPaneOut.setTranslateY(0);
        });
        parallelTransition.play();
    }

    private void changePassword() {
        String employeeID = employeeIDTextField.getText();
        String fullName = fullNameTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String cardID = cardIDTextField.getText();
        String email = emailTextField.getText();
        String username = usernameTextField.getText();

        if (
                employeeID.isBlank()
                || fullName.isBlank()
                || phoneNumber.isBlank()
                || cardID.isBlank()
                || email.isBlank()
                || username.isBlank()
        ) {
            dialogPane.showWarning(
                    "Cảnh báo",
                    "Bạn phải nhập đầy đủ thông tin xác nhận để có thể thay đổi mật khẩu"
            );
            return;
        }

        Employee employee = EmployeeDAO.getEmployeeByEmployeeID(employeeID);
        Account account = AccountDAO.getAccountByEmployeeID(employeeID);

        if (employee == null || account == null) {
            dialogPane.showWarning(
                    "Cảnh báo",
                    "Thông tin bạn nhập chưa chính xác.\nXin vui lòng nhập lại!!!");
            return;
        }


        if (
                !employee.getFullName().equals(fullName)
                || !employee.getPhoneNumber().equals(phoneNumber)
                || !employee.getIdCardNumber().equals(cardID)
                || !employee.getEmail().equals(email)
                || !account.getUserName().equals(username)
        ) {
            dialogPane.showWarning(
                    "Cảnh báo",
                    "Thông tin bạn nhập chưa chính xác.\nXin vui lòng nhập lại!!!"
            );
            return;
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(20, 10, 10, 10));
        Label successLabel = new Label("Xác thực thành công");
        successLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Nhập mật khẩu mới");
        newPasswordField.setPrefWidth(250);
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Xác nhận mật khẩu mới");
        confirmPasswordField.setPrefWidth(250);
        content.getChildren().addAll(
                successLabel,
                new VBox(5, new Label("Mật khẩu mới:"), newPasswordField),
                new VBox(5, new Label("Xác nhận mật khẩu:"), confirmPasswordField)
        );
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Cập nhật mật khẩu");
        dialog.getDialogPane().setContent(content);
        ButtonType confirmButton = new ButtonType("Xác nhận", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirmButton);
        Button confirmButtonElement = (Button) dialog.getDialogPane().lookupButton(confirmButton);
        confirmButtonElement.addEventFilter(ActionEvent.ACTION, e -> {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng nhập đầy đủ thông tin");
                alert.showAndWait();
                e.consume();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Lỗi");
                alert.setContentText("Mật khẩu xác nhận không khớp");
                alert.showAndWait();
                e.consume();
                return;
            }

            if (!RegexChecker.isValidPassword(newPassword)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Lỗi");
                alert.setContentText("Mật khẩu mới không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.");
                alert.showAndWait();
                e.consume();
                return;
            }
            String hashedNewPassword = PasswordHashing.hashPassword(newPassword);
            if (hashedNewPassword.equals(account.getPassword())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Lỗi");
                alert.setContentText("Mật khẩu mới phải khác mật khẩu cũ.");
                alert.showAndWait();
                e.consume();
                return;
            }
            account.setPassword(hashedNewPassword);
            AccountDAO.updateData(account);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cập nhật thành công");
            alert.setContentText("Cập nhật mật khẩu thành công!\n Bây giờ bạn có thể đăng nhập lại.");
            alert.showAndWait();
            resetAction();
        });
        dialog.showAndWait();
    }

    private void resetAction(){
        employeeIDTextField.setText("");
        fullNameTextField.setText("");
        phoneNumberTextField.setText("");
        cardIDTextField.setText("");
        emailTextField.setText("");
        usernameTextField.setText("");
    }
}
