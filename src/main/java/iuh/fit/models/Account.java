package iuh.fit.models;

import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

/**
 * Lớp Account đại diện cho tài khoản của một nhân viên trong hệ thống.
 * Mỗi tài khoản có các thuộc tính như ID tài khoản, nhân viên sở hữu, tên đăng nhập, mật khẩu và trạng thái tài khoản.
 */
public class Account {
    private String accountID;          // ID của tài khoản
    private Employee employee;         // Nhân viên liên kết với tài khoản này
    private String userName;           // Tên đăng nhập của tài khoản
    private String password;           // Mật khẩu của tài khoản
    private AccountStatus accountStatus; // Trạng thái của tài khoản (Active, Inactive, v.v.)

    /**
     * Phương thức equals kiểm tra xem hai đối tượng Account có bằng nhau hay không.
     * So sánh dựa trên ID tài khoản (accountID).
     *
     * @param o Đối tượng khác để so sánh.
     * @return true nếu hai đối tượng Account có cùng ID tài khoản, ngược lại là false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Nếu hai đối tượng là cùng một đối tượng.
        if (!(o instanceof Account account)) return false; // Nếu đối tượng không phải kiểu Account.
        return Objects.equals(accountID, account.accountID); // So sánh accountID.
    }

    /**
     * Phương thức hashCode trả về mã băm của Account dựa trên accountID.
     * Điều này giúp quản lý Account trong các cấu trúc dữ liệu như HashSet hoặc HashMap.
     *
     * @return mã băm dựa trên accountID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountID); // Tạo mã băm từ accountID.
    }

    /**
     * Hàm khởi tạo cho Account với đầy đủ tham số.
     *
     * @param accountID ID của tài khoản
     * @param employee Nhân viên sở hữu tài khoản
     * @param userName Tên đăng nhập
     * @param password Mật khẩu
     * @param accountStatus Trạng thái tài khoản
     */
    public Account(String accountID, Employee employee, String userName, String password, AccountStatus accountStatus) {
        setAccountID(accountID);
        setEmployee(employee);
        setUserName(userName);
        setPassword(password);
        setAccountStatus(accountStatus);
    }

    /**
     * Hàm khởi tạo mặc định (không tham số).
     */
    public Account() {}

    /**
     * Lấy ID tài khoản.
     *
     * @return ID của tài khoản.
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * Thiết lập ID cho tài khoản.
     * Kiểm tra ID có hợp lệ theo định dạng hay không bằng cách sử dụng RegexChecker.
     *
     * @param accountID ID của tài khoản
     * @throws IllegalArgumentException nếu ID không hợp lệ.
     */
    public void setAccountID(String accountID) {
        if(!RegexChecker.isValidIDFormat(GlobalConstants.ACCOUNT_PREFIX, accountID)) {
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_ID);
        }
        this.accountID = accountID;
    }

    /**
     * Lấy nhân viên sở hữu tài khoản.
     *
     * @return đối tượng Employee.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Thiết lập nhân viên sở hữu tài khoản.
     *
     * @param employee Đối tượng Employee cần gán cho tài khoản.
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Lấy tên đăng nhập của tài khoản.
     *
     * @return tên đăng nhập.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Thiết lập tên đăng nhập cho tài khoản.
     * Kiểm tra tên đăng nhập có nằm trong độ dài hợp lệ (5-20 ký tự) không bằng RegexChecker.
     *
     * @param userName Tên đăng nhập của tài khoản.
     * @throws IllegalArgumentException nếu tên đăng nhập không hợp lệ.
     */
    public void setUserName(String userName) {
        if(!RegexChecker.isValidUsername(userName, 5, 20))
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_USERNAME);
        this.userName = userName;
    }

    /**
     * Lấy mật khẩu của tài khoản.
     *
     * @return mật khẩu.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Thiết lập mật khẩu cho tài khoản.
     * Kiểm tra mật khẩu có hợp lệ hay không bằng RegexChecker.
     *
     * @param password Mật khẩu của tài khoản.
     * @throws IllegalArgumentException nếu mật khẩu không hợp lệ.
     */
    public void setPassword(String password) {
        if(!(RegexChecker.isValidPassword(password)))
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_PASSWORD);
        this.password = password;
    }

    /**
     * Lấy trạng thái của tài khoản.
     *
     * @return trạng thái tài khoản (AccountStatus).
     */
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    /**
     * Thiết lập trạng thái cho tài khoản.
     *
     * @param accountStatus Trạng thái cần gán cho tài khoản.
     */
    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    /**
     * Phương thức toString trả về chuỗi biểu diễn các thuộc tính của đối tượng Account.
     *
     * @return chuỗi biểu diễn Account.
     */
    @Override
    public String toString() {
        return "Account{" +
                "accountID='" + accountID + '\'' +
                ", employee=" + employee.toString() +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", accountStatus=" + accountStatus +
                '}';
    }
}
