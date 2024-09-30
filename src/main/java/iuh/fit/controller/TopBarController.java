package iuh.fit.controller;

import iuh.fit.models.Account;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class TopBarController {
    @FXML
    private Text helloLabel;

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
        helloLabel.setText("Xin chào, " + account.getEmployee().getFullName());
    }

    @FXML
    public void initialize() {
    }
}
