package iuh.fit.controller.features.backup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProgressController implements Initializable {

    @FXML private ProgressBar progressBar;

    private Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setProgress(double value){
        this.progressBar.setProgress(value);
    }

    public Parent getScene() throws IOException {
        return scene;
    }
}
