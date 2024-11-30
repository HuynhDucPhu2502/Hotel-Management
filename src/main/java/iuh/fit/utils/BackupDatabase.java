package iuh.fit.utils;

import iuh.fit.security.PreferencesKey;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class BackupDatabase {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void backupDifDatabase(String filePath) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String backupQuery = "BACKUP DATABASE [HotelDatabase] TO DISK = '" + filePath + "' " + " WITH DIFFERENTIAL ";
        if(connection == null) throw new IllegalArgumentException("Connection is NULL!!!");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(backupQuery);
            preparedStatement.executeQuery();
            System.out.println("Backup completed: " + filePath);
        }catch (Exception ignored){

        }
    }

    public static void backupFullDatabase(String filePath) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String backupQuery = "BACKUP DATABASE [HotelDatabase] TO DISK = '" + filePath + "' ";
        if(connection == null) throw new IllegalArgumentException("Connection is NULL!!!");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(backupQuery);
            preparedStatement.executeQuery();
            System.out.println("Backup completed: " + filePath);
        }catch (Exception ignored){

        }
    }

    // handle backup event when slose the app
    public static void backupData(Stage primaryStage) throws SQLException {
        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_VALUE)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_NO_VALUE)) System.exit(0);

        String defaultFullBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL.bak";
        String defaultBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF.bak";
        String defaultZipBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF.zip";
        String zipFilePath;
        String filePath = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH);
        if(filePath.equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)){
            System.out.println("Chon dia chi di");
            return;
        } else{
            filePath = filePath + defaultBackupName;
            zipFilePath = FilePathManager.getPath(
                    PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                    PreferencesKey.DEFAULT_FILE_PATH) + defaultZipBackupName;
        }

        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_FILE_PATH)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            try {
                if(new File(filePath).exists()) new File(filePath).delete();
                BackupDatabase.backupDifDatabase(filePath);
                if(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, PreferencesKey.DEFAULT_VALUE)
                        .equalsIgnoreCase("1")){
                    CompressFile.compressWithPassword(filePath, zipFilePath, null);
                    new File(filePath).delete();
                }
                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Sao luu thanh cong",
                        "Du lieu da duoc sao luu thanh cong",
                        "Nhan ok de Xac nhan"
                ).showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        else if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, PreferencesKey.DEFAULT_FILE_PATH)
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sao luu du lieu");
            alert.setHeaderText("Ban co muon sao luu du lieu cua ngay hom nay khong");
            alert.setContentText("Nhan ok de LUU, cancel thi KHONG");

            if(alert.showAndWait().get() == ButtonType.OK){
                try {
                    if(new File(filePath).exists()) new File(filePath).delete();
                    BackupDatabase.backupDifDatabase(filePath);
                    if(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, PreferencesKey.DEFAULT_VALUE)
                            .equalsIgnoreCase("1")){
                        CompressFile.compressWithPassword(filePath, zipFilePath, null);
                        new File(filePath).delete();
                    }
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Sao luu thanh cong",
                            "Du lieu da duoc sao luu thanh cong",
                            "Nhan ok de Xac nhan"
                    ).showAndWait();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.exit(0);
            }
        }
        System.exit(0);
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
