package iuh.fit.controller;

import iuh.fit.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainController {
    private Account account;

    @FXML
    private AnchorPane menuBar;
    private MenuBarController menuBarController;

    public void setAccount(Account account) {
        this.account = account;

        initializeTopBar();
    }

    // Phương thức khởi tạo phần topBar khi đã có account
    private void initializeTopBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/Menubar.fxml"));
            AnchorPane menuBarLayout = loader.load();

            menuBarController = loader.getController();
            if (menuBarController != null) {
                menuBarController.setAccount(account);
            }

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuBarLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }
}
