package iuh.fit.controller;

import iuh.fit.controller.features.MenuController;
import iuh.fit.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MainController {
    private Account account;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private AnchorPane mainPanel;

    @FXML
    public void initialize() {
    }


    public void setAccount(Account account) {
        this.account = account;
        initializeMenuBar();
    }

    private void initializeMenuBar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/MenuPanel.fxml"));
            AnchorPane menuLayout = loader.load();

            MenuController menuController = loader.getController();
            if (menuController != null) {
                menuController.setAccount(account);

                menuController.getServiceCategoryButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/ServiceCategoryManagerPanel.fxml"));
                menuController.getHotelServiceButton().setOnAction(event -> loadPanel("/iuh/fit/view/features/service/HotelServiceManagerPanel.fxml"));
            }

            menuBar.getChildren().clear();
            menuBar.getChildren().addAll(menuLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPanel(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane layout = loader.load();

            mainPanel.getChildren().clear();
            mainPanel.getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
