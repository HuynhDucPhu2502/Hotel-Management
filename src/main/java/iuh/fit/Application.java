package iuh.fit;

import iuh.fit.controller.LoginController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("unused")
public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/iuh/fit/imgs/hotel_logo.png")).toString()));

        startWithLogin(primaryStage);
    }

    public void startWithLogin(Stage primaryStage) {
        loadUI(primaryStage);
    }

    private void loadUI(Stage mainStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);

            LoginController loginController = loader.getController();
            loginController.setupContext(mainStage);

            mainStage.setTitle("Quản Lý Khách Sạn");
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.setWidth(610);
            mainStage.setHeight(400);
            mainStage.setMaximized(false);
            mainStage.centerOnScreen();

            mainStage.show();

            mainStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}