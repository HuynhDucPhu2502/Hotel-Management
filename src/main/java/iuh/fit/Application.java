package iuh.fit;

import iuh.fit.controller.LoginController;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.utils.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public class Application extends javafx.application.Application {

    private NotificationButtonController topBarController;
    private boolean isNotificationControllerSet = false;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        startWithLogin(primaryStage);
        //startWithoutLogin(primaryStage);
    }

    public void setNotificationControllerForMain(NotificationButtonController controller){
        this.topBarController = controller;
        isNotificationControllerSet = true;
        System.out.println("KET QUA NOTIFYCONTROLLER (6): "+topBarController);
    }

//    // Khởi động chương trình không cần đăng nhập
//    public void startWithoutLogin(Stage primaryStage) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
//            AnchorPane root = loader.load();
//
//            Account account = AccountDAO.getLogin("huynhducphu", "test123@");
//            MainController mainController = loader.getController();
//            topBarController = mainController.setContext(account, topBarController);
//
//            Scene scene = new Scene(root);
//
//            primaryStage.setTitle("Quản Lý Khách Sạn");
//
//            primaryStage.setScene(scene);
//            primaryStage.setResizable(true);
//            primaryStage.setWidth(1200);
//            primaryStage.setHeight(680);
//            primaryStage.setMaximized(true);
//            primaryStage.centerOnScreen();
//
//            primaryStage.show();
//
//            primaryStage.setOnCloseRequest(event -> {
//                Platform.exit();
//                System.exit(0);
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // Khởi động chương trình cần đăng nhập
    public void startWithLogin(Stage primaryStage) throws SQLException {
        loadUI(primaryStage);

        if(!RestoreDatabase.isDatabaseExist("HotelDatabase")) return;

        handelCloseButton(primaryStage);
    }

    private void loadUI(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane root = loader.load();

            LoginController controller = loader.getController();
            controller.initialize(this);

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

    private void handelCloseButton(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            try {
                BackupDatabase.backupData(primaryStage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}