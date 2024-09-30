package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
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
            preparedStatement.setString(6, employee.getGender().toString());
            preparedStatement.setString(7, employee.getIdCardNumber());
            preparedStatement.setDate(8, ConvertHelper.dateConvertertoSQL(employee.getDob()));
            preparedStatement.setString(9, employee.getPosition().toString());

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
            preparedStatement.setString(5, employee.getGender().toString());
            preparedStatement.setString(6, employee.getIdCardNumber());
            preparedStatement.setDate(7, ConvertHelper.dateConvertertoSQL(employee.getDob()));
            preparedStatement.setString(8, employee.getPosition().toString());
            preparedStatement.setString(9, employee.getEmployeeID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
