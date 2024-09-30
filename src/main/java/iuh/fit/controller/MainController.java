package iuh.fit.controller;

import iuh.fit.models.Account;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainController {
    private Account account;

    @FXML
    private AnchorPane topBar;
    private TopBarController topBarController;

    public void setAccount(Account account) {
        this.account = account;

        initializeTopBar();
    }

    // Phương thức khởi tạo phần topBar khi đã có account
    private void initializeTopBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/Topbar.fxml"));
            AnchorPane topBarPane = loader.load();

            topBar.getChildren().clear();
            topBar.getChildren().addAll(topBarPane.getChildren());

            topBarController = loader.getController();
            if (topBarController != null) {
                topBarController.setAccount(account);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }
}
