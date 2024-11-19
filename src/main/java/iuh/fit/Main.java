package iuh.fit;

import iuh.fit.controller.MainController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.BackupDatabase;
import iuh.fit.utils.CompressFile;
import iuh.fit.utils.FilePathManager;
import iuh.fit.utils.RoomStatusHelper;
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

@SuppressWarnings("unused")
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        RoomStatusHelper.startAutoCheckoutScheduler();

//        startWithoutLogin(primaryStage);
        startWithLogin(primaryStage);

        // handle backup event when slose the app
        primaryStage.setOnCloseRequest(event -> {
           backupData(primaryStage);
        });
    }

    // handle backup event when slose the app
    private void backupData(Stage primaryStage){
        String defaultBackupName = "/HotelBackup-" + LocalDate.now() + "-Full.bak";
        String defaultZipBackupName = "/HotelBackup-" + LocalDate.now() + "-Full.zip";
        String zipFilePath = "";
        String filePath = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH).equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)
                ? null
                : FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH) + defaultBackupName;

        if(filePath == null) throw new IllegalArgumentException("File path is NULL!!!");
        else zipFilePath = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH) + defaultZipBackupName;

        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_FILE_PATH)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            try {
                BackupDatabase.backupDatabase(filePath);
                if(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, PreferencesKey.DEFAULT_VALUE)
                        .equalsIgnoreCase("1")){
                    CompressFile.compressWithPassword(filePath, zipFilePath, null);
                    new File(filePath).delete();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        else if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_FILE_PATH)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("logout");
            alert.setHeaderText("logout ne");
            alert.setContentText("do you wanana logout");

            if(alert.showAndWait().get() == ButtonType.OK){
                try {
                    BackupDatabase.backupDatabase(filePath);
                    if(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, PreferencesKey.DEFAULT_VALUE)
                            .equalsIgnoreCase("1")){
                        CompressFile.compressWithPassword(filePath, zipFilePath, null);
                        new File(filePath).delete();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.exit(0);
            }
        }
        System.exit(0);
    }

    // Khởi động chương trình không cần đăng nhập
    public void startWithoutLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/MainUI.fxml"));
            AnchorPane root = loader.load();

            Account account = AccountDAO.getLogin("huynhducphu", "test123@");
            MainController mainController = loader.getController();
            mainController.setupContext(account, primaryStage);

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