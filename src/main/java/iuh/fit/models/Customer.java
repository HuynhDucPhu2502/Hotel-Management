package iuh.fit.models;

import iuh.fit.models.enums.Gender;         // Import enum Gender (Nam, Nữ, ...)
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.ErrorMessages;         // Import các thông điệp lỗi dùng chung
import iuh.fit.utils.GlobalConstants;       // Import các hằng số toàn cục
import iuh.fit.utils.RegexChecker;          // Import công cụ kiểm tra định dạng thông qua regex

import java.time.LocalDate;                 // Import class LocalDate dùng để quản lý ngày tháng
import java.util.Objects;                   // Import class Objects dùng để so sánh và tạo hash code

public class Customer {
    private String customerID;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private Gender gender;
    private String idCardNumber;
    private LocalDate dob;
    private ObjectStatus objectStatus;

    public Customer(String customerID, String fulName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob, ObjectStatus objectStatus) {
        setCustomerID(customerID);
        setFullName(fulName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setAddress(address);
        setGender(gender);
        setIdCardNumber(idCardNumber);
        setDob(dob);
        setObjectStatus(objectStatus);
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(customerID, customer.customerID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerID);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", fulName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", dob=" + dob +
                '}';
    }

    public void setCustomerID(String customerID) {
        if (!RegexChecker.isValidIDFormat(GlobalConstants.CUSTOMER_PREFIX, customerID))
            throw new IllegalArgumentException(ErrorMessages.CUS_INVALID_ID);
        this.customerID = customerID;
    }

    public void setFullName(String fullName) {
        fullName = fullName.trim().replaceAll("\\s+", " ");
        if (!RegexChecker.isValidName(fullName, 3, 30))
            throw new IllegalArgumentException(ErrorMessages.CUS_INVALID_FULLNAME);
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!RegexChecker.isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_PHONENUMBER);
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (!RegexChecker.isValidEmail(email))
            throw new IllegalArgumentException(ErrorMessages.INVALID_EMAIL);
        this.email = email;
    }

    public void setAddress(String address) {
        if(address.trim().isBlank()){
            throw new IllegalArgumentException(ErrorMessages.INVALID_ADDRESS);
        }
        this.address = address;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setIdCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidIDCardNumber(idCardNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_ID_CARD_NUMBER);
        this.idCardNumber = idCardNumber;
    }

    public void setDob(LocalDate dob) {
        if (!RegexChecker.isValidDOB(dob))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_DOB);
        this.dob = dob;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Gender getGender() {
        return gender;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }
}
