package iuh.fit;

import iuh.fit.controller.LoginController;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.dao.AccountDAO;
import iuh.fit.models.Account;
import iuh.fit.utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class Main extends Application {
  
    public static void main(String[] args) {
        Application.main(args);
    }
}
