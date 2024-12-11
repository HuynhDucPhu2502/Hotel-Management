package iuh.fit.controller.features;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.employee.ShiftDetailForEachEmployeeDialogController;
import iuh.fit.controller.features.statistics.AnalyzeBeforeLogOutController;
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
    private Account currentAccount;
    private MainController mainController;

    private NotificationButtonController topBarController;
    private Stage stage;

    @FXML
    public NotificationButtonController initialize(Account account, MainController mainController) {
        setupContext(account,mainController);
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

    public void setupContext(Account account, MainController mainController) {
        this.currentAccount = account;
        this.mainController = mainController;
    }

    private void updateClock() {
        String currentTime = LocalTime.now().format(timeFormatter);
        LocalDate localDate = LocalDate.now();
        String currentDate = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("vi", "VN"))+", "+
                +localDate.getDayOfMonth()+"/"+localDate.getMonthValue()+"/"+localDate.getYear();
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

                AnalyzeBeforeLogOutController analyzeBeforeLogOutController = loader.getController();
                analyzeBeforeLogOutController.initialize(logoutBtn, mainController, this);

                Scene scene = new Scene(layout);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Thống kê ca làm");
                stage.setScene(scene);
                stage.setResizable(false);
                setStage(stage);
                analyzeBeforeLogOutController.getComponentFormController();
                stage.show();
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

            AnalyzeBeforeLogOutController analyzeBeforeLogOutController = loader.getController();
            analyzeBeforeLogOutController.initialize(logoutBtn, mainController, this);

            Scene scene = new Scene(layout);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Thống kê ca làm");
            stage.setScene(scene);
            stage.setResizable(false);
            setStage(stage);
            analyzeBeforeLogOutController.getComponentFormController();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setStage (Stage stage){
        this.stage = stage;
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

            NotificationButtonController notificationButtonController = loader.getController();
            topBarController = notificationButtonController.initialize(currentAccount);
            buttonPanel.getChildren().clear();
            buttonPanel.getChildren().addAll(buttonLayout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
