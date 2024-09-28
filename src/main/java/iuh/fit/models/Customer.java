package iuh.fit.models;

import iuh.fit.models.enums.Gender;         // Import enum Gender (Nam, Nữ, ...)
import iuh.fit.utils.ErrorMessages;         // Import các thông điệp lỗi dùng chung
import iuh.fit.utils.GlobalConstants;       // Import các hằng số toàn cục
import iuh.fit.utils.RegexChecker;          // Import công cụ kiểm tra định dạng thông qua regex

import java.time.LocalDate;                 // Import class LocalDate dùng để quản lý ngày tháng
import java.util.Objects;                   // Import class Objects dùng để so sánh và tạo hash code

public class Customer {
    // Các thuộc tính của khách hàng
    private String customerID;              // Mã khách hàng
    private String fullName;                 // Tên đầy đủ
    private String phoneNumber;             // Số điện thoại
    private String email;                   // Địa chỉ email
    private String address;                 // Địa chỉ
    private Gender gender;                  // Giới tính
    private String idCardNumber;            // Số CMND/CCCD
    private LocalDate dob;                  // Ngày sinh

    // Phương thức equals để so sánh hai đối tượng Customer
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(getCustomerID(), customer.getCustomerID()) &&
                Objects.equals(getIdCardNumber(), customer.getIdCardNumber());
    }

    // Phương thức hashCode để tạo mã băm từ customerID và idCardNumber
    @Override
    public int hashCode() {
        return Objects.hash(getCustomerID(), getIdCardNumber());
    }

    // Constructor với tất cả các thuộc tính
    public Customer(String customerID, String fullName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob) {
        setCustomerID(customerID);
        setFullName(fullName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setAddress(address);
        setGender(gender);
        setIdCardNumber(idCardNumber);
        setDob(dob);
    }

    // Constructor không tham số
    public Customer() {
    }

    // Các phương thức getter và setter để truy cập và thay đổi thuộc tính của Customer

    // Kiểm tra mã khách hàng hợp lệ theo định dạng quy định trong RegexChecker và GlobalConstants
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        if (!RegexChecker.isValidIDFormat(GlobalConstants.CUSTOMER_PREFIX, customerID))
            throw new IllegalArgumentException(ErrorMessages.CUS_INVALID_ID);
        this.customerID = customerID;
    }

    // Kiểm tra tên hợp lệ
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        fullName = fullName.trim().replaceAll("\\s+", " ");
        if (!RegexChecker.isValidName(fullName, 3, 30))
            throw new IllegalArgumentException(ErrorMessages.CUS_INVALID_FULLNAME);
        this.fullName = fullName;
    }

    // Kiểm tra số điện thoại hợp lệ
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!RegexChecker.isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_PHONENUMBER);
        this.phoneNumber = phoneNumber;
    }

    // Kiểm tra email hợp lệ
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!RegexChecker.isValidEmail(email))
            throw new IllegalArgumentException(ErrorMessages.INVALID_EMAIL);
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    //Kiểm tra địa chỉ hợp lệ
    public void setAddress(String address) {
        if(address.trim().isBlank()){
            throw new IllegalArgumentException(ErrorMessages.INVALID_ADDRESS);
        }
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // Kiểm tra số CCCD hợp lệ
    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidCCCD(idCardNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_CCCD);
        this.idCardNumber = idCardNumber;
    }

    // Kiểm tra ngày sinh hợp lệ
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        if (!RegexChecker.isValidDOB(dob))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_DOB);
        this.dob = dob;
    }

    // Phương thức toString để hiển thị thông tin của đối tượng Customer
    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", dob=" + dob +
                '}';
    }
}
