package iuh.fit.controller;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.ErrorMessages;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
        }
    }
}
