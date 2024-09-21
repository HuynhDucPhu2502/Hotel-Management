package iuh.fit.models;

import iuh.fit.models.enums.AccountStatus;

public class Account {
    private String accountID;
    private String userName;
    private String password;
    private AccountStatus accountStatus;

    public Account(String accountID, String userName, String password, AccountStatus accountStatus) {
        this.accountID = accountID;
        this.userName = userName;
        this.password = password;
        this.accountStatus = accountStatus;
    }

    public Account() {
    }
}
