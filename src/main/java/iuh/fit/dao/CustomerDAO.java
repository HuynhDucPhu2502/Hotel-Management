package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static List<Customer> getCustomer() {
        ArrayList<Customer> data = new ArrayList<Customer>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, isActivate " +
                    "FROM Customer " +
                    "WHERE isActivate = 'ACTIVATE'";
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
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));

                data.add(customer);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static Customer getDataByID(String customerID) {

        String SQLQueryStatement = "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob "
                + "FROM Customer " +
                "WHERE customerID = ? AND isActivate = 'ACTIVATE'";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement);
        ) {

            preparedStatement.setString(1, customerID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();

                    customer.setCustomerID(rs.getString(1));
                    customer.setFullName(rs.getString(2));
                    customer.setPhoneNumber(rs.getString(3));
                    customer.setEmail(rs.getString(4));
                    customer.setAddress(rs.getString(5));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    customer.setIdCardNumber(rs.getString(7));
                    customer.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));

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

                // Câu lệnh để thêm dữ liệu vào Customer
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Customer(" +
                                "customerID, fullName, phoneNumber, " +
                                "email, address, gender, " +
                                "idCardNumber, dob, isActivate" +
                                ") " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVATE')"
                );

                // Câu lệnh để lấy nextID từ GlobalSequence
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh để cập nhật nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            // Lấy nextID hiện tại cho Customer từ GlobalSequence
            selectSequenceStatement.setString(1, "Customer");
            ResultSet rs = selectSequenceStatement.executeQuery();

            String newCustomerID = "CUS-000001"; // ID mặc định nếu không có trong DB
            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.CUSTOMER_PREFIX + "-"; // Tiền tố cho Customer ID

                // Tách phần số từ nextID và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                newCustomerID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật nextID mới trong GlobalSequence
                updateSequenceStatement.setString(1, newCustomerID);
                updateSequenceStatement.setString(2, "Customer");
                updateSequenceStatement.executeUpdate();
            }

            // Thiết lập các giá trị cho câu lệnh INSERT
            insertStatement.setString(1, newCustomerID);
            insertStatement.setString(2, customer.getFullName());
            insertStatement.setString(3, customer.getPhoneNumber());
            insertStatement.setString(4, customer.getEmail());
            insertStatement.setString(5, customer.getAddress());
            insertStatement.setString(6, customer.getGender().name());
            insertStatement.setString(7, customer.getIdCardNumber());
            insertStatement.setDate(8, ConvertHelper.dateToSQLConverter(customer.getDob()));

            insertStatement.executeUpdate();
        } catch (SQLException sqlException) {
            String sqlMessage = sqlException.getMessage();

            if (sqlMessage.contains("Violation of UNIQUE KEY constraint")) {
                throw new IllegalArgumentException("ID Card Number đã tồn tại trong hệ thống.");
            } else {
                sqlException.printStackTrace();
                System.exit(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String customerID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Customer " +
                                "SET isActivate = 'DEACTIVATE' "
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
            preparedStatement.setString(1, customer.getFullName());
            preparedStatement.setString(2, customer.getPhoneNumber());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getGender().name());
            preparedStatement.setString(6, customer.getIdCardNumber());
            preparedStatement.setDate(7, ConvertHelper.dateToSQLConverter(customer.getDob()));
            preparedStatement.setString(8, customer.getCustomerID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static List<Customer> findDataByContainsId(String input) {
        ArrayList<Customer> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, isActivate " +
                                "FROM Customer " +
                                "WHERE LOWER(customerID) LIKE ? AND isActivate = 'ACTIVATE'"
                )
        ) {
            preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();

                customer.setCustomerID(rs.getString(1));
                customer.setFullName(rs.getString(2));
                customer.setPhoneNumber(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setAddress(rs.getString(5));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                customer.setIdCardNumber(rs.getString(7));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));

                data.add(customer);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<String> getTopThreeID() {
        ArrayList<String> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ) {
            String sql = "SELECT TOP 3 customerID " +
                    "FROM Customer " +
                    "WHERE isActivate = 'ACTIVATE' " +
                    "ORDER BY customerID DESC";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
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

    public static Customer getDataByIDCardNumber(String idCardNumber) {

        String SQLQueryStatement = "SELECT customerID, fullName, phoneNumber, email, address, gender, idCardNumber, dob "
                + "FROM Customer " +
                "WHERE idCardNumber = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement);
        ) {

            preparedStatement.setString(1, idCardNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();

                    customer.setCustomerID(rs.getString(1));
                    customer.setFullName(rs.getString(2));
                    customer.setPhoneNumber(rs.getString(3));
                    customer.setEmail(rs.getString(4));
                    customer.setAddress(rs.getString(5));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                    customer.setIdCardNumber(rs.getString(7));
                    customer.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));

                    return customer;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Customer> searchCustomer(
            String customerID, String fullName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob
    ) {

        List<Customer> data = new ArrayList<>();

        String sql = "SELECT customerID, fullName, phoneNumber, " +
                "email, address, gender, " +
                "idCardNumber, dob, isActivate " +
                "FROM Customer " +
                "WHERE (customerID like ? or ? is null) and " +
                "(fullName like ? or ? is null) and " +
                "(phoneNumber like ? or ? is null) and " +
                "(email like ? or ? is null) and " +
                "(address like ? or ? is null) and " +
                "(idCardNumber like ? or ? is null) and " +
                "(gender = ? OR ? IS NULL) and " +
                "(dob = ? OR ? IS NULL) and isActivate = 'ACTIVATE'";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, "%" + customerID + "%");
            preparedStatement.setString(2, customerID);
            preparedStatement.setString(3, "%" + fullName + "%");
            preparedStatement.setString(4, fullName);
            preparedStatement.setString(5, "%" + phoneNumber + "%");
            preparedStatement.setString(6, phoneNumber);
            preparedStatement.setString(7, "%" + email + "%");
            preparedStatement.setString(8, email);
            preparedStatement.setString(9, "%" + address + "%");
            preparedStatement.setString(10, address);
            preparedStatement.setString(11, "%" + idCardNumber + "%");
            preparedStatement.setString(12, idCardNumber);
            if(gender == null){
                preparedStatement.setObject(13, gender);
                preparedStatement.setObject(14, gender);
            }else {
                preparedStatement.setString(13, ConvertHelper.genderConverterToSQL(gender));
                preparedStatement.setString(14, ConvertHelper.genderConverterToSQL(gender));
            }

            if (dob == null){
                preparedStatement.setObject(15, dob);
                preparedStatement.setObject(16, dob);
            } else {
                preparedStatement.setDate(15, ConvertHelper.dateToSQLConverter(dob));
                preparedStatement.setDate(16, ConvertHelper.dateToSQLConverter(dob));
            }

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(rs.getString("customerID"));
                customer.setFullName(rs.getString("fullName"));
                customer.setPhoneNumber(rs.getString("phoneNumber"));
                customer.setEmail(rs.getString("email"));
                customer.setAddress(rs.getString("address"));
                customer.setIdCardNumber(rs.getString("idCardNumber"));
                customer.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));

                data.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
