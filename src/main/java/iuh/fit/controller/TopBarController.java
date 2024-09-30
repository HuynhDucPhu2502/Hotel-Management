package iuh.fit.controller;

import iuh.fit.models.Account;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TopBarController {
    @FXML
    private Text helloLabel;

    @FXML
    private Label clockLabel;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
        helloLabel.setText("Xin chào, " + account.getEmployee().getFullName());
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop forever
        timeline.play(); // Start the clock
    }

    private void updateClock() {
        // Get the current time and format it
        String currentTime = LocalTime.now().format(timeFormatter);
        clockLabel.setText(currentTime); // Update the label with the current time
    }

}
