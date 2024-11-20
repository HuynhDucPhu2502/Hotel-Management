package iuh.fit.controller.features.backup;

import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.FilePathManager;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

public class BackupController {

    private final Preferences prefs = Preferences.userNodeForPackage(BackupController.class);

    @FXML private RadioButton noBackUpRadioButton;
    @FXML private RadioButton warningBackUpRadioButton;
    @FXML private RadioButton autoBackUpRadioButton;
    @FXML private TextField fileAdressText;
    @FXML private CheckBox compressFileCheckBox;

    @FXML
    public void initialize() {

        //fileAdressText.setText(ConfigManager.get("key1", "C:/Users/Default"));
        fileAdressText.setText(FilePathManager.getPath(PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY, PreferencesKey.DEFAULT_FILE_PATH));
        this.setBackupForm();
        this.setCompressFileCheckBox();
    }

    private void setCompressFileCheckBox(){
        compressFileCheckBox.setSelected(FilePathManager.getPath(PreferencesKey.BACKUP_COMPRESS_FILE, "None")
                .equalsIgnoreCase("1"));
    }

    private void setBackupForm(){
        if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, "None")
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_AUTO_VALUE))
            autoBackUpRadioButton.setSelected(true);
        else if(FilePathManager.getPath(PreferencesKey.BACK_UP_FORM_KEY, "None")
                .equalsIgnoreCase(PreferencesKey.BACK_UP_FORM_WARNING_VALUE))
            warningBackUpRadioButton.setSelected(true);
        else noBackUpRadioButton.setSelected(true);
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
    void getFileAddress() {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(null);
        if(file == null) return;
        fileAdressText.setText(file.getAbsolutePath());
        FilePathManager.savePath(PreferencesKey.BACK_UP_DATA_FILE_ADDRESS_KEY, file.getAbsolutePath());
        //ConfigManager.put("key1", file.getAbsolutePath());
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
}

