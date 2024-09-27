package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private PasswordField hiddenPasswordField; // ẩn mật khẩu

    @FXML
    private TextField visiblePasswordField; // hiện mật khẩu

    @FXML
    private CheckBox passwordCheckbox; // checkbox ẩn hiện mật khẩu

    @FXML
    private void initialize() {
        hiddenPasswordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        passwordCheckbox.setOnAction(event -> {
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
        });
    }
}
