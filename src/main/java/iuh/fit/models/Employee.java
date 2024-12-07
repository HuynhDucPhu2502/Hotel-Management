package iuh.fit.models;

import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private String employeeID;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private Gender gender;
    private String idCardNumber;
    private LocalDate dob;
    private Position position;
    private ObjectStatus objectStatus;
    private String avatar;

    public Employee(String employeeID, String fullName, String phoneNumber, String email, String address, String idCardNumber, Gender gender, LocalDate dob, Position position, ObjectStatus objectStatus) {
        setEmployeeID(employeeID);
        setFullName(fullName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setAddress(address);
        setIdCardNumber(idCardNumber);
        setGender(gender);
        setDob(dob);
        setPosition(position);
        setObjectStatus(objectStatus);
        setAvatar(GlobalConstants.DEFAULT_AVATAR_BASE64);
    }


    public Employee(String employeeID) {
        setEmployeeID(employeeID);
    }


    public Employee() {
    }


    public void setEmployeeID(String employeeID) {
        if (!RegexChecker.isValidIDFormat(GlobalConstants.EMPLOYEE_PREFIX, employeeID))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_ID);
        this.employeeID = employeeID;
    }

    public void setFullName(String fullName) {
        fullName = fullName.trim().replaceAll("\\s+", " ");

        if (!RegexChecker.isValidName(fullName, 3, 50))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_FULLNAME);
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
        this.address = address;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDob(LocalDate dob) {
        if (!RegexChecker.isValidDOB(dob))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_DOB);
        this.dob = dob;
    }

    public void setIdCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidIDCardNumber(idCardNumber))
            throw new IllegalArgumentException(ErrorMessages.INVALID_ID_CARD_NUMBER);
        this.idCardNumber = idCardNumber;
    }

    public void setPosition(Position position) {
        if (position == null) throw new IllegalArgumentException(ErrorMessages.NULL_POSITION);
        this.position = position;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
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

    public Position getPosition() {
        return position;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID='" + employeeID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", dob=" + dob +
                ", position=" + position +
                ", objectStatus=" + objectStatus +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(employeeID, employee.employeeID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(employeeID);
    }


}
