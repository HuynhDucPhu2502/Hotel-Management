package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public static List<Employee> getEmployees() {
        ArrayList<Employee> data = new ArrayList<Employee>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT employeeID, fullName, phoneNumber, " +
                    "email, address, gender, " +
                    "idCardNumber, dob, position " +
                    "FROM Employee";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Employee employee = new Employee();

                employee.setEmployeeID(rs.getString(1));
                employee.setFullName(rs.getString(2));
                employee.setPhoneNumber(rs.getString(3));
                employee.setEmail(rs.getString(4));
                employee.setAddress(rs.getString(5));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                employee.setIdCardNumber(rs.getString(7));
                employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));

                data.add(employee);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static Employee getDataByID(String employeeID) {

        String SQLQueryStatement = "SELECT employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position "
                + "FROM Employee " +
                "WHERE employeeID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, employeeID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee();

                    employee.setEmployeeID(rs.getString(1));
                    employee.setFullName(rs.getString(2));
                    employee.setPhoneNumber(rs.getString(3));
                    employee.setEmail(rs.getString(4));
                    employee.setAddress(rs.getString(5));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    employee.setIdCardNumber(rs.getString(7));
                    employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));


                    return employee;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(Employee employee) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Employee(employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"
                )
        ){

            preparedStatement.setString(1, employee.getEmployeeID());
            preparedStatement.setString(2, employee.getFullName());
            preparedStatement.setString(3, employee.getPhoneNumber());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getAddress());
            preparedStatement.setString(6, ConvertHelper.genderConverterToSQL(employee.getGender()));
            preparedStatement.setString(7, employee.getIdCardNumber());
            preparedStatement.setDate(8, ConvertHelper.dateConvertertoSQL(employee.getDob()));
            preparedStatement.setString(9, employee.getPosition().name());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(Employee employee) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Employee " +
                                "SET fullName = ?, phoneNumber = ?, email = ?, " +
                                "address = ?, gender = ?, idCardNumber = ?, " +
                                "dob = ?, position = ? " +
                                "WHERE employeeID = ? "
                );
        ){
            preparedStatement.setString(1, employee.getFullName());
            preparedStatement.setString(2, employee.getPhoneNumber());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, ConvertHelper.genderConverterToSQL(employee.getGender()));
            preparedStatement.setString(6, employee.getIdCardNumber());
            preparedStatement.setDate(7, ConvertHelper.dateConvertertoSQL(employee.getDob()));
            preparedStatement.setString(8, employee.getPosition().name());
            preparedStatement.setString(9, employee.getEmployeeID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }


    public static String getNextEmployeeID() {
        String nextID = "EMP-000001";

        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "Employee");
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

    public static List<String> getTopThreeID() {
        ArrayList<String> data = new ArrayList<>();

        String query = "SELECT TOP 3 employeeID FROM Employee ORDER BY employeeID DESC";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)
        ) {
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<Employee> findDataByContainsId(String input) {
            ArrayList<Employee> data = new ArrayList<>();

            String query = "SELECT employeeID, fullName, phoneNumber, " +
                    "email, address, gender, " +
                    "idCardNumber, dob, position " +
                    "FROM Employee " +
                    "WHERE LOWER(employeeID) LIKE ?";

            try (
                    Connection connection = DBHelper.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)
            ) {
                preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    Employee employee = new Employee();

                    employee.setEmployeeID(rs.getString(1));
                    employee.setFullName(rs.getString(2));
                    employee.setPhoneNumber(rs.getString(3));
                    employee.setEmail(rs.getString(4));
                    employee.setAddress(rs.getString(5));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    employee.setIdCardNumber(rs.getString(7));
                    employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));

                    data.add(employee);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                System.exit(1);
            }

            return data;
    }
    public static Employee getEmployeeByAccountID(String accountID) {
        String sql = "SELECT e.employeeID, e.fullName, e.phoneNumber, e.email, e.address, " +
                "e.gender, e.idCardNumber, e.dob, e.position " +
                "FROM Employee e " +
                "JOIN Account a ON e.employeeID = a.employeeID " +
                "WHERE a.accountID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, accountID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee();

                    employee.setEmployeeID(rs.getString(1));
                    employee.setFullName(rs.getString(2));
                    employee.setPhoneNumber(rs.getString(3));
                    employee.setEmail(rs.getString(4));
                    employee.setAddress(rs.getString(5));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    employee.setIdCardNumber(rs.getString(7));
                    employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));

                    return employee;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
