package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.HotelServiceDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.HotelService;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EmployeeSearchingController {
    // Input Fields
    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField cardIDTextField;
    @FXML
    private ComboBox<String> positionCBox;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private ToggleGroup gender;

    // Table
    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> employeeIDColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> phoneNumberColumn;
    @FXML
    private TableColumn<Employee, String> cardIDColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee , Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button searchBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Employee> items;

    public void initialize() {
        loadData();
        setupTable();
        searchBtn.setOnAction(e -> handleSearchAction());
        resetBtn.setOnAction(e -> handleResetAction());
    }

    private void loadData() {
        positionCBox.getItems().setAll(
                Stream.of(Position.values())
                        .map(Enum::name)
                        .map(position -> {
                            switch (position) {
                                case "MANAGER" -> {
                                    return "QUẢN LÝ";
                                }
                                case "RECEPTIONIST" -> {
                                    return "LỄ TÂN";
                                }
                                default -> {
                                    return position;
                                }
                            }
                        })
                        .toList()
        );
        positionCBox.getItems().addFirst("TẤT CẢ");
        List<Employee> employeeList = EmployeeDAO.getEmployees();
        items = FXCollections.observableArrayList(employeeList);
        employeeTableView.setItems(items);
        employeeTableView.refresh();


        positionCBox.getSelectionModel().selectFirst();
    }

    private void setupTable() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        cardIDColumn.setCellValueFactory(new PropertyValueFactory<>("idCardNumber"));
        positionColumn.setCellValueFactory(data -> {
            String position = data.getValue().getPosition().name();
            return new SimpleStringProperty(
                    switch (position) {
                        case "MANAGER" -> "QUẢN LÝ";
                        case "RECEPTIONIST" -> "LỄ TÂN";
                        default -> position;
                    }
            );
        });
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = param -> new TableCell<>() {
            private final Button showInfoButton = new Button("Thông tin");
            private final HBox hBox = new HBox(10);
            {
                // Thêm class CSS cho các button
                showInfoButton.getStyleClass().add("button-view");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                showInfoButton.setOnAction(e -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    Account account = AccountDAO.getAccountByEmployeeID(employee.getEmployeeID());
                    try {
                        handleShowEmployeeInformation(employee, account);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(showInfoButton);
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleShowEmployeeInformation(Employee employee, Account account) throws IOException {
        String source = "/iuh/fit/view/features/employee/EmployeeInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        EmployeeInformationViewController employeeInformationViewController = loader.getController();
        employeeInformationViewController.setEmployee(employee, account);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.setTitle("Thông tin nhân viên");
        stage.setScene(scene);
        stage.show();
    }

    private void handleResetAction() {
        employeeIDTextField.setText("");
        fullNameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        positionCBox.getSelectionModel().selectFirst();
        cardIDTextField.setText("");
        male.setSelected(true);
        DOBPicker.setValue(null);
        addressTextField.setText("");
        gender.getSelectedToggle().setSelected(false);
        positionCBox.getSelectionModel().clearSelection();


        loadData();
    }

    private void handleSearchAction() {
        try {
            String employeeID = employeeIDTextField.getText().isBlank() ? null : employeeIDTextField.getText().trim();
            String fullName = fullNameColumn.getText().isBlank() ? null : fullNameTextField.getText().trim();
            String phoneNumber = phoneNumberTextField.getText().isBlank() ? null : phoneNumberTextField.getText().trim();
            String email = emailTextField.getText().isBlank() ? null : emailTextField.getText().trim();
            String address = addressTextField.getText().isBlank() ? null : addressTextField.getText().trim();
            Gender gder;
            if (gender.getSelectedToggle() == null){
                gder = null;
            } else {
                gder = ((RadioButton) gender.getSelectedToggle()).getText().equals(Gender.MALE.toString()) ? Gender.MALE : Gender.FEMALE;
            }
            LocalDate dob =  DOBPicker.getValue();
            String cardID = cardIDTextField.getText().isBlank() ? null : cardIDTextField.getText().trim();
            Position position;
            if(positionCBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("TẤT CẢ")){
                position = null;
            } else{
               position = ConvertHelper.positionConverter(positionCBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("QUẢN LÝ")?"MANAGER":"RECEPTIONIST");
            }
            List<Employee> searchResults = EmployeeDAO.searchEmployee(
                        employeeID, fullName, phoneNumber, email, address, gder, cardID, dob, position
                    );
            items.setAll(searchResults);
            employeeTableView.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
