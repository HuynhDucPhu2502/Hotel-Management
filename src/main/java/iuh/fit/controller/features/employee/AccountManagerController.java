package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> employeeIDSearchField;
    @FXML
    private TextField fullNameSearchField;
    @FXML
    private TextField usernameSearchField;
    @FXML
    private TextField passwordSearchField;
    @FXML
    private TextField statusSearchField;

    // Input Fields
    @FXML
    private TextField accountIDTextField;
    @FXML
    private ComboBox<String> employeeIDCBox;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ComboBox<String> statusCBox;

    // Table
    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, String> employeeIDColumn;
    @FXML
    private TableColumn<Account, String> usernameColumn;
    @FXML
    private TableColumn<Account, String> passwordColumn;
    @FXML
    private TableColumn<Account, String> statusColumn;
    @FXML
    private TableColumn<Account , Void> actionColumn;

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

    private ObservableList<Account> items;

    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        employeeIDSearchField.setOnAction(e -> handleSearchAction());
        employeeIDCBox.setOnAction(e -> handleSearchFullName());
    }

    private void loadData() {
        statusCBox.getItems().setAll(
                Stream.of(AccountStatus.values()).map(Enum::name).toList()
        );
        statusCBox.getSelectionModel().selectFirst();

        List<String> comboBoxItems = EmployeeDAO.getEmployees()
                .stream()
                .map(Employee::getEmployeeID)
                .collect(Collectors.toList());

        ObservableList<String> observableComboBoxItems = FXCollections.observableArrayList(comboBoxItems);
        employeeIDCBox.getItems().setAll(observableComboBoxItems);

        accountIDTextField.setText(AccountDAO.getNextAccountID());

        List<String> Ids = EmployeeDAO.getTopThreeID();
        employeeIDSearchField.getItems().setAll(Ids);

        List<Account> AccountList = AccountDAO.getAccount();
        items = FXCollections.observableArrayList(AccountList);
        accountTableView.setItems(items);
        accountTableView.refresh();
    }

    private void setupTable() {
        employeeIDColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmployee().getEmployeeID()));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAccountStatus().name()));
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Account, Void>, TableCell<Account, Void>> cellFactory = param -> new TableCell<>() {
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
                    Account account = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(account);
                });


                showInfoButton.setOnAction(e -> {
                    Account account = getTableView().getItems().get(getIndex());
                    try {
                        handleShowAccountInformation(account.getEmployee(), account);
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

    private void handleUpdateBtn(Account account) {
        accountIDTextField.setText(account.getAccountID());

        employeeIDCBox.getSelectionModel().select(account.getEmployee().getEmployeeID());

        fullNameTextField.setText(account.getEmployee().getFullName());

        usernameTextField.setText(account.getUserName());

        passwordTextField.setText(account.getPassword());

        statusCBox.getSelectionModel().select(account.getAccountStatus().name());

        employeeIDCBox.setEditable(false);
        employeeIDCBox.setDisable(true);

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handleShowAccountInformation(Employee employee ,Account account) throws IOException {
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
        employeeIDCBox.setEditable(true);
        employeeIDCBox.setDisable(false);
        accountIDTextField.setText(AccountDAO.getNextAccountID());
        if (!employeeIDCBox.getItems().isEmpty()) employeeIDCBox.getSelectionModel().select(null);
        fullNameTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
        if (!statusCBox.getItems().isEmpty()) statusCBox.getSelectionModel().selectFirst();


        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    private void handleAddAction() {
        try {
            Employee employee = EmployeeDAO.getDataByID(employeeIDCBox.getValue());

            Account account = new Account(
                    accountIDTextField.getText(),
                    employee,
                    usernameTextField.getText(),
                    passwordTextField.getText(),
                    ConvertHelper.accountStatusConverter(statusCBox.getSelectionModel().getSelectedItem())
            );
            Account account1 = AccountDAO.getAccountByEmployeeID(employeeIDCBox.getValue());
            if (account1 == null){
                AccountDAO.createData(account);
            } else {
                dialogPane.showWarning("LỖI", "Nhân viên đã có tài khoản");
                return;
            }
            handleResetAction();
            loadData();
            dialogPane.showInformation("Thành công", "Đã thêm tài khoản thành công");
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handleSearchAction() {
        fullNameSearchField.setText("");
        usernameSearchField.setText("");
        passwordSearchField.setText("");
        statusSearchField.setText("");

        String searchText = employeeIDSearchField.getValue();
        List<Account> accountList;

        if (searchText == null || searchText.isEmpty()) {
            accountList = AccountDAO.getAccount();
        } else {
            accountList = AccountDAO.findDataByContainsId(searchText);
            if (accountList.size() == 1) {
                Account account = accountList.getFirst();
                fullNameSearchField.setText(String.valueOf(account.getEmployee().getFullName()));
                usernameSearchField.setText(account.getUserName());
                passwordSearchField.setText(account.getPassword());
                statusSearchField.setText(account.getAccountStatus().name());
            }
        }

        // Cập nhật lại bảng với dữ liệu tìm kiếm
        items.setAll(accountList);
        accountTableView.setItems(items);
    }

    private void handleSearchFullName(){
        fullNameTextField.setText("");
        String searchText = employeeIDCBox.getValue();
        List<Employee> employeeList;

        if (searchText == null || searchText.isEmpty()) {
            employeeList = EmployeeDAO.getEmployees();
        } else {
            employeeList = EmployeeDAO.findDataByContainsId(searchText);
            if (employeeList.size() >= 1) {
                Employee employee = employeeList.getFirst();
                fullNameTextField.setText(String.valueOf(employee.getFullName()));
                employeeIDCBox.getSelectionModel().select(employee.getEmployeeID());
            }
        }
    }

    private void handleUpdateAction() {
        try {
            Employee employee = EmployeeDAO.getDataByID(employeeIDCBox.getValue());

            Account account = new Account(
                    accountIDTextField.getText(),
                    employee,
                    usernameTextField.getText(),
                    passwordTextField.getText(),
                    ConvertHelper.accountStatusConverter(statusCBox.getSelectionModel().getSelectedItem())
            );

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin tài khoản của nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        AccountDAO.updateData(account);
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
}
