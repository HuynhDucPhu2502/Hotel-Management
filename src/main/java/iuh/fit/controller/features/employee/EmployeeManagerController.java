package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;

import iuh.fit.utils.PasswordHashing;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
    private TextField fullNameSearchField, phoneNumberSearchField,
            positionSearchField;

    // Input Fields
    @FXML
    private TextField employeeIDTextField, fullNameTextField,
            phoneNumberTextField, emailTextField,
            addressTextField, cardIDTextFiled;
    @FXML
    private ComboBox<String> positionCBox;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private RadioButton radMale, radFemale;
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
    private Button resetBtn, addBtn, updateBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Employee> items;

    public void initialize() {
        dialogPane.toFront();

        employeeTableView.setFixedCellSize(40);

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        employeeIDSearchField.setOnAction(e -> handleSearchAction());
    }

    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                // Tải danh sách vị trí
                Platform.runLater(() -> {
                    positionCBox.getItems().setAll(
                            Stream.of(Position.values())
                                    .map(position -> switch (position.name()) {
                                        case "MANAGER" -> "QUẢN LÝ";
                                        case "RECEPTIONIST" -> "LỄ TÂN";
                                        default -> position.name();
                                    })
                                    .toList()
                    );
                    positionCBox.getSelectionModel().selectFirst();
                });

                Platform.runLater(() -> employeeIDTextField.setText(EmployeeDAO.getNextEmployeeID()));
                List<String> Ids = EmployeeDAO.getTopThreeID();
                Platform.runLater(() -> employeeIDSearchField.getItems().setAll(Ids));

                List<Employee> employeeList = EmployeeDAO.getEmployees();
                items = FXCollections.observableArrayList(employeeList);

                Platform.runLater(() -> {
                    employeeTableView.setItems(items);
                    employeeTableView.refresh();
                });

                return null;
            }
        };

        loadDataTask.setOnRunning(e -> setButtonsDisabled(true));
        loadDataTask.setOnSucceeded(e -> setButtonsDisabled(false));
        loadDataTask.setOnFailed(e -> {
            setButtonsDisabled(false);
            dialogPane.showWarning("LỖI", "Lỗi khi tải dữ liệu");
        });

        new Thread(loadDataTask).start();
    }

    private void setupTable() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
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
                    try {
                        handleShowEmployeeInformation(employee);
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

        dobPicker.setValue(null);
        radMale.setSelected(true);

        fullNameSearchField.setText("");
        phoneNumberSearchField.setText("");
        positionSearchField.setText("");
        employeeIDSearchField.getSelectionModel().select(null);

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
                    dobPicker.getValue(),
                    ConvertHelper.positionConverter(positionCBox.getSelectionModel().getSelectedItem().equals("QUẢN LÝ") ? "MANAGER" : "RECEPTIONIST"),
                    ObjectStatus.ACTIVATE
            );

            EmployeeDAO.createData(employee);
            Account account = new Account(AccountDAO.getNextAccountID(), employee, this.removePrefix(employee.getEmployeeID()), PasswordHashing.hashPassword("test123@"), AccountStatus.ACTIVE);
            AccountDAO.createData(account);
            handleResetAction();
            loadData();
            dialogPane.showInformation("Thông báo", "Thêm nhân viên thành công\nTài khoản: " + this.removePrefix(employee.getEmployeeID()) + "\nMật khẩu: " + "test123@");
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

        // Ánh xạ giá trị position tiếng Anh thành tiếng Việt
        positionCBox.getSelectionModel().select(employee.getPosition().name().equalsIgnoreCase("MANAGER") ? "QUẢN LÝ" : "LỄ TÂN");

        dobPicker.setValue(employee.getDob());
        if (employee.getGender().equals(Gender.MALE)) {
            radMale.setSelected(true);
        } else {
            radFemale.setSelected(true);
        }

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
                    dobPicker.getValue(),
                    ConvertHelper.positionConverter(positionCBox.getSelectionModel().getSelectedItem().equals("QUẢN LÝ") ? "MANAGER" : "RECEPTIONIST"),
                    ObjectStatus.ACTIVATE
            );

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        EmployeeDAO.updateData(employee);
                        dialogPane.showInformation("Thông báo", "Cập nhật thông tin nhân viên thành công");
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

        Task<ObservableList<Employee>> searchTask = new Task<>() {
            @Override
            protected ObservableList<Employee> call() {
                List<Employee> employeeList;
                if (searchText == null || searchText.isEmpty()) {
                    employeeList = EmployeeDAO.getEmployees();
                } else {
                    employeeList = EmployeeDAO.findDataByContainsId(searchText);
                }
                return FXCollections.observableArrayList(employeeList);
            }
        };

        searchTask.setOnSucceeded(e -> {
            ObservableList<Employee> employeeList = searchTask.getValue();
            Platform.runLater(() -> {
                items.setAll(employeeList);
                employeeTableView.setItems(items);

                if (employeeList.size() == 1) {
                    Employee employee = employeeList.getFirst();
                    fullNameSearchField.setText(employee.getFullName());
                    phoneNumberSearchField.setText(employee.getPhoneNumber());
                    positionSearchField.setText(employee.getPosition().name().equalsIgnoreCase("MANAGER") ? "QUẢN LÝ" : "LỄ TÂN");
                }
            });
        });

        searchTask.setOnFailed(e -> Platform.runLater(() -> dialogPane.showWarning("LỖI", "Lỗi khi tìm kiếm dữ liệu")));

        new Thread(searchTask).start();
    }

    private void handleShowEmployeeInformation(Employee employee) throws IOException {
        String source = "/iuh/fit/view/features/employee/EmployeeInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        EmployeeInformationViewController employeeInformationViewController = loader.getController();
        employeeInformationViewController.setEmployee(employee);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();

        String iconPath = "/iuh/fit/icons/menu_icons/ic_employee.png"; // Đường dẫn đến icon
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
        stage.setTitle("Thông tin nhân viên");

        stage.setScene(scene);
        stage.show();
    }
    public String removePrefix(String input) {
        if (input != null && input.startsWith("EMP-")) {
            return input.substring(4);
        }
        return input;
    }

    private void setButtonsDisabled(boolean disabled) {
        resetBtn.setDisable(disabled);
        addBtn.setDisable(disabled);
        updateBtn.setDisable(disabled);
    }
}
