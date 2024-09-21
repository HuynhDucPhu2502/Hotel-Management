package iuh.fit.models;

import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Lớp Employee đại diện cho thông tin của một nhân viên trong hệ thống.
 */
public class Employee {
    // ID nhân viên
    private String employeeID;
    // Họ và tên của nhân viên
    private String fullName;
    // Số điện thoại của nhân viên
    private String phoneNumber;
    // Email của nhân viên
    private String email;
    // Địa chỉ của nhân viên
    private String address;
    // Giới tính của nhân viên (Nam, Nữ)
    private Gender gender;
    // Số CMND/CCCD của nhân viên
    private String idCardNumber;
    // Ngày sinh của nhân viên
    private LocalDate dob;
    // Vị trí của nhân viên trong công ty
    private Position position;

    /**
     * Hàm khởi tạo đầy đủ cho lớp Employee.
     *
     * @param fullName Họ và tên của nhân viên.
     * @param phoneNumber Số điện thoại của nhân viên.
     * @param email Email của nhân viên.
     * @param address Địa chỉ của nhân viên.
     * @param gender Giới tính của nhân viên.
     * @param idCardNumber Số CMND/CCCD của nhân viên.
     * @param dob Ngày sinh của nhân viên.
     * @param position Vị trí của nhân viên trong công ty.
     */
    public Employee(String fullName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob, Position position) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.idCardNumber = idCardNumber;
        this.dob = dob;
        this.position = position;
    }

    /**
     * Hàm khởi tạo mặc định cho lớp Employee.
     */
    public Employee() {
    }

    /**
     * Phương thức equals để so sánh hai đối tượng Employee dựa trên employeeID.
     *
     * @param o Đối tượng khác cần so sánh.
     * @return true nếu employeeID của hai đối tượng là giống nhau, ngược lại false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(employeeID, employee.employeeID);
    }

    /**
     * Phương thức hashCode để tạo ra mã hash dựa trên employeeID.
     *
     * @return mã hash của employeeID.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(employeeID);
    }

    // Getter và Setter cho các thuộc tính

    /**
     * Trả về ID của nhân viên.
     *
     * @return ID của nhân viên.
     */
    public String getEmployeeID() {
        return employeeID;
    }

    /**
     * Thiết lập ID cho nhân viên, kiểm tra tính hợp lệ của ID bằng Regex.
     *
     * @param employeeID ID của nhân viên.
     */
    public void setEmployeeID(String employeeID) {
        if (!RegexChecker.isValidIDFormat("EMP", employeeID))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_ID);
        this.employeeID = employeeID;
    }

    /**
     * Trả về họ và tên của nhân viên.
     *
     * @return Họ và tên của nhân viên.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Thiết lập họ và tên cho nhân viên, kiểm tra độ dài tên bằng Regex.
     *
     * @param fullName Họ và tên của nhân viên.
     */
    public void setFullName(String fullName) {
        if (!RegexChecker.isValidNameLength(fullName, 2, 50))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_FULLNAME);
        this.fullName = fullName;
    }

    /**
     * Trả về số điện thoại của nhân viên.
     *
     * @return Số điện thoại của nhân viên.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Thiết lập số điện thoại cho nhân viên, kiểm tra tính hợp lệ của số điện thoại bằng Regex.
     *
     * @param phoneNumber Số điện thoại của nhân viên.
     */
    public void setPhoneNumber(String phoneNumber) {
        if (!RegexChecker.isValidPhoneNumber(phoneNumber))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_PHONENUMBER);
        this.phoneNumber = phoneNumber;
    }

    /**
     * Trả về email của nhân viên.
     *
     * @return Email của nhân viên.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Thiết lập email cho nhân viên, kiểm tra tính hợp lệ của email bằng Regex.
     *
     * @param email Email của nhân viên.
     */
    public void setEmail(String email) {
        if (!RegexChecker.isValidEmail(email))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_ADDRESS);
        this.email = email;
    }

    /**
     * Trả về địa chỉ của nhân viên.
     *
     * @return Địa chỉ của nhân viên.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Thiết lập địa chỉ cho nhân viên.
     *
     * @param address Địa chỉ của nhân viên.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Trả về giới tính của nhân viên.
     *
     * @return Giới tính của nhân viên.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Thiết lập giới tính cho nhân viên.
     *
     * @param gender Giới tính của nhân viên.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Trả về ngày sinh của nhân viên.
     *
     * @return Ngày sinh của nhân viên.
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Thiết lập ngày sinh cho nhân viên, kiểm tra tính hợp lệ của ngày sinh (đủ 18 tuổi) bằng Regex.
     *
     * @param dob Ngày sinh của nhân viên.
     */
    public void setDob(LocalDate dob) {
        if (!RegexChecker.isValidDOB(dob))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_DOB);
        this.dob = dob;
    }

    /**
     * Trả về số CMND/CCCD của nhân viên.
     *
     * @return Số CMND/CCCD của nhân viên.
     */
    public String getIdCardNumber() {
        return idCardNumber;
    }

    /**
     * Thiết lập số CMND/CCCD cho nhân viên, kiểm tra tính hợp lệ của số CMND/CCCD bằng Regex.
     *
     * @param idCardNumber Số CMND/CCCD của nhân viên.
     */
    public void setIdCardNumber(String idCardNumber) {
        if (!RegexChecker.isValidCCCD(idCardNumber))
            throw new IllegalArgumentException(ErrorMessages.EMP_INVALID_ID);
        this.idCardNumber = idCardNumber;
    }

    /**
     * Trả về vị trí công việc của nhân viên.
     *
     * @return Vị trí công việc của nhân viên.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Thiết lập vị trí công việc cho nhân viên.
     *
     * @param position Vị trí công việc của nhân viên.
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}
