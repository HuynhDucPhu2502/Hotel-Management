package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.models.Account;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TopController {
    @FXML
    private Label clockLabel;
    @FXML
    private Button logoutBtn;
    @FXML
    private AnchorPane buttonPanel;
    @FXML
    private ImageView img;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Account currentAccount;
//    private MainController mainController;

    private NotificationButtonController topBarController;

    @FXML
    public NotificationButtonController initialize(Account account) {
        setupContext(account);
        // clock
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE); //
        timeline.play();

        initializeNotificationButton();
        handleTooltips();
        handleButtons();

        // logout
        return topBarController;
    }

    public void setupContext(Account account) {
        this.currentAccount = account;
    }

    private void updateClock() {
        String currentTime = LocalTime.now().format(timeFormatter);
        clockLabel.setText(currentTime);
    }

    public void logout() throws IOException {
        try{
            // Create a confirmation alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Đăng xuất");
            alert.setHeaderText("Xác nhận đăng xuất");
            alert.setContentText("Bạn có muốn đăng xuất và bàn giao lại ca?");

            // Show the dialog and wait for a response
            Optional<ButtonType> result = alert.showAndWait();

            // Check the user's response
            if (((java.util.Optional<?>) result).isPresent() && result.get() == ButtonType.OK) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
                AnchorPane loginPane = fxmlLoader.load();

                Stage currentStage = (Stage) (logoutBtn.getScene().getWindow());

                Scene loginScene = new Scene(loginPane);

                currentStage.setScene(loginScene);

                currentStage.setResizable(false);
                currentStage.setWidth(610);
                currentStage.setHeight(400);
                currentStage.setMaximized(false);
                currentStage.centerOnScreen();

                currentStage.show();
                currentStage.centerOnScreen();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleTooltips() {
        Tooltip tooltip = new Tooltip("Đăng xuất");
        Tooltip.install(logoutBtn, tooltip);

        logoutBtn.setTooltip(tooltip);
        tooltip.setShowDelay(javafx.util.Duration.millis(400));
    }

    private void handleButtons() {
        // Tạo hiệu ứng khi hover
        ColorAdjust hoverEffect = new ColorAdjust();
        hoverEffect.setBrightness(-0.2); // Làm màu đậm hơn

        ColorAdjust hoverEffect2 = new ColorAdjust();
        hoverEffect2.setBrightness(-0.5); // Làm màu đậm hơn

        // Khi hover vào button
        logoutBtn.setOnMouseEntered(event -> img.setEffect(hoverEffect));

        // Khi rời chuột khỏi button
        logoutBtn.setOnMouseExited(event -> img.setEffect(null));

        logoutBtn.setOnMousePressed(event -> img.setEffect(hoverEffect2));

        logoutBtn.setOnMouseReleased(event -> img.setEffect(null));

        // logout
        logoutBtn.setOnAction(e -> {
            try {
                logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void initializeNotificationButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/NotificationButton.fxml"));
            AnchorPane buttonLayout = loader.load();

            NotificationButtonController notificationButtonController = loader.getController();
            topBarController = notificationButtonController.initialize(currentAccount);
            buttonPanel.getChildren().clear();
            buttonPanel.getChildren().addAll(buttonLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
