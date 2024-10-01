package iuh.fit.controller.features.bar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TopController {
    @FXML
    private Label clockLabel;
    @FXML
    private Button logoutBtn;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    public void initialize() {
        // clock
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE); //
        timeline.play();

        // logout
        logoutBtn.setOnAction(e -> logout());
    }

    private void updateClock() {
        String currentTime = LocalTime.now().format(timeFormatter);
        clockLabel.setText(currentTime);
    }

    public void logout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/panels/LoginUI.fxml"));
            AnchorPane loginPane = fxmlLoader.load();

            Stage currentStage = (Stage) (logoutBtn.getScene().getWindow());

            Scene loginScene = new Scene(loginPane);
            currentStage.setScene(loginScene);
            currentStage.setWidth(600);
            currentStage.setHeight(400);

            currentStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
