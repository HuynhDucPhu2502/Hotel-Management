package iuh.fit.controller.features.backup;

import iuh.fit.models.wrapper.FileDisplayOnTable;
import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.BackupDatabase;
import iuh.fit.utils.FilePathManager;
import iuh.fit.utils.PropertiesFile;
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

public class BackupAndRestoreController {
    @FXML private RadioButton noBackUpRadioButton;
    @FXML private RadioButton warningBackUpRadioButton;
    @FXML private RadioButton autoBackUpRadioButton;
    @FXML private TextField fileAddressAutoBackupText;
    @FXML private TextField fileAddressHandBackupText;
    @FXML private TextField fileNameHandBackupText;
    @FXML private Text currentUsingDataText;
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

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final String settingFilePath = "setting.properties";

    // progress bar controller
    private FXMLLoader progressPanelFXML;
    private Scene progressbarScene;

    @FXML
    public void initialize() throws IOException, SQLException {
        this.loadSettingData();
        this.setDataOnTable();
        this.setValueForBackupWayCombobox();
        this.loadingProgressBarPanel();
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

        Stage stage = createNewStage(progressbarScene, "Phục hồi dữ liệu");
        FileDisplayOnTable displayOnTableTableRow = tableData.getSelectionModel().getSelectedItem();

        if(displayOnTableTableRow == null){
            showMessage(
                    Alert.AlertType.ERROR,
                    "Chưa chọn dữ liệu",
                    "Hãy chọn dữ liệu để phục hồi",
                    "Nhấn OK để xác nhận"
            ).show();
            return;
        }

        Optional<ButtonType> optional = showMessage(
                Alert.AlertType.CONFIRMATION,
                "Khôi phục dữ liệu",
                "Bạn có muốn sao lưu dữ liệu?",
                "Nhấn OK để xác nhận, Cancel để hủy"
        ).showAndWait();

        if(optional.isPresent() && optional.get() == ButtonType.OK){

            if(displayOnTableTableRow.getFilePath().contains("DIF")){
                String fileAddressRestore = PropertiesFile.readFile(
                        settingFilePath,
                        PreferencesKey.RESTORE_FILE
                );
                File[] files = new File(fileAddressRestore).listFiles();

                if (files == null || files.length == 0) return;

                File fullBackup = Arrays.stream(files).filter(x -> x.getName().contains("FULL"))
                        .findFirst().orElse(null);
                if(fullBackup == null) {
                    showMessage(
                            Alert.AlertType.ERROR,
                            "Khôi phục thất bại",
                            "Thư mục phải chứa tệp dữ liệu phục hồi đầy đủ (FULL) đi kèm",
                            "Nhấn OK để xác nhận"
                    ).show();
                    return;
                }
                LocalDateTime fullBackUpDateCreated = convertLastModifiedDateToLocalDateTime(fullBackup);

                if(fullBackUpDateCreated.isAfter(displayOnTableTableRow.getDateCreated())) {
                    showMessage(
                            Alert.AlertType.ERROR,
                            "Khôi phục thất bại",
                            "Tệp dữ liệu phục hồi đầy đủ (FULL) phải được tạo trước tệp dữ liệu bạn muốn phục hồi",
                            "Nhấn OK để xác nhận"
                    ).show();
                    return;
                }

//                if(dataIsUsing(displayOnTableTableRow.getFilePath())){
//                    showMessage(
//                            Alert.AlertType.ERROR,
//                            "Dữ liệu đã sử dụng",
//                            "Dữ liệu bạn muốn khôi phục đã và đang được sử dụng",
//                            "Nhấn OK để xác nhận")
//                            .show();
//                    tableData.getSelectionModel().clearSelection();
//                    return;
//                }

                String filePath = displayOnTableTableRow.getFilePath();
                File f = new File(filePath);

                // dung xoa cai nay
                if(!f.exists()) {
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Dường dẫn dữ liệu rỗng",
                            "Dữ liệu muốn phục hồi không tồn tại, làm mới bảng dữ liệu và thử lại",
                            "Nhấn OK để xác nhận"
                    ).show();
                    tableData.getSelectionModel().clearSelection();
                    return;
                }

                stage.show();

                try {
                    RestoreDatabase.restoreDif(fullBackup.getAbsolutePath(), filePath);
                }catch (Exception e){
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Khôi phục thất bại",
                            "Dữ liệu muốn phục hồi không tồn tại, làm mới bảng dữ liệu và thử lại",
                            "Nhấn OK để xác nhận"
                    ).show();
                }

                PropertiesFile.writeFile(
                        settingFilePath,
                        PreferencesKey.CURRENT_USING_DATA,
                        filePath
                );
                currentUsingDataText.setText(filePath);

                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Phục hồi dữ liệu thành công",
                        "Dữ liệu đã phục hồi thành công",
                        "Nhấn OK để xác nhận"
                ).show();
            }
            else if (displayOnTableTableRow.getFilePath().contains("FULL")){
//                if(dataIsUsing(displayOnTableTableRow.getFilePath())){
//                    showMessage(
//                            Alert.AlertType.ERROR,
//                            "Dữ liệu đã sử dụng",
//                            "Dữ liệu bạn muốn khôi phục đã và đang được sử dụng",
//                            "Nhấn OK để xác nhận"
//                    )
//                            .show();
//                    tableData.getSelectionModel().clearSelection();
//                    return;
//                }

                String filePath = displayOnTableTableRow.getFilePath();
                File f = new File(filePath);

                // dung xoa cai nay
                if(!f.exists()) {
                    showMessage(
                            Alert.AlertType.INFORMATION,
                            "Khôi phục thất bại",
                            "Dữ liệu muốn phục hồi không tồn tại, làm mới bảng dữ liệu và thử lại",
                            "Nhấn OK để xác nhận"
                    ).show();
                    return;
                }

                stage.show();

                RestoreDatabase.restoreFull(filePath);

                PropertiesFile.writeFile(
                        settingFilePath,
                        PreferencesKey.CURRENT_USING_DATA,
                        filePath
                );
                currentUsingDataText.setText(filePath);

                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Phục hồi dữ liệu thành công",
                        "Dữ liệu đã phục hồi thành công",
                        "Nhấn OK để xác nhận"
                ).show();
            }

            stage.close();
        }
        tableData.getSelectionModel().clearSelection();
    }

    @FXML
    void backupDataByHand() throws SQLException, IOException, InterruptedException {
        Stage stage = createNewStage(progressbarScene, "Sao lưu dữ liệu");

        Alert alert = showMessage(
                Alert.AlertType.CONFIRMATION,
                "Sao lưu dữ liệu",
                "Bạn có muốn sao lưu dữ liệu?",
                "Nhấn OK để xác nhận, Cancel để hủy"
        );
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get() == ButtonType.OK){
            stage.show();
            String filePath = fileAddressHandBackupText.getText();
            File file = new File(filePath);
            if(!file.exists()) file.mkdirs();

            filePath = fileAddressHandBackupText.getText()+ "\\" + fileNameHandBackupText.getText()  + ".bak";
            file = new File(filePath);
            if(file.exists()) file.delete();

            String option = backupWaysCombobox.getSelectionModel().getSelectedItem();
            if(option.equalsIgnoreCase("Toàn bộ")) BackupDatabase.backupFullDatabase(filePath);
            if(option.equalsIgnoreCase("Ngày hôm nay")) BackupDatabase.backupDifDatabase(filePath);

            setDataOnTable();

            stage.close();

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Sao lưu thành công!!!",
                    "Dữ liệu đã được sao lưu thành công!!!",
                    "Nhấn OK để xác nhận"
            ).show();
        }
    }

    @FXML
    void autoBackupSetting() {
        if(autoBackUpRadioButton.isSelected()) {
            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.BACK_UP_OPTION_KEY,
                    PreferencesKey.BACK_UP_FORM_AUTO_VALUE);
        }
        else if(warningBackUpRadioButton.isSelected()) {
            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.BACK_UP_OPTION_KEY,
                    PreferencesKey.BACK_UP_FORM_WARNING_VALUE);
        }
        else {
            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.BACK_UP_OPTION_KEY,
                    PreferencesKey.BACK_UP_FORM_NO_VALUE);
        }
    }

    @FXML
    void setFileAddressForAutoBackup() throws SQLException {
        // get folder address selected
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(null);
        if(file == null) return;

        fileAddressAutoBackupText.setText(file.getAbsolutePath());
        PropertiesFile.writeFile(
                settingFilePath,
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY,
                file.getAbsolutePath());

        // check if not exist full backup file
        this.checkFullBackupExistion();

        this.setValueForRestoreView(file);

        // use backup funciton for the first time
        String useBackupFirstTime = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.USING_BACKUP_FUCTION
        );

        if(useBackupFirstTime.equalsIgnoreCase("1")){
            this.warningPapaText.setVisible(false);
            this.warningText.setVisible(false);
            this.currenFileUsingText.setVisible(true);
            this.currentUsingDataText.setVisible(true);
            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.USING_BACKUP_FUCTION,
                    "0"
            );
        }
    }

    @FXML
    void setFileAddressForHandBackup() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(null);
        if(file == null) return;
        fileAddressHandBackupText.setText(file.getAbsolutePath());
        PropertiesFile.writeFile(
                settingFilePath,
                PreferencesKey.BACK_UP_FULL_HAND_ADDRESS_KEY,
                file.getAbsolutePath()
        );
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
        String restoreFolder = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.RESTORE_FILE
        );

        if (restoreFolder.equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)) return;

        ObservableList<FileDisplayOnTable> data = FXCollections.observableArrayList();
        File[] files = new File(restoreFolder).listFiles();

        if(files == null) {
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

                LocalDateTime dateCreated = convertLastModifiedDateToLocalDateTime(f);
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

        // clear selection
        tableData.getSelectionModel().clearSelection();
    }

    private void loadSettingData() throws SQLException {
        // using fuction for the fisrt time
        // 1 is yes
        // 0 is no

        String fileBackUpAddress = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY
        );

        if(fileBackUpAddress.equalsIgnoreCase(PreferencesKey.DEFAULT_FILE_PATH)){
            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.USING_BACKUP_FUCTION,
                    "1"
            );
        }

        String useBackupFirstTime = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.USING_BACKUP_FUCTION
        );

        if(useBackupFirstTime.equalsIgnoreCase("1")){
            this.warningPapaText.setVisible(true);
            this.warningText.setVisible(true);
            this.currenFileUsingText.setVisible(false);
            this.currentUsingDataText.setVisible(false);
        } else {
            this.warningPapaText.setVisible(false);
            this.warningText.setVisible(false);
            this.currenFileUsingText.setVisible(true);
            this.currentUsingDataText.setVisible(true);
        }

        // check restore folder was created and contained the full backup
        String addressRestore = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.RESTORE_FILE
        );
        fileAddressRestoreText.setText(addressRestore);

        File restoreFolder = new File(addressRestore);
        if(!restoreFolder.exists()) {
            restoreFolder.mkdirs();
            String defaultDifBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL.bak";
            String filePath = restoreFolder + defaultDifBackupName;
            BackupDatabase.backupFullDatabase(filePath);

            PropertiesFile.writeFile(
                    settingFilePath,
                    PreferencesKey.CURRENT_USING_DATA,
                    filePath
            );
        }else{
            File[] files = restoreFolder.listFiles();
            if(files == null) return;
            File fullBackup = Arrays.stream(files).filter(x -> x.getName().contains("FULL"))
                    .findFirst().orElse(null);

            if(fullBackup != null) {
                String currentFile = PropertiesFile.readFile(
                        settingFilePath,
                        PreferencesKey.CURRENT_USING_DATA
                );
                if(currentFile == null || currentFile.isBlank())
                    PropertiesFile.writeFile(
                            settingFilePath,
                            PreferencesKey.CURRENT_USING_DATA,
                            fullBackup.getAbsolutePath()
                    );
            } else {
                String defaultDifBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL.bak";
                String filePath = restoreFolder + defaultDifBackupName;
                BackupDatabase.backupFullDatabase(filePath);

                PropertiesFile.writeFile(
                        settingFilePath,
                        PreferencesKey.CURRENT_USING_DATA,
                        filePath
                );
            }
        }


        // set file address for all
        String addressForAutoBackup = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY
        );
        fileAddressAutoBackupText.setText(addressForAutoBackup);

        String addressForHandBackup = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.BACK_UP_FULL_HAND_ADDRESS_KEY
        );
        fileAddressHandBackupText.setText(addressForHandBackup);

        String currentUsingData = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.CURRENT_USING_DATA
        );
        currentUsingDataText.setText(currentUsingData);

        // backup form
        String autoBackupOption = PropertiesFile.readFile(settingFilePath, PreferencesKey.BACK_UP_OPTION_KEY);
        if(autoBackupOption == null || autoBackupOption.isBlank()) {
            noBackUpRadioButton.setSelected(true);
            return;
        }

        if(autoBackupOption.equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            autoBackUpRadioButton.setSelected(true);
        else if(autoBackupOption.equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE))
            warningBackUpRadioButton.setSelected(true);
        else noBackUpRadioButton.setSelected(true);
    }

    private void setValueForBackupWayCombobox() {
        this.backupWaysCombobox.getItems().setAll(Arrays.asList("Ngày hôm nay", "Toàn bộ"));
        this.backupWaysCombobox.setValue("Ngày hôm nay");
        setBackupWay();
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
        PropertiesFile.writeFile(
                settingFilePath,
                PreferencesKey.RESTORE_FILE,
                file.getAbsolutePath()
        );
        setDataOnTable();
    }

    private void checkFullBackupExistion() throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String defaultFullBackupName = "\\HotelBackup-" + LocalDate.now().format(dateTimeFormatter) + "-FULL.bak";

        String backupFolderAddress = PropertiesFile.readFile(
                settingFilePath,
                PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY);

        File[] files = new File(backupFolderAddress).listFiles();
        if(files != null){
            File fullBackup = Arrays.stream(files).filter(x -> x.getName().contains("FULL"))
                    .findFirst().orElse(null);

            if(fullBackup == null) {
                String filePath = backupFolderAddress + defaultFullBackupName;
                BackupDatabase.backupFullDatabase(filePath);
                currentUsingDataText.setText(filePath);
                PropertiesFile.writeFile(
                        settingFilePath,
                        PreferencesKey.CURRENT_USING_DATA,
                        filePath
                );
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

    private void loadingProgressBarPanel() throws IOException {
        this.progressPanelFXML = getProgressPanelFXML();
        this.progressPanelFXML.load();
        this.progressbarScene = new Scene(progressPanelFXML.getRoot());
    }

    private boolean dataIsUsing(String dataFilePath){
        File currentUsingFile = new File(currentUsingDataText.getText());
        File checkingFile = new File(dataFilePath);
        return currentUsingFile.equals(checkingFile);
    }

    private LocalDateTime convertLastModifiedDateToLocalDateTime(File f){
        Date date = new Date(f.lastModified());
        Instant instant = Instant.ofEpochMilli(date.getTime());
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ Việt Nam
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}

