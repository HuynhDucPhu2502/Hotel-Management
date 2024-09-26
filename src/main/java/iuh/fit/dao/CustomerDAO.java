package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static List<Customer> getEmployees() {
        ArrayList<Customer> data = new ArrayList<Customer>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob " +
                    "FROM Customer";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Customer customer = new Customer();

                customer.setCustomerID(rs.getString(1));
                customer.setFullName(rs.getString(2));
                customer.setPhoneNumber(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setAddress(rs.getString(5));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                customer.setIdCardNumber(rs.getString(7));
                customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));

                data.add(customer);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
