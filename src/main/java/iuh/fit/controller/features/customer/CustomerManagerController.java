package iuh.fit.controller.features.customer;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerManagerController implements Initializable {

    // input field
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerCCCDTextField;
    @FXML
    private DatePicker customerDOBDatePicker;
    @FXML
    private TextField customerEmailTextField;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerPhoneNumberTextField;
    @FXML
    private RadioButton radFemale;

    @FXML
    private RadioButton radMale;

    // search field
    @FXML
    private TextField customerNameSearchField;
    @FXML
    private TextField customerGenderSearchField;
    @FXML
    private TextField customerPhoneSearchField1;
    @FXML
    private ComboBox<?> roomCategoryIDSearchField;

    // table
    @FXML
    private TableColumn<Customer, Void> actionColumn;
    @FXML
    private TableColumn<Customer, Gender> customerGenderColumn;
    @FXML
    private TableColumn<Customer, String> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableView<Customer> customerTableView;

    // button
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // group
    @FXML
    private ToggleGroup gender;

    // dialogs
    @FXML
    private DialogPane dialogPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // vì cái biến count của customer khi chạy lại nó sẽ thành 0,
        // nên cần gáng lại số lượng hiện tại của nhân viên
        Customer.setCount(CustomerDAO.getValueCount());
        loadNextid();
        dialogPane.toFront();
        loadDataToTable();
    }

    // load next id to textID field
    private void loadNextid(){
        // tai cusid len cusid textfield
        int count = Customer.getCount();
        String cusID = String.format("CUS-%06d", count);
        customerIDTextField.setText(cusID);

    }

    private void loadDataToTable(){
        List<Customer> list = CustomerDAO.getCustomer();
        ObservableList<Customer> customers = FXCollections.observableList(list);

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("fullName"));
        customerGenderColumn.setCellValueFactory(new PropertyValueFactory<Customer, Gender>("gender"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerTableView.setItems(customers);
        setupActionColumn();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final Button showInfoButton = new Button("Thông tin");
            private final HBox hBox = new HBox(10);
            {
                // Thêm class CSS cho các button
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                showInfoButton.getStyleClass().add("button-view");

                // Thêm file CSS vào HBox
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                // Set hành động cho các button
                updateButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleUpdateBtn(customer);
                });

                deleteButton.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    handleDeleteAction(customer);
                });

                showInfoButton.setOnAction(e -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    try {
                        handleShowCustomerInformation(customer);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton, showInfoButton);
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

    public void resetField() {
        customerNameTextField.setText("");
        customerPhoneNumberTextField.setText("");
        customerEmailTextField.setText("");
        customerAddressTextField.setText("");
        Toggle rad =  gender.getSelectedToggle();
        if(rad != null)
            rad.setSelected(false);
        customerCCCDTextField.setText("");
        customerDOBDatePicker.setValue(null);
        customerNameTextField.requestFocus();
    }

    private Customer createCustomer() {
        String id = customerIDTextField.getText();
        String name = customerNameTextField.getText();
        String phone = customerPhoneNumberTextField.getText();
        String email = customerEmailTextField.getText();
        String address = customerAddressTextField.getText();
        RadioButton rad =  (RadioButton) gender.getSelectedToggle();
        Gender gender = null;
        if(rad != null){
            String value = rad.getText();
            gender = value.equals(Gender.MALE.toString()) ? Gender.MALE : Gender.FEMALE;
        }
        String CCCD = customerCCCDTextField.getText();
        LocalDate dob = customerDOBDatePicker.getValue();
        System.out.println(CCCD);

        return new Customer(id, name, phone, email, address, gender, CCCD, dob);
    }

    public void addCus(ActionEvent event) {
        try{
            Customer customer = createCustomer();
            CustomerDAO.createData(customer);
            int valueCount = Customer.getCount();
            Customer.setCount(++valueCount);
            CustomerDAO.setValueCount(valueCount);
            loadNextid();
            loadDataToTable();
            resetField();
            dialogPane.showInformation("Thành công", "Đã thêm khách hàng thành công");
        }catch (Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }


    private void handleDeleteAction(Customer customer) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                CustomerDAO.deleteData(customer.getCustomerID());
                resetField();
                loadDataToTable();
            }
        });
    }

    private void handleUpdateBtn(Customer customer) {
        customerIDTextField.setText(customer.getCustomerID());
        customerNameTextField.setText(customer.getFullName());
        customerPhoneNumberTextField.setText(customer.getPhoneNumber());
        customerEmailTextField.setText(customer.getEmail());
        customerAddressTextField.setText(customer.getAddress());
        customerIDTextField.setText(customer.getCustomerID());
        customerIDTextField.setText(customer.getCustomerID());
        customerIDTextField.setText(customer.getCustomerID());
        if(customer.getGender().equals(Gender.MALE))
            radMale.setSelected(true);
        radFemale.setSelected(true);
        customerCCCDTextField.setText(customer.getIdCardNumber());
        customerDOBDatePicker.setValue(customer.getDob());

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    public void updateCustomer(ActionEvent event) {
        Customer newInfor = createCustomer();
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn cập nhật?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                CustomerDAO.updateData(newInfor);
                resetField();
                loadDataToTable();
            }
        });

        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
        addBtn.setManaged(true);
        addBtn.setVisible(true);
    }
}
