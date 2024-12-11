package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.statistics.AnalyzeBeforeLogOutController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class TopController {
    @FXML
    private Label clockLabel, dateLabel;
    @FXML
    private Button logoutBtn, analyzeShiftBtn;
    @FXML
    private AnchorPane buttonPanel;
    @FXML
    private ImageView img, imgForAnalyzeBtn;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private MainController mainController;

    private NotificationButtonController notificationButtonController;
    private Stage stage;

    @FXML
    public void initialize(MainController mainController, Stage stage) {
        this.mainController = mainController;
        this.stage = stage;

        initializeNotificationButton();

        // clock
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE); //
        timeline.play();


        handleTooltips();
        handleButtons();
    }


    private void updateClock() {
        String currentTime = LocalTime.now().format(timeFormatter);
        LocalDate localDate = LocalDate.now();
        String currentDate = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("vi", "VN"))+", "+
                localDate.getDayOfMonth() +"/"+localDate.getMonthValue()+"/"+localDate.getYear();
        clockLabel.setText(currentTime);
        dateLabel.setText("  "+currentDate);
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
            if (result.isPresent() && result.get() == ButtonType.OK) {

                String source = "/iuh/fit/view/features/statistics/AnalyzeBeforeLogOut.fxml";

                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
                AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

                Stage newStage = new Stage();

                AnalyzeBeforeLogOutController analyzeBeforeLogOutController = loader.getController();
                analyzeBeforeLogOutController.initialize(logoutBtn, mainController,
                        this, stage, newStage);

                Scene scene = new Scene(layout);

                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.setTitle("Thống kê ca làm");
                newStage.setScene(scene);
                newStage.setResizable(false);
                newStage.show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void analyzeBeforeLogout() throws IOException {
        try{
            String source = "/iuh/fit/view/features/statistics/AnalyzeBeforeLogOut.fxml";

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
            AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

            Stage newStage = new Stage();

            AnalyzeBeforeLogOutController analyzeBeforeLogOutController = loader.getController();
            analyzeBeforeLogOutController.initialize(logoutBtn, mainController,
                    this, stage, newStage);

            Scene scene = new Scene(layout);

            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setTitle("Thống kê ca làm");
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Stage getStage(){
        return stage;
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
        hoverEffect2.setBrightness(-0.3); // Làm màu đậm hơn

        // Khi hover vào button
        logoutBtn.setOnMouseEntered(event -> img.setEffect(hoverEffect));
        analyzeShiftBtn.setOnMouseEntered(event -> imgForAnalyzeBtn.setEffect(hoverEffect));

        // Khi rời chuột khỏi button
        logoutBtn.setOnMouseExited(event -> img.setEffect(null));
        analyzeShiftBtn.setOnMouseExited(event -> imgForAnalyzeBtn.setEffect(null));

        logoutBtn.setOnMousePressed(event -> img.setEffect(hoverEffect2));
        analyzeShiftBtn.setOnMousePressed(event -> imgForAnalyzeBtn.setEffect(hoverEffect2));

        logoutBtn.setOnMouseReleased(event -> img.setEffect(null));
        analyzeShiftBtn.setOnMouseReleased(event -> imgForAnalyzeBtn.setEffect(null));

        // logout
        logoutBtn.setOnAction(e -> {
            try {
                logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        analyzeShiftBtn.setOnAction(e->{
            try {
                analyzeBeforeLogout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void initializeNotificationButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/NotificationButton.fxml"));
            AnchorPane buttonLayout = loader.load();

            this.notificationButtonController = loader.getController();
            notificationButtonController.initialize(mainController.getAccount());

            buttonPanel.getChildren().clear();
            buttonPanel.getChildren().addAll(buttonLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public NotificationButtonController getNotificationButtonController() {
        return notificationButtonController;
    }
}
