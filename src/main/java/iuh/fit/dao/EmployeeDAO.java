package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
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
}
