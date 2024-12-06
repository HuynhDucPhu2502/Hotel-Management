package iuh.fit.controller.features;

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

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane loginPane = fxmlLoader.load();

            Stage currentStage = (Stage) (logoutBtn.getScene().getWindow());

            Scene loginScene = new Scene(loginPane);

            currentStage.setScene(loginScene);
            currentStage.setResizable(false);
            currentStage.setWidth(700);
            currentStage.setHeight(500);
            currentStage.setMaximized(false);

            currentStage.show();
            currentStage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
