package iuh.fit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Button roomBtn;
    @FXML
    private Button serviceBtn;
    @FXML
    private Button customerBtn;
    @FXML
    private Button accountBtn;
    @FXML
    private Button statisticsBtn;
    @FXML
    private Button historyBtn;

    @FXML
    private ScrollPane scrollPane;

    private final Map<String, Boolean> buttonStates = new HashMap<>();

    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        buttonStates.put("employee", false);
        buttonStates.put("room", false);
        buttonStates.put("service", false);
        buttonStates.put("customer", false);
        buttonStates.put("account", false);
        buttonStates.put("statistics", false);
        buttonStates.put("history", false);

        employeeBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer, buttonTwoContainer, buttonThreeContainer), arrowUpForEmpBtn, "employee"));
        roomBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer1, buttonTwoContainer1, buttonThreeContainer1), arrowUpForEmpBtn1, "room"));
        serviceBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer2, buttonTwoContainer2, buttonThreeContainer2), arrowUpForEmpBtn2, "service"));
        customerBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer21, buttonTwoContainer21, buttonThreeContainer21), arrowUpForEmpBtn21, "customer"));
        accountBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer211, buttonTwoContainer211, buttonThreeContainer211), arrowUpForEmpBtn211, "account"));
        statisticsBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer2111, buttonTwoContainer2111, buttonThreeContainer2111), arrowUpForEmpBtn2111, "statistics"));
        historyBtn.setOnAction(e -> dropDownMenuEvent(List.of(buttonOneContainer21111, buttonTwoContainer21111, buttonThreeContainer21111), arrowUpForEmpBtn21111, "history"));
    }

    public void dropDownMenuEvent(List<HBox> buttons, ImageView arrow, String stateKey) {
        Boolean state = buttonStates.get(stateKey);

        if (!state) {
            for (HBox button : buttons) {
                button.setVisible(true);
                button.setManaged(true);
            }
            arrow.setRotate(180);
        } else {
            for (HBox button : buttons) {
                button.setVisible(false);
                button.setManaged(false);
            }
            arrow.setRotate(0);
        }

        buttonStates.put(stateKey, !state);
    }

}
