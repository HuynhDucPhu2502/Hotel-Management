package iuh.fit.controller.features.statistics;

import iuh.fit.security.PreferencesKey;
import iuh.fit.utils.FilePathManager;
import iuh.fit.utils.PropertiesFile;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Properties;

public class FileAddressController {

    @FXML private TextField fileAddressText;

    private String fileParth;
    private Stage stage;
    private String preferencesKey;

    private static final String settingFilePath = "setting.properties";

    public void initialize(String preferencesKey) {
        fileAddressText.setText(PropertiesFile.readFile(settingFilePath, preferencesKey));
    }

    @FXML
    void getFileAddress() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(null);
        if(file == null) return;
        fileParth = file.getAbsolutePath();
        fileAddressText.setText(fileParth);
    }

    @FXML
    void cancel() {
        stage.close();
    }

    @FXML
    void comfirm() {
        if(fileParth != null && !fileParth.isEmpty())
            PropertiesFile.writeFile(
                    settingFilePath,
                    preferencesKey,
                    fileParth
            );
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPreferencesKey(String preferencesKey) {
        this.preferencesKey = preferencesKey;
    }
}
