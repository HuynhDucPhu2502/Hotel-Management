package iuh.fit.controller.features.backup;

import iuh.fit.models.wrapper.FileDisplayOnTable;
import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.BackupDatabase;
import iuh.fit.utils.FilePathManager;
import iuh.fit.utils.RestoreDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.prefs.Preferences;

public class BackupAndRestoreController {
    @FXML private RadioButton noBackUpRadioButton;
    @FXML private RadioButton warningBackUpRadioButton;
    @FXML private RadioButton autoBackUpRadioButton;
    @FXML private TextField fileAddressAutoBackupText;
    @FXML private TextField fileAddressHandBackupText;
    @FXML private TextField fileNameHandBackupText;
    @FXML private Text currentUsingDataText;
    @FXML private CheckBox compressFileCheckBox;
    @FXML private TextField fileAddressRestoreText;
    @FXML private Text fileNum;
    @FXML private Text warningText;
    @FXML private Text currenFileUsingText;
    @FXML private Text warningPapaText;
    @FXML private ComboBox<String> backupWaysCombobox;

    @FXML private TableView<FileDisplayOnTable> tableData;
    @FXML private TableColumn<FileDisplayOnTable, String> fileNameColumn;
    @FXML private TableColumn<FileDisplayOnTable, LocalDateTime> createdColumn;
    @FXML private TableColumn<FileDisplayOnTable, String> fileTypeColumn;
    @FXML private TableColumn<FileDisplayOnTable, Double> sizeColumn;
    @FXML private TableColumn<FileDisplayOnTable, String> filePathColumn;

    private final Preferences prefs = Preferences.userNodeForPackage(BackupAndRestoreController.class);

    // progress bar controller
    private FXMLLoader progressPanelFXML;
    private Scene progressbarScene;
    private ProgressController progressController;

    @FXML
    public void initialize() throws IOException {
        this.loadSettingData();
        this.setDataOnTable();
        this.setDefaultValueForHandBackup();
        this.setValueForBackupWayCombobox();
        this.loadingProgressBarPanel();
    }

    private void loadingProgressBarPanel() throws IOException {
        this.progressPanelFXML = getProgressPanelFXML();
        this.progressPanelFXML.load();
        this.progressbarScene = new Scene(progressPanelFXML.getRoot());
        this.progressController = progressPanelFXML.getController();
    }

    @FXML
    void setBackupWay() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String defaultFullBackupName = "HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL";
        String defaultDifBackupName = "HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-DIF";
        if(backupWaysCombobox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Ngày hôm nay"))
            fileNameHandBackupText.setText(defaultDifBackupName);
        else if(backupWaysCombobox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Toàn bộ"))
            fileNameHandBackupText.setText(defaultFullBackupName);
        else fileNameHandBackupText.setText("--None--");
    }

    @FXML
    void restore() throws SQLException {

        Stage stage = createNewStage(progressbarScene, "Phuc hoi du lieu");
        FileDisplayOnTable displayOnTableTableRow = tableData.getSelectionModel().getSelectedItem();

        if(displayOnTableTableRow == null){
            showMessage(
                    Alert.AlertType.ERROR,
                    "Canh bao",
                    "Chua chon bang du lieu hoi phuc",
                    "Nhan ok de thoat"
            ).show();
            return;
        }


        Optional<ButtonType> optional = showMessage(
                Alert.AlertType.CONFIRMATION,
                "Khoi phuc du lieu",
                "Ban co muon khoi phuc du lieu",
                "Nhan ok de khoi phuc, cancel de huy"
        ).showAndWait();

        if(optional.isPresent() && optional.get() == ButtonType.OK){

            if(displayOnTableTableRow.getFilePath().contains("DIF")){
                File[] files = new File(FilePathManager.getPath(
                        PreferencesKey.RESTORE_FILE,
                        PreferencesKey.DEFAULT_FILE_PATH
                )).listFiles();

                if (files == null || files.length == 0) return;

                File fullBackup = Arrays.stream(files).filter(x -> x.getName().contains("FULL"))
                        .findFirst().orElse(null);
                if(fullBackup == null) return;

                if(dataIsUnsing(displayOnTableTableRow.getFilePath())){
                    showMessage(
                            Alert.AlertType.ERROR,
                            "Canh bao",
                            "Du lieu da va dang duoc su dung!!!",
                            "Nhan ok de xac nhan")
                            .show();
                    tableData.getSelectionModel().clearSelection();
                    return;
                }
                stage.show();

                progressController.setProgress(0.3);

                RestoreDatabase.restoreDif(fullBackup.getAbsolutePath(), displayOnTableTableRow.getFilePath());
                progressController.setProgress(0.5);

                FilePathManager.savePath(
                        PreferencesKey.CURRENT_USING_DATA,
                        displayOnTableTableRow.getFilePath());
                currentUsingDataText.setText(displayOnTableTableRow.getFilePath());
                progressController.setProgress(1);
            } else if (displayOnTableTableRow.getFilePath().contains("FULL")){
                if(dataIsUnsing(displayOnTableTableRow.getFilePath())){
                    showMessage(
                            Alert.AlertType.ERROR,
                            "Canh bao",
                            "Du lieu da va dang duoc su dung!!!",
                            "Nhan ok de xac nhan")
                            .show();
                    tableData.getSelectionModel().clearSelection();
                    return;
                }
                stage.show();
                progressController.setProgress(0.3);
                RestoreDatabase.restoreFull(displayOnTableTableRow.getFilePath());
                progressController.setProgress(0.5);
                FilePathManager.savePath(
                        PreferencesKey.CURRENT_USING_DATA,
                        displayOnTableTableRow.getFilePath());
                currentUsingDataText.setText(displayOnTableTableRow.getFilePath());
                progressController.setProgress(1);
            }

            stage.close();

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Phu hoi du lieu thanh cong",
                    "Du lieu da duoc phuc hoi thanh cong",
                    "Nhan ok de Xac nhan"
            ).show();

            progressController.setProgress(0.1);
        }
        tableData.getSelectionModel().clearSelection();
    }

    private boolean dataIsUnsing(String dataFilePath){
        File currentUsingFile = new File(currentUsingDataText.getText());
        File checkingFile = new File(dataFilePath);
        return currentUsingFile.equals(checkingFile);
    }

    @FXML
    void restoreDataByHand() throws SQLException, IOException, InterruptedException {
        Stage stage = createNewStage(progressbarScene, "Sao luu du lieu");

        Alert alert = showMessage(
                Alert.AlertType.CONFIRMATION,
                "Sao luu du lieu",
                "Ban co muon sao luu du lieu",
                "nhan ok de xac nhan, cancel de huy"
        );
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get() == ButtonType.OK){
            stage.show();
            String filePath = fileAddressHandBackupText.getText()+ "\\" + fileNameHandBackupText.getText()  + ".bak";
            File file = new File(filePath);
            if(file.exists()) file.delete();

            progressController.setProgress(0.3);

            String option = backupWaysCombobox.getSelectionModel().getSelectedItem();
            if(option.equalsIgnoreCase("Toàn bộ")) BackupDatabase.backupFullDatabase(filePath);
            if(option.equalsIgnoreCase("Ngày hôm nay")) BackupDatabase.backupDifDatabase(filePath);

            progressController.setProgress(0.5);

            setDataOnTable();

            progressController.setProgress(1);

            stage.close();

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Sao luu thanh cong",
                    "Du lieu da duoc sao luu thanh cong",
                    "Nhan ok de Xac nhan"
            ).show();

            progressController.setProgress(0.1);
        }
    }

    @FXML
    void backupForm() {
        if(autoBackUpRadioButton.isSelected())
            FilePathManager.savePath(
                PreferencesKey.BACK_UP_FORM_KEY,
                PreferencesKey.BACK_UP_FORM_AUTO_VALUE);
        else if(warningBackUpRadioButton.isSelected())
            FilePathManager.savePath(
                    PreferencesKey.BACK_UP_FORM_KEY,
                    PreferencesKey.BACK_UP_FORM_WARNING_VALUE);
        else
            FilePathManager.savePath(
                    PreferencesKey.BACK_UP_FORM_KEY,
                    PreferencesKey.BACK_UP_FORM_NO_VALUE);
    }

    @FXML
    void setFileAddressForAutoBackup() throws SQLException {
        // get folder address selected
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(null);
        if(file == null) return;
        fileAddressAutoBackupText.setText(file.getAbsolutePath());
        FilePathManager.savePath(PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY, file.getAbsolutePath());

        // check if not exist full backup file
        this.checkFullBackupExistion();

        this.setValueForRestoreView(file);

        if(FilePathManager.getPath(PreferencesKey.USING_BACKUP_FUCTION, "None").equalsIgnoreCase("0")){
            this.warningPapaText.setVisible(false);
            this.warningText.setVisible(false);
            this.currenFileUsingText.setVisible(true);
            this.currentUsingDataText.setVisible(true);
            FilePathManager.savePath(
                    PreferencesKey.USING_BACKUP_FUCTION,
                    "1"
            );
        }
    }

    @FXML
    void setFileAddressForHandBackup() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(null);
        if(file == null) return;
        fileAddressHandBackupText.setText(file.getAbsolutePath());
        FilePathManager.savePath(PreferencesKey.BACK_UP_FULL_HAND_ADDRESS_KEY, file.getAbsolutePath());
    }

    @FXML
    void compressFile() throws IOException {
        // 1 that means compress
        // 0 that means not compress
        if(compressFileCheckBox.isSelected()){
            FilePathManager.savePath(PreferencesKey.BACKUP_COMPRESS_FILE, "1");
        }else{
            FilePathManager.savePath(PreferencesKey.BACKUP_COMPRESS_FILE, "0");
        }
    }

    @FXML
    void setFileAddressForRestore() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        if (file == null) return;
        this.setValueForRestoreView(file);
    }

    @FXML
    void refreshhRestoreFolder() {
        setDataOnTable();
    }

    private void setDataOnTable(){
        String restoreFolder = FilePathManager.getPath(
                PreferencesKey.RESTORE_FILE,
                PreferencesKey.DEFAULT_FILE_PATH);

        if (restoreFolder.equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)) return;

        ObservableList<FileDisplayOnTable> data = FXCollections.observableArrayList();
        File[] files = new File(restoreFolder).listFiles();

        if(files == null || files.length == 0) {
            tableData.getItems().setAll(data);
            return;
        }

        // get only .bak files
        files = Arrays.stream(files)
                .filter(x -> x.getName().contains(".bak"))
                .toArray(File[]::new);
        fileNum.setText(String.valueOf(files.length));

        for(File f : files){
            FileDisplayOnTable fileDisplayOnTable = new FileDisplayOnTable();
            if(f.isFile()){
                String fileName = f.getName().split("\\.")[0];
                fileDisplayOnTable.setName(fileName);

                Date date = new Date(f.lastModified());
                Instant instant = Instant.ofEpochMilli(date.getTime());
                ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
                LocalDateTime dateCreated = LocalDateTime.ofInstant(instant, zoneId);
                fileDisplayOnTable.setDateCreated(dateCreated);

                fileDisplayOnTable.setSize((double) f.length() / 1024);

                String fileType = f.getName().split("\\.")[f.getName().split("\\.").length - 1];
                fileDisplayOnTable.setFileType(fileType);

                String filepath = f.getAbsolutePath();
                fileDisplayOnTable.setFilePath(filepath);

                data.add(fileDisplayOnTable);
            }
        }

        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy", new Locale("vi", "VN"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        createdColumn.setCellFactory(col -> new TableCell<>(){
            @Override
            protected void updateItem(LocalDateTime localDateTime, boolean empty) {
                super.updateItem(localDateTime, empty);
                setText(empty || localDateTime == null ? null : localDateTime.format(dateTimeFormatter));
            }
        });
        fileTypeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeColumn.setCellFactory(col -> new TableCell<>(){
            @Override
            protected void updateItem(Double aDouble, boolean b) {
                super.updateItem(aDouble, b);
                setText(b || aDouble == null ? null : formatCurrency(aDouble) + " Kbs");
            }
        });
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));

        tableData.getItems().setAll(data);

        // sort dateCreated descending
        tableData.getSortOrder().add(createdColumn);
        createdColumn.setSortType(TableColumn.SortType.DESCENDING);
        tableData.sort();
    }

    private void loadSettingData(){
        //fileAddressAutoBackupText.setText(ConfigManager.get("key1", "C:/Users/Default"));
        fileAddressAutoBackupText.setText(FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH));
        fileAddressRestoreText.setText(FilePathManager.getPath(
                PreferencesKey.RESTORE_FILE,
                PreferencesKey.DEFAULT_FILE_PATH));
        currentUsingDataText.setText(FilePathManager.getPath(
                PreferencesKey.CURRENT_USING_DATA,
                PreferencesKey.DEFAULT_FILE_PATH));

        // backup form
        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, "None")
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            autoBackUpRadioButton.setSelected(true);
        else if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, "None")
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE))
            warningBackUpRadioButton.setSelected(true);
        else noBackUpRadioButton.setSelected(true);

        // compress file
        compressFileCheckBox.setSelected(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, "None")
                .equalsIgnoreCase("1"));

        // using fuction for the fisrt time
        // 1 is yes
        // 0 is no
        if(FilePathManager.getPath(PreferencesKey.USING_BACKUP_FUCTION, "None")
                .equalsIgnoreCase("1")){
            this.warningPapaText.setVisible(false);
            this.warningText.setVisible(false);
            this.currenFileUsingText.setVisible(true);
            this.currentUsingDataText.setVisible(true);
            FilePathManager.savePath(
                    PreferencesKey.USING_BACKUP_FUCTION,
                    "1"
            );
        }
        if(warningText.isVisible())
            FilePathManager.savePath(
                    PreferencesKey.USING_BACKUP_FUCTION,
                    "0"
            );
    }

    private void setValueForBackupWayCombobox() {
        this.backupWaysCombobox.getItems().setAll(Arrays.asList("Ngày hôm nay", "Toàn bộ"));
        this.backupWaysCombobox.setValue("Ngày hôm nay");
        setBackupWay();
    }

    private void setDefaultValueForHandBackup(){
        fileAddressHandBackupText.setText(FilePathManager
                .getPath(PreferencesKey.BACK_UP_FULL_HAND_ADDRESS_KEY, PreferencesKey.DEFAULT_FILE_PATH));
    }

    private Alert showMessage(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    private void setValueForRestoreView(File file){
        fileAddressRestoreText.setText(file.getAbsolutePath());
        FilePathManager.savePath(PreferencesKey.RESTORE_FILE, file.getAbsolutePath());
        setDataOnTable();
    }

    private void checkFullBackupExistion() throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String defaultFullBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL.bak";
        String backupFolderAddress = FilePathManager.getPath(
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                PreferencesKey.DEFAULT_FILE_PATH);

        File[] files = new File(backupFolderAddress).listFiles();
        if(files != null){
            File fullBackup = Arrays.stream(files).filter(x -> x.getName().contains("FULL"))
                    .findFirst().orElse(null);

            if(fullBackup == null) {
                String filePath = backupFolderAddress + defaultFullBackupName;
                BackupDatabase.backupFullDatabase(filePath);
                currentUsingDataText.setText(filePath);
                FilePathManager.savePath(
                        PreferencesKey.CURRENT_USING_DATA,
                        filePath);
            }
        }
    }

    private static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    private FXMLLoader getProgressPanelFXML() throws IOException {
        return new FXMLLoader(
                Objects.requireNonNull(this.getClass()
                        .getResource("/iuh/fit/view/features/backup_restore_database/ProgressPanel.fxml"))
        );
    }

    private Stage createNewStage(Scene scene, String title){
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        return stage;
    }
}

