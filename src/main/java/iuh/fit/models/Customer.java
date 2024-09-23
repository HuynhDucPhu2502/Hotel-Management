package iuh.fit.models;

import iuh.fit.models.enums.Gender;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private String customerID;
    private String fulName;
    private String phoneNumber;
    private String email;
    private String address;
    private Gender gender;
    private String idCardNumber;
    private LocalDate dob;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getCustomerID(), customer.getCustomerID()) && Objects.equals(getIdCardNumber(), customer.getIdCardNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerID(), getIdCardNumber());
    }

    public Customer(String customerID, String fulName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob) {
        setCustomerID(customerID);
        setFulName(fulName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setAddress(address);
        setGender(gender);
        setIdCardNumber(idCardNumber);
        setDob(dob);
    }

    public Customer() {
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        if(!RegexChecker.isValidIDFormat(GlobalConstants.CUSTOMER_PREFIX, customerID))
            throw new IllegalArgumentException(ErrorMessages.CUS_INVALID_ID);
        this.customerID = customerID;
    }

    public String getFulName() {
        return fulName;
    }

    public void setFulName(String fulName) {
        if(!RegexChecker.isValidCusFullName(fulName))
        this.fulName = fulName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(!RegexChecker.isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_PHONENUMBER);
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!RegexChecker.isValidEmail(email))
            throw new IllegalArgumentException(ErrorMessages.INVALID_EMAIL);
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        if(RegexChecker.isValidCCCD(idCardNumber)){
            throw new IllegalArgumentException(ErrorMessages.INVALID_CCCD);
        }
        this.idCardNumber = idCardNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        if(RegexChecker.isValidDOB(dob))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_DOB);
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", fulName='" + fulName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", dob=" + dob +
                '}';
    }
}
