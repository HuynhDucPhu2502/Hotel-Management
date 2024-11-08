package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.AccountDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.PasswordHashing;
import iuh.fit.utils.RegexChecker;
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
import javafx.scene.text.Text;
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
    private PasswordField passwordTextField;
    @FXML
    private ComboBox<String> statusCBox;
    @FXML
    private PasswordField newPasswordTextField;
    @FXML
    private Text passwordLabel;
    @FXML
    private Text newPasswordLabel;

    // Table
    @FXML
    private TableView<Account> accountTableView;
    @FXML
    private TableColumn<Account, String> employeeIDColumn;
    @FXML
    private TableColumn<Account, String> usernameColumn;
    @FXML
    private TableColumn<Account, String> fullNameColumn;
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
        accountTableView.setFixedCellSize(40);

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
                Stream.of(AccountStatus.values()).map(Enum::name)
                        .map(position -> {
                        switch (position) {
                            case "ACTIVE" -> {
                                return "ĐANG HOẠT ĐỘNG";
                            }
                            case "INACTIVE" -> {
                                return "KHÔNG HOẠT ĐỘNG";
                            }
                            case "LOCKED" -> {
                                return "BỊ KHÓA";
                            }
                            default -> {
                                return position;
                            }
                    }
                })
                        .toList()
        );
        statusCBox.getSelectionModel().selectFirst();

        List<String> comboBoxItems = EmployeeDAO.getEmployees()
                .stream()
                .filter(employee -> AccountDAO.getAccountByEmployeeID(employee.getEmployeeID()) == null)
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
        fullNameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmployee().getFullName()));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        statusColumn.setCellValueFactory(data -> {
            String status = switch (data.getValue().getAccountStatus().name()) {
                case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
                case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
                case "LOCKED" -> "BỊ KHÓA";
                default -> data.getValue().getAccountStatus().name(); // Giữ nguyên nếu không nằm trong các trạng thái trên
            };
            return new SimpleStringProperty(status);
        });
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
        passwordLabel.setText("Mật khẩu cũ");

        employeeIDCBox.getSelectionModel().select(account.getEmployee().getEmployeeID());

        fullNameTextField.setText(account.getEmployee().getFullName());

        usernameTextField.setText(account.getUserName());

        statusCBox.getSelectionModel().select(account.getAccountStatus().name());

        employeeIDCBox.setEditable(false);
        employeeIDCBox.setDisable(true);
        usernameTextField.setEditable(false);
        usernameTextField.setDisable(true);
        passwordLabel.setVisible(true);
        newPasswordLabel.setVisible(true);
        passwordTextField.setVisible(true);
        newPasswordTextField.setVisible(true);


        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handleShowAccountInformation(Employee employee ,Account account) throws IOException {
        String source = "/iuh/fit/view/features/employee/EmployeeInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load();

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
        passwordLabel.setText("Mật khẩu");
        newPasswordLabel.setVisible(false);
        passwordTextField.setText("");
        newPasswordTextField.setText("");
        newPasswordTextField.setVisible(false);
        usernameTextField.setEditable(true);
        usernameTextField.setDisable(false);
        accountIDTextField.setText(AccountDAO.getNextAccountID());
        employeeIDCBox.getSelectionModel().select("");
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
            if(employeeIDCBox.getSelectionModel().getSelectedItem() == null){
                dialogPane.showWarning("LỖI", "Phải chọn nhân viên chưa có tài khoản để tạo tài khoản");
                return;
            }
            Employee employee = EmployeeDAO.getDataByID(employeeIDCBox.getValue());

            if (!RegexChecker.isValidPassword(passwordTextField.getText())) {
                dialogPane.showWarning("LỖI", "Mật khẩu không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.");
                return;
            }

            String hashedPassword = PasswordHashing.hashPassword(passwordTextField.getText());

            String status = switch (statusCBox.getSelectionModel().getSelectedItem()) {
                case "ĐANG HOẠT ĐỘNG" -> "ACTIVE";
                case "KHÔNG HOẠT ĐỘNG" -> "INACTIVE";
                case "BỊ KHÓA" -> "LOCKED";
                default -> statusCBox.getSelectionModel().getSelectedItem();
            };
            Account account = new Account(
                    accountIDTextField.getText(),
                    employee,
                    usernameTextField.getText(),
                    hashedPassword,
                    ConvertHelper.accountStatusConverter(status)
            );

            Account acc = AccountDAO.getAccountByEmployeeID(employeeIDCBox.getValue());
            if(acc == null){
                AccountDAO.createData(account);
            } else {
                dialogPane.showInformation("Thành công", "Nhân viên đã có tài khoản");
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

                String status = switch (account.getAccountStatus().name()) {
                    case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
                    case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
                    case "LOCKED" -> "BỊ KHÓA";
                    default -> account.getAccountStatus().name();
                };

                statusSearchField.setText(status);
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
            employeeList = EmployeeDAO.getEmployees().stream()
                    .filter(x -> AccountDAO.getAccountByEmployeeID(x.getEmployeeID()) == null)
                    .toList();
        } else {
            employeeList = EmployeeDAO.findDataByContainsId(searchText);
            if (!employeeList.isEmpty()) {
                Employee employee = employeeList.getFirst();
                fullNameTextField.setText(String.valueOf(employee.getFullName()));
                employeeIDCBox.getSelectionModel().select(employee.getEmployeeID());
            }
        }
    }

    private void handleUpdateAction() {
        try {
            Employee employee = EmployeeDAO.getDataByID(employeeIDCBox.getValue());

            String hashedNewPassword = PasswordHashing.hashPassword(newPasswordTextField.getText());

            String status = switch (statusCBox.getSelectionModel().getSelectedItem()) {
                case "ĐANG HOẠT ĐỘNG" -> "ACTIVE";
                case "KHÔNG HOẠT ĐỘNG" -> "INACTIVE";
                case "BỊ KHÓA" -> "LOCKED";
                default -> statusCBox.getSelectionModel().getSelectedItem();
            };

            Account account = new Account(
                    accountIDTextField.getText(),
                    employee,
                    usernameTextField.getText(),
                    hashedNewPassword,
                    ConvertHelper.accountStatusConverter(status)
            );

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin tài khoản của nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {
                        String oldPass = Objects.requireNonNull(AccountDAO.getAccountByEmployeeID(employeeIDCBox.getValue())).getPassword();
                        String tmpPass = PasswordHashing.hashPassword(passwordTextField.getText());
                        String newPass = PasswordHashing.hashPassword(newPasswordTextField.getText());

                        if (!RegexChecker.isValidPassword(newPasswordTextField.getText())) {
                            dialogPane.showWarning("LỖI", "Mật khẩu mới không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.");
                            return;
                        }

                        if (!oldPass.equals(tmpPass)) {
                            dialogPane.showWarning("LỖI", "Mật khẩu cũ không đúng!");
                            return;
                        } else if (tmpPass.equals(newPass)) {
                            dialogPane.showWarning("LỖI", "Mật khẩu cũ và mật khẩu mới phải khác nhau!");
                            return;
                        }
                        AccountDAO.updateData(account);
                        Platform.runLater(() -> {
                            dialogPane.showInformation("Thành công", "Đã đổi mật khẩu thành công");
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
