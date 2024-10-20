package iuh.fit.controller.features.customer;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class InformationView_Controller {

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
    private ToggleGroup gender;

    @FXML
    private RadioButton radFemale;

    @FXML
    private RadioButton radMale;

    public TextArea getAddressTextAria() {
        return addressTextAria;
    }

    public TextField getCustomerCCCDTextField() {
        return customerCCCDTextField;
    }

    public DatePicker getCustomerDOBDatePicker() {
        return customerDOBDatePicker;
    }

    public TextField getCustomerEmailTextField() {
        return customerEmailTextField;
    }

    public TextField getCustomerIDTextField() {
        return customerIDTextField;
    }

    public TextField getCustomerNameTextField() {
        return customerNameTextField;
    }

    public TextField getCustomerPhoneNumberTextField() {
        return customerPhoneNumberTextField;
    }

    public ToggleGroup getGender() {
        return gender;
    }

    public RadioButton getRadFemale() {
        return radFemale;
    }

    public RadioButton getRadMale() {
        return radMale;
    }
}
