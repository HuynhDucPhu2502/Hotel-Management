package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private ImageView arrowUpForEmpBtn;

    @FXML
    private HBox buttonOneContainer1;
    @FXML
    private HBox buttonTwoContainer1;
    @FXML
    private HBox buttonThreeContainer1;
    @FXML
    private ImageView arrowUpForEmpBtn1;

    @FXML
    private HBox buttonOneContainer2;
    @FXML
    private HBox buttonTwoContainer2;
    @FXML
    private HBox buttonThreeContainer2;
    @FXML
    private ImageView arrowUpForEmpBtn2;

    @FXML
    private HBox buttonOneContainer21;
    @FXML
    private HBox buttonTwoContainer21;
    @FXML
    private HBox buttonThreeContainer21;
    @FXML
    private ImageView arrowUpForEmpBtn21;

    @FXML
    private HBox buttonOneContainer211;
    @FXML
    private HBox buttonTwoContainer211;
    @FXML
    private HBox buttonThreeContainer211;
    @FXML
    private ImageView arrowUpForEmpBtn211;

    @FXML
    private HBox buttonOneContainer2111;
    @FXML
    private HBox buttonTwoContainer2111;
    @FXML
    private HBox buttonThreeContainer2111;
    @FXML
    private ImageView arrowUpForEmpBtn2111;

    @FXML
    private HBox buttonOneContainer21111;
    @FXML
    private HBox buttonTwoContainer21111;
    @FXML
    private HBox buttonThreeContainer21111;
    @FXML
    private ImageView arrowUpForEmpBtn21111;


    @FXML
    private Button employeeBtn;
    @FXML
    private Button employeeBtn1;
    @FXML
    private Button employeeBtn2;
    @FXML
    private Button employeeBtn21;
    @FXML
    private Button employeeBtn211;
    @FXML
    private Button employeeBtn2111;
    @FXML
    private Button employeeBtn21111;

    @FXML
    private ScrollPane scrollPane;

    private boolean stateEmployee = false;



    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        employeeBtn.setOnAction(e -> dropDownMenuEvent(buttonOneContainer, buttonTwoContainer, buttonThreeContainer, arrowUpForEmpBtn));
        employeeBtn1.setOnAction(e -> dropDownMenuEvent(buttonOneContainer1, buttonTwoContainer1, buttonThreeContainer1, arrowUpForEmpBtn1));
        employeeBtn2.setOnAction(e -> dropDownMenuEvent(buttonOneContainer2, buttonTwoContainer2, buttonThreeContainer2, arrowUpForEmpBtn2));
        employeeBtn21.setOnAction(e -> dropDownMenuEvent(buttonOneContainer21, buttonTwoContainer21, buttonThreeContainer21, arrowUpForEmpBtn21));
        employeeBtn211.setOnAction(e -> dropDownMenuEvent(buttonOneContainer211, buttonTwoContainer211, buttonThreeContainer211, arrowUpForEmpBtn211));
        employeeBtn2111.setOnAction(e -> dropDownMenuEvent(buttonOneContainer2111, buttonTwoContainer2111, buttonThreeContainer2111, arrowUpForEmpBtn2111));
        employeeBtn21111.setOnAction(e -> dropDownMenuEvent(buttonOneContainer21111, buttonTwoContainer21111, buttonThreeContainer21111, arrowUpForEmpBtn21111));

    }

    public void dropDownMenuEvent(HBox btn1, HBox btn2, HBox btn3, ImageView arrow){
        if (!stateEmployee) {
            btn1.setVisible(true);
            btn1.setManaged(true);
            btn2.setVisible(true);
            btn2.setManaged(true);
            btn3.setVisible(true);
            btn3.setManaged(true);
        } else {
            btn1.setVisible(false);
            btn1.setManaged(false);
            btn1.setManaged(false);
            btn2.setVisible(false);
            btn2.setManaged(false);
            btn3.setVisible(false);
            btn3.setManaged(false);
        }
        stateEmployee = !stateEmployee;

        arrow.setRotate(!stateEmployee ? 0 : 180); // Rotate arrow based on visibility
    }

}
