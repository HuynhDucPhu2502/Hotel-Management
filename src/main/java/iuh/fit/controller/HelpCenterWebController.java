package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HelpCenterWebController {
    @FXML
    private Label locationLabel;
    @FXML
    private WebView webView;

    @FXML
    private void initialize() {
        WebEngine engine = webView.getEngine();
        String websitePath = "https://huynhducphu2502.github.io/Hotel-Management-HelpCenter/index.html";
        engine.load(websitePath);

        locationLabel.textProperty().bind(engine.locationProperty());
    }
}
