package iuh.fit.controller;

import iuh.fit.controller.features.bar.MenuController;
import iuh.fit.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MainController {
    private Account account;

    @FXML
    private AnchorPane menuBar;

    public void setAccount(Account account) {
        this.account = account;

        initializeTopBar();
    }

    // Phương thức khởi tạo phần topBar khi đã có account
    private void initializeTopBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/bar/MenuPanel.fxml"));
            AnchorPane menuLayout = loader.load();

            MenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.setAccount(account);
            }

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
    }
}
