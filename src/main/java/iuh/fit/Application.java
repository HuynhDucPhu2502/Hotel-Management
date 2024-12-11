package iuh.fit;

import iuh.fit.controller.LoginController;
import iuh.fit.utils.*;
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
        //startWithoutLogin(primaryStage);
    }

    public void startWithLogin(Stage primaryStage) throws SQLException {
        loadUI(primaryStage);

        if(!RestoreDatabase.isDatabaseExist("HotelDatabase")) return;

        handelCloseButton(primaryStage);
    }

    private void loadUI(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);

            LoginController loginController = loader.getController();
            loginController.setupContext(primaryStage);

            primaryStage.setTitle("Quản Lý Khách Sạn");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setWidth(610);
            primaryStage.setHeight(400);
            primaryStage.setMaximized(false);
            primaryStage.centerOnScreen();

            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handelCloseButton(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            try {
                BackupDatabase.backupData(primaryStage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}