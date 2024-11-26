package iuh.fit;

import iuh.fit.controller.MainController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@SuppressWarnings("unused")
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        RoomStatusHelper.startAutoCheckoutScheduler();

        startWithoutLogin(primaryStage);

        //startWithLogin(primaryStage);

        // check if we have 30 backup dif already
        // if > 30, backupfull with the day nearest the backupfule file
        // and remove this file
        // BackupDatabase.checkNumOfBackupFile();

        // handle backup event when slose the app
        primaryStage.setOnCloseRequest(event -> {

//        startWithLogin(primaryStage);

        primaryStage.setOnCloseRequest(event -> backupData(primaryStage));
    }

    private void backupData(Stage primaryStage){
        String defaultBackupName = "/HotelBackup-" + LocalDate.now() + "-Full.bak";
        String defaultZipBackupName = "/HotelBackup-" + LocalDate.now() + "-Full.zip";
        String zipFilePath;
        String filePath = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH).equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)
                ? null
                : FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH) + defaultBackupName;

        if(filePath == null) return;
        else zipFilePath = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH) + defaultZipBackupName;

        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_FILE_PATH)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))

            try {
                BackupDatabase.backupData(primaryStage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
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
            primaryStage.setWidth(1200);
            primaryStage.setHeight(680);
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