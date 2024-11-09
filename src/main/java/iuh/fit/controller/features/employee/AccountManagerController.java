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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
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
    @FXML
    private TextField positionSearchField;

    // Input Fields
    @FXML
    private TextField accountIDTextField;
    @FXML
    private TextField employeeIDCBox;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private ComboBox<String> statusCBox;
    @FXML
    private Text passwordLabel;

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
    private TableColumn<Account, String> positionColumn;
    @FXML
    private TableColumn<Account , Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
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
        updateBtn.setOnAction(e -> handleUpdateAction());
        employeeIDSearchField.setOnAction(e -> handleSearchAction());
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
        positionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmployee().getPosition().name().equals("MANAGER")?"QUẢN LÝ":"LỄ TÂN"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        statusColumn.setCellValueFactory(data -> {
            String status = switch (data.getValue().getAccountStatus().name()) {
                case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
                case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
                case "LOCKED" -> "BỊ KHÓA";
                default -> data.getValue().getAccountStatus().name();
            };
            return new SimpleStringProperty(status);
        });
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Account, Void>, TableCell<Account, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButtonPass = new Button("Cập nhật mật khẩu");
            private final Button updateButtonStatus = new Button("Cập nhật trạng thái");
            private final Button showInfoButton = new Button("Thông tin");
            private final HBox hBox = new HBox(10);
            {
                // Thêm class CSS cho các button
                updateButtonPass.getStyleClass().add("button-update");
                updateButtonStatus.getStyleClass().add("button-update");
                showInfoButton.getStyleClass().add("button-view");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                // Set hành động cho các button
                updateButtonPass.setOnAction(event -> {
                    Account account = getTableView().getItems().get(getIndex());
                    handleUpdatePassBtn(account);
                });

                updateButtonStatus.setOnAction(event -> {
                    Account account = getTableView().getItems().get(getIndex());
                    handleUpdateStatusBtn(account);
                });

                showInfoButton.setOnAction(e -> {
                    Account account = getTableView().getItems().get(getIndex());
                    try {
                        handleShowAccountInformation(account.getEmployee());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButtonStatus, updateButtonPass, showInfoButton);
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

    private void handleUpdatePassBtn(Account account) {
        accountIDTextField.setText(account.getAccountID());
        passwordLabel.setText("Mật khẩu mới");

        employeeIDCBox.setText(account.getEmployee().getEmployeeID());

        fullNameTextField.setText(account.getEmployee().getFullName());
        usernameTextField.setText(account.getUserName());

        String status = switch (account.getAccountStatus().name()) {
            case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
            case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
            case "LOCKED" -> "BỊ KHÓA";
            default -> account.getAccountStatus().name();
        };
        statusCBox.getSelectionModel().select(status);

        statusCBox.setEditable(false);

        employeeIDCBox.setEditable(false);

        passwordTextField.setEditable(true);
        passwordTextField.setText("");
        passwordTextField.setStyle("-fx-background-color: white;");

        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handleUpdateStatusBtn(Account account) {
        accountIDTextField.setText(account.getAccountID());

        employeeIDCBox.setText(account.getEmployee().getEmployeeID());

        fullNameTextField.setText(account.getEmployee().getFullName());

        passwordTextField.setText(account.getPassword());

        usernameTextField.setText(account.getUserName());

        String status = switch (account.getAccountStatus().name()) {
            case "ACTIVE" -> "ĐANG HOẠT ĐỘNG";
            case "INACTIVE" -> "KHÔNG HOẠT ĐỘNG";
            case "LOCKED" -> "BỊ KHÓA";
            default -> account.getAccountStatus().name();
        };
        statusCBox.getSelectionModel().select(status);

        passwordTextField.setEditable(false);
        passwordTextField.setStyle("-fx-background-color: rgb(211, 211, 211);");
        employeeIDCBox.setEditable(false);

        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handleShowAccountInformation(Employee employee) throws IOException {
        String source = "/iuh/fit/view/features/employee/EmployeeInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load();

        EmployeeInformationViewController employeeInformationViewController = loader.getController();
        employeeInformationViewController.setEmployee(employee);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.setTitle("Thông tin nhân viên");
        stage.setScene(scene);
        stage.show();
    }

    private void handleResetAction() {
        passwordLabel.setText("Mật khẩu");

        passwordTextField.setText("");
        accountIDTextField.setText(AccountDAO.getNextAccountID());
        employeeIDCBox.setText("");
        fullNameTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
        if (!statusCBox.getItems().isEmpty()) statusCBox.getSelectionModel().selectFirst();
        passwordTextField.setEditable(false);
        passwordTextField.setStyle("-fx-background-color: rgb(211, 211, 211);");

        fullNameSearchField.setText("");
        usernameSearchField.setText("");
        statusSearchField.setText("");
        employeeIDSearchField.getSelectionModel().select(null);
        setupTable();

        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
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
                positionSearchField.setText(account.getEmployee().getPosition().name().equalsIgnoreCase("MANAGER")?"QUẢN LÝ":"LỄ TÂN");
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

    private void handleUpdateAction() {
        try {
            Account account = AccountDAO.getAccountByEmployeeID(employeeIDCBox.getText());

            if (account.getEmployee().getPosition().name().equals("MANAGER")){
                dialogPane.showWarning("LỖI", "Không thể cập nhật mật khẩu cho QUẢN LÝ!!! Chỉ cho phép cập nhật mật khẩu cho LỄ TÂN");
                return;
            }

            com.dlsc.gemsfx.DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật thông tin tài khoản của nhân viên này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    try {

                        if(passwordTextField.isEditable()){
                            if (!RegexChecker.isValidPassword(passwordTextField.getText())) {
                                dialogPane.showWarning("LỖI", "Mật khẩu mới không hợp lệ! Phải có ít nhất 8 ký tự, bao gồm chữ, số và ký tự đặc biệt.");
                                return;
                            }
                            String hashedNewPassword = PasswordHashing.hashPassword(passwordTextField.getText());
                            if (hashedNewPassword.equals(account.getPassword())){
                                dialogPane.showWarning("LỖI", "Mật khẩu mới phải khác mật khẩu cũ.");
                                return;
                            }
                            account.setPassword(hashedNewPassword);

                        } else {
                            String status = switch (statusCBox.getSelectionModel().getSelectedItem()) {
                                case "ĐANG HOẠT ĐỘNG" -> "ACTIVE";
                                case "KHÔNG HOẠT ĐỘNG" -> "INACTIVE";
                                case "BỊ KHÓA" -> "LOCKED";
                                default -> statusCBox.getSelectionModel().getSelectedItem();
                            };
                            account.setAccountStatus(ConvertHelper.accountStatusConverter(status));

                        }
                        AccountDAO.updateData(account);
                        Platform.runLater(() -> {
                            dialogPane.showInformation("Thành công", "Cập nhật thông tin thành công");
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
