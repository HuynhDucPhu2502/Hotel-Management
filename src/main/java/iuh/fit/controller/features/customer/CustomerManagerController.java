package iuh.fit.controller.features.customer;

import com.dlsc.gemsfx.CalendarPicker;
import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.CustomerDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
import iuh.fit.utils.ErrorMessages;
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

public class CustomerManagerController {

    // Input Fields
    @FXML
    private TextField customerAddressTextField;
    @FXML
    private TextField customerIDCardNumberTextField;
    @FXML
    private CalendarPicker customerDOBCalendarPicker;
    @FXML
    private TextField customerEmailTextField;
    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField customerPhoneNumberTextField;
    @FXML
    private ToggleGroup genderToggleGroup;
    @FXML
    private RadioButton radFemale;
    @FXML
    private RadioButton radMale;

    // Search Fields
    @FXML
    private TextField customerNameSearchField;
    @FXML
    private TextField customerGenderSearchField;
    @FXML
    private TextField customerPhoneSearchField;
    @FXML
    private ComboBox<String> customerIDSearchField;

    // Table
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Gender> customerGenderColumn;
    @FXML
    private TableColumn<Customer, String> customerIDColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableColumn<Customer, Void> actionColumn;

    // Buttons
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button resetBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<Customer> items;

    // Gọi mấy phương thức để gắn sự kiện và dữ liệu cho lúc đầu khởi tạo giao diện
    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        resetBtn.setOnAction(e -> handleResetAction());
        customerIDSearchField.setOnAction(e -> handleSearchAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        List<Customer> customerList = CustomerDAO.getCustomer();
        items = FXCollections.observableArrayList(customerList);
        customerTableView.setItems(items);
        customerTableView.refresh();

        customerIDTextField.setText(CustomerDAO.getNextCustomerID());
        List<String> Ids = CustomerDAO.getTopThreeID();
        customerIDSearchField.getItems().setAll(Ids);
    }


    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        List<Customer> list = CustomerDAO.getCustomer();
        ObservableList<Customer> customers = FXCollections.observableList(list);

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        customerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        customerTableView.setItems(customers);
        setupActionColumn();
    }

    // setup cho cột thao tác
    // THAM KHẢO
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

    // Chức năng 1: Làm mới
    public void handleResetAction() {
        customerIDTextField.setText(CustomerDAO.getNextCustomerID());
        customerIDCardNumberTextField.setText("");
        customerNameTextField.setText("");
        customerPhoneNumberTextField.setText("");
        customerEmailTextField.setText("");
        customerAddressTextField.setText("");
        Toggle rad =  genderToggleGroup.getSelectedToggle();
        if (rad != null) rad.setSelected(false);
        customerDOBCalendarPicker.setValue(null);
        customerNameTextField.requestFocus();

        switchButton(false, true);

    }

    // Chức năng 2: Thêm
    public void handleAddAction() {
        try{
            Customer customer = createCustomer();
            CustomerDAO.createData(customer);
            setupTable();
            handleResetAction();
            dialogPane.showInformation("Thành công", "Đã thêm khách hàng thành công");
        }catch (Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(Customer customer) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                CustomerDAO.deleteData(customer.getCustomerID());
                handleResetAction();
                setupTable();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(Customer customer) {
        customerIDTextField.setText(customer.getCustomerID());
        customerNameTextField.setText(customer.getFullName());
        customerPhoneNumberTextField.setText(customer.getPhoneNumber());
        customerEmailTextField.setText(customer.getEmail());
        customerAddressTextField.setText(customer.getAddress());
        customerIDCardNumberTextField.setText(customer.getIdCardNumber());
        customerDOBCalendarPicker.setValue(customer.getDob());

        if (customer.getGender().equals(Gender.MALE)) radMale.setSelected(true);
        else radFemale.setSelected(true);

        switchButton(true, false);
    }

    // 4.2 Chức năng cập nhật
    public void handleUpdateAction() {
        try {
            Customer newInfor = createCustomer();
            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Bạn có chắc chắn muốn cập nhật?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    CustomerDAO.updateData(newInfor);
                    loadData();
                    handleResetAction();
                }
            });

            switchButton(false, true);
        } catch (Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 5: Tìm kiếm
    private void handleSearchAction() {
        // Xóa các trường tìm kiếm trước đó
        customerNameSearchField.setText("");
        customerPhoneSearchField.setText("");
        customerGenderSearchField.setText("");

        // Lấy giá trị tìm kiếm từ ComboBox
        String searchText = customerIDSearchField.getValue();
        List<Customer> customers;

        if (searchText == null || searchText.isEmpty()) {
            // Lấy toàn bộ danh sách khách hàng nếu không có input
            customers = CustomerDAO.getCustomer();
        } else {
            // Tìm kiếm khách hàng theo ID
            customers = CustomerDAO.findDataByContainsId(searchText);

            if (customers.size() == 1) {
                // Nếu chỉ có 1 khách hàng được tìm thấy, điền dữ liệu vào các trường
                Customer customer = customers.getFirst();
                customerNameSearchField.setText(customer.getFullName());
                customerPhoneSearchField.setText(customer.getPhoneNumber());
                String gender = customer.getGender() != null ? customer.getGender().name() : "KHÔNG XÁC ĐỊNH";
                customerGenderSearchField.setText(gender);
            }
        }

        // Cập nhật lại bảng với dữ liệu đã tìm kiếm
        items.setAll(customers);
        customerTableView.setItems(items);
    }


    // Chức năng 6: Xem thông tin khách hàng
    private void handleShowCustomerInformation(Customer customer) throws IOException {
        String source = "/iuh/fit/view/features/customer/CustomerInformationView.fxml";

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(source)));
        AnchorPane layout = loader.load();

        CustomerInformationViewController customerInformationViewController = loader.getController();
        customerInformationViewController.setCustomer(customer);

        Scene scene = new Scene(layout);

        Stage stage = new Stage();
        stage.setTitle("Thông Tin " + customer.getCustomerID());
        stage.setScene(scene);
        stage.show();
    }

    private Customer createCustomer() {
        String id = customerIDTextField.getText();
        String name = customerNameTextField.getText();
        String phone = customerPhoneNumberTextField.getText();
        String email = customerEmailTextField.getText();
        String address = customerAddressTextField.getText();

        Gender gender;
        RadioButton rad =  (RadioButton) genderToggleGroup.getSelectedToggle();
        if (rad == null) throw new IllegalArgumentException(ErrorMessages.CUS_GENDER_NOT_SELECTED);
        else if (rad.getText().equalsIgnoreCase("NAM")) gender = Gender.MALE;
        else gender = Gender.FEMALE;

        String idCardNumber = customerIDCardNumberTextField.getText();
        LocalDate dob = customerDOBCalendarPicker.getValue();

        return new Customer(id, name, phone, email, address, gender, idCardNumber, dob);
    }

    private void switchButton(boolean updateButton, boolean addButton){
        updateBtn.setManaged(updateButton);
        updateBtn.setVisible(updateButton);
        addBtn.setManaged(addButton);
        addBtn.setVisible(addButton);
    }
}
