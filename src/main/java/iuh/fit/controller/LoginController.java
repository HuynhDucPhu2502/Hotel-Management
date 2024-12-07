package iuh.fit.controller;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.*;
import iuh.fit.security.PasswordHashing;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class LoginController {
    @FXML private TextField userNameField;
    @FXML private PasswordField hiddenPasswordField;
    @FXML private TextField visiblePasswordField;
    @FXML private Button ShowPasswordBtn;
    @FXML private Text errorMessage;
    @FXML private Button signInButton;
    @FXML private ImageView showPassButton;
    @FXML private DialogPane dialogPane;
    @FXML private Text forgotPasswordBtn;
    @FXML private Label loginBtn;
    @FXML private Button confirmBtn;
    @FXML private Button resetBtn;

    @FXML private GridPane loginGrid;
    @FXML private GridPane forgotPasswordGrid;
    @FXML private GridPane restoreDataGrid;

    @FXML private TextField employeeIDTextField;
    @FXML private TextField fullNameTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField cardIDTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField usernameTextField;

    private boolean isDefaultIcon = true;

    @FXML private PasswordField passRestorePasswordField;
    @FXML private TextField passRestoreTextField;
    @FXML private TextField filePathRestoreTextField;
    @FXML private Text passTitleText;
    @FXML private Text filepathTitleText;

    @FXML private Button showPassRestoreButton;
    @FXML private Button filePathRestoreButton;
    @FXML private Button researchFileRestoreButton;

    @FXML private Button refreshPassRestoreButton;
    @FXML private Button cancelRestoreButton;
    @FXML private Button confirmPassRestoreButton;
    @FXML private Button restoreDataButton;

    @FXML
    public void initialize() {
        dialogPane.toFront();
        registerEventEnterKey();
        hiddenPasswordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());
        passRestorePasswordField.textProperty().bindBidirectional(passRestoreTextField.textProperty());

        ShowPasswordBtn.setOnAction(event -> {
            PasswordVisibility();
            changeButtonIconForShowPasswordBtn();
        });

        signInButton.setOnAction(event -> {
            try {
                signIn();
            } catch (SQLException ignored) {

            }
        });

        Image defaultIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_show_password.png")));
        showPassButton.setImage(defaultIcon);

        forgotPasswordBtn.setOnMouseClicked(event -> forgotPass());
        loginBtn.setOnMouseClicked(event -> login());
        confirmBtn.setOnAction(event -> changePassword());
        resetBtn.setOnAction(event -> resetAction());
    }

    private void registerEventEnterKey() {
        userNameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    signIn();
                } catch (SQLException ignored) {

                }
            }
        });

        hiddenPasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    signIn();
                } catch (SQLException ignored) {

                }
            }
        });

        visiblePasswordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                try {
                    signIn();
                } catch (SQLException ignored) {

                }
            }
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

    private void signIn() throws SQLException {
        if(!RestoreDatabase.isDatabaseExist(DBHelper.getDatabaseName())) {
            errorMessage.setText(ErrorMessages.DATABASE_NOT_FOUND);
            return;
        }

        String userName = userNameField.getText();
        String password = hiddenPasswordField.getText();

        if (userName.isEmpty()) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_USERNAME);
            return;
        }

        if (password.isEmpty()) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_PASSWORD);
            return;
        }

        Account account = AccountDAO.getLogin(userName, password);
        if (account == null) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_ACCOUNT);
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
            else {
                loadMainUI(account, currentShift);
            }
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

    @FXML
    void restoreData() {
        slideOutGridFromTop(loginGrid, restoreDataGrid);
    }

    @FXML
    void backToLoginPanel() {
        slideOutGridFromTop(restoreDataGrid, loginGrid);
        switchFromPassPanelToRestorePanel(true, false);
        passRestorePasswordField.setText("");
        filePathRestoreTextField.setText("");
    }

    @FXML
    void confirmPassRestore() {
        final String key = "7523C62ABDB7628C5A9DAD8F97D8D8C5C040EDE36535E531A8A3748B6CAE7E00";
        final String resourcePath = "/iuh/fit/security/E201065D0554652615C320C00A1D5BC8EDCA469D72C2790E24152D0C1E2B6189.properties";

        String userPassInput = passRestorePasswordField.getText();

        // Kiểm tra mật khẩu người dùng nhập
        if (userPassInput == null || userPassInput.isBlank()) {
            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Thông báo",
                    "Vui lòng nhập mật khẩu",
                    "Nhấn OK để xác nhận"
            ).show();
            return;
        }

        // Kiểm tra file và xác thực mật khẩu
        try {
            String storedHash = PropertiesFile.readFileFromResources(resourcePath, key);
            if (PasswordHashing.hashPassword(userPassInput).equalsIgnoreCase(storedHash)) {
                switchFromPassPanelToRestorePanel(false, true);
            } else {
                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Sai mật khẩu",
                        "Mật khẩu không đúng, vui lòng thử lại.",
                        "Nhấn OK để xác nhận"
                ).show();
            }
        } catch (Exception e) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Lỗi",
                    "Đã xảy ra lỗi khi đọc file từ resources.",
                    "Vui lòng kiểm tra lại file và thử lại."
            ).show();
        }
    }


    private void switchFromPassPanelToRestorePanel(boolean passPanelShow, boolean restorePanelShow){
        passTitleText.setVisible(passPanelShow);
        passTitleText.setManaged(passPanelShow);
        filepathTitleText.setVisible(restorePanelShow);
        filepathTitleText.setManaged(restorePanelShow);

        passRestorePasswordField.setVisible(passPanelShow);
        passRestorePasswordField.setManaged(passPanelShow);
        passRestoreTextField.setVisible(passPanelShow);
        passRestoreTextField.setManaged(passPanelShow);
        filePathRestoreTextField.setVisible(restorePanelShow);
        filePathRestoreTextField.setManaged(restorePanelShow);

        showPassRestoreButton.setVisible(passPanelShow);
        showPassRestoreButton.setManaged(passPanelShow);
        filePathRestoreButton.setVisible(restorePanelShow);
        filePathRestoreButton.setManaged(restorePanelShow);
        researchFileRestoreButton.setVisible(restorePanelShow);
        researchFileRestoreButton.setManaged(restorePanelShow);

        refreshPassRestoreButton.setVisible(passPanelShow);
        refreshPassRestoreButton.setManaged(passPanelShow);
        confirmPassRestoreButton.setVisible(passPanelShow);
        confirmPassRestoreButton.setManaged(passPanelShow);
        cancelRestoreButton.setVisible(restorePanelShow);
        cancelRestoreButton.setManaged(restorePanelShow);
        restoreDataButton.setVisible(restorePanelShow);
        restoreDataButton.setManaged(restorePanelShow);
    }

    @FXML
    void refreshPassRestore() {
        passRestorePasswordField.setText("");
        passRestorePasswordField.requestFocus();
    }

    @FXML
    void showPassRestore() {
        changeButtonIconForShowPasswordBtn();
        if(passRestorePasswordField.isVisible()){
            passRestoreTextField.setVisible(true);
            passRestoreTextField.setManaged(true);
            passRestorePasswordField.setVisible(false);
            passRestorePasswordField.setManaged(false);
        }else{
            passRestoreTextField.setVisible(false);
            passRestoreTextField.setManaged(false);
            passRestorePasswordField.setVisible(true);
            passRestorePasswordField.setManaged(true);
        }
    }

    @FXML
    void confirmRestoreData() throws SQLException {
        String filePath = filePathRestoreTextField.getText();
        if (filePath == null || filePath.isBlank()) {
            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Thông báo",
                    "Hãy chọn tệp dữ liệu để khôi phục.",
                    "Nhấn OK để xác nhận."
            ).show();
            return;
        }

        String[] parts = filePath.split("\\\\");
        String fileName = parts[parts.length - 1];
        if (!fileName.contains(".bak")) {
            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Thông báo",
                    "Sai loại tệp, tệp phải có đuôi .bak.",
                    "Nhấn OK để xác nhận."
            ).show();
            return;
        }

        String databaseName = "HotelDatabase";
        if (RestoreDatabase.isDatabaseExist(databaseName)) {
            Optional<ButtonType> optional = showMessage(
                    Alert.AlertType.CONFIRMATION,
                    "Thông báo",
                    "Dữ liệu đã tồn tại. Bạn có muốn ghi đè không?",
                    "Nhấn OK để ghi đè, Cancel để hủy."
            ).showAndWait();

            if (optional.isPresent() && optional.get().equals(ButtonType.OK)) {
                try {
                    RestoreDatabase.restoreFullWhenNoDB(filePath);
                    System.out.println("Database restored successfully!");
                } catch (Exception e) {
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Thông báo",
                            "Tệp dữ liệu phục hồi rỗng.",
                            "Nhấn OK để xác nhận."
                    ).show();
                }
            }
        } else {
            try {
                RestoreDatabase.restoreFullWhenNoDB(filePath);
                System.out.println("Database restored successfully!");
            } catch (Exception e) {
                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Thông báo",
                        "Tệp dữ liệu phục hồi rỗng.",
                        "Nhấn OK để xác nhận."
                ).show();
            }
        }
    }


    @FXML
    void cancelRestore() {
        switchFromPassPanelToRestorePanel(true, false);
        filePathRestoreTextField.setText("");
    }

    @FXML
    void researchFileRestore() {

    }

    @FXML
    void getFilePath() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(null);

        if(f == null) return;
        filePathRestoreTextField.setText(f.getAbsolutePath());
    }

    private Alert showMessage(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}
