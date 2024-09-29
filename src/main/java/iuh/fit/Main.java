package iuh.fit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Le Tran Gia Huy
 * @created 29/09/2024 - 3:29 PM
 * @project HotelManagement
 * @package iuh.fit
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/MenuBar.fxml")); // Update with the correct path
            AnchorPane root = loader.load();

            // Set the scene and stage
            Scene scene = new Scene(root);
            primaryStage.setTitle("Dropdown Menu Example");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true); // Optional: prevent resizing
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
