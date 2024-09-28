package iuh.fit.dao;

import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public static List<Account> getEmployees() {
        ArrayList<Account> data = new ArrayList<Account>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT a.accountID, a.userName, a.password, " +
                    "a.status, b.employeeID, b.fullName, " +
                    "b.phoneNumber, b.email, b.address, " +
                    "b.gender, b.idCardNumber, b.dob, " +
                    "b.position " +
                    "FROM Account a INNER JOIN Employee b " +
                    "ON a.employeeID = b.employeeID";
            ResultSet rs = statement.executeQuery(sql);


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
                employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(12)));
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

    public static Account getLogin(String username, String password) {

        String SQLQueryStatement = "SELECT a.accountID, a.userName, a.password, " +
                "a.status, b.employeeID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position " +
                "FROM Account a INNER JOIN Employee b " +
                "ON a.employeeID = b.employeeID " +
                "WHERE a.userName = ? AND a.password = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

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
                    employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(12)));
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
}

