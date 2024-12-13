package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.util.Objects;

public class HelpCenterWebController {
    @FXML
    private Label locationLabel;
    @FXML
    private WebView webView;

    private WebEngine engine;

    private final String WEBSITE_PATH =
            "https://huynhducphu2502.github.io/Hotel-Management-HelpCenter/index.html";

    @FXML
    private void initialize() {
        engine = webView.getEngine();
        engine.load(WEBSITE_PATH);

        locationLabel.textProperty().bind(engine.locationProperty());
    }
}
