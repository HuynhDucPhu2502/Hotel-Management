package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.enums.Gender;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.models.enums.Position;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public static List<Employee> getEmployees() {
        ArrayList<Employee> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT employeeID, fullName, phoneNumber, " +
                    "email, address, gender, " +
                    "idCardNumber, dob, position, isActivate " +
                    "FROM Employee " +
                    "WHERE employeeID != 'EMP-000000' AND isActivate = 'ACTIVATE'";
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
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));
                employee.setObjectStatus(ConvertHelper.objectStatusConverter(rs.getString(10)));

                data.add(employee);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static ArrayList<Employee> getEmployeesWithoutAllShift() {
        ArrayList<Employee> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT e.employeeID, fullName, phoneNumber, " +
                    "email, address, gender, " +
                    "idCardNumber, dob, position, isActivate " +
                    "FROM Employee e " +
                    "LEFT JOIN ShiftAssignment s ON e.employeeID = s.employeeId " +
                    "WHERE e.isActivate = 'ACTIVATE' AND s.employeeId IS NULL";
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
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));
                employee.setObjectStatus(ConvertHelper.objectStatusConverter(rs.getString(10)));

                data.add(employee);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static ArrayList<Employee> getEmployeesWithoutSpecificShift(Shift shift) {
        ArrayList<Employee> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        """
                                SELECT DISTINCT e.employeeID, fullName, phoneNumber,\s
                                email, address, gender,\s
                                idCardNumber, dob, position, isActivate\s
                                FROM Employee e\s
                                LEFT JOIN ShiftAssignment s ON e.employeeID = s.employeeId\s
                                WHERE e.isActivate = 'ACTIVATE' AND (s.shiftId IS NULL OR e.employeeID NOT IN
                                	(
                                		SELECT employeeID
                                		FROM ShiftAssignment
                                		WHERE shiftId = ?
                                	)
                                )
                        """)
        ){
            statement.setString(1, shift.getShiftID());

            ResultSet rs = statement.executeQuery();


            while (rs.next()) {
                Employee employee = new Employee();

                employee.setEmployeeID(rs.getString(1));
                employee.setFullName(rs.getString(2));
                employee.setPhoneNumber(rs.getString(3));
                employee.setEmail(rs.getString(4));
                employee.setAddress(rs.getString(5));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(6)));
                employee.setIdCardNumber(rs.getString(7));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));
                employee.setObjectStatus(ConvertHelper.objectStatusConverter(rs.getString(10)));

                data.add(employee);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static Employee getDataByID(String employeeID) {
        String SQLQueryStatement = "SELECT employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position, isActivate "
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
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));
                    employee.setObjectStatus(ConvertHelper.objectStatusConverter(rs.getString(10)));

                    if(employee.getObjectStatus().equals(ObjectStatus.DEACTIVATE)){
                        return null;
                    }

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
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Employee(employeeID, fullName, phoneNumber, email, address, gender, idCardNumber, dob, position, isActivate) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh để cập nhật nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ){
            selectSequenceStatement.setString(1, "Employee");
            ResultSet rs = selectSequenceStatement.executeQuery();
            String newEmployeeID = "EMP-000001"; // ID mặc định nếu không có trong DB

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.EMPLOYEE_PREFIX + "-"; // Tiền tố cho Customer ID

                // Tách phần số từ nextID và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                newEmployeeID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật nextID mới trong GlobalSequence
                updateSequenceStatement.setString(1, newEmployeeID);
                updateSequenceStatement.setString(2, "Employee");
                updateSequenceStatement.executeUpdate();
            }

            insertStatement.setString(1, employee.getEmployeeID());
            insertStatement.setString(2, employee.getFullName());
            insertStatement.setString(3, employee.getPhoneNumber());
            insertStatement.setString(4, employee.getEmail());
            insertStatement.setString(5, employee.getAddress());
            insertStatement.setString(6, ConvertHelper.genderToSQLConverter(employee.getGender()));
            insertStatement.setString(7, employee.getIdCardNumber());
            insertStatement.setDate(8, ConvertHelper.localDateToSQLConverter(employee.getDob()));
            insertStatement.setString(9, employee.getPosition().name());
            insertStatement.setString(10, ConvertHelper.objectStatusToSQLConverter(employee.getObjectStatus()));

            insertStatement.executeUpdate();
            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.EMPLOYEE_PREFIX + "-";

                // tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật giá trị nextID trong bảng GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "Employee");
                updateSequenceStatement.executeUpdate();
            }
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
                                "dob = ?, position = ?, avatar = ? " +
                                "WHERE employeeID = ? "
                )
        ){
            preparedStatement.setString(1, employee.getFullName());
            preparedStatement.setString(2, employee.getPhoneNumber());
            preparedStatement.setString(3, employee.getEmail());
            preparedStatement.setString(4, employee.getAddress());
            preparedStatement.setString(5, ConvertHelper.genderToSQLConverter(employee.getGender()));
            preparedStatement.setString(6, employee.getIdCardNumber());
            preparedStatement.setDate(7, ConvertHelper.localDateToSQLConverter(employee.getDob()));
            preparedStatement.setString(8, employee.getPosition().name());
            preparedStatement.setString(9, employee.getAvatar());
            preparedStatement.setString(10, employee.getEmployeeID());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static String getNextEmployeeID() {
        String sql = "SELECT nextID FROM GlobalSequence WHERE tableName = 'Employee'";
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
        return "EMP-000001";
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
                    "WHERE LOWER(employeeID) LIKE ? AND isActivate = 'ACTIVATE'";

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
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
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
                "WHERE a.accountID = ? AND e.isActivate = 'ACTIVATE'";

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
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));

                    return employee;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Employee getEmployeeByEmployeeID(String employeeID) {
        String sql = "SELECT e.employeeID, e.fullName, e.phoneNumber, e.email, e.address, " +
                "e.gender, e.idCardNumber, e.dob, e.position " +
                "FROM Employee e " +
                "WHERE e.employeeID = ? AND e.isActivate = 'ACTIVATE'";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
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
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(8)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(9)));

                    return employee;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Employee> searchEmployee(
            String employeeID, String fullName, String phoneNumber, String email, String address, Gender gender, String idCardNumber, LocalDate dob, Position position
            ) {

        List<Employee> data = new ArrayList<>();

        String sql = "SELECT employeeID, fullName, phoneNumber, " +
                "email, address, gender, " +
                "idCardNumber, dob, position " +
                "FROM Employee " +
                "WHERE (employeeID like ? or ? is null) and " +
                "(fullName like ? or ? is null) and " +
                "(phoneNumber like ? or ? is null) and " +
                "(email like ? or ? is null) and " +
                "(address like ? or ? is null) and " +
                "(idCardNumber like ? or ? is null) and " +
                "(gender = ? OR ? IS NULL) and " +
                "(dob = ? OR ? IS NULL) and " +
                "(position = ? OR ? IS NULL) and isActivate = 'ACTIVATE'";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, "%" + employeeID + "%");
            preparedStatement.setString(2, employeeID);
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
                preparedStatement.setString(13, ConvertHelper.genderToSQLConverter(gender));
                preparedStatement.setString(14, ConvertHelper.genderToSQLConverter(gender));
            }

            if (dob == null){
                preparedStatement.setObject(15, dob);
                preparedStatement.setObject(16, dob);
            } else {
                preparedStatement.setDate(15, ConvertHelper.localDateToSQLConverter(dob));
                preparedStatement.setDate(16, ConvertHelper.localDateToSQLConverter(dob));
            }

            if (position == null){
                preparedStatement.setObject(17, position);
                preparedStatement.setObject(18, position);
            } else {
                preparedStatement.setString(17, ConvertHelper.positionConverterToSQL(position));
                preparedStatement.setObject(18, ConvertHelper.positionConverterToSQL(position));
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeID(rs.getString("employeeID"));
                employee.setFullName(rs.getString("fullName"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setEmail(rs.getString("email"));
                employee.setAddress(rs.getString("address"));
                employee.setIdCardNumber(rs.getString("idCardNumber"));
                employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

                data.add(employee);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
