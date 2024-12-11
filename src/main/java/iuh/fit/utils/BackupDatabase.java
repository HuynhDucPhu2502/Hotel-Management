package iuh.fit.utils;

import iuh.fit.security.PreferencesKey;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class BackupDatabase {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String settingFilePath = "setting.properties";

    public static void backupDifDatabase(String filePath) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String backupQuery = "BACKUP DATABASE [HotelDatabase] TO DISK = '" + filePath + "' WITH DIFFERENTIAL ";

        if(connection == null) throw new IllegalArgumentException("Connection is NULL!!!");

        try {
            Statement statement = connection.createStatement();
            statement.execute(backupQuery);  // Sử dụng execute() thay vì executeQuery()
            System.out.println("Backup completed: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }


    public static void backupFullDatabase(String filePath) throws SQLException {
        Connection connection = DBHelper.getConnection();

        String backupQuery = "BACKUP DATABASE [HotelDatabase] TO DISK = '" + filePath + "' ";
        if(connection == null) throw new IllegalArgumentException("Connection is NULL!!!");

        try {
            Statement statement = connection.createStatement();
            statement.execute(backupQuery);
            System.out.println("Backup completed: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }


    // handle backup event when slose the app
    public static void backupData(Stage primaryStage) throws SQLException {
        String autoBackupOption = PropertiesFile.readFile(settingFilePath, PreferencesKey.BACK_UP_OPTION_KEY);
        if(autoBackupOption == null || autoBackupOption.equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_NO_VALUE)) {
            if(primaryStage != null){
                System.exit(0);
            }
        }

        String defaultBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF.bak";

        String filePath = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY);
        if(filePath == null || filePath.equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)) return;

        File file = new File(filePath);
        if(!file.exists()) file.mkdirs();

        filePath = filePath + defaultBackupName;

        if(autoBackupOption.equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            try {
                if(primaryStage == null){
                    if(new File(filePath).exists()) new File(filePath).delete();
                    BackupDatabase.backupDifDatabase(filePath);
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Sao lưu thành công",
                            "Dữ liệu đã được sao lưu thành công",
                            "Nhấn OK để xác nhận"
                    ).showAndWait();
                }else{
                    if(new File(filePath).exists()) new File(filePath).delete();
                    BackupDatabase.backupDifDatabase(filePath);
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Sao lưu thành công",
                            "Dữ liệu đã được sao lưu thành công",
                            "Nhấn OK để xác nhận"
                    ).showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        else if(autoBackupOption.equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sao lưu dữ liệu");
            alert.setHeaderText("Bạn có muốn sao lưu dữ liệu của ngày hôm nay không?");
            alert.setContentText("Nhấn OK để lưu, Cancel để hủy");

            if(alert.showAndWait().get() == ButtonType.OK){
                try {
                    if(new File(filePath).exists()) new File(filePath).delete();
                    BackupDatabase.backupDifDatabase(filePath);
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Sao lưu thành công",
                            "Dữ liệu đã được sao lưu thành công",
                            "Nhấn OK để xác nhận"
                    ).showAndWait();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                if(primaryStage != null){
                    System.exit(0);
                }
            }
        }
        if(primaryStage != null){
            System.exit(0);
        }
    }

    public static void checkNumOfBackupFile() throws SQLException {
        File[] files = new File(FilePathManager.getPath(PreferencesKey.RESTORE_FILE, PreferencesKey.DEFAULT_FILE_PATH))
                .getAbsoluteFile().listFiles();
        if(files == null || files.length == 0) return;

        File backupFullFile = Arrays.stream(files)
                .min(Comparator.comparing(x -> new Date(x.lastModified()))).orElse(null);
        File nearestBackupFullFile = Arrays.stream(files)
                .sorted(Comparator.comparing(x -> new Date(x.lastModified())))
                .skip(1)
                .findFirst().orElse(null);

        if(files.length > 31 && nearestBackupFullFile != null){

            String defaultBackupName = "\\HotelBackup-"
                    + new Date(nearestBackupFullFile.lastModified()) + "-FULL.bak";
            String defaultDifBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF.bak";
            String defaultZipBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF.zip";


            BackupDatabase.backupDifDatabase(FilePathManager
                    .getPath(PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY, PreferencesKey.DEFAULT_FILE_PATH) + defaultDifBackupName);


            RestoreDatabase.restoreDif(backupFullFile.getAbsolutePath(), nearestBackupFullFile.getAbsolutePath());

            backupFullFile.delete();
            nearestBackupFullFile.delete();

            BackupDatabase.backupFullDatabase(FilePathManager
                    .getPath(PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY, PreferencesKey.DEFAULT_FILE_PATH) + defaultBackupName);


            File[] newFiles = new File(FilePathManager.getPath(PreferencesKey.RESTORE_FILE, PreferencesKey.DEFAULT_FILE_PATH))
                    .getAbsoluteFile().listFiles();
            if(newFiles == null || newFiles.length == 0) return;
            File latestFile = Arrays.stream(files)
                    .max(Comparator.comparing(x -> new Date(x.lastModified()))).orElse(null);
            latestFile.setLastModified(nearestBackupFullFile.lastModified());


            backupFullFile.delete();
            nearestBackupFullFile.delete();

            File difFile = Arrays.stream(files)
                    .max(Comparator.comparing(x -> new Date(x.lastModified()))).orElse(null);
            RestoreDatabase.restoreDif(latestFile.getAbsolutePath(), difFile.getAbsolutePath());
        }

        System.out.println(new Date(backupFullFile.lastModified()));
        if(nearestBackupFullFile != null)
            System.out.println(new Date(nearestBackupFullFile.lastModified()));
    }

    private static Alert showMessage(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }
}
