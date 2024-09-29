package iuh.fit.controller;

import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.utils.ErrorMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField hiddenPasswordField; // ẩn mật khẩu

    @FXML
    private TextField visiblePasswordField; // hiện mật khẩu

    @FXML
    private Button ShowPasswordBtn; // checkbox ẩn hiện mật khẩu

    @FXML
    private Text errorMessage;

    @FXML
    private Button signInButton;

    @FXML
    private ImageView showPassButton; // Reference to the ImageView in FXML

    private boolean isDefaultIcon = true; // A flag to track the current icon state


    @FXML
    private void initialize() {
        hiddenPasswordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        ShowPasswordBtn.setOnAction(event -> {
            PasswordVisibility();
            changeButtonIconForShowPasswordBtn();
        });
        signInButton.setOnAction(event -> signIn());

        Image defaultIcon = new Image(getClass().getResourceAsStream("/iuh/fit/icons/show_password_icon.png"));
        showPassButton.setImage(defaultIcon);
    }

    // Event handler for the button click
    @FXML
    public void changeButtonIconForShowPasswordBtn() {
        if (isDefaultIcon) {
            // Change to a new icon when clicked
            Image newIcon = new Image(getClass().getResourceAsStream("/iuh/fit/icons/unshow_password_icon.png"));
            showPassButton.setImage(newIcon);
        } else {
            // Revert to the original icon
            Image defaultIcon = new Image(getClass().getResourceAsStream("/iuh/fit/icons/show_password_icon.png"));
            showPassButton.setImage(defaultIcon);
        }

        // Toggle the flag to track icon state
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

        if (account == null) {
            errorMessage.setText(ErrorMessages.LOGIN_INVALID_ACCOUNT);
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/Dashboard.fxml"));
                Scene dashboardScene = new Scene(fxmlLoader.load());

                // Lấy Stage hiện tại và set Scene mới
                Stage currentStage = (Stage) signInButton.getScene().getWindow();
                currentStage.setScene(dashboardScene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
