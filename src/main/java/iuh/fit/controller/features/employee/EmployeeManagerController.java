package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;

import javafx.application.Platform;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EmployeeManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> employeeIDSearchField;
    @FXML
    private TextField fullNameSearchField;
    @FXML
    private TextField phoneNumberSearchField;
    @FXML
    private TextField positionSearchField;

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
    private TextField cardIDTextFiled;
    @FXML
    private ComboBox<String> positionCBox;
    @FXML
    private DatePicker DOBPicker;
    @FXML
    private RadioButton radMale;
    @FXML
    private RadioButton radFemale;
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
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee , Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Employee> items;

    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        employeeIDSearchField.setOnAction(e -> handleSearchAction());
    }

    private void loadData() {
        positionCBox.getItems().setAll(
                Stream.of(Position.values()).map(Enum::name).toList()
        );
        positionCBox.getSelectionModel().selectFirst();

        employeeIDTextField.setText(EmployeeDAO.getNextEmployeeID());

        List<String> Ids = EmployeeDAO.getTopThreeID();
        employeeIDSearchField.getItems().setAll(Ids);

        List<Employee> employeeList = EmployeeDAO.getEmployees();
        items = FXCollections.observableArrayList(employeeList);
        employeeTableView.setItems(items);
        employeeTableView.refresh();
    }

    private void setupTable() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        positionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPosition().name()));
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button showInfoButton = new Button("Thông tin");
            private final HBox hBox = new HBox(10);
            {
                // Thêm class CSS cho các button
                updateButton.getStyleClass().add("button-update");
                showInfoButton.getStyleClass().add("button-view");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                // Set hành động cho các button
                updateButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(employee);
                });

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
                hBox.getChildren().addAll(updateButton, showInfoButton);
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

    private void handleResetAction() {
        employeeIDTextField.setText(EmployeeDAO.getNextEmployeeID());
        fullNameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        addressTextField.setText("");
        cardIDTextFiled.setText("");
        if (!positionCBox.getItems().isEmpty()) positionCBox.getSelectionModel().selectFirst();

        DOBPicker.setValue(null);
        radMale.setSelected(true);

        fullNameTextField.requestFocus();
        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    private void handleAddAction() {
        try {
            Employee employee = new Employee(
                    employeeIDTextField.getText(),
                    fullNameTextField.getText(),
                    phoneNumberTextField.getText(),
                    emailTextField.getText(),
                    addressTextField.getText(),
                    cardIDTextFiled.getText(),
                    ((RadioButton) gender.getSelectedToggle()).getText().equals(Gender.MALE.toString()) ? Gender.MALE : Gender.FEMALE,
                    DOBPicker.getValue(),
                    ConvertHelper.positionConverter(positionCBox.getSelectionModel().getSelectedItem())
            );

            EmployeeDAO.createData(employee);
            handleResetAction();
            loadData();
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(Employee employee) {
        employeeIDTextField.setText(employee.getEmployeeID());
        fullNameTextField.setText(employee.getFullName());
        emailTextField.setText(employee.getEmail());
        phoneNumberTextField.setText(employee.getPhoneNumber());
        addressTextField.setText(employee.getAddress());
        cardIDTextFiled.setText(employee.getIdCardNumber());
        positionCBox.getSelectionModel().select(employee.getPosition().name());

        DOBPicker.setValue(employee.getDob());
        if(employee.getGender().equals(Gender.MALE))
            radMale.setSelected(true);
        else
            radFemale.setSelected(true);

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    // 4.2 Chức năng cập nhật
    private void handleUpdateAction() {
        try {
            Employee employee = new Employee(
                    employeeIDTextField.getText(),
                    fullNameTextField.getText(),
                    phoneNumberTextField.getText(),
                    emailTextField.getText(),
                    addressTextField.getText(),
                    cardIDTextFiled.getText(),
                    ((RadioButton) gender.getSelectedToggle()).getText().equals(Gender.MALE.toString()) ? Gender.MALE : Gender.FEMALE,
                    DOBPicker.getValue(),
                    ConvertHelper.positionConverter(positionCBox.getSelectionModel().getSelectedItem())
            );

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        EmployeeDAO.updateData(employee);
                        Platform.runLater(() -> {
                            handleResetAction();
                            loadData();
                        });
                    } catch (IllegalArgumentException e) {
                        dialogPane.showWarning("LỖI", e.getMessage());
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // 5. Chức năng Tìm kiếm
    private void handleSearchAction() {
        fullNameSearchField.setText("");
        phoneNumberSearchField.setText("");
        positionSearchField.setText("");

        String searchText = employeeIDSearchField.getValue();
        List<Employee> employeeList;

        if (searchText == null || searchText.isEmpty()) {
            employeeList = EmployeeDAO.getEmployees();
        } else {
            employeeList = EmployeeDAO.findDataByContainsId(searchText);
            if (employeeList.size() == 1) {
                Employee employee = employeeList.getFirst();
                fullNameSearchField.setText(String.valueOf(employee.getFullName()));
                phoneNumberSearchField.setText(employee.getPhoneNumber());
                positionSearchField.setText(employee.getPosition().name());
            }
        }

        // Cập nhật lại bảng với dữ liệu tìm kiếm
        items.setAll(employeeList);
        employeeTableView.setItems(items);
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
}
