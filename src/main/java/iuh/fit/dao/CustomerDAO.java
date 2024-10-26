package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static List<Customer> getCustomer() {
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
                customer.setCusFullName(rs.getString(2));
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

    public static int getValueCount() {
        int count = 0;
        String sql = "SELECT vCount FROM ValueCount WHERE vName = ?"; // Thêm khoảng trắng
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, "Customer");
            ResultSet rs = statement.executeQuery();

            if (rs.next()) { // Kiểm tra xem có bản ghi nào không
                count = rs.getInt("vCount"); // Sử dụng tên cột thay vì chỉ số
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
        return count;
    }

    public static void setValueCount(int valueCount) {
        int n = 0;
        String sql = "UPDATE ValueCount SET vCount = ? WHERE vName = ?"; // Thêm khoảng trắng
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, valueCount);
            statement.setString(2, "Customer");
            statement.executeUpdate();
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }


    public static Customer getDataByID(String customerID) {

        String SQLQueryStatement = "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob "
                + "FROM Customer " +
                "WHERE customerID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement);
        ) {

            preparedStatement.setString(1, customerID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();

                    customer.setCustomerID(rs.getString(1));
                    customer.setCusFullName(rs.getString(2));
                    customer.setPhoneNumber(rs.getString(3));
                    customer.setEmail(rs.getString(4));
                    customer.setAddress(rs.getString(5));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    customer.setIdCardNumber(rs.getString(7));
                    customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(8)));

                    return customer;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(Customer customer) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Customer(customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, customer.getCustomerID());
            preparedStatement.setString(2, customer.getCusFullName());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getAddress());
            preparedStatement.setString(6, customer.getGender().name());
            preparedStatement.setString(7, customer.getIdCardNumber());
            preparedStatement.setDate(8, ConvertHelper.dateConvertertoSQL(customer.getDob()));


            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String customerID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Customer "
                                + "WHERE customerID = ?"
                )
        ){
            preparedStatement.setString(1, customerID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(Customer customer) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Customer " +
                                "SET fullName = ?, phoneNumber = ?, " +
                                "email = ?, address = ?, gender = ?, " +
                                "idCardNumber = ?, dob = ?" +
                                "WHERE customerID = ? "
                );
        ){
            preparedStatement.setString(1, customer.getCusFullName());
            preparedStatement.setString(2, customer.getPhoneNumber());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getGender().name());
            preparedStatement.setString(6, customer.getIdCardNumber());
            preparedStatement.setDate(7, ConvertHelper.dateConvertertoSQL(customer.getDob()));
            preparedStatement.setString(8, customer.getCustomerID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static String getNextCustomerID() {
        String sql = "SELECT nextID FROM GlobalSequence WHERE tableName = 'Customer'";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RC-000001";
    }
}
