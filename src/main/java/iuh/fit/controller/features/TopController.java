package iuh.fit.controller.features;

import iuh.fit.Application;
import iuh.fit.Main;
import iuh.fit.controller.LoginController;
import iuh.fit.controller.MainController;
import iuh.fit.dao.MessageDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.misc.Notifications;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private MainController mainController;

    private NotificationButtonController topBarController;

    private Application main;

    public NotificationButtonController initialize(Account account, MainController mainController, Application main) {
        setupContext(account, mainController, main);
        // clock
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE); //
        timeline.play();

        initializeNotificationButton();

        handleTooltips();
        handleButtons();

        System.out.println("KET QUA NOTIFYCONTROLLER (2): "+topBarController);
        return topBarController;
    }

    public void setupContext(Account account, MainController mainController, Application main) {
        this.currentAccount = account;
        this.mainController = mainController;
        this.main = main;
    }

    private void updateClock() {
        String currentTime = LocalTime.now().format(timeFormatter);
        clockLabel.setText(currentTime);
    }

    public void logout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane loginPane = fxmlLoader.load();

            LoginController controller = fxmlLoader.getController();
            controller.initialize(main);

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTooltips() {
        // Tạo Tooltip
        Tooltip tooltip = new Tooltip("Đăng xuất");
        Tooltip.install(logoutBtn, tooltip); // Gắn Tooltip vào Button

        // Thêm Tooltip bằng cách setTooltip
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
        logoutBtn.setOnAction(e -> logout());
    }

    public void initializeNotificationButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/NotificationButton.fxml"));
            AnchorPane buttonLayout = loader.load();

            NotificationButtonController notificationButtonController = loader.getController();
            topBarController = notificationButtonController.initialize(currentAccount, mainController);
            buttonPanel.getChildren().clear();
            buttonPanel.getChildren().addAll(buttonLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
