package iuh.fit.models;

import iuh.fit.models.enums.AccountStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class Account {
    private String accountID;
    private Employee employee;
    private String userName;
    private String password;
    private AccountStatus accountStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Objects.equals(accountID, account.accountID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountID);
    }

    public Account(String accountID, Employee employee, String userName, String password, AccountStatus accountStatus) {
        setAccountID(accountID);
        setEmployee(employee);
        setUserName(userName);
        setPassword(password);
        setAccountStatus(accountStatus);
    }

    public Account() {

    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        if(!RegexChecker.isValidIDFormat("ACC", accountID)){
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_ID);
        }
        this.accountID = accountID;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(!RegexChecker.isValidNameLength(userName, 5, 20))
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_USERNAME);
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(!(RegexChecker.isValidPassword(password)))
            throw new IllegalArgumentException(ErrorMessages.ACC_INVALID_PASSWORD);
        this.password = password;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

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
