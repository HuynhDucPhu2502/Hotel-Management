package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

@SuppressWarnings(value = "unchecked")
public class MenuController {
    @FXML
    private HBox buttonOneContainer;
    @FXML
    private HBox buttonTwoContainer;
    @FXML
    private HBox buttonThreeContainer;

    @FXML
    private Button employeeBtn;

    private boolean stateEmployee = false;

    @FXML
    public void initialize() {
        employeeBtn.setOnAction(e -> {
            if (!stateEmployee) {
                buttonOneContainer.setVisible(true);
                buttonOneContainer.setManaged(true);
                buttonTwoContainer.setVisible(true);
                buttonTwoContainer.setManaged(true);
                buttonThreeContainer.setVisible(true);
                buttonThreeContainer.setManaged(true);
            } else {
                buttonOneContainer.setVisible(false);
                buttonOneContainer.setManaged(false);
                buttonTwoContainer.setVisible(false);
                buttonTwoContainer.setManaged(false);
                buttonThreeContainer.setVisible(false);
                buttonThreeContainer.setManaged(false);
            }
            stateEmployee = !stateEmployee;
        });

    }
}
