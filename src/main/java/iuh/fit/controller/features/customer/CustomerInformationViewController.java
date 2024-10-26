package iuh.fit.controller.features.customer;

import iuh.fit.models.Customer;
import iuh.fit.models.enums.Gender;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerInformationViewController {

    @FXML
    private TextArea addressTextAria;

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

    public void setCustomer(Customer customer) {
        customerIDTextField.setText(customer.getCustomerID());
        customerNameTextField.setText(customer.getCusFullName());
        customerPhoneNumberTextField.setText(customer.getPhoneNumber());
        customerEmailTextField.setText(customer.getEmail());
        addressTextAria.setText(customer.getAddress());

        if (customer.getGender().equals(Gender.MALE)) {
            radMale.setSelected(true);
        } else {
            radFemale.setSelected(true);
        }

        customerCCCDTextField.setText(customer.getIdCardNumber());
        customerDOBDatePicker.setValue(customer.getDob());
    }

}
