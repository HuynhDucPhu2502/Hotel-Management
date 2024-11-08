package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.PasswordHashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public static List<Account> getAccount() {
        ArrayList<Account> data = new ArrayList<>();
        String sql = "SELECT a.accountID, a.userName, a.password, " +
                "a.status, b.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID";
        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static Account getLogin(String username, String password) {
        String SQLQueryStatement = "SELECT a.accountID, a.userName, a.password, " +
                "a.status, b.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID " +
                "WHERE a.userName = ?";

        try (Connection con = DBHelper.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)) {
            preparedStatement.setString(1, username);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString(3);
                    String inputHashedPassword = PasswordHashing.hashPassword(password);

                    if (hashedPassword.equals(inputHashedPassword)) {
                        return extractData(rs);
                    } else {
                        System.out.println("Password verification failed.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getDataByID(String accountID) {
        String SQLQueryStatement = "SELECT a.accountID, a.userName, a.password, " +
                "a.status, b.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID " +
                "WHERE accountID = ?";

        try (Connection con = DBHelper.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)) {

            preparedStatement.setString(1, accountID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return extractData(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createData(Account account) {
        String sql = "INSERT INTO Account(accountID, userName, password, status, employeeID) " +
                "VALUES(?, ?, ?, ?, ?)";
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Account(accountID, userName, password, status, employeeID) " +
                                "VALUES(?, ?, ?, ?, ?)"
                );
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh để cập nhật nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            selectSequenceStatement.setString(1, "Account");
            ResultSet rs = selectSequenceStatement.executeQuery();
            String newAccountID = "ACC-000001"; // ID mặc định nếu không có trong DB

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.ACCOUNT_PREFIX + "-"; // Tiền tố cho Customer ID

                // Tách phần số từ nextID và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                newAccountID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật nextID mới trong GlobalSequence
                updateSequenceStatement.setString(1, newAccountID);
                updateSequenceStatement.setString(2, "Account");
                updateSequenceStatement.executeUpdate();
            }

            insertStatement.setString(1, account.getAccountID());
            insertStatement.setString(2, account.getUserName());

            insertStatement.setString(3, account.getPassword());

            insertStatement.setString(4, account.getAccountStatus().toString());
            insertStatement.setString(5, account.getEmployee().getEmployeeID());

            insertStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String accountID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Account "
                                + "WHERE accountID = ?"
                )
        ){
            preparedStatement.setString(1, accountID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(Account account) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Account " +
                                "SET userName = ?, password = ?, status = ?, employeeID = ? " +
                                "WHERE accountID = ? "
                );
        ){

            preparedStatement.setString(1, account.getUserName());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setString(3, account.getAccountStatus().toString());
            preparedStatement.setString(4, account.getEmployee().getEmployeeID());
            preparedStatement.setString(5, account.getAccountID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static String getNextAccountID() {
        String nextID = "ACC-000001";

        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "Account");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return nextID;
    }

    public static Account getAccountByEmployeeID(String employeeID) {
        String SQLQueryStatement =  "SELECT a.accountID, a.userName, a.password, " +
                "a.status, b.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID " +
                "WHERE a.employeeID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, employeeID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();

                    account.setAccountID(rs.getString(1));
                    account.setUserName(rs.getString(2));
                    account.setPassword(rs.getString(3));
                    account.setAccountStatus(ConvertHelper.accountStatusConverter(rs.getString(4)));

                    Employee employee = new Employee();
                    employee.setEmployeeID(rs.getString(5));
                    employee.setFullName(rs.getString(6));
                    employee.setPhoneNumber(rs.getString(7));
                    employee.setEmail(rs.getString(8));
                    employee.setAddress(rs.getString(9));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(10)));
                    employee.setIdCardNumber(rs.getString(11));
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(12)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(13)));

                    account.setEmployee(employee);

                    return account;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Account> findDataByContainsId(String input) {
        ArrayList<Account> data = new ArrayList<>();

        String query = "SELECT a.accountID, a.userName, a.password, " +
                "a.status, a.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID " +
                "WHERE LOWER(a.employeeID) LIKE ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account account = new Account();

                account.setAccountID(rs.getString(1));
                account.setUserName(rs.getString(2));
                account.setPassword(rs.getString(3));
                account.setAccountStatus(ConvertHelper.accountStatusConverter(rs.getString(4)));

                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString(5));
                employee.setFullName(rs.getString(6));
                employee.setPhoneNumber(rs.getString(7));
                employee.setEmail(rs.getString(8));
                employee.setAddress(rs.getString(9));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(10)));
                employee.setIdCardNumber(rs.getString(11));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(12)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(13)));

                account.setEmployee(employee);

                data.add(account);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    private static Account extractData(ResultSet rs) throws Exception {
        Account account = new Account();
        account.setAccountID(rs.getString(1));
        account.setUserName(rs.getString(2));
        account.setPassword(rs.getString(3));
        account.setAccountStatus(ConvertHelper.accountStatusConverter(rs.getString(4)));

        Employee employee = new Employee();
        employee.setEmployeeID(rs.getString(5));
        employee.setFullName(rs.getString(6));
        employee.setPhoneNumber(rs.getString(7));
        employee.setEmail(rs.getString(8));
        employee.setAddress(rs.getString(9));
        employee.setGender(ConvertHelper.genderConverter(rs.getString(10)));
        employee.setIdCardNumber(rs.getString(11));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate(12)));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString(13)));

        account.setEmployee(employee);
        return account;
    }
}

