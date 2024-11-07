package iuh.fit.controller.features.customer;

import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.enums.Gender;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerInformationViewController {

    @FXML
    private TextField customerIDTextField, customerNameTextField,
            customerPhoneNumberTextField, customerEmailTextField,
            customerIDCardNumberTextField, customerDOBTextField;
    @FXML
    private TextArea addressTextAria;
    @FXML
    private RadioButton radFemale, radMale;

    @FXML
    private TableView<ReservationForm> ReservationFormTableView;
    @FXML
    private TableColumn<ReservationForm, String> indexColumn;
    @FXML
    private TableColumn<ReservationForm, String> reservationFormIDColumn;
    @FXML
    private TableColumn<ReservationForm, String> checkInDateColumn;
    @FXML
    private TableColumn<ReservationForm, String> checkoutDateColumn;

    private final ObservableList<ReservationForm> reservationItems = FXCollections.observableArrayList();

    public void setCustomer(Customer customer) {
        customerIDTextField.setText(customer.getCustomerID());
        customerNameTextField.setText(customer.getFullName());
        customerPhoneNumberTextField.setText(customer.getPhoneNumber());
        customerEmailTextField.setText(customer.getEmail());
        addressTextAria.setText(customer.getAddress());

        if (customer.getGender().equals(Gender.MALE)) {
            radMale.setSelected(true);
        } else {
            radFemale.setSelected(true);
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        customerIDCardNumberTextField.setText(customer.getIdCardNumber());
        customerDOBTextField.setText(dateTimeFormatter.format(customer.getDob()));

        setupTable();

        loadReservationForms(customer.getCustomerID());
    }

    private void setupTable() {
        indexColumn.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(ReservationFormTableView.getItems().indexOf(param.getValue()) + 1)));
        reservationFormIDColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        checkInDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCheckInDate().toString()));
        checkoutDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCheckOutDate().toString()));

        ReservationFormTableView.setItems(reservationItems);
    }

    private void loadReservationForms(String customerID) {
        Task<List<ReservationForm>> loadTask = new Task<>() {
            @Override
            protected List<ReservationForm> call() {
                return ReservationFormDAO.getReservationFormByCustomerID(customerID);
            }
        };

        loadTask.setOnSucceeded(e -> Platform.runLater(() -> {
            reservationItems.setAll(loadTask.getValue());
            ReservationFormTableView.refresh();
        }));

        loadTask.setOnFailed(e -> {
            System.out.println("Không tải được dữ liệu");
            e.getSource().getException().printStackTrace();
        });

        new Thread(loadTask).start();
    }

}
