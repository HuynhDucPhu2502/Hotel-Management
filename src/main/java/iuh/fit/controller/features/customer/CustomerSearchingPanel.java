package iuh.fit.controller.features.customer;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
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

public class CustomerSearchingPanel {
    // Input Fields
    @FXML
    private TextField customerIDTextField;
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
    private DatePicker DOBPicker;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;
    @FXML
    private ToggleGroup gender;

    // Table
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> fullNameColumn;
    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> cardIDColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer , Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button searchBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Customer> items;

    public void initialize() {
        loadData();
        setupTable();
        searchBtn.setOnAction(e -> handleSearchAction());
        resetBtn.setOnAction(e -> handleResetAction());
    }

    private void loadData() {
        List<Customer> customerList = CustomerDAO.getCustomer();
        items = FXCollections.observableArrayList(customerList);
        customerTableView.setItems(items);
        customerTableView.refresh();
    }

    private void setupTable() {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        cardIDColumn.setCellValueFactory(new PropertyValueFactory<>("idCardNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellFactory = param -> new TableCell<>() {
            private final Button showInfoButton = new Button("Thông tin");
            private final HBox hBox = new HBox(10);
            {
                // Thêm class CSS cho các button
                showInfoButton.getStyleClass().add("button-view");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                showInfoButton.setOnAction(e -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    try {
                        handleShowCustomerInformation(customer);
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

    private void handleShowCustomerInformation(Customer customer) throws IOException {
        String source = "/iuh/fit/view/features/customer/CustomerInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load(); // Gọi load() trước khi getController()

        CustomerInformationViewController customerInformationViewController = loader.getController();
        customerInformationViewController.setCustomer(customer);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.setTitle("Thông tin khách hàng");
        stage.setScene(scene);
        stage.show();
    }

    private void handleResetAction() {
        customerIDTextField.setText("");
        fullNameTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        cardIDTextField.setText("");
        male.setSelected(true);
        DOBPicker.setValue(null);
        addressTextField.setText("");
        gender.getSelectedToggle().setSelected(false);

        loadData();
    }

    private void handleSearchAction() {
        try {
            String customerID = customerIDTextField.getText().isBlank() ? null : customerIDColumn.getText().trim();
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

            List<Customer> searchResults = CustomerDAO.searchCustomer(
                    customerID, fullName, phoneNumber, email, address, gder, cardID, dob
            );
            items.setAll(searchResults);
            customerTableView.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
