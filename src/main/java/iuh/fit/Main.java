package iuh.fit;

import iuh.fit.controller.MainController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.utils.RoomStatusHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        RoomStatusHelper.startAutoCheckoutScheduler();

//        startWithoutLogin(primaryStage);
        startWithLogin(primaryStage);


    }

    // Khởi động chương trình không cần đăng nhập
    public void startWithoutLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
            AnchorPane root = loader.load();

            Account account = AccountDAO.getLogin("huynhducphu", "test123@");
            MainController mainController = loader.getController();
            mainController.setAccount(account);

            Scene scene = new Scene(root);

            primaryStage.setTitle("Quản Lý Khách Sạn");

            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
            primaryStage.centerOnScreen();

            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Khởi động chương trình cần đăng nhập
    public void startWithLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);

            primaryStage.setTitle("Quản Lý Khách Sạn");

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setWidth(610);
            primaryStage.setHeight(400);
            primaryStage.setMaximized(false);
            primaryStage.centerOnScreen();

            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}