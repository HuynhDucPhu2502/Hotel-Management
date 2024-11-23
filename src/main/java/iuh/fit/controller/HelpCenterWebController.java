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

    @FXML
    private void initialize() {
        String helpFilePath = Objects.requireNonNull(getClass().getResource(
                "/iuh/fit/help-center-website/html/index.html"
        )).toExternalForm();

        engine = webView.getEngine();
        engine.load(helpFilePath);

        locationLabel.textProperty().bind(engine.locationProperty());
    }
}
